package com.washinflash.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.washinflash.common.exception.AuthenticationException;
import com.washinflash.common.exception.BusinessException;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.AuthenticationDetails;
import com.washinflash.common.object.response.FailureResponse;
import com.washinflash.common.object.response.SuccessResponse;

public final class GenericUtils {

	public static String getSystemFailureJsonResponse() {

		return GsonUtils.toJsonString(new FailureResponse(GenericConstant.SYSTEM_EXEPTION_MSG));
	}

	public static String getBusinessFailureJsonResponse(BusinessException be) {

		return GsonUtils.toJsonString(new FailureResponse(be.getErrorMessage()));
	}

	public static String getAuthFailureJsonResponse(AuthenticationException ae) {

		return GsonUtils.toJsonString(new FailureResponse(ae.getErrorMessage(), ae.getErrorCode()));
	}
	
	public static String getDefaultSuccessJsonResponse() {

		return GsonUtils.toJsonString(new SuccessResponse());
	}	

	public static Date getCurrentDateTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	public static boolean isEmpty (String str) {
		return (str == null) || str.trim().isEmpty();
	}

	public static String getCurrentISTFormatedDate(String format) {

		String returnStr = "";
		Calendar cal = getISTCalendar();
		SimpleDateFormat formatter  = getISTSimpleDateFormat(format);
		returnStr = formatter.format(cal.getTime());

		return returnStr;
	}

	public static int getIntegerFromString(String str) throws SystemException {

		int returnValue = 0;

		if(null == str || str.trim().isEmpty()) {
			return 0;
		}

		try {
			returnValue = Integer.parseInt(str.trim());
		} catch(NumberFormatException nfe) {
			throw new SystemException(StatusConstant.FAILED, "Invalid number format. " + str);
		}

		return returnValue;
	}

	public static Date getDateFromString(String str) throws SystemException {

		Date returnDate = null;

		if(null == str || str.trim().isEmpty()) {
			return null;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(GenericConstant.DEFAULT_DATE_FORMAT);
		try {
			returnDate = formatter.parse(str.trim());
		} catch(ParseException pe) {
			throw new SystemException(StatusConstant.FAILED, "Invalid date format. " + str);
		}

		return returnDate;
	}	
	
	public static Date getISTDateFromString(String str) throws SystemException {

		Date returnDate = null;

		if(null == str || str.trim().isEmpty()) {
			return null;
		}

		SimpleDateFormat formatter = getISTSimpleDateFormat(GenericConstant.DEFAULT_DATE_FORMAT);
		try {
			returnDate = formatter.parse(str.trim());
		} catch(ParseException pe) {
			throw new SystemException(StatusConstant.FAILED, "Invalid date format. " + str);
		}

		return returnDate;
	}

	public static String getFormattedStringFromDate(Date date, String format) {

		String returnStr = "";

		if(null == date) {
			return "";
		}

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		returnStr = formatter.format(date);

		return returnStr;
	}

	public static String getISTFormattedStringFromDate(Date date, String format) {

		String returnStr = "";

		if(null == date) {
			return "";
		}

		SimpleDateFormat formatter = getISTSimpleDateFormat(format);
		returnStr = formatter.format(date);

		return returnStr;
	}


	public static Calendar getISTCalendar() {
		return Calendar.getInstance(GenericConstant.IST_TIME_ZONE);
	}

	public static SimpleDateFormat getISTSimpleDateFormat(String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setTimeZone(GenericConstant.IST_TIME_ZONE);

		return dateFormat;
	}

	public static String getUniqueRef() {

		String uniqueRef = "";

		/*Calendar cal = getISTCalendar();
		SimpleDateFormat format = getISTSimpleDateFormat("yyMMddHHmmssSSS");

		uniqueRef = format.format(cal.getTime());*/
		
		//String charsFirstPart = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String charsSecondPart = "0123456789";
		//char stringCharsFirstPart[] = new char[2];
		char stringCharsSecondPart[] = new char[5];
		Random random = new Random();

		/*for (int i = 0; i < stringCharsFirstPart.length; i++) {
			stringCharsFirstPart[i] = charsFirstPart.charAt(random.nextInt(charsFirstPart.length()));
		}*/
		for (int i = 0; i < stringCharsSecondPart.length; i++) {
			stringCharsSecondPart[i] = charsSecondPart.charAt(random.nextInt(charsSecondPart.length()));
		}
		uniqueRef = "WD" + new String(stringCharsSecondPart);

		return uniqueRef;	
	}

	public static String getSHA1EncryptedString(String plainStr) throws SystemException {

		String encryptedString = "";

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(plainStr.getBytes("UTF-8"));

			encryptedString =  new BigInteger(1, crypt.digest()).toString(16);
		} catch(Exception e) {
			throw new SystemException(StatusConstant.FAILED, "Failed to encrypt provided string. " + plainStr);
		}

		return encryptedString;

	}

	public static void validateToken(AuthenticationDetails authDetails) throws AuthenticationException {
		
		if(!GenericConstant.SECURITY_TOKEN.equals(authDetails.getToken())) {
			throw new AuthenticationException(StatusConstant.INVALID_TOKEN, GenericConstant.INVALID_TOKEN_ERROR_MSG);
		}
	}
	
	public static int getMobileOTP() {

		return (int) (Math.random() * 9000) + 1000;
	}

	public static String getReferralCode() {

		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		char stringChars[] = new char[7];
		Random random = new Random();

		for (int i = 0; i < stringChars.length; i++) {
			stringChars[i] = chars.charAt(random.nextInt(chars.length()));
		}

		return new String(stringChars);
	}

	public static String generatePassword() throws SystemException {

		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		char stringChars[] = new char[6];
		Random random = new Random();

		for (int i = 0; i < stringChars.length; i++) {
			stringChars[i] = chars.charAt(random.nextInt(chars.length()));
		}

		return new String(stringChars);
	}

	public static String generateSessionToken() throws SystemException {

		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		char stringChars[] = new char[20];
		Random random = new Random();

		for (int i = 0; i < stringChars.length; i++) {
			stringChars[i] = chars.charAt(random.nextInt(chars.length()));
		}

		return new String(stringChars);
	}
	
	public static String replaceNullString(String str) {
		if(str == null) {
			return "";
		}
		
		return str;
	}
	
	public static String replaceEmptyString(String str, String replacedStr) {
		if(isEmpty(str)) {
			return replacedStr;
		}
		
		return str;
	}
}