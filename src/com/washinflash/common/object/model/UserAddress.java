package com.washinflash.common.object.model;

import com.washinflash.common.util.GenericUtils;

public class UserAddress {

	private int addressId;
	private int userDetailsId;
	private String name;
	private String mobileNo;
	private String landmark;
	private String address;
	private int pinCode;
	
	
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
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
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public int getPinCode() {
		return pinCode;
	}
	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFullAddress() {

		StringBuffer addressStr = new StringBuffer();

		addressStr.append(formatAddressField(getName(), ""));
		addressStr.append(formatAddressField(getAddress(), "<br/>"));
		addressStr.append(formatAddressField(getLandmark(), ", "));
		addressStr.append(formatAddressField(new Integer(getPinCode()).toString(), " "));
		addressStr.append(formatAddressField(getMobileNo(), "<br/>Mobile: "));

		return addressStr.toString();
	}

	private String formatAddressField(String str, String pre) {
		String returnStr = "";
		if (!GenericUtils.isEmpty(str)) {
			returnStr = pre + str;
		}
		return returnStr;
	}	
	
}
