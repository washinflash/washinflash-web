package com.washinflash.rest.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

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
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.StatusConstant;
import com.washinflash.rest.dao.ApplicationDAO;
import com.washinflash.rest.dao.jdbctemplate.SpringJDBCTemplate;
import com.washinflash.rest.mapper.ApplicationPicDetailsMapper;
import com.washinflash.rest.mapper.CityDetailsMapper;
import com.washinflash.rest.mapper.DeliveryTypeDetailsMapper;
import com.washinflash.rest.mapper.FAQDetailsMapper;
import com.washinflash.rest.mapper.ServiceAreaDetailsMapper;
import com.washinflash.rest.mapper.ServiceCategoryDetailsMapper;
import com.washinflash.rest.mapper.ServiceDeliveryMapMapper;
import com.washinflash.rest.mapper.ServiceTypeDetailsMapper;

@Component
public class ApplicationDAOImpl extends SpringJDBCTemplate implements ApplicationDAO {

	@Override
	public List<ApplicationPicDetails> getPictureDetailsList(String type) throws SystemException {
		
		String selectSQL = "SELECT APP_PIC_ID, PIC_PATH, PIC_TYPE, PIC_TITLE, PIC_DETAILS FROM APPLICATION_PIC WHERE PIC_TYPE = ? AND STATUS = ?";
		
		List<ApplicationPicDetails> returnList = getJDBCTemplateObject().query(selectSQL, new ApplicationPicDetailsMapper(), type, StatusConstant.ACTIVE.toString());

		return returnList;
	}
	
	@Override
	public List<ApplicationPicDetails> getOfferDetails() throws SystemException {
		
		String selectSQL = "SELECT APP_PIC_ID, PIC_PATH, PIC_TYPE, PIC_TITLE, PIC_DETAILS FROM APPLICATION_PIC WHERE PIC_TYPE = ? AND STATUS = ? " +
				"AND APP_PIC_ID IN (SELECT APP_PIC_ID FROM OFFER_DETAILS WHERE STR_TO_DATE(?,'%d-%m-%Y') BETWEEN START_DATE AND END_DATE AND STATUS = ?)";
		
		String currDateStr = GenericUtils.getCurrentISTFormatedDate(GenericConstant.DEFAULT_DATE_FORMAT);
		
		List<ApplicationPicDetails> returnList = getJDBCTemplateObject().query(selectSQL, new ApplicationPicDetailsMapper(),
				GenericConstant.PIC_TYPE_OFFR, StatusConstant.ACTIVE.toString(), currDateStr, StatusConstant.ACTIVE.toString());

		return returnList;
	}
	
	@Override
	public List<CityDetails> getCityList() throws SystemException {

		String selectSQL = "SELECT SERVICE_CITY_ID, CITY_CODE, CITY_NAME FROM SERVICE_CITY WHERE STATUS = ?";

		List<CityDetails> cityList = getJDBCTemplateObject().query(selectSQL, new CityDetailsMapper(), StatusConstant.ACTIVE.toString());

		return cityList;
	}

	@Override
	public List<ServiceTypeDetails> getServiceTypeList() throws SystemException {

		String selectSQL = "SELECT SERVICE_TYPE_ID, SERVICE_TYPE, SERVICE_TYPE_DESC, MIN_ORDER_VALUE FROM SERVICE_TYPE WHERE STATUS = ?";

		List<ServiceTypeDetails> serviceTypeList = getJDBCTemplateObject().query(selectSQL, new ServiceTypeDetailsMapper(), StatusConstant.ACTIVE.toString());

		return serviceTypeList;
	}
	
	@Override
	public List<ServiceDeliveryMapDetails> getServiceDeliveryMapList(int serviceTypeId) throws SystemException {

		String selectSQL = "SELECT SERV_DEL_MAPPING_ID, SERVICE_TYPE_ID, DELIVERY_TYPE_ID, DELIVERY_TIME_HOURS FROM SERVICE_DELIVERY_MAPPING WHERE STATUS = ? AND SERVICE_TYPE_ID = ?";

		List<ServiceDeliveryMapDetails> serviceDelMapList = getJDBCTemplateObject().query(selectSQL, new ServiceDeliveryMapMapper(), 
				StatusConstant.ACTIVE.toString(), serviceTypeId);

		return serviceDelMapList;
	}

	@Override
	public List<DeliveryTypeDetails> getDeliveryTypeList() throws SystemException {

		String selectSQL = "SELECT DELIVERY_TYPE_ID, DELIVERY_TYPE, DELIVERY_TYPE_DESC, TOTAL_BILL_MULTI FROM DELIVERY_TYPE WHERE STATUS = ?";

		List<DeliveryTypeDetails> deliveryTypeList = getJDBCTemplateObject().query(selectSQL, new DeliveryTypeDetailsMapper(), StatusConstant.ACTIVE.toString());

		return deliveryTypeList;
	}
	
	@Override
	public List<ServiceCategoryDetails> getServiceCategoryList(String serviceType) throws SystemException {

		String selectSQL = "SELECT CAT.SERVICE_CAT_ID, CAT.SERVICE_CAT, CAT.SERVICE_CAT_DESC, CAT.SERVICE_TYPE_ID  FROM SERVICE_TYPE TYPE, SERVICE_CATEGORY CAT " +
				"WHERE TYPE.SERVICE_TYPE_ID = CAT.SERVICE_TYPE_ID AND TYPE.STATUS = ? AND CAT.STATUS = ? AND TYPE.SERVICE_TYPE = ?";

		List<ServiceCategoryDetails> serviceCategoryList = getJDBCTemplateObject().query(selectSQL, new ServiceCategoryDetailsMapper(), 
				StatusConstant.ACTIVE.toString(), StatusConstant.ACTIVE.toString(), serviceType);

		return serviceCategoryList;
	}
	
	@Override
	public List<ServiceAreaDetails> getServiceAreaList(ApplicationDetailsRequest appDetailsReq) throws SystemException {

		String selectSQL = "SELECT SERVICE_AREA_ID, SERVICE_AREA_CODE, SERVICE_AREA_NAME, SERVICE_CITY_ID FROM SERVICE_AREA WHERE STATUS = ? AND SERVICE_CITY_ID = ?";

		List<ServiceAreaDetails> serviceAreaList = getJDBCTemplateObject().query(selectSQL, 
				new ServiceAreaDetailsMapper(), StatusConstant.ACTIVE.toString(), appDetailsReq.getCityId());

		return serviceAreaList;
	}

	@Override
	public Map<String, Object> getApplicationParamMap() throws SystemException {

		String selectSQL = "SELECT PARAM_NAME, PARAM_VALUE FROM APPLICATION_PARAM WHERE STATUS = ?";
		Map<String, Object> applicationParamMap = new HashMap<>();

		List<Map<String, Object>> returnList = getJDBCTemplateObject().queryForList(selectSQL, StatusConstant.ACTIVE.toString());

		for (Map<String, Object> row : returnList) {
			applicationParamMap.put((String) row.get("PARAM_NAME"), row.get("PARAM_VALUE"));
		}

		return applicationParamMap;
	}
	
	@Override
	public boolean addReview(ApplicationDetailsRequest appDetailsReq) throws SystemException {
		boolean success = true;
		
		String insertSQL = "INSERT INTO APP_REVIEW (USER_DETAILS_ID, RATING, REVIEW, UPDATED_ON, STATUS) VALUES (?,?,?,?,?)";
		int noOfRecordsUpdated = getJDBCTemplateObject().update(insertSQL, appDetailsReq.getUserDetailsId(), 
				appDetailsReq.getRating(), appDetailsReq.getComments(), GenericUtils.getCurrentDateTime(), StatusConstant.ACTIVE.toString());

		if(noOfRecordsUpdated != 1) {
			success = false;
			throw new SystemException(StatusConstant.FAILED, GenericConstant.SYSTEM_EXEPTION_MSG);
		}

		return success;
	}
	
	@Override
	public List<FAQDetails> getFaqDetails() throws SystemException {
		
		String selectSQL = "SELECT FAQ_DETAILS_ID, FAQ_QUESTION, FAQ_ANSWER FROM FAQ_DETAILS WHERE STATUS = ?";

		List<FAQDetails> faqDetailsList = getJDBCTemplateObject().query(selectSQL, 
				new FAQDetailsMapper(), StatusConstant.ACTIVE.toString());

		return faqDetailsList;
		
	}

}
