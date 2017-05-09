package com.washinflash.admin.model;

import java.util.List;

import com.washinflash.common.object.model.EmployeeDetails;
import com.washinflash.common.object.model.OrderDetails;
import com.washinflash.common.object.model.ServiceTypeFullDetails;
import com.washinflash.common.object.model.UserAddress;
import com.washinflash.common.object.model.UserDetails;

public class AdminOrderDetails {

	private OrderDetails orderDetails;
	private UserDetails userDetails;
	private UserAddress address;
	
	private EmployeeDetails pickedUpBy;
	private EmployeeDetails deliveredBy;
	
	private List<ServiceTypeFullDetails> serviceTypeFullDetailsList;
	
	private boolean enableOrderDetailsUpdate;

	
	public OrderDetails getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(OrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}
	public UserDetails getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
	public UserAddress getAddress() {
		return address;
	}
	public void setAddress(UserAddress address) {
		this.address = address;
	}
	public List<ServiceTypeFullDetails> getServiceTypeFullDetailsList() {
		return serviceTypeFullDetailsList;
	}
	public void setServiceTypeFullDetailsList(
			List<ServiceTypeFullDetails> serviceTypeFullDetailsList) {
		this.serviceTypeFullDetailsList = serviceTypeFullDetailsList;
	}
	public EmployeeDetails getPickedUpBy() {
		return pickedUpBy;
	}
	public void setPickedUpBy(EmployeeDetails pickedUpBy) {
		this.pickedUpBy = pickedUpBy;
	}
	public EmployeeDetails getDeliveredBy() {
		return deliveredBy;
	}
	public void setDeliveredBy(EmployeeDetails deliveredBy) {
		this.deliveredBy = deliveredBy;
	}
	public boolean isEnableOrderDetailsUpdate() {
		return enableOrderDetailsUpdate;
	}
	public void setEnableOrderDetailsUpdate(boolean enableOrderDetailsUpdate) {
		this.enableOrderDetailsUpdate = enableOrderDetailsUpdate;
	}
	
}
