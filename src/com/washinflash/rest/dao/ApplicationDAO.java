package com.washinflash.rest.dao;

import java.util.List;
import java.util.Map;

import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.ApplicationPicDetails;
import com.washinflash.common.object.model.CityDetails;
import com.washinflash.common.object.model.DeliveryTypeDetails;
import com.washinflash.common.object.model.FAQDetails;
import com.washinflash.common.object.model.ServiceAreaDetails;
import com.washinflash.common.object.model.ServiceCategoryDetails;
import com.washinflash.common.object.model.ServiceDeliveryMapDetails;
import com.washinflash.common.object.model.ServiceTypeDetails;
import com.washinflash.common.object.request.ApplicationDetailsRequest;

public interface ApplicationDAO {

	public List<ServiceTypeDetails> getServiceTypeList() throws SystemException;
	public List<DeliveryTypeDetails> getDeliveryTypeList() throws SystemException;
	public List<ServiceDeliveryMapDetails> getServiceDeliveryMapList(int serviceTypeId) throws SystemException;
	public List<ServiceCategoryDetails> getServiceCategoryList(String serviceType) throws SystemException;
	public List<ApplicationPicDetails> getPictureDetailsList(String type) throws SystemException;
	public List<ApplicationPicDetails> getOfferDetails() throws SystemException;
	public List<CityDetails> getCityList() throws SystemException;
	public List<ServiceAreaDetails> getServiceAreaList(ApplicationDetailsRequest appDetailsReq) throws SystemException;
	public Map<String, Object> getApplicationParamMap() throws SystemException;
	public boolean addReview(ApplicationDetailsRequest appDetailsReq) throws SystemException;
	public List<FAQDetails> getFaqDetails() throws SystemException;
}
