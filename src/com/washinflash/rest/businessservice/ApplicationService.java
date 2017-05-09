package com.washinflash.rest.businessservice;

import java.util.Map;

import com.washinflash.common.exception.AuthenticationException;
import com.washinflash.common.exception.SystemException;


public interface ApplicationService {
	
	public void checkAuthenticity(String reqJsonString) throws AuthenticationException, SystemException;
	public String getApplicationDetails(Map<String, Object> appParam) throws SystemException;
	public String getAboutUsDetails(Map<String, Object> appParam) throws SystemException;
	public String getFaqDetails() throws SystemException;
	public String initialise(String reqJsonString, Map<String, Object> appParam) throws SystemException;
	public String getOfferDetails() throws SystemException;
	public String getCityList() throws SystemException;
	public String getServiceAreaList(String reqJsonString) throws SystemException;
	public String addReview(String reqJsonString) throws SystemException;
	public String getReferralDetails() throws SystemException;
}
