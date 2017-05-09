package com.washinflash.common.util;

import java.util.TimeZone;

public final class GenericConstant {

	public static final String DEFAULT_SUCCESS_MSG = "Successful";
	public static final String SYSTEM_EXEPTION_MSG = "Something went wrong, please try again.";
	public static final String INVALID_TOKEN_ERROR_MSG = "You are not authorized for this request.";
	public static final String INVALID_SESSION_ERROR_MSG = "Your session has expired. Please login to continue.";	
	
	public static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
	public static final String DEFAULT_DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
	public static final String DISPLAY_DATE_FORMAT = "dd MMM, EEE";
	public static final String DISPLAY_DATE_YEAR_FORMAT = "dd MMMM, yyyy HH:mm:ss";	
	
	public static final String FLAG_YES = "Y";
	public static final String FLAG_NO = "N";
	
	public static final String SHOW_NA = "NA";

	public static final String OFFER_TYPE_REFERRAL_CANDIDATE = "REFERRAL_CANDIDATE";
	public static final String OFFER_TYPE_REFERRAL_REFERRER = "REFERRAL_REFERRER";

	public static final int REFERRAL_VALIDITY_DAYS = 180;
	
	public static final int SEARCH_PAGE_SIZE = 12;
	
	public static final String DATA_SPLIT_STR = "##@@";
	
	public static final String SEARCH_TYPE_SEARCH = "SEARCH";
	public static final String SEARCH_TYPE_REFRESH = "REFRESH";
	public static final String SEARCH_TYPE_FIRST = "FIRST";
	public static final String SEARCH_TYPE_PREV = "PREV";
	public static final String SEARCH_TYPE_NEXT = "NEXT";
	public static final String SEARCH_TYPE_LAST = "LAST";
	
	public static final String ALL_OPEN_STATUS = "OPEN";
	public static final String DONATE_ONLY = "DONATE_ONLY";
	public static final String DONATE_CHARITY = "Donate Charity";
	
	public static final String EMP_TYPE_DELIVERY_RES = "DELIVERY_RES";
	public static final String EMP_TYPE_ADMIN = "ADMIN";
	
	public static TimeZone IST_TIME_ZONE = TimeZone.getTimeZone("IST");
	
	public static final String RECORD_UPDATE_TYPE_ADD = "A";
	public static final String RECORD_UPDATE_TYPE_UPDATE = "U";
	public static final String RECORD_UPDATE_TYPE_DELETE = "D";
	
	public static final String USER_TYPE_FB = "FB_USER";
	public static final String USER_TYPE_REG = "REG_USER";
	
	public static final String PIC_TYPE_HOME = "HOME";
	public static final String PIC_TYPE_OFFR = "OFFR";
	public static final String PIC_TYPE_SHARE = "SHARE";
	
	public static final String APP_PARAM_MAP_KEY = "APPLICATION_PARAM_MAP";

	public static final String COMPANY_NAME_KEY = "COMPANY_NAME";
	public static final String TERMS_COND_URL_KEY = "TERMS_COND_URL";
	public static final String CONTACT_MAIL_ID_KEY = "CONTACT_MAIL_ID";
	public static final String CONTACT_PHONE_NO_KEY = "CONTACT_PHONE_NO";
	
	public static final String SMS_FROM = "WDCARE";
	public static final String ENCODE_UTF8 = "UTF-8";
	
	public static final String EMAIL_FROM = "Washinflash <no-reply@sandboxf661c8ebc3004e62af323457435f9e7d.mailgun.org>";
	
	public static final String APP_MIN_SUPPORTED_VER_KEY = "MIN_SUPPORTED_VER";
	
	public static final String SERVICE_TYPE_REGULAR_WASH = "REGULAR_WASH";
	public static final String SERVICE_TYPE_PREMIUM_WASH = "PREMIUM_WASH";
	public static final String SERVICE_TYPE_DRY_CLEAN = "DRY_CLEAN";
	public static final String SERVICE_TYPE_DONATE = "DONATE";
	
	public static final String SECURITY_TOKEN = "1234567890987654321@washinflash.com";
	
	public static final String DISPLAY_WAY_TO_DELIVERY_MESSAGE = "Out for Delivery";
	public static final String DISPLAY_COMPLETED_MESSAGE = "Done";
	public static final String DISPLAY_PENDING_MESSAGE = "Pending";
	public static final String DISPLAY_CANCELLED_MESSAGE = "Cancelled";
	
	public static final String DISPLAY_OVERALL_STATUS_INPROGRESS = "In-Progress";
	public static final String DISPLAY_OVERALL_STATUS_COMPLETED = "Completed";
	public static final String DISPLAY_OVERALL_STATUS_CANCELLED = "Cancelled";
	
	public static final String APP_PARAM_NO_PICKUP_DAYS_KEY = "NO_PICKUP_DAYS";
	public static final String APP_PARAM_NO_DELIVERY_DAYS_KEY = "NO_DELIVERY_DAYS";
	public static final String APP_PARAM_MIN_SERVICE_DAYS_KEY = "MIN_SERVICE_DAYS";
	public static final String APP_PARAM_PICKUP_TIME_SLOTS_KEY = "PICKUP_TIME_SLOTS";
	public static final String APP_PARAM_DELIVERY_TIME_SLOTS_KEY = "DELIVERY_TIME_SLOTS";
	public static final String APP_PARAM_WEEKLY_CLOSE_DAY_KEY = "WEEKLY_CLOSE_DAY";
	public static final String APP_PARAM_DELIVERY_TYPES_KEY = "DELIVERY_TYPES";
	public static final String APP_PARAM_HOME_PAGE_MSG_KEY = "HOME_PAGE_MSG";
	public static final String APP_PARAM_CONTACT_PHONE_NO_KEY = "CONTACT_PHONE_NO";
	

}
