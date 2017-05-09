package com.washinflash.rest.dao;

import java.util.List;

import com.washinflash.common.exception.BusinessException;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.UserAddress;
import com.washinflash.common.object.model.UserDetails;
import com.washinflash.common.object.request.UserAddressRequest;
import com.washinflash.common.object.request.UserDetailsRequest;

public interface UserDAO {

	public boolean registerUser(UserDetailsRequest userDetailsRequest) throws BusinessException, SystemException;
	public UserDetails registerFacebookUser(UserDetailsRequest userDetailsRequest) throws BusinessException, SystemException;
	public UserDetails validateUserDetails(UserDetailsRequest userDetailsRequest) throws BusinessException, SystemException;
	public UserDetails validateFacebookUserDetails(UserDetailsRequest userDetailsRequest) throws BusinessException, SystemException;
	public UserDetails getUserDetails(UserDetailsRequest userDetailsRequest) throws SystemException;	
	public boolean updateAddress(UserAddressRequest userDetailsRequest) throws BusinessException, SystemException;
	public List<UserAddress> getActiveAddressList(UserDetailsRequest userDetailsRequest) throws SystemException;
	public List<UserAddress> getAddress(UserAddressRequest userAddressRequest) throws SystemException;	
	public boolean updateMobileVerified(UserDetailsRequest userDetailsRequest) throws BusinessException, SystemException;
	public boolean updateOTP(UserDetailsRequest userDetailsRequest) throws SystemException;
	public boolean updateUserDetails(UserDetailsRequest userDetailsRequest) throws SystemException, BusinessException;
	public boolean updateUserPassword(UserDetailsRequest userDetailsRequest) throws SystemException, BusinessException;
	public boolean updateForgotPassword(UserDetailsRequest userDetailsRequest) throws SystemException, BusinessException;
	public boolean isValidUserSession(String sessionToken) throws SystemException;	
	public void logoutUserSession(int userId) throws SystemException;
	public void addReferralBonus(int userDetailsId, String referralCode) throws SystemException;	
}
