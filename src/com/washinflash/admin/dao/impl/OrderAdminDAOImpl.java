package com.washinflash.admin.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.washinflash.admin.dao.OrderAdminDAO;
import com.washinflash.admin.dao.jdbctemplate.SpringJDBCTemplate;
import com.washinflash.admin.mapper.AdminOrderDetailsMapper;
import com.washinflash.admin.mapper.AdminSearchMapper;
import com.washinflash.admin.mapper.ServiceTypeFullDetailsMapper;
import com.washinflash.admin.model.AdminSearchCriteria;
import com.washinflash.admin.model.AdminSearchResult;
import com.washinflash.admin.model.OrderDetailsBean;
import com.washinflash.admin.model.SearchOrderDetails;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.OrderDetails;
import com.washinflash.common.object.model.ServiceTypeFullDetails;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.StatusConstant;


@Component
public class OrderAdminDAOImpl extends SpringJDBCTemplate implements OrderAdminDAO {
	
	
	@Override
	public AdminSearchResult searchOrder(AdminSearchCriteria criteria) throws SystemException {

		StringBuffer searchQuery = new StringBuffer();
		
		String selectClause = "DET.ORDER_ID, DET.ORDER_REFERENCE, ADDR.NAME, ADDR.MOBILE_NO, DET.DELIVERY_TYPE, DET.PICKUP_DATE, DET.PICKUP_TIME, " +
				"DET.DELIVERY_DATE, DET.DELIVERY_TIME, HIST.STATUS, ADDR.PIN_CODE, SERV.SERVICE_TYPE";
		
		String selectCountClause = "COUNT(*)";
		
		String fromClause = "ORDER_DETAILS DET, " +
				"(SELECT * FROM ORDER_HISTORY WHERE ORDER_HISTORY_ID IN (SELECT MAX(ORDER_HISTORY_ID) FROM ORDER_HISTORY WHERE STATUS <> 'DELETED' GROUP BY ORDER_ID)) HIST, " +
				"(SELECT MAP.ORDER_ID ORDER_ID, GROUP_CONCAT(SERV.SERVICE_TYPE_DESC SEPARATOR ', ') SERVICE_TYPE FROM ORDER_SERVICE_MAPPING MAP, SERVICE_TYPE SERV WHERE SERV.SERVICE_TYPE_ID = MAP.SERVICE_TYPE_ID GROUP BY MAP.ORDER_ID) SERV, " +
				"USER_ADDRESS ADDR";
		String orderbyClause = "DET.ORDER_ID DESC";
		String limitClause = " LIMIT ? OFFSET ? ";
		
		StringBuffer whereClause = new StringBuffer();
		
		whereClause.append("DET.ORDER_ID = HIST.ORDER_ID AND DET.ADDRESS_ID = ADDR.ADDRESS_ID " +
				"AND DET.STATUS = ? AND SERV.ORDER_ID = DET.ORDER_ID ");
		
		List<Object> queryParam = new ArrayList<>();
		queryParam.add(StatusConstant.ACTIVE.toString());
		
		String status = criteria.getLatestStatus();
		
		if(!GenericUtils.isEmpty(status)) {
			if(GenericConstant.ALL_OPEN_STATUS.equals(status)) {
				whereClause.append(" AND HIST.STATUS IN (" + "?, ?, ?" + ")");
				queryParam.add(StatusConstant.BOOKED.toString());
				queryParam.add(StatusConstant.WASHED.toString());
				queryParam.add(StatusConstant.PACKAGED.toString());
			} else {
				whereClause.append(" AND HIST.STATUS = ? ");
				queryParam.add(criteria.getLatestStatus());				
			}
		}
		if(!GenericUtils.isEmpty(criteria.getPickupDate())) {
			whereClause.append(" AND DET.PICKUP_DATE = ? ");
			queryParam.add(GenericUtils.getDateFromString(criteria.getPickupDate()));
		}
		if(!GenericUtils.isEmpty(criteria.getPickupTime())) {
			whereClause.append(" AND DET.PICKUP_TIME = ? ");
			queryParam.add(criteria.getPickupTime());
		}
		if(!GenericUtils.isEmpty(criteria.getDeliveryDate())) {
			whereClause.append(" AND DET.DELIVERY_DATE = ? ");
			queryParam.add(GenericUtils.getDateFromString(criteria.getDeliveryDate()));
		}
		if(!GenericUtils.isEmpty(criteria.getDeliveryTime())) {
			whereClause.append(" AND DET.DELIVERY_TIME = ? ");
			queryParam.add(criteria.getDeliveryTime());
		}
		if(!GenericUtils.isEmpty(criteria.getDeliveryType())) {
			whereClause.append(" AND DET.DELIVERY_TYPE = ? ");
			queryParam.add(criteria.getDeliveryType());
		}
		if(!GenericUtils.isEmpty(criteria.getServiceType())) {
			if(GenericConstant.DONATE_ONLY.equals(criteria.getServiceType())) {
				whereClause.append(" AND DET.DONATE_ONLY = ? ");
				queryParam.add(GenericConstant.FLAG_YES);
			} else {
				whereClause.append(" AND SERV.SERVICE_TYPE LIKE ? ");
				queryParam.add("%" + criteria.getServiceType() + "%");
			}
		}
		if(!GenericUtils.isEmpty(criteria.getSpecialField())) {
			whereClause.append(" AND (UPPER(DET.ORDER_REFERENCE) LIKE ? ")
			.append(" OR UPPER(ADDR.NAME) LIKE ? ")
			.append(" OR UPPER(ADDR.MOBILE_NO) LIKE ? ")
			.append(" OR UPPER(ADDR.PIN_CODE) LIKE ? ) ");
			
			queryParam.add("%" + criteria.getSpecialField().trim().toUpperCase() + "%");
			queryParam.add("%" + criteria.getSpecialField().trim().toUpperCase() + "%");
			queryParam.add("%" + criteria.getSpecialField().trim().toUpperCase() + "%");
			queryParam.add("%" + criteria.getSpecialField().trim().toUpperCase() + "%");
		}
		
		
		StringBuffer countQuery = new StringBuffer()
		.append(" SELECT ")
		.append(selectCountClause)
		.append(" FROM ")
		.append(fromClause)
		.append(" WHERE ")
		.append(whereClause);
		
		int recordCount = getJDBCTemplateObject().queryForObject(countQuery.toString(), queryParam.toArray(), Integer.class);
		int startIndex = 0;
		int prevLastRecordIndex = criteria.getLastRecordIndex();
		int pageSize = GenericConstant.SEARCH_PAGE_SIZE;
		
		if(GenericConstant.SEARCH_TYPE_SEARCH.equalsIgnoreCase(criteria.getSearchType()) || 
				GenericConstant.SEARCH_TYPE_FIRST.equalsIgnoreCase(criteria.getSearchType())) {
			
			startIndex = 0;
		} else if(GenericConstant.SEARCH_TYPE_REFRESH.equalsIgnoreCase(criteria.getSearchType())) {
			
			startIndex = prevLastRecordIndex - ((prevLastRecordIndex % pageSize));
		} else if(GenericConstant.SEARCH_TYPE_PREV.equalsIgnoreCase(criteria.getSearchType())) {
			int prevPageRecordCount = (prevLastRecordIndex % pageSize) + 1;
			startIndex = prevLastRecordIndex - pageSize - prevPageRecordCount + 1;
		} else if(GenericConstant.SEARCH_TYPE_NEXT.equalsIgnoreCase(criteria.getSearchType())) {
			
			startIndex = prevLastRecordIndex + 1;
		} else if(GenericConstant.SEARCH_TYPE_LAST.equalsIgnoreCase(criteria.getSearchType())) {
			
			startIndex = recordCount - (recordCount % pageSize);
		} else {
			startIndex = 0;
		}
		
		if(startIndex < 0) {
			startIndex = 0;
		}
		
		searchQuery.append(" SELECT ")
		.append(selectClause)
		.append(" FROM ")
		.append(fromClause)
		.append(" WHERE ")
		.append(whereClause)
		.append(" ORDER BY ")
		.append(orderbyClause);
		
		if(!criteria.isFetchAllOrders()) {
			searchQuery.append(limitClause);
			queryParam.add(pageSize);
			queryParam.add(startIndex);
		}
		
		List<SearchOrderDetails> orderDetailsList = getJDBCTemplateObject().query(searchQuery.toString(), queryParam.toArray(),
				new AdminSearchMapper());
		
		orderDetailsList = (orderDetailsList == null) ? new ArrayList<SearchOrderDetails>() : orderDetailsList;
		
		int newLastRecordIndex = (recordCount < (startIndex + pageSize)) ? (recordCount - 1) : (startIndex + pageSize - 1);
		
		AdminSearchResult searchResult = new AdminSearchResult();
		searchResult.setOrderDetailsList(orderDetailsList);
		searchResult.setLastRecordIndex(newLastRecordIndex);
		
		if(startIndex == 0) {
			searchResult.setDisableFirstRecordButton(true);
			searchResult.setDisablePrevRecordButton(true);
		}		
		if(newLastRecordIndex >= (recordCount - 1)) {
			searchResult.setDisableLastRecordButton(true);
			searchResult.setDisableNextRecordButton(true);
		}
		
		return searchResult;
	}
	
	
	@Override
	public void updateOrder(OrderDetailsBean bean) throws SystemException {
	
		int[] selectedOrders = bean.getSelectedOrder();
		
		if(selectedOrders != null) {

			for(int i = 0; i < selectedOrders.length; i++) {
				
				int orderId = selectedOrders[i];

				if(StatusConstant.PICKEDUP.toString().equals(bean.getUpdateType()) && bean.getPickedupBy() != 0) {
					
					String updateSQL = "UPDATE ORDER_DETAILS SET UPDATED_ON = ?, PICKED_UP_BY = ? WHERE ORDER_ID = ? AND STATUS = ?";
					getJDBCTemplateObject().update(updateSQL, GenericUtils.getCurrentDateTime(), bean.getPickedupBy(),
							orderId, StatusConstant.ACTIVE.toString());
					
				} else if(StatusConstant.DELIVERED.toString().equals(bean.getUpdateType()) && bean.getDeliveredBy() != 0) {
					
					String updateSQL = "UPDATE ORDER_DETAILS SET UPDATED_ON = ?, DELIVERED_BY = ? WHERE ORDER_ID = ? AND STATUS = ?";
					getJDBCTemplateObject().update(updateSQL, GenericUtils.getCurrentDateTime(), bean.getDeliveredBy(),
							orderId, StatusConstant.ACTIVE.toString());
				}
				
				String insertSQL = "INSERT INTO ORDER_HISTORY (ORDER_ID, UPDATED_ON, COMMENTS, STATUS) VALUES(?, ?, ?, ?)";

				getJDBCTemplateObject().update(insertSQL, orderId, 
						GenericUtils.getCurrentDateTime(), null, bean.getUpdateType());

			}
		}
		
	}
	
	
	@Override
	public void updateOrderDetails(OrderDetailsBean bean) throws SystemException {
	
		String updateSQL = "UPDATE ORDER_DETAILS SET UPDATED_ON = ?, ACTUAL_NO_CLOTHES = ?, TOTAL_PRICE = ? WHERE ORDER_ID = ? AND STATUS = ?";
		
		getJDBCTemplateObject().update(updateSQL, GenericUtils.getCurrentDateTime(), bean.getActualNoClothes(), 
				bean.getTotalPrice(), bean.getOrderId(), StatusConstant.ACTIVE.toString());
	}
	
	
	@Override
	public OrderDetails getOrderDetails(OrderDetailsBean orderDetailsBean) throws SystemException {
		
		String selectSQL = "SELECT DET.ORDER_ID, DET.USER_DETAILS_ID, DET.PICKED_UP_BY, DET.DELIVERED_BY, DET.ORDER_REFERENCE, DET.CREATED_ON, DET.UPDATED_ON, " +
				"DET.PICKUP_DATE, DET.PICKUP_TIME, DET.DELIVERY_DATE, DET.DELIVERY_TIME, DET.TOTAL_PRICE_MULTIPLIER, DET.TOTAL_PRICE, DET.MINIMUM_ORDER_VALUE, DET.DELIVERY_TYPE, HIST.STATUS, DET.ADDRESS_ID, " +
				"DET.APPROX_NO_CLOTHES, DET.ACTUAL_NO_CLOTHES, DET.COUPON_CODE, IFNULL(ELT(FIELD(DET.DONATE_ONLY, 'Y', 'N'),'Yes','No'), 'No') DONATE_ONLY	" +
				"FROM ORDER_DETAILS DET, " +
				"(SELECT * FROM ORDER_HISTORY WHERE ORDER_HISTORY_ID IN (SELECT MAX(ORDER_HISTORY_ID) FROM ORDER_HISTORY WHERE STATUS <> ?  AND ORDER_ID = ? GROUP BY ORDER_ID)) HIST " +
				"WHERE DET.ORDER_ID = HIST.ORDER_ID AND DET.ORDER_ID = ?";

		List<OrderDetails> myOrderDetailsList = getJDBCTemplateObject().query(
				selectSQL, new AdminOrderDetailsMapper(), StatusConstant.DELETED.toString(), orderDetailsBean.getOrderId(), orderDetailsBean.getOrderId());

		OrderDetails orderDetails = null;
		
		if(myOrderDetailsList != null && myOrderDetailsList.size() > 0) {
			orderDetails = myOrderDetailsList.get(0);
		}
		
		return orderDetails;
	}
	
	
	public List<ServiceTypeFullDetails> getServiceTypeFullDetailsList(int orderid) throws SystemException {
		
		String selectSQL = "SELECT SERV.ASSIGNED_VENDOR_ON, SERV.ORDER_STATUS, SERV.SERVICE_TYPE, SERV.SERVICE_TYPE_DESC, " +
				"VEND.VENDOR_NAME, VEND.VENDOR_TYPE, VEND.MOBILE_NO, VEND.EMAIL_ID, VEND.ADDRESS " +
				"FROM (SELECT MAP.ASSIGNED_VENDOR_ID, MAP.ASSIGNED_VENDOR_ON, MAP.ORDER_STATUS, SERV.SERVICE_TYPE, SERV.SERVICE_TYPE_DESC " +
				"		FROM ORDER_SERVICE_MAPPING MAP, SERVICE_TYPE SERV " +
				"		WHERE MAP.SERVICE_TYPE_ID = SERV.SERVICE_TYPE_ID AND MAP.ORDER_ID = ?) SERV " +
				"LEFT OUTER JOIN VENDOR_DETAILS VEND ON SERV.ASSIGNED_VENDOR_ID = VEND.VENDOR_DETAILS_ID";

		List<ServiceTypeFullDetails> serviceTypeFullDetailsList = getJDBCTemplateObject().query(selectSQL, new ServiceTypeFullDetailsMapper(), orderid);
		
		return serviceTypeFullDetailsList;
	}
		
}
