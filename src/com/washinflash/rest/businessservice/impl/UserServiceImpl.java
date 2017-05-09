package com.washinflash.rest.businessservice.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.washinflash.common.exception.BusinessException;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.helper.SMSHelper;
import com.washinflash.common.object.model.UserAddress;
import com.washinflash.common.object.model.UserDetails;
import com.washinflash.common.object.request.UserAddressRequest;
import com.washinflash.common.object.request.UserDetailsRequest;
import com.washinflash.common.object.response.AddressListResponse;
import com.washinflash.common.object.response.LoginResponse;
import com.washinflash.common.object.response.RegistrationResponse;
import com.washinflash.common.object.response.SuccessResponse;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.GsonUtils;
import com.washinflash.rest.businessservice.UserService;
import com.washinflash.rest.dao.UserDAO;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDAO userDAO;


	@Override
	//@Transactional(propagation=Propagation.REQUIRED, readOnly=false, rollbackFor=Exception.class)
	public String registerUser(String reqJsonString) throws SystemException {

		UserDetailsRequest userDetailsRequest = (UserDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserDetailsRequest.class);
		String jsonResponse = "";

		try {		
			RegistrationResponse response = new RegistrationResponse();
			userDetailsRequest.setUserType(GenericConstant.USER_TYPE_REG);
			int otp = GenericUtils.getMobileOTP();

			userDetailsRequest.setMobOTP(otp);

			boolean regSuccess = userDAO.registerUser(userDetailsRequest);

			if(regSuccess) {
				UserDetails userDetails = userDAO.getUserDetails(userDetailsRequest);
				response.setUserDetails(userDetails);
				
				if(!GenericUtils.isEmpty(userDetailsRequest.getReferralCode())) {
					userDAO.addReferralBonus(userDetails.getUserDetailsId(), userDetailsRequest.getReferralCode());
				}

				boolean smsSuccess = sendRegistrationOTP(otp, userDetails.getMobileNo());

				if(smsSuccess) {
					response.setOTPSendSuccess(GenericConstant.FLAG_YES);
				} else {
					response.setOTPSendSuccess(GenericConstant.FLAG_NO);
				}
				jsonResponse = GsonUtils.toJsonString(response);
			} else {
				jsonResponse = GenericUtils.getSystemFailureJsonResponse();
			}

		} catch (BusinessException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getBusinessFailureJsonResponse(be);
		}	

		return jsonResponse;	
	}


	@Override
	public String registerFacebookUser(String reqJsonString) throws SystemException {

		UserDetailsRequest userDetailsRequest = (UserDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserDetailsRequest.class);
		String jsonResponse = "";

		try {
			LoginResponse response = new LoginResponse();

			UserDetails userDetails = userDAO.registerFacebookUser(userDetailsRequest);
			response.setUserDetails(userDetails);

			jsonResponse = GsonUtils.toJsonString(response);
		} catch (BusinessException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getBusinessFailureJsonResponse(be);
		}	

		return jsonResponse;	
	}


	@Override
	public String resendFreshOTP(String reqJsonString) throws SystemException {

		UserDetailsRequest userDetailsRequest = (UserDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserDetailsRequest.class);
		String jsonResponse = "";

		try {
			int otp = GenericUtils.getMobileOTP();
			userDetailsRequest.setMobOTP(otp);
			userDetailsRequest.setUserType(GenericConstant.USER_TYPE_REG);
			boolean success = userDAO.updateOTP(userDetailsRequest);
			RegistrationResponse response = new RegistrationResponse();

			if(success) {	
				UserDetails userDetails = userDAO.getUserDetails(userDetailsRequest);
				response.setUserDetails(userDetails);				

				boolean smsSuccess = sendRegistrationOTP(otp, userDetails.getMobileNo());

				if(smsSuccess) {
					response.setOTPSendSuccess(GenericConstant.FLAG_YES);
				} else {
					response.setOTPSendSuccess(GenericConstant.FLAG_NO);
				}
				jsonResponse = GsonUtils.toJsonString(response);
			} else {
				jsonResponse = GenericUtils.getSystemFailureJsonResponse();
			}

		} catch (SystemException se) {
			log.debug(se.getCause() + se.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;	
	}


	@Override
	public String sendOTPForFacebookReg(String reqJsonString) throws SystemException {

		UserDetailsRequest userDetailsRequest = (UserDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserDetailsRequest.class);

		int otp = GenericUtils.getMobileOTP();
		RegistrationResponse response = new RegistrationResponse();		

		boolean smsSuccess = sendRegistrationOTP(otp, userDetailsRequest.getMobileNo());

		if(smsSuccess) {
			response.setOTPSendSuccess(GenericConstant.FLAG_YES);
			UserDetails userDetails = new UserDetails();
			userDetails.setMobOTP(otp);
			response.setUserDetails(userDetails);
		} else {
			response.setOTPSendSuccess(GenericConstant.FLAG_NO);
		}

		return GsonUtils.toJsonString(response);	
	}


	@Override
	public String validateUserDetails(String reqJsonString) throws SystemException {

		UserDetailsRequest userDetailsRequest = (UserDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserDetailsRequest.class);
		String jsonResponse = "";

		try {
			UserDetails userDetails = userDAO.validateUserDetails(userDetailsRequest);
			LoginResponse response = new LoginResponse();		
			response.setUserDetails(userDetails);
			jsonResponse = GsonUtils.toJsonString(response);

		} catch (BusinessException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getBusinessFailureJsonResponse(be);
		}	

		return jsonResponse;
	}
	
	@Override
	public String validateFacebookUserDetails(String reqJsonString) throws SystemException {

		UserDetailsRequest userDetailsRequest = (UserDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserDetailsRequest.class);
		String jsonResponse = "";

		try {
			UserDetails userDetails = userDAO.validateFacebookUserDetails(userDetailsRequest);
			LoginResponse response = new LoginResponse();
			response.setUserDetails(userDetails);
			
			jsonResponse = GsonUtils.toJsonString(response);

		} catch (BusinessException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getBusinessFailureJsonResponse(be);
		}	

		return jsonResponse;
	}

	@Override
	public String updateAddress(String reqJsonString) throws SystemException {

		UserAddressRequest userAddress = (UserAddressRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserAddressRequest.class);
		String jsonResponse = "";
		boolean success = false;

		try {
			success = userDAO.updateAddress(userAddress);
			if(success) {
				jsonResponse = GenericUtils.getDefaultSuccessJsonResponse();
			} else {
				jsonResponse = GenericUtils.getSystemFailureJsonResponse();
			}

		} catch (BusinessException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getBusinessFailureJsonResponse(be);
		}	

		return jsonResponse;
	}

	@Override
	public String getActiveAddressList(String reqJsonString) throws SystemException {

		UserDetailsRequest userDetailsRequest = (UserDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserDetailsRequest.class);
		String jsonResponse = "";

		try {

			List<UserAddress> addressList = userDAO.getActiveAddressList(userDetailsRequest);
			AddressListResponse response = new AddressListResponse();
			response.setAddressList(addressList);
			jsonResponse = GsonUtils.toJsonString(response);

		} catch (SystemException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;
	}

	@Override
	public String getAddress(String reqJsonString) throws SystemException {

		UserAddressRequest userAddressRequest = (UserAddressRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserAddressRequest.class);
		String jsonResponse = "";

		try {

			List<UserAddress> addressList = userDAO.getAddress(userAddressRequest);
			AddressListResponse response = new AddressListResponse();
			response.setAddressList(addressList);
			jsonResponse = GsonUtils.toJsonString(response);

		} catch (SystemException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;
	}



	@Override
	public String updateMobileVerified(String reqJsonString) throws SystemException {

		UserDetailsRequest userDetailsRequest = (UserDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserDetailsRequest.class);
		String jsonResponse = "";
		boolean success = false;

		try {
			LoginResponse response = new LoginResponse();
			userDetailsRequest.setUserType(GenericConstant.USER_TYPE_REG);
			success = userDAO.updateMobileVerified(userDetailsRequest);
			if(success) {
				UserDetails userDetails = userDAO.getUserDetails(userDetailsRequest);
				response.setUserDetails(userDetails);
				jsonResponse = GsonUtils.toJsonString(response);
			} else {
				jsonResponse = GenericUtils.getSystemFailureJsonResponse();
			}

		} catch (BusinessException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getBusinessFailureJsonResponse(be);
		}	

		return jsonResponse;
	}


	@Override
	public String updateUserDetails(String reqJsonString) throws SystemException {

		UserDetailsRequest userDetailsRequest = (UserDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserDetailsRequest.class);
		String jsonResponse = "";

		try {
			boolean success = userDAO.updateUserDetails(userDetailsRequest);
			if(success) {
				jsonResponse = GenericUtils.getDefaultSuccessJsonResponse();
			} else {
				jsonResponse = GenericUtils.getSystemFailureJsonResponse();
			}

		} catch (BusinessException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getBusinessFailureJsonResponse(be);
		}	

		return jsonResponse;
	}


	@Override
	public String updateUserPassword(String reqJsonString) throws SystemException {

		UserDetailsRequest userDetailsRequest = (UserDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserDetailsRequest.class);
		String jsonResponse = "";

		try {
			boolean success = userDAO.updateUserPassword(userDetailsRequest);
			if(success) {
				userDAO.logoutUserSession(userDetailsRequest.getUserDetailsId());
				UserDetails userDetails = userDAO.getUserDetails(userDetailsRequest);

				LoginResponse response = new LoginResponse();
				response.setUserDetails(userDetails);

				jsonResponse = GsonUtils.toJsonString(response);
			} else {
				jsonResponse = GenericUtils.getSystemFailureJsonResponse();
			}

		} catch (BusinessException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getBusinessFailureJsonResponse(be);
		}	

		return jsonResponse;
	}


	@Override
	public String forgotPassword(String reqJsonString) throws SystemException {

		UserDetailsRequest userDetailsRequest = (UserDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserDetailsRequest.class);
		String jsonResponse = "";

		try {			
			String newPassword = GenericUtils.generatePassword();
			userDetailsRequest.setNewPassword(newPassword);

			boolean updateSuccess = userDAO.updateForgotPassword(userDetailsRequest);

			if(updateSuccess) {
				boolean smsSuccess = sendForgotPasswordSMS(newPassword, userDetailsRequest.getMobileNo());

				if(smsSuccess) {
					SuccessResponse response = new SuccessResponse("New password has been sent to your registered mobile number. Please login with the new password.");
					jsonResponse = GsonUtils.toJsonString(response);
				} else {
					jsonResponse = GenericUtils.getSystemFailureJsonResponse();
				}
			} else {
				jsonResponse = GenericUtils.getSystemFailureJsonResponse();
			}

		} catch (BusinessException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getBusinessFailureJsonResponse(be);
		}	

		return jsonResponse;
	}


	@Override
	public String logoutUserSession(String reqJsonString) throws SystemException {

		UserDetailsRequest userDetailsRequest = (UserDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, UserDetailsRequest.class);
		String jsonResponse = "";
		try {	

			userDAO.logoutUserSession(userDetailsRequest.getUserDetailsId());
			jsonResponse = GenericUtils.getDefaultSuccessJsonResponse();

		} catch (SystemException se) {
			log.debug(se.getCause() + se.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;
	}


	private boolean sendRegistrationOTP(int otp, String toMob) {		

		SMSHelper helper = new SMSHelper();
		String msg = "The OTP for your account activation is : " + otp + ". Thank you for the registration.";
		boolean smsSuccess = helper.sendSMS(GenericConstant.SMS_FROM, toMob, msg);

		return smsSuccess;
	}

	private boolean sendForgotPasswordSMS(String newPassword, String toMob) {	

		SMSHelper helper = new SMSHelper();
		String msg = "The new password is : " + newPassword + ". Please login with the new password.";
		boolean smsSuccess = helper.sendSMS(GenericConstant.SMS_FROM, toMob, msg);

		return smsSuccess;
	}

}
