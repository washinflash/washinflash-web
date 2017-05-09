package com.washinflash.rest.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.washinflash.common.exception.BusinessException;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.OrderDetails;
import com.washinflash.common.object.request.OrderDetailsRequest;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.StatusConstant;
import com.washinflash.rest.dao.OrderDAO;
import com.washinflash.rest.dao.jdbctemplate.SpringJDBCTemplate;
import com.washinflash.rest.mapper.MyOrderDetailsMapper;

@Component
public class OrderDAOImpl extends SpringJDBCTemplate implements OrderDAO {

	@Override
	public String createOrder(OrderDetailsRequest orderReq) throws BusinessException, SystemException {

		String insertSQL = "INSERT INTO ORDER_DETAILS (USER_DETAILS_ID, ADDRESS_ID, DELIVERY_TYPE, PICKUP_DATE, PICKUP_TIME, " +
				"DELIVERY_DATE, DELIVERY_TIME, ORDER_REFERENCE, TOTAL_PRICE_MULTIPLIER, MINIMUM_ORDER_VALUE, APPROX_NO_CLOTHES, COUPON_CODE, DONATE_ONLY, SPECIAL_INSTRUCTION, " +
				"CREATED_ON, UPDATED_ON, STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		String orderRef = "";
		boolean isOrderRefExist = true;

		while(isOrderRefExist) {
			orderRef = GenericUtils.getUniqueRef();
			isOrderRefExist = isOrderRefExist(orderRef);
		}

		int noOfRecordsUpdated = getJDBCTemplateObject().update(
				insertSQL, orderReq.getUserDetailsId(), orderReq.getAddressId(), orderReq.getDeliveryType(),
				GenericUtils.getDateFromString(orderReq.getPickupDate()), orderReq.getPickupTime(), 
				GenericUtils.getDateFromString(orderReq.getDeliveryDate()), orderReq.getDeliveryTime(),
				orderRef, orderReq.getTotalPriceMultiplier(), orderReq.getMinimumOrderValue(), orderReq.getApproxNoClothes(), orderReq.getCouponCode(), 
				orderReq.getDonateOnly(), orderReq.getSpecialInstruction(), GenericUtils.getCurrentDateTime(), GenericUtils.getCurrentDateTime(), StatusConstant.ACTIVE.toString());

		if(noOfRecordsUpdated != 1) {
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}

		String selectSQL = "SELECT ORDER_ID FROM ORDER_DETAILS WHERE ORDER_REFERENCE = ? AND STATUS = ?";
		
		int orderId = getJDBCTemplateObject().queryForObject(
				selectSQL, new Object[] {orderRef, StatusConstant.ACTIVE.toString()}, Integer.class);		
		
		String childInsertSQL = "INSERT INTO ORDER_HISTORY (ORDER_ID, UPDATED_ON, COMMENTS, STATUS) VALUES(?, ?, ?, ?)";

		int noOfChildRecordsUpdated = getJDBCTemplateObject().update(
				childInsertSQL, orderId, GenericUtils.getCurrentDateTime(), orderReq.getHistoryComments(), StatusConstant.BOOKED.toString());

		if(noOfChildRecordsUpdated != 1) {
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}

		List<String> selectedServicesList = orderReq.getServiceTypeList();
		if(selectedServicesList != null && selectedServicesList.size() > 0) {
			for(String serviceType : selectedServicesList) {
				
				String insertMappingSQL = "INSERT INTO ORDER_SERVICE_MAPPING (ORDER_ID, SERVICE_TYPE_ID, UPDATED_ON, STATUS) " +
						"SELECT ?, SERVICE_TYPE_ID, ?, ? FROM SERVICE_TYPE WHERE SERVICE_TYPE = ? AND STATUS = ?";

				getJDBCTemplateObject().update(insertMappingSQL, orderId, GenericUtils.getCurrentDateTime(), 
						StatusConstant.ACTIVE.toString(), serviceType, StatusConstant.ACTIVE.toString());
				
			}
		}
		
		return orderRef;		
	}
	

	private boolean isOrderRefExist(String orderRef) {
		
		boolean isOrderRefExist = false;
		
		String selectSQL = "SELECT COUNT(*) FROM ORDER_DETAILS WHERE ORDER_REFERENCE = ?";

		int orderRefCount = getJDBCTemplateObject().queryForObject(selectSQL, 
				new Object[] {orderRef}, Integer.class);

		if(orderRefCount > 0) {
			isOrderRefExist = true;
		}
		
		return isOrderRefExist;
	}
	
	@Override
	public List<OrderDetails> getMyOrderDetailsList(OrderDetailsRequest orderDetailsRequest) throws SystemException {

		String selectSQL = "SELECT DET.ORDER_ID, DET.ORDER_REFERENCE, DET.CREATED_ON ORDER_DATE, DET.DELIVERY_TYPE, DET.PICKUP_DATE, DET.PICKUP_TIME, DET.DELIVERY_DATE, DET.DELIVERY_TIME, " +
				"DET.TOTAL_PRICE_MULTIPLIER, DET.TOTAL_PRICE, DET.MINIMUM_ORDER_VALUE, HIST.STATUS, DET.ADDRESS_ID, DET.APPROX_NO_CLOTHES, DET.ACTUAL_NO_CLOTHES, DET.COUPON_CODE, DET.DONATE_ONLY FROM ORDER_DETAILS DET, (" +
				"SELECT * FROM ORDER_HISTORY WHERE ORDER_HISTORY_ID IN (SELECT MAX(ORDER_HISTORY_ID) FROM ORDER_HISTORY WHERE STATUS <> ? GROUP BY ORDER_ID)) HIST " +
				"WHERE DET.ORDER_ID = HIST.ORDER_ID AND DET.USER_DETAILS_ID = ? AND DET.STATUS = ? ORDER BY DET.ORDER_ID DESC";

		List<OrderDetails> myOrderDetailsList = getJDBCTemplateObject().query(
				selectSQL, new MyOrderDetailsMapper(), StatusConstant.DELETED.toString(), orderDetailsRequest.getUserDetailsId(), StatusConstant.ACTIVE.toString());


		return myOrderDetailsList;		
	}
	
	@Override
	public List<String> getServiceTypeListForOrder(OrderDetailsRequest orderDetailsRequest) throws SystemException {

		String selectSQL = "SELECT SERVICE_TYPE_DESC FROM ORDER_SERVICE_MAPPING MAP, SERVICE_TYPE SERV " +
				"WHERE MAP.SERVICE_TYPE_ID = SERV.SERVICE_TYPE_ID AND MAP.ORDER_ID = ? AND MAP.STATUS = ? AND SERV.STATUS = ?";
		List<String> serviceTypeList = new ArrayList<>();

		List<Map<String, Object>> returnList = getJDBCTemplateObject().queryForList(selectSQL, orderDetailsRequest.getOrderId(), 
				StatusConstant.ACTIVE.toString(), StatusConstant.ACTIVE.toString());

		for (Map<String, Object> row : returnList) {
			serviceTypeList.add((String) row.get("SERVICE_TYPE_DESC"));
		}
		
		return serviceTypeList;
	}
	
	@Override
	public OrderDetails cancelMyOrder(OrderDetailsRequest orderDetailsRequest) throws BusinessException, SystemException {

		String insertSQL = "INSERT INTO ORDER_HISTORY (ORDER_ID, UPDATED_ON, COMMENTS, STATUS) VALUES(?, ?, ?, ?)";

		int noOfRecordsUpdated = getJDBCTemplateObject().update(insertSQL, orderDetailsRequest.getOrderId(), 
				GenericUtils.getCurrentDateTime(), orderDetailsRequest.getHistoryComments(), StatusConstant.CANCELLED.toString());

		if(noOfRecordsUpdated != 1) {
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}
		
		OrderDetails orderDetails = getOrderDetails(orderDetailsRequest.getOrderId());

		return orderDetails;
	}
	
	@Override
	public OrderDetails getOrderDetails(int orderId) throws SystemException {
	
		String selectSQL = "SELECT DET.ORDER_ID, DET.ORDER_REFERENCE, DET.CREATED_ON ORDER_DATE, DET.DELIVERY_TYPE, DET.PICKUP_DATE, DET.PICKUP_TIME, DET.DELIVERY_DATE, DET.DELIVERY_TIME, " +
				"DET.TOTAL_PRICE_MULTIPLIER, DET.TOTAL_PRICE, DET.MINIMUM_ORDER_VALUE, HIST.STATUS, DET.ADDRESS_ID, DET.APPROX_NO_CLOTHES, DET.ACTUAL_NO_CLOTHES, DET.COUPON_CODE, DET.DONATE_ONLY FROM ORDER_DETAILS DET, (" +
				"SELECT * FROM ORDER_HISTORY WHERE ORDER_HISTORY_ID IN (SELECT MAX(ORDER_HISTORY_ID) FROM ORDER_HISTORY WHERE STATUS <> ? GROUP BY ORDER_ID)) HIST " +
				"WHERE DET.ORDER_ID = HIST.ORDER_ID AND DET.ORDER_ID = ? AND DET.STATUS = ? ORDER BY DET.ORDER_ID DESC";

		List<OrderDetails> orderDetailsList = getJDBCTemplateObject().query(
				selectSQL, new MyOrderDetailsMapper(), StatusConstant.DELETED.toString(), orderId, StatusConstant.ACTIVE.toString());

		OrderDetails orderDetails = null;
		
		if(orderDetailsList != null && orderDetailsList.size() > 0) {
			orderDetails = orderDetailsList.get(0);
		}
		
		return orderDetails;
	}
	
}
