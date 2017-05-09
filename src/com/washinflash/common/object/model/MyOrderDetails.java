package com.washinflash.common.object.model;

import java.util.List;

public class MyOrderDetails {

	private OrderTrackDetails orderTrackDetails;
	private OrderDetails orderDetails;
	private UserAddress address;
	private List<String> serviceTypeList;
	
	
	public OrderTrackDetails getOrderTrackDetails() {
		return orderTrackDetails;
	}
	public void setOrderTrackDetails(OrderTrackDetails orderTrackDetails) {
		this.orderTrackDetails = orderTrackDetails;
	}
	public OrderDetails getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(OrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}
	public UserAddress getAddress() {
		return address;
	}
	public void setAddress(UserAddress address) {
		this.address = address;
	}
	public List<String> getServiceTypeList() {
		return serviceTypeList;
	}
	public void setServiceTypeList(List<String> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

}
