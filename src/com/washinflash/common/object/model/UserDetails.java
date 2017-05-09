package com.washinflash.common.object.model;

public class UserDetails {

	private int userDetailsId;
	private String name;
	private String email;
	private String mobileNo;
	private String password;
	private int mobOTP;
	private String mobVerified;
	private String referralCode;
	private String forcePassReset;
	private String userType;
	private String facebookId;
	
	private String sessionToken;
	
	
	public int getUserDetailsId() {
		return userDetailsId;
	}
	public void setUserDetailsId(int userDetailsId) {
		this.userDetailsId = userDetailsId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getMobOTP() {
		return mobOTP;
	}
	public void setMobOTP(int mobOTP) {
		this.mobOTP = mobOTP;
	}
	public String getMobVerified() {
		return mobVerified;
	}
	public void setMobVerified(String mobVerified) {
		this.mobVerified = mobVerified;
	}
	public String getReferralCode() {
		return referralCode;
	}
	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}
	public String getForcePassReset() {
		return forcePassReset;
	}
	public void setForcePassReset(String forcePassReset) {
		this.forcePassReset = forcePassReset;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

}
