package com.washinflash.common.object.response;

import java.util.List;

import com.washinflash.common.object.model.UserAddress;

public class AddressListResponse extends SuccessResponse {

	private List<UserAddress> addressList;

	
	public List<UserAddress> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<UserAddress> addressList) {
		this.addressList = addressList;
	}
	
}
