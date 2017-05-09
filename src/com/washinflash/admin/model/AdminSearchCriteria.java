package com.washinflash.admin.model;


public class AdminSearchCriteria {

	private String latestStatus;
	private String pickupDate;
	private String pickupTime;
	private String deliveryDate;
	private String deliveryTime;
	private String deliveryType;
	private String serviceType;
	
	private String specialField;
	
	private int lastRecordIndex;
	private String searchType;
	
	private String custPickupDate;
	private String vendPickupDate;
	private int vendorId;
	
	private boolean fetchAllOrders;
	

	public String getLatestStatus() {
		return latestStatus;
	}

	public void setLatestStatus(String latestStatus) {
		this.latestStatus = latestStatus;
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

	public String getSpecialField() {
		return specialField;
	}

	public void setSpecialField(String specialField) {
		this.specialField = specialField;
	}

	public int getLastRecordIndex() {
		return lastRecordIndex;
	}

	public void setLastRecordIndex(int lastRecordIndex) {
		this.lastRecordIndex = lastRecordIndex;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getCustPickupDate() {
		return custPickupDate;
	}

	public void setCustPickupDate(String custPickupDate) {
		this.custPickupDate = custPickupDate;
	}

	public String getVendPickupDate() {
		return vendPickupDate;
	}

	public void setVendPickupDate(String vendPickupDate) {
		this.vendPickupDate = vendPickupDate;
	}

	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public boolean isFetchAllOrders() {
		return fetchAllOrders;
	}

	public void setFetchAllOrders(boolean fetchAllOrders) {
		this.fetchAllOrders = fetchAllOrders;
	}	
	
}
