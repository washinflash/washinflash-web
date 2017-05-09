package com.washinflash.rest.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.washinflash.common.exception.BusinessException;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.UserAddress;
import com.washinflash.common.object.model.UserDetails;
import com.washinflash.common.object.request.UserAddressRequest;
import com.washinflash.common.object.request.UserDetailsRequest;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.StatusConstant;
import com.washinflash.rest.dao.UserDAO;
import com.washinflash.rest.dao.jdbctemplate.SpringJDBCTemplate;
import com.washinflash.rest.mapper.UserAddressMapper;
import com.washinflash.rest.mapper.UserDetailsMapper;

@Component
public class UserDAOImpl extends SpringJDBCTemplate implements UserDAO {

	@Override
	public boolean registerUser(UserDetailsRequest userDetailsRequest) throws BusinessException, SystemException {

		boolean success = true;

		checkMobileEmail(userDetailsRequest);

		String insertSQL = "INSERT INTO USER_DETAILS (NAME, EMAIL, MOBILE_NO, PASSWORD, MOB_OTP, MOB_VERIFIED, REFERRAL_CODE, " +
				"FORCE_PASS_RESET, USER_TYPE, CREATED_ON, UPDATED_ON, STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		int noOfRecordsUpdated = getJDBCTemplateObject().update(
				insertSQL, userDetailsRequest.getName(), userDetailsRequest.getEmail(), userDetailsRequest.getMobileNo(),
				GenericUtils.getSHA1EncryptedString(userDetailsRequest.getPassword()), userDetailsRequest.getMobOTP(), 
				GenericConstant.FLAG_NO, getValidReferralCode(), GenericConstant.FLAG_NO, GenericConstant.USER_TYPE_REG,
				GenericUtils.getCurrentDateTime(), GenericUtils.getCurrentDateTime(), StatusConstant.ACTIVE.toString());

		if(noOfRecordsUpdated != 1) {
			success = false;
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}

		return success;		
	}

	@Override
	public void addReferralBonus(int candidateUserDetailsId, String referralCode) throws SystemException {

		String selectUserIdSQL = "SELECT USER_DETAILS_ID FROM USER_DETAILS WHERE REFERRAL_CODE = ? AND STATUS = ? LIMIT 1";
		Integer referrerUserDetailsId = null;		

		List<Map<String, Object>> returnUserIdList = getJDBCTemplateObject().queryForList(selectUserIdSQL, referralCode, StatusConstant.ACTIVE.toString());

		if(returnUserIdList != null && returnUserIdList.size() > 0) {
			referrerUserDetailsId = (Integer) returnUserIdList.get(0).get("USER_DETAILS_ID");
		}


		String selectOfferIdSQL = "SELECT OFFER_DETAILS_ID FROM OFFER_DETAILS WHERE OFFER_TYPE = ? AND STATUS = ? " +
				"AND STR_TO_DATE(?,'%d-%m-%Y') BETWEEN START_DATE AND END_DATE LIMIT 1";
		Integer candidateOfferDetailsId = null;
		Integer referrerOfferDetailsId = null;

		String currDateStr = GenericUtils.getCurrentISTFormatedDate(GenericConstant.DEFAULT_DATE_FORMAT);

		List<Map<String, Object>> candidateOfferDetailsIdList = getJDBCTemplateObject().queryForList(
				selectOfferIdSQL, GenericConstant.OFFER_TYPE_REFERRAL_CANDIDATE, StatusConstant.ACTIVE.toString(), currDateStr);

		if(candidateOfferDetailsIdList != null && candidateOfferDetailsIdList.size() > 0) {
			candidateOfferDetailsId = (Integer) candidateOfferDetailsIdList.get(0).get("OFFER_DETAILS_ID");
		}

		List<Map<String, Object>> referrerOfferDetailsIdList = getJDBCTemplateObject().queryForList(
				selectOfferIdSQL, GenericConstant.OFFER_TYPE_REFERRAL_REFERRER, StatusConstant.ACTIVE.toString(), currDateStr);

		if(referrerOfferDetailsIdList != null && referrerOfferDetailsIdList.size() > 0) {
			referrerOfferDetailsId = (Integer) referrerOfferDetailsIdList.get(0).get("OFFER_DETAILS_ID");
		}

		Calendar cal = GenericUtils.getISTCalendar();
		SimpleDateFormat format = GenericUtils.getISTSimpleDateFormat(GenericConstant.DEFAULT_DATE_FORMAT);
		cal.add(Calendar.DATE, GenericConstant.REFERRAL_VALIDITY_DAYS);
		String validTillDateStr = format.format(cal.getTime());

		String insertSQL = "INSERT INTO REFERRAL_DETAILS (USER_DETAILS_ID, OFFER_DETAILS_ID, VALID_TILL, CANDIDATE_USER_ID, REFERRER_USER_ID, " +
				"REDEEM_STATUS, UPDATED_ON, STATUS) VALUES (?,?,?,?,?,?,?,?)";

		if(referrerUserDetailsId != null && candidateOfferDetailsId != null) {
			//For candidate
			getJDBCTemplateObject().update(insertSQL, candidateUserDetailsId, candidateOfferDetailsId, GenericUtils.getDateFromString(validTillDateStr), 
					null, referrerUserDetailsId, StatusConstant.NOT_REDEEMED.toString(), GenericUtils.getCurrentDateTime(), StatusConstant.ACTIVE.toString());
		}
		if(referrerUserDetailsId != null && referrerOfferDetailsId != null) {
			//For referrer
			getJDBCTemplateObject().update(insertSQL, referrerUserDetailsId, referrerOfferDetailsId, GenericUtils.getDateFromString(validTillDateStr), 
					candidateUserDetailsId, null, StatusConstant.NOT_APPROVED.toString(), GenericUtils.getCurrentDateTime(), StatusConstant.ACTIVE.toString());
		}


	}

	@Override
	public UserDetails registerFacebookUser(UserDetailsRequest userDetailsRequest) throws BusinessException, SystemException {

		checkMobileEmail(userDetailsRequest);

		String insertSQL = "INSERT INTO USER_DETAILS (NAME, EMAIL, MOBILE_NO, MOB_OTP, MOB_VERIFIED, REFERRAL_CODE, " +
				"FORCE_PASS_RESET, USER_TYPE, FACEBOOK_ID, CREATED_ON, UPDATED_ON, STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		int noOfRecordsUpdated = getJDBCTemplateObject().update(
				insertSQL, userDetailsRequest.getName(), userDetailsRequest.getEmail(), userDetailsRequest.getMobileNo(),
				userDetailsRequest.getMobOTP(), GenericConstant.FLAG_YES, getValidReferralCode(), GenericConstant.FLAG_NO, 
				GenericConstant.USER_TYPE_FB, userDetailsRequest.getFacebookId(), GenericUtils.getCurrentDateTime(), 
				GenericUtils.getCurrentDateTime(), StatusConstant.ACTIVE.toString());

		if(noOfRecordsUpdated != 1) {
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}

		return getUserDetails(userDetailsRequest);		
	}


	private void checkMobileEmail(UserDetailsRequest userDetailsRequest) throws BusinessException {

		if(!GenericUtils.isEmpty(userDetailsRequest.getEmail())) {

			String selectEmailSQL = "SELECT COUNT(*) FROM USER_DETAILS WHERE EMAIL = ? AND STATUS = ?";

			int emailUserCount = getJDBCTemplateObject().queryForObject(
					selectEmailSQL, new Object[] {userDetailsRequest.getEmail(), StatusConstant.ACTIVE.toString()}, Integer.class);

			if(emailUserCount > 0) {
				throw new BusinessException(StatusConstant.FAILED, "This email id is already registered with us.");
			}
		}

		if(!GenericUtils.isEmpty(userDetailsRequest.getMobileNo())) {

			String selectMobileSQL = "SELECT COUNT(*) FROM USER_DETAILS WHERE MOBILE_NO = ? AND STATUS = ?";

			int mobUserCount = getJDBCTemplateObject().queryForObject(
					selectMobileSQL, new Object[] {userDetailsRequest.getMobileNo(), StatusConstant.ACTIVE.toString()}, Integer.class);

			if(mobUserCount > 0) {
				throw new BusinessException(StatusConstant.FAILED, "This mobile number is already registered with us.");
			}
		}

		if(!GenericUtils.isEmpty(userDetailsRequest.getReferralCode())) {
			boolean isReferralCodeExist = isReferralCodeExist(userDetailsRequest.getReferralCode().toUpperCase());
			if(!isReferralCodeExist) {
				throw new BusinessException(StatusConstant.FAILED, "Invalid referral code provided");
			}
		}

	}

	@Override
	public UserDetails validateUserDetails(UserDetailsRequest userDetailsRequest) throws BusinessException, SystemException {

		String selectSQL = "SELECT USER_DETAILS_ID, NAME, EMAIL, MOBILE_NO, MOB_VERIFIED, REFERRAL_CODE, FORCE_PASS_RESET, USER_TYPE, FACEBOOK_ID FROM USER_DETAILS " +
				"WHERE (MOBILE_NO = ? OR EMAIL = ?) AND USER_TYPE = ? AND PASSWORD = ? AND STATUS = ?";

		List<UserDetails> userDetailsList = getJDBCTemplateObject().query(
				selectSQL, new UserDetailsMapper(), userDetailsRequest.getMobileNo(), userDetailsRequest.getEmail(), GenericConstant.USER_TYPE_REG,
				GenericUtils.getSHA1EncryptedString(userDetailsRequest.getPassword()), StatusConstant.ACTIVE.toString());

		if(userDetailsList == null || userDetailsList.isEmpty()) {
			throw new BusinessException(StatusConstant.FAILED, "Invalid credentials provided, please try again.");
		}

		UserDetails userDetails = userDetailsList.get(0);
		String sessionToken = getLoggedInUserSession(userDetails.getUserDetailsId());

		if(sessionToken == null) {
			sessionToken = GenericUtils.generateSessionToken();
			createUserSessionRecord(userDetails.getUserDetailsId(), sessionToken);
		}
		userDetails.setSessionToken(sessionToken);

		return userDetails;
	}


	@Override
	public UserDetails validateFacebookUserDetails(UserDetailsRequest userDetailsRequest) throws BusinessException, SystemException {

		String selectSQL = "SELECT USER_DETAILS_ID, NAME, EMAIL, MOBILE_NO, MOB_VERIFIED, REFERRAL_CODE, FORCE_PASS_RESET, USER_TYPE, FACEBOOK_ID FROM USER_DETAILS " +
				"WHERE FACEBOOK_ID = ? AND USER_TYPE = ? AND STATUS = ?";

		List<UserDetails> userDetailsList = getJDBCTemplateObject().query(
				selectSQL, new UserDetailsMapper(), userDetailsRequest.getFacebookId(), GenericConstant.USER_TYPE_FB, StatusConstant.ACTIVE.toString());

		if(userDetailsList == null || userDetailsList.isEmpty()) {

			//May be due to facebook id issue for old users (USER_DETAILS_ID <= 72).. cross check with name if exists update facebook id

			String selectSQL2 = "SELECT USER_DETAILS_ID, NAME, EMAIL, MOBILE_NO, MOB_VERIFIED, REFERRAL_CODE, FORCE_PASS_RESET, USER_TYPE, FACEBOOK_ID FROM USER_DETAILS " +
					"WHERE USER_DETAILS_ID <= 72 AND NAME = ? AND USER_TYPE = ? AND STATUS = ?";

			userDetailsList = getJDBCTemplateObject().query(
					selectSQL2, new UserDetailsMapper(), userDetailsRequest.getName(), GenericConstant.USER_TYPE_FB, StatusConstant.ACTIVE.toString());

			if(userDetailsList != null && userDetailsList.size() == 1 && userDetailsRequest.getFacebookId() != null) {

				UserDetails userDetails = userDetailsList.get(0);

				String updateSQL = "UPDATE USER_DETAILS SET FACEBOOK_ID = ?, UPDATED_ON = ? WHERE USER_DETAILS_ID = ? AND STATUS = ?";
				getJDBCTemplateObject().update(updateSQL, userDetailsRequest.getFacebookId(),
						GenericUtils.getCurrentDateTime(), userDetails.getUserDetailsId(), StatusConstant.ACTIVE.toString());
				
				//Refresh user details data
				
				String selectSQL3 = "SELECT USER_DETAILS_ID, NAME, EMAIL, MOBILE_NO, MOB_VERIFIED, REFERRAL_CODE, FORCE_PASS_RESET, USER_TYPE, FACEBOOK_ID FROM USER_DETAILS " +
						"WHERE FACEBOOK_ID = ? AND USER_TYPE = ? AND STATUS = ?";

				userDetailsList = getJDBCTemplateObject().query(
						selectSQL3, new UserDetailsMapper(), userDetailsRequest.getFacebookId(), GenericConstant.USER_TYPE_FB, StatusConstant.ACTIVE.toString());

			} else {
				return null;
			}

		}

		UserDetails userDetails = userDetailsList.get(0);

		if((!GenericUtils.isEmpty(userDetailsRequest.getName()) && !GenericUtils.isEmpty(userDetailsRequest.getEmail())) && 
				(!userDetails.getName().equals(userDetailsRequest.getName()) || !userDetails.getEmail().equals(userDetailsRequest.getEmail()))) {

			userDetailsRequest.setUserDetailsId(userDetails.getUserDetailsId());
			updateUserDetails(userDetailsRequest);	

			userDetails.setName(userDetailsRequest.getName());
			userDetails.setEmail(userDetailsRequest.getEmail());
		}

		String sessionToken = getLoggedInUserSession(userDetails.getUserDetailsId());

		if(sessionToken == null) {
			sessionToken = GenericUtils.generateSessionToken();
			createUserSessionRecord(userDetails.getUserDetailsId(), sessionToken);
		}
		userDetails.setSessionToken(sessionToken);

		return userDetails;
	}


	@Override
	public UserDetails getUserDetails(UserDetailsRequest userDetailsRequest) throws SystemException {

		String userType = userDetailsRequest.getUserType();
		if(userDetailsRequest.getUserDetailsId() == 0 && (userType == null || userType.trim().length() == 0)) {
			throw new SystemException(StatusConstant.FAILED, "Please provide user type.");
		}


		List<UserDetails> userDetailsList = new ArrayList<>();

		if(GenericConstant.USER_TYPE_FB.equalsIgnoreCase(userType)) {
			String selectSQL = "SELECT USER_DETAILS_ID, NAME, EMAIL, MOBILE_NO, MOB_VERIFIED, REFERRAL_CODE, FORCE_PASS_RESET, USER_TYPE, FACEBOOK_ID FROM USER_DETAILS " +
					"WHERE ((FACEBOOK_ID = ? AND USER_TYPE = ?) OR USER_DETAILS_ID = ?) AND STATUS = ?";

			userDetailsList = getJDBCTemplateObject().query(selectSQL, new UserDetailsMapper(), userDetailsRequest.getFacebookId(), 
					userDetailsRequest.getUserType(), userDetailsRequest.getUserDetailsId(), StatusConstant.ACTIVE.toString());

		} else {
			String selectSQL = "SELECT USER_DETAILS_ID, NAME, EMAIL, MOBILE_NO, MOB_VERIFIED, REFERRAL_CODE, FORCE_PASS_RESET, USER_TYPE, FACEBOOK_ID FROM USER_DETAILS " +
					"WHERE (((MOBILE_NO = ? OR EMAIL = ?) AND USER_TYPE = ?) OR USER_DETAILS_ID = ?) AND STATUS = ?";

			userDetailsList = getJDBCTemplateObject().query(selectSQL, new UserDetailsMapper(), userDetailsRequest.getMobileNo(), 
					userDetailsRequest.getEmail(), userDetailsRequest.getUserType(), userDetailsRequest.getUserDetailsId(), StatusConstant.ACTIVE.toString());
		}

		UserDetails userDetails = userDetailsList.get(0);
		String sessionToken = getLoggedInUserSession(userDetails.getUserDetailsId());

		if(sessionToken == null) {
			sessionToken = GenericUtils.generateSessionToken();
			createUserSessionRecord(userDetails.getUserDetailsId(), sessionToken);
		}
		userDetails.setSessionToken(sessionToken);

		return userDetails;
	}

	@Override
	public boolean updateAddress(UserAddressRequest address) throws BusinessException, SystemException {

		boolean success = true;

		if(address == null) {
			success = false;
			throw new SystemException(StatusConstant.FAILED, "Invalid address provided. Please check and try again.");
		}

		String addressUpdateType = address.getUpdateType();

		if(GenericConstant.RECORD_UPDATE_TYPE_ADD.equalsIgnoreCase(addressUpdateType)) {

			checkAddressValidity(address);

			String insertSQL = "INSERT INTO USER_ADDRESS (USER_DETAILS_ID, NAME, MOBILE_NO, LANDMARK, ADDRESS, PIN_CODE, " +
					"UPDATED_ON, STATUS) VALUES (?,?,?,?,?,?,?,?)";
			int noOfRecordsUpdated = getJDBCTemplateObject().update(
					insertSQL, address.getUserDetailsId(), address.getName(), address.getMobileNo(), address.getLandmark(), 
					address.getAddress(), address.getPinCode(), GenericUtils.getCurrentDateTime(), StatusConstant.ACTIVE.toString());

			if(noOfRecordsUpdated != 1) {
				success = false;
				throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
			}

		} else if (GenericConstant.RECORD_UPDATE_TYPE_UPDATE.equalsIgnoreCase(addressUpdateType)) {

			checkAddressValidity(address);

			String updateSQL = "UPDATE USER_ADDRESS SET STATUS = ?, UPDATED_ON = ? WHERE ADDRESS_ID = ? AND STATUS = ?";
			int noOfRecordsUpdated = getJDBCTemplateObject().update(updateSQL, StatusConstant.DELETED.toString(),
					GenericUtils.getCurrentDateTime(), address.getAddressId(), StatusConstant.ACTIVE.toString());

			String insertSQL = "INSERT INTO USER_ADDRESS (USER_DETAILS_ID, NAME, MOBILE_NO, LANDMARK, ADDRESS, " +
					"PIN_CODE, UPDATED_ON, STATUS) VALUES (?,?,?,?,?,?,?,?)";
			noOfRecordsUpdated = getJDBCTemplateObject().update(
					insertSQL, address.getUserDetailsId(), address.getName(), address.getMobileNo(),
					address.getLandmark(), address.getAddress(), address.getPinCode(), GenericUtils.getCurrentDateTime(), StatusConstant.ACTIVE.toString());

			if(noOfRecordsUpdated < 1) {
				success = false;
				throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
			}

		} else if (GenericConstant.RECORD_UPDATE_TYPE_DELETE.equalsIgnoreCase(addressUpdateType)) {

			String updateSQL = "UPDATE USER_ADDRESS SET STATUS = ?, UPDATED_ON = ? WHERE ADDRESS_ID = ? AND STATUS = ?";
			int noOfRecordsUpdated = getJDBCTemplateObject().update(updateSQL, StatusConstant.DELETED.toString(),
					GenericUtils.getCurrentDateTime(), address.getAddressId(), StatusConstant.ACTIVE.toString());

			if(noOfRecordsUpdated < 1) {
				success = false;
				throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
			} 

		} else {
			success = false;
			throw new SystemException(StatusConstant.FAILED, "Invalid address update type provided. Supported values are A/U/D. " + addressUpdateType);
		}

		return success;
	}


	private void checkAddressValidity(UserAddressRequest address) throws BusinessException {

		String selectAreaSQL = "SELECT COUNT(*) FROM SERVICE_AREA WHERE PIN_CODE = ? AND STATUS = ?";

		int serviceAreaCount = getJDBCTemplateObject().queryForObject(
				selectAreaSQL, new Object[] {address.getPinCode(), StatusConstant.ACTIVE.toString()}, Integer.class);

		if(serviceAreaCount == 0) {
			throw new BusinessException(StatusConstant.FAILED, "Sorry, currently we are not serving in your area. Please check FAQ for more information on our service area.");
		}

	}


	@Override
	public List<UserAddress> getActiveAddressList(UserDetailsRequest userDetailsRequest) throws SystemException {

		String selectSQL = "SELECT ADDR.ADDRESS_ID, ADDR.NAME, ADDR.MOBILE_NO, ADDR.LANDMARK, ADDR.ADDRESS, ADDR.PIN_CODE " +
				"FROM USER_ADDRESS AS ADDR WHERE ADDR.STATUS = ? AND ADDR.USER_DETAILS_ID = ? ORDER BY ADDR.ADDRESS_ID DESC";

		List<UserAddress> addressList = getJDBCTemplateObject().query(selectSQL, new UserAddressMapper(), 
				StatusConstant.ACTIVE.toString(), userDetailsRequest.getUserDetailsId());


		return addressList;
	}

	@Override
	public List<UserAddress> getAddress(UserAddressRequest userAddressRequest) throws SystemException {

		String selectSQL = "SELECT ADDR.ADDRESS_ID, ADDR.NAME, ADDR.MOBILE_NO, ADDR.LANDMARK, ADDR.ADDRESS, ADDR.PIN_CODE " +
				"FROM USER_ADDRESS AS ADDR WHERE ADDR.ADDRESS_ID = ?";

		List<UserAddress> addressList = getJDBCTemplateObject().query(selectSQL, new UserAddressMapper(), userAddressRequest.getAddressId());


		return addressList;
	}

	@Override
	public boolean updateMobileVerified(UserDetailsRequest userDetailsRequest) throws BusinessException, SystemException {

		boolean success = true;
		String selectSQL = "SELECT MOB_OTP FROM USER_DETAILS WHERE USER_DETAILS_ID = ? AND STATUS = ?";

		Integer mobOTP = getJDBCTemplateObject().queryForObject(selectSQL, 
				new Object[] {userDetailsRequest.getUserDetailsId(), StatusConstant.ACTIVE.toString()}, Integer.class);

		if(mobOTP == userDetailsRequest.getMobOTP()) {

			String updateSQL = "UPDATE USER_DETAILS SET MOB_VERIFIED = ?, UPDATED_ON = ? WHERE USER_DETAILS_ID = ? AND STATUS = ?";
			int noOfRecordsUpdated = getJDBCTemplateObject().update(updateSQL, GenericConstant.FLAG_YES,
					GenericUtils.getCurrentDateTime(), userDetailsRequest.getUserDetailsId(), StatusConstant.ACTIVE.toString());

			if(noOfRecordsUpdated != 1) {
				success = false;
				throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
			}	
		} else {
			success = false;
			throw new BusinessException(StatusConstant.FAILED, "Invalid OTP provided");
		}

		return success;
	}


	@Override
	public boolean updateOTP(UserDetailsRequest userDetailsRequest) throws SystemException {

		boolean success = true;

		String updateSQL = "UPDATE USER_DETAILS SET MOB_OTP = ?, MOB_VERIFIED = ?, UPDATED_ON = ? WHERE USER_DETAILS_ID = ? AND STATUS = ?";
		int noOfRecordsUpdated = getJDBCTemplateObject().update(updateSQL, userDetailsRequest.getMobOTP(), GenericConstant.FLAG_NO,
				GenericUtils.getCurrentDateTime(), userDetailsRequest.getUserDetailsId(), StatusConstant.ACTIVE.toString());

		if(noOfRecordsUpdated != 1) {
			success = false;
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}	
		return success;
	}


	@Override
	public boolean updateUserDetails(UserDetailsRequest userDetailsRequest) throws SystemException, BusinessException {
		boolean success = true;

		String name = userDetailsRequest.getName();
		String email = userDetailsRequest.getEmail();

		if(GenericUtils.isEmpty(name) || GenericUtils.isEmpty(email)) {
			success = false;
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}

		String selectSQL = "SELECT COUNT(*) FROM USER_DETAILS WHERE EMAIL = ? AND USER_DETAILS_ID <> ? AND STATUS = ?";

		int userCount = getJDBCTemplateObject().queryForObject(
				selectSQL, new Object[] {email, userDetailsRequest.getUserDetailsId(), StatusConstant.ACTIVE.toString()}, Integer.class);

		if(userCount > 0) {
			success = false;
			throw new BusinessException(StatusConstant.FAILED, "User with this email id is already registered with us.");
		}

		String updateSQL = "UPDATE USER_DETAILS SET NAME = ?, EMAIL = ?, UPDATED_ON = ? WHERE USER_DETAILS_ID = ? AND STATUS = ?";
		int noOfRecordsUpdated = getJDBCTemplateObject().update(updateSQL, name, email,
				GenericUtils.getCurrentDateTime(), userDetailsRequest.getUserDetailsId(), StatusConstant.ACTIVE.toString());

		if(noOfRecordsUpdated != 1) {
			success = false;
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}	

		return success;
	}

	@Override
	public boolean updateUserPassword(UserDetailsRequest userDetailsRequest) throws SystemException, BusinessException {
		boolean success = true;

		String existingPass = userDetailsRequest.getPassword();
		String newPass = userDetailsRequest.getNewPassword();

		if(GenericUtils.isEmpty(existingPass) || GenericUtils.isEmpty(newPass)) {
			success = false;
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}

		String selectSQL = "SELECT COUNT(*) FROM USER_DETAILS WHERE USER_DETAILS_ID = ? AND PASSWORD = ? AND STATUS = ?";

		int userCount = getJDBCTemplateObject().queryForObject(
				selectSQL, new Object[] {userDetailsRequest.getUserDetailsId(), GenericUtils.getSHA1EncryptedString(existingPass),
						StatusConstant.ACTIVE.toString()}, Integer.class);

		if(userCount == 0) {
			success = false;
			throw new BusinessException(StatusConstant.FAILED, "Invalid old password provided.");
		}

		String updateSQL = "UPDATE USER_DETAILS SET PASSWORD = ?, FORCE_PASS_RESET = ?, UPDATED_ON = ? WHERE USER_DETAILS_ID = ? AND STATUS = ?";
		int noOfRecordsUpdated = getJDBCTemplateObject().update(updateSQL, GenericUtils.getSHA1EncryptedString(newPass),
				GenericConstant.FLAG_NO, GenericUtils.getCurrentDateTime(), userDetailsRequest.getUserDetailsId(), StatusConstant.ACTIVE.toString());

		if(noOfRecordsUpdated != 1) {
			success = false;
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}	

		return success;
	}

	@Override
	public boolean updateForgotPassword(UserDetailsRequest userDetailsRequest) throws SystemException, BusinessException {
		boolean success = true;

		String newPass = userDetailsRequest.getNewPassword();

		String selectSQL = "SELECT COUNT(*) FROM USER_DETAILS WHERE MOBILE_NO = ? AND STATUS = ?";

		int userCount = getJDBCTemplateObject().queryForObject(
				selectSQL, new Object[] {userDetailsRequest.getMobileNo(), StatusConstant.ACTIVE.toString()}, Integer.class);

		if(userCount == 0) {
			success = false;
			throw new BusinessException(StatusConstant.FAILED, "Invalid mobile number provided.");
		}

		String updateSQL = "UPDATE USER_DETAILS SET PASSWORD = ?, MOB_VERIFIED = ?, FORCE_PASS_RESET = ?, UPDATED_ON = ? WHERE MOBILE_NO = ? AND USER_TYPE = ? AND STATUS = ?";
		int noOfRecordsUpdated = getJDBCTemplateObject().update(updateSQL, GenericUtils.getSHA1EncryptedString(newPass), GenericConstant.FLAG_YES, 
				GenericConstant.FLAG_YES, GenericUtils.getCurrentDateTime(), userDetailsRequest.getMobileNo(), GenericConstant.USER_TYPE_REG, StatusConstant.ACTIVE.toString());

		if(noOfRecordsUpdated != 1) {
			success = false;
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}	

		return success;
	}


	@Override
	public void logoutUserSession(int userId) throws SystemException {

		String updateSQL = "UPDATE USER_SESSION SET LOGGED_OUT_TIME = ?, STATUS = ? WHERE USER_DETAILS_ID = ? AND STATUS = ?";

		getJDBCTemplateObject().update(updateSQL, GenericUtils.getCurrentDateTime(), StatusConstant.LOGOUT.toString(), 
				userId, StatusConstant.ACTIVE.toString());

	}

	@Override
	public boolean isValidUserSession(String sessionToken) throws SystemException {

		boolean success = false;

		String selectSQL = "SELECT COUNT(*) FROM USER_SESSION WHERE SESSION_TOKEN = ? AND STATUS = ?";

		int sessionCount = getJDBCTemplateObject().queryForObject(selectSQL, 
				new Object[] {sessionToken, StatusConstant.ACTIVE.toString()}, Integer.class);

		if(sessionCount > 0) {
			success = true;
		}

		return success;
	}


	private String getLoggedInUserSession(int userId) throws SystemException {

		String sessionToken = null;
		String selectSQL = "SELECT SESSION_TOKEN FROM USER_SESSION WHERE USER_DETAILS_ID = ? AND STATUS = ? ORDER BY USER_SESSION_ID DESC";

		List<Map<String, Object>> returnList = getJDBCTemplateObject().queryForList(selectSQL, userId, StatusConstant.ACTIVE.toString());

		if(returnList != null && returnList.size() > 0) {
			sessionToken = (String) returnList.get(0).get("SESSION_TOKEN");
		}

		return sessionToken;
	}

	private void createUserSessionRecord(int userDetailsId, String sessionToken) throws SystemException {

		String insertSQL = "INSERT INTO USER_SESSION (USER_DETAILS_ID, SESSION_TOKEN, LOGGED_ON_TIME, STATUS) VALUES (?,?,?,?)";
		int noOfRecordsUpdated = getJDBCTemplateObject().update(
				insertSQL, userDetailsId, sessionToken, GenericUtils.getCurrentDateTime(), StatusConstant.ACTIVE.toString());

		if(noOfRecordsUpdated != 1) {
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}
	}

	private String getValidReferralCode() {

		String referralCode = "";
		boolean isReferralCodeExist = true;

		while(isReferralCodeExist) {
			referralCode = GenericUtils.getReferralCode();
			isReferralCodeExist = isReferralCodeExist(referralCode);
		}

		return referralCode;
	}

	private boolean isReferralCodeExist(String referralCode) {

		boolean exist = false;

		String selectSQL = "SELECT COUNT(*) FROM USER_DETAILS WHERE REFERRAL_CODE = ?";

		int refCodeCount = getJDBCTemplateObject().queryForObject(selectSQL, 
				new Object[] {referralCode}, Integer.class);

		if(refCodeCount > 0) {
			exist = true;
		}

		return exist;
	}
}
