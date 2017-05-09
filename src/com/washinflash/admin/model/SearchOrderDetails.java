package com.washinflash.admin.model;


public class SearchOrderDetails {
	
	private int orderId;
	private String orderRef;
	private String latestStatus;
	private String name;
	private String mobileNo;
	private String serviceAreaName;	
	private int pinCode;
	private String pickupDate;
	private String pickupTime;
	private String deliveryDate;
	private String deliveryTime;
	private String deliveryType;

	private String serviceType;

	private int  orderServiceMappingId;
	private String vendorPickupDate;
	private String serviceStatus;
	
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderRef() {
		return orderRef;
	}

	public void setOrderRef(String orderRef) {
		this.orderRef = orderRef;
	}

	public String getLatestStatus() {
		return latestStatus;
	}

	public void setLatestStatus(String latestStatus) {
		this.latestStatus = latestStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getServiceAreaName() {
		return serviceAreaName;
	}

	public void setServiceAreaName(String serviceAreaName) {
		this.serviceAreaName = serviceAreaName;
	}

	public int getPinCode() {
		return pinCode;
	}

	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}

	public String getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public int getOrderServiceMappingId() {
		return orderServiceMappingId;
	}

	public void setOrderServiceMappingId(int orderServiceMappingId) {
		this.orderServiceMappingId = orderServiceMappingId;
	}

	public String getVendorPickupDate() {
		return vendorPickupDate;
	}

	public void setVendorPickupDate(String vendorPickupDate) {
		this.vendorPickupDate = vendorPickupDate;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	
}
