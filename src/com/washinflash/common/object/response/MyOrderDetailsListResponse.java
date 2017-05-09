package com.washinflash.common.object.response;

import java.util.List;

import com.washinflash.common.object.model.MyOrderDetails;

public class MyOrderDetailsListResponse extends SuccessResponse {

	private List<MyOrderDetails> myOrderDetailsList;

	public List<MyOrderDetails> getMyOrderDetailsList() {
		return myOrderDetailsList;
	}

	public void setMyOrderDetailsList(List<MyOrderDetails> myOrderDetailsList) {
		this.myOrderDetailsList = myOrderDetailsList;
	}
}
