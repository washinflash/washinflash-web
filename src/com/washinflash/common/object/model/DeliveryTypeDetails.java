package com.washinflash.common.object.model;

public class DeliveryTypeDetails {

	private int deliveryTypeId;
	private String deliveryType;
	private String deliveryTypeDesc;
	private double totalBillMulti;
	
	
	public int getDeliveryTypeId() {
		return deliveryTypeId;
	}
	public void setDeliveryTypeId(int deliveryTypeId) {
		this.deliveryTypeId = deliveryTypeId;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	public String getDeliveryTypeDesc() {
		return deliveryTypeDesc;
	}
	public void setDeliveryTypeDesc(String deliveryTypeDesc) {
		this.deliveryTypeDesc = deliveryTypeDesc;
	}
	public double getTotalBillMulti() {
		return totalBillMulti;
	}
	public void setTotalBillMulti(double totalBillMulti) {
		this.totalBillMulti = totalBillMulti;
	}
	
}
