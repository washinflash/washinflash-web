package com.washinflash.common.object.response;

import java.util.List;
import java.util.Map;

import com.washinflash.common.object.model.ApplicationPicDetails;
import com.washinflash.common.object.model.CityDetails;
import com.washinflash.common.object.model.DeliveryTypeDetails;
import com.washinflash.common.object.model.FAQDetails;
import com.washinflash.common.object.model.ServiceAreaDetails;
import com.washinflash.common.object.model.ServiceTypeDetails;

public class ApplicationDetailsResponse extends SuccessResponse {

	private List<CityDetails> cityList;
	private List<ServiceTypeDetails> serviceTypeList;
	private List<DeliveryTypeDetails> deliveryTypeList;	
	private List<ServiceAreaDetails> serviceAreaList;
	private Map<String, Object> applicationParamMap;
	private List<ApplicationPicDetails> appPicDetails;
	private List<FAQDetails> faqDetailsList;
	private String aboutUsDetails;
	private String referralHeader;
	private String referralDetails;
	
	
	public List<CityDetails> getCityList() {
		return cityList;
	}
	public void setCityList(List<CityDetails> cityList) {
		this.cityList = cityList;
	}
	public List<ServiceTypeDetails> getServiceTypeList() {
		return serviceTypeList;
	}
	public void setServiceTypeList(List<ServiceTypeDetails> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}
	public List<DeliveryTypeDetails> getDeliveryTypeList() {
		return deliveryTypeList;
	}
	public void setDeliveryTypeList(List<DeliveryTypeDetails> deliveryTypeList) {
		this.deliveryTypeList = deliveryTypeList;
	}
	public List<ServiceAreaDetails> getServiceAreaList() {
		return serviceAreaList;
	}
	public void setServiceAreaList(List<ServiceAreaDetails> serviceAreaList) {
		this.serviceAreaList = serviceAreaList;
	}
	public Map<String, Object> getApplicationParamMap() {
		return applicationParamMap;
	}
	public void setApplicationParamMap(Map<String, Object> applicationParamMap) {
		this.applicationParamMap = applicationParamMap;
	}
	public List<ApplicationPicDetails> getAppPicDetails() {
		return appPicDetails;
	}
	public void setAppPicDetails(List<ApplicationPicDetails> appPicDetails) {
		this.appPicDetails = appPicDetails;
	}
	public List<FAQDetails> getFaqDetailsList() {
		return faqDetailsList;
	}
	public void setFaqDetailsList(List<FAQDetails> faqDetailsList) {
		this.faqDetailsList = faqDetailsList;
	}
	public String getAboutUsDetails() {
		return aboutUsDetails;
	}
	public void setAboutUsDetails(String aboutUsDetails) {
		this.aboutUsDetails = aboutUsDetails;
	}
	public String getReferralHeader() {
		return referralHeader;
	}
	public void setReferralHeader(String referralHeader) {
		this.referralHeader = referralHeader;
	}
	public String getReferralDetails() {
		return referralDetails;
	}
	public void setReferralDetails(String referralDetails) {
		this.referralDetails = referralDetails;
	}

}
