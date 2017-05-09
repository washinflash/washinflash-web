package com.washinflash.admin.businessservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.washinflash.admin.businessservice.OrderAdminService;
import com.washinflash.admin.dao.OrderAdminDAO;
import com.washinflash.admin.dao.UserAdminDAO;
import com.washinflash.admin.model.AdminOrderDetails;
import com.washinflash.admin.model.AdminSearchCriteria;
import com.washinflash.admin.model.AdminSearchResult;
import com.washinflash.admin.model.OrderDetailsBean;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.helper.EmailHelper;
import com.washinflash.common.helper.SMSHelper;
import com.washinflash.common.object.model.EmployeeDetails;
import com.washinflash.common.object.model.OrderDetails;
import com.washinflash.common.object.model.ServiceTypeFullDetails;
import com.washinflash.common.object.model.UserAddress;
import com.washinflash.common.object.model.UserDetails;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.StatusConstant;

@Service
public class OrderAdminServiceImpl implements OrderAdminService {

	@Autowired
	private OrderAdminDAO orderAdminDAO;	
	@Autowired
	private UserAdminDAO userAdminDAO;

	
	@Override
	public AdminSearchResult searchOrder(AdminSearchCriteria criteria) throws SystemException {
		
		return orderAdminDAO.searchOrder(criteria);
	}
	
	
	@Override
	public AdminOrderDetails getAdminOrderDetails(OrderDetailsBean orderDetailsBean) throws SystemException {
		
		AdminOrderDetails adminOrderDetails = new AdminOrderDetails();
		
		OrderDetails orderDetails = orderAdminDAO.getOrderDetails(orderDetailsBean);
		adminOrderDetails.setOrderDetails(orderDetails);
		
		UserDetails userDetails = userAdminDAO.getUserDetails(orderDetails.getUserDetailsId());
		adminOrderDetails.setUserDetails(userDetails);
		
		UserAddress userAddress = userAdminDAO.getAddress(orderDetails.getAddressId());
		adminOrderDetails.setAddress(userAddress);
		
		List<ServiceTypeFullDetails> serviceTypeFullDetailsList = orderAdminDAO.getServiceTypeFullDetailsList(orderDetails.getOrderId());
		adminOrderDetails.setServiceTypeFullDetailsList(serviceTypeFullDetailsList);
		
		if(orderDetails.getPickedUpBy() != 0) {
			EmployeeDetails employeeDetails = userAdminDAO.getEmployeeDetails(orderDetails.getPickedUpBy());
			adminOrderDetails.setPickedUpBy(employeeDetails);
		}

		if(orderDetails.getDeliveredBy() != 0) {
			EmployeeDetails employeeDetails = userAdminDAO.getEmployeeDetails(orderDetails.getDeliveredBy());
			adminOrderDetails.setDeliveredBy(employeeDetails);
		}
		
		boolean enableOrderDetailsUpdate = false;
		if(!orderDetails.getLatestStatus().equals(StatusConstant.CANCELLED.toString()) 
				&& !orderDetails.getLatestStatus().equals(StatusConstant.DELIVERED.toString())) {
			enableOrderDetailsUpdate = true;
		}
		adminOrderDetails.setEnableOrderDetailsUpdate(enableOrderDetailsUpdate);
		
		return adminOrderDetails;
	}
	
	@Override
	public void updateOrder(OrderDetailsBean bean) throws SystemException {
		
		orderAdminDAO.updateOrder(bean);
		
		if(StatusConstant.DELIVERED.toString().equals(bean.getUpdateType())) {

			int[] selectedOrders = bean.getSelectedOrder();
			if(selectedOrders != null) {
				for(int i = 0; i < selectedOrders.length; i++) {

					int orderId = selectedOrders[i];
					OrderDetailsBean orderDetailsBean = new OrderDetailsBean();
					orderDetailsBean.setOrderId(orderId);
					
					OrderDetails orderDetails = orderAdminDAO.getOrderDetails(orderDetailsBean);
					UserDetails userDetails = userAdminDAO.getUserDetails(orderDetails.getUserDetailsId());
					
					if(StatusConstant.DELIVERED.toString().equals(orderDetails.getLatestStatus())) {
						sendDeliveredOrderEmail(orderDetails.getOrderRef(), userDetails.getEmail());
						sendDeliveredOrderSMS(orderDetails.getOrderRef(), userDetails.getMobileNo());
					}
				}
			}
		}
	}
	
	@Override
	public void updateOrderDetails(OrderDetailsBean bean) throws SystemException {
		
		orderAdminDAO.updateOrderDetails(bean);
	}
	
	
	private void sendDeliveredOrderEmail(String refNo, String emailTo) {	
		
		EmailHelper helper = new EmailHelper();
		String msg = "Your order with refference no " + refNo + " has been successfully delivered.\n\n Thanks,\nWashinflash Team";
		String emailSub = "Order Delivered : " + refNo;
		
		helper.sendEmail(GenericConstant.EMAIL_FROM, emailTo, emailSub, msg);
		
	}
	
	private boolean sendDeliveredOrderSMS(String refNo, String toMob) {	

		SMSHelper helper = new SMSHelper();
		String msg = "Your order with refference no " + refNo + " has been successfully delivered.";
		boolean smsSuccess = helper.sendSMS(GenericConstant.SMS_FROM, toMob, msg);

		return smsSuccess;
	}
}
