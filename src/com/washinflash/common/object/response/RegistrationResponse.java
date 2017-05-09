package com.washinflash.common.object.response;

import com.washinflash.common.object.model.UserDetails;


public class RegistrationResponse extends SuccessResponse {

	private UserDetails userDetails;
	private String OTPSendSuccess;

	
	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public String getOTPSendSuccess() {
		return OTPSendSuccess;
	}

	public void setOTPSendSuccess(String oTPSendSuccess) {
		OTPSendSuccess = oTPSendSuccess;
	}

}
