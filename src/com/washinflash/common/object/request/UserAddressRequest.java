package com.washinflash.common.object.request;

import com.washinflash.common.object.model.UserAddress;

public class UserAddressRequest extends UserAddress {

	private String token;
	private CommonInfo commonInfo;
	
	private String updateType;
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	public String getUpdateType() {
		return updateType;
	}
	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}
	public CommonInfo getCommonInfo() {
		return commonInfo;
	}

	public void setCommonInfo(CommonInfo commonInfo) {
		this.commonInfo = commonInfo;
	}
}
