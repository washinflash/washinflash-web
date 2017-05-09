package com.washinflash.common.object.response;


public class CreateOrderResponse extends SuccessResponse {

	private String orderRef;
	private String successMessage;

	
	public String getOrderRef() {
		return orderRef;
	}

	public void setOrderRef(String orderRef) {
		this.orderRef = orderRef;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	
}
