package com.washinflash.admin.model;

public class OrderDetailsBean {

	private int[] selectedOrder;
	private String  updateType;
	
	private int  pickedupBy;
	private int  deliveredBy;

	private int[] selectedOrderService;
	private int  assignToVendor;
	
	private int orderId;
	private int actualNoClothes;
	private double totalPrice;
	
	
	public int[] getSelectedOrder() {
		return selectedOrder;
	}
	public void setSelectedOrder(int[] selectedOrder) {
		this.selectedOrder = selectedOrder;
	}
	public String getUpdateType() {
		return updateType;
	}
	public int getPickedupBy() {
		return pickedupBy;
	}
	public void setPickedupBy(int pickedupBy) {
		this.pickedupBy = pickedupBy;
	}
	public int getDeliveredBy() {
		return deliveredBy;
	}
	public void setDeliveredBy(int deliveredBy) {
		this.deliveredBy = deliveredBy;
	}
	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}
	public int[] getSelectedOrderService() {
		return selectedOrderService;
	}
	public void setSelectedOrderService(int[] selectedOrderService) {
		this.selectedOrderService = selectedOrderService;
	}
	public int getAssignToVendor() {
		return assignToVendor;
	}
	public void setAssignToVendor(int assignToVendor) {
		this.assignToVendor = assignToVendor;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getActualNoClothes() {
		return actualNoClothes;
	}
	public void setActualNoClothes(int actualNoClothes) {
		this.actualNoClothes = actualNoClothes;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
