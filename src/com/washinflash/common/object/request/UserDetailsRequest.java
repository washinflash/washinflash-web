package com.washinflash.common.object.request;

import com.washinflash.common.object.model.UserDetails;


public class UserDetailsRequest extends UserDetails {

	private String token;
	private CommonInfo commonInfo;
	
	private String newPassword;
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public CommonInfo getCommonInfo() {
		return commonInfo;
	}

	public void setCommonInfo(CommonInfo commonInfo) {
		this.commonInfo = commonInfo;
	}
}