package com.washinflash.common.object.model;


public class OrderDetails {

	private int orderId;
	private int userDetailsId;
	private int addressId;
	private String deliveryType;
	private String orderDate;
	private String updateTime;
	private String pickupDate;
	private String pickupTime;
	private String deliveryDate;
	private String deliveryTime;
	private String orderRef;
	private double totalPrice;
	private double totalPriceMultiplier;
	private int minimumOrderValue;
	private int approxNoClothes;
	private int actualNoClothes;
	private String couponCode;
	private String specialInstruction;
	private String donateOnly;
	private String latestStatus;
	
	private int pickedUpBy;
	private int deliveredBy;
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getUserDetailsId() {
		return userDetailsId;
	}
	public void setUserDetailsId(int userDetailsId) {
		this.userDetailsId = userDetailsId;
	}
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
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
	public String getOrderRef() {
		return orderRef;
	}
	public void setOrderRef(String orderRef) {
		this.orderRef = orderRef;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public double getTotalPriceMultiplier() {
		return totalPriceMultiplier;
	}
	public void setTotalPriceMultiplier(double totalPriceMultiplier) {
		this.totalPriceMultiplier = totalPriceMultiplier;
	}
	public int getMinimumOrderValue() {
		return minimumOrderValue;
	}
	public void setMinimumOrderValue(int minimumOrderValue) {
		this.minimumOrderValue = minimumOrderValue;
	}
	public int getApproxNoClothes() {
		return approxNoClothes;
	}
	public void setApproxNoClothes(int approxNoClothes) {
		this.approxNoClothes = approxNoClothes;
	}
	public int getActualNoClothes() {
		return actualNoClothes;
	}
	public void setActualNoClothes(int actualNoClothes) {
		this.actualNoClothes = actualNoClothes;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getSpecialInstruction() {
		return specialInstruction;
	}
	public void setSpecialInstruction(String specialInstruction) {
		this.specialInstruction = specialInstruction;
	}
	public String getDonateOnly() {
		return donateOnly;
	}
	public void setDonateOnly(String donateOnly) {
		this.donateOnly = donateOnly;
	}
	public String getLatestStatus() {
		return latestStatus;
	}
	public void setLatestStatus(String latestStatus) {
		this.latestStatus = latestStatus;
	}
	public int getPickedUpBy() {
		return pickedUpBy;
	}
	public void setPickedUpBy(int pickedUpBy) {
		this.pickedUpBy = pickedUpBy;
	}
	public int getDeliveredBy() {
		return deliveredBy;
	}
	public void setDeliveredBy(int deliveredBy) {
		this.deliveredBy = deliveredBy;
	}
	
}
