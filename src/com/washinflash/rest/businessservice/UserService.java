package com.washinflash.rest.businessservice;

import com.washinflash.common.exception.SystemException;

public interface UserService {
	
	public String registerUser(String reqJsonString) throws SystemException;
	public String registerFacebookUser(String reqJsonString) throws SystemException;
	public String validateUserDetails(String reqJsonString) throws SystemException;
	public String validateFacebookUserDetails(String reqJsonString) throws SystemException;
	public String updateAddress(String reqJsonString) throws SystemException;
	public String getActiveAddressList(String reqJsonString) throws SystemException;
	public String getAddress(String reqJsonString) throws SystemException;		
	public String updateMobileVerified(String reqJsonString) throws SystemException;	
	public String resendFreshOTP(String reqJsonString) throws SystemException;
	public String sendOTPForFacebookReg(String reqJsonString) throws SystemException;
	public String updateUserDetails(String reqJsonString) throws SystemException;
	public String updateUserPassword(String reqJsonString) throws SystemException;
	public String forgotPassword(String reqJsonString) throws SystemException;
	public String logoutUserSession(String reqJsonString) throws SystemException;
}
