package com.washinflash.common.object.model;

public class ServiceDeliveryMapDetails {

	private int serviceDelMapId;
	private int serviceTypeId;
	private int deliveryTypeId;
	private int deliveryTime;
	private String deliveryTimeStr;
	
	
	public int getServiceDelMapId() {
		return serviceDelMapId;
	}
	public void setServiceDelMapId(int serviceDelMapId) {
		this.serviceDelMapId = serviceDelMapId;
	}
	public int getServiceTypeId() {
		return serviceTypeId;
	}
	public void setServiceTypeId(int serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	public int getDeliveryTypeId() {
		return deliveryTypeId;
	}
	public void setDeliveryTypeId(int deliveryTypeId) {
		this.deliveryTypeId = deliveryTypeId;
	}
	public int getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(int deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getDeliveryTimeStr() {
		return deliveryTimeStr;
	}
	public void setDeliveryTimeStr(String deliveryTimeStr) {
		this.deliveryTimeStr = deliveryTimeStr;
	}
	
}
