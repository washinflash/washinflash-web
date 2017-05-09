package com.washinflash.rest.businessservice.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.washinflash.common.exception.AuthenticationException;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.ApplicationPicDetails;
import com.washinflash.common.object.model.AuthenticationDetails;
import com.washinflash.common.object.model.CityDetails;
import com.washinflash.common.object.model.DeliveryTypeDetails;
import com.washinflash.common.object.model.FAQDetails;
import com.washinflash.common.object.model.ServiceAreaDetails;
import com.washinflash.common.object.model.ServiceTypeDetails;
import com.washinflash.common.object.request.ApplicationDetailsRequest;
import com.washinflash.common.object.response.ApplicationDetailsResponse;
import com.washinflash.common.object.response.InitialisationResponse;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.GsonUtils;
import com.washinflash.common.util.StatusConstant;
import com.washinflash.rest.businessservice.ApplicationService;
import com.washinflash.rest.dao.ApplicationDAO;
import com.washinflash.rest.dao.UserDAO;

@Service
public class ApplicationServiceImpl implements ApplicationService {

	private static final Logger log = Logger.getLogger(ApplicationServiceImpl.class);

	@Autowired
	private ApplicationDAO applicationDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Value("${aboutus.details.message}")
	private String aboutUsDetails;


	@Override
	public void checkAuthenticity(String reqJsonString) throws AuthenticationException, SystemException {
		
		AuthenticationDetails authDetails = (AuthenticationDetails) GsonUtils.getObjectFromJsonString(reqJsonString, AuthenticationDetails.class);
		
		// Check for the token string in the request
		GenericUtils.validateToken(authDetails);
		
		// Check for the valid session
		boolean valid = userDAO.isValidUserSession(authDetails.getSessionToken());
		if(!valid) {
			throw new AuthenticationException(StatusConstant.INVALID_SESSION, GenericConstant.INVALID_SESSION_ERROR_MSG);
		}
		
	}
	
	
	@Override
	public String getAboutUsDetails(Map<String, Object> appParam) throws SystemException {

		String jsonResponse = "";

		ApplicationDetailsResponse response = new ApplicationDetailsResponse();

		response.setApplicationParamMap(appParam);
		response.setAboutUsDetails(aboutUsDetails);

		jsonResponse = GsonUtils.toJsonString(response);


		return jsonResponse;
	}
	
	@Override
	public String getApplicationDetails(Map<String, Object> appParam) throws SystemException {
		
		String jsonResponse = "";

		try {
			List<ServiceTypeDetails> serviceTypeList = applicationDAO.getServiceTypeList();
			List<DeliveryTypeDetails> deliveryTypeList = applicationDAO.getDeliveryTypeList();
			Map<String, Object> applicationParamMap = appParam;
			List<ApplicationPicDetails> picPathList = applicationDAO.getPictureDetailsList(GenericConstant.PIC_TYPE_HOME);

			ApplicationDetailsResponse response = new ApplicationDetailsResponse();
			
			for(ServiceTypeDetails serviceType : serviceTypeList) {
				serviceType.setServiceDeliveryMapList(applicationDAO.getServiceDeliveryMapList(serviceType.getServiceTypeId()));
			}
			
			response.setServiceTypeList(serviceTypeList);
			response.setDeliveryTypeList(deliveryTypeList);
			response.setApplicationParamMap(applicationParamMap);
			response.setAppPicDetails(picPathList);

			jsonResponse = GsonUtils.toJsonString(response);

		} catch (SystemException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;
	}	
	public String getFaqDetails() throws SystemException {
		
		String jsonResponse = "";

		try {
			List<FAQDetails> faqDetailsList = applicationDAO.getFaqDetails();

			ApplicationDetailsResponse response = new ApplicationDetailsResponse();
			response.setFaqDetailsList(faqDetailsList);
			
			jsonResponse = GsonUtils.toJsonString(response);

		} catch (SystemException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;
	}
	
	@Override
	public String initialise(String reqJsonString, Map<String, Object> appParam) throws SystemException {
		ApplicationDetailsRequest appDetailsRequest = (ApplicationDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, ApplicationDetailsRequest.class);
		String jsonResponse = "";

		try {
			InitialisationResponse response = new InitialisationResponse();
			
			Map<String, Object> applicationParamMap = appParam;
			String minSupportedVersion = (String) applicationParamMap.get(GenericConstant.APP_MIN_SUPPORTED_VER_KEY);
			String mobVersion = appDetailsRequest.getVersion();
			
			response.setApplicationParamMap(appParam);
			
			boolean supported = true;
			
			if(minSupportedVersion != null) {
				if(mobVersion != null) {
					String minSuppVerArray [] = minSupportedVersion.split("\\.");
					String mobVerArray [] = mobVersion.split("\\.");
					if(minSuppVerArray.length == mobVerArray.length) {
						for(int i = 0; i < minSuppVerArray.length; i++) {
							if(GenericUtils.getIntegerFromString(minSuppVerArray[i]) > GenericUtils.getIntegerFromString(mobVerArray[i])) {
								supported = false;
								break;
							}
						}
					}
				}
			}
			
			if(!supported) {
				response.setVerSupportedFlag(GenericConstant.FLAG_NO);
				response.setVerNotSupportedMessage("Newer version is available. Please update your app.");
			} else {
				response.setVerSupportedFlag(GenericConstant.FLAG_YES);
			}

			boolean validSession = false;
			String sessionToken = appDetailsRequest.getSessionToken();
			if(sessionToken != null && userDAO.isValidUserSession(sessionToken)) {
				validSession = true;
			}
			response.setValidSession(validSession);
			
			jsonResponse = GsonUtils.toJsonString(response);
			
		} catch (SystemException se) {
			log.debug(se.getCause() + se.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;
	}
	
	
	@Override
	public String getOfferDetails() throws SystemException {
		
		String jsonResponse = "";

		try {
			List<ApplicationPicDetails> picPathList = applicationDAO.getOfferDetails();

			ApplicationDetailsResponse response = new ApplicationDetailsResponse();
			response.setAppPicDetails(picPathList);

			jsonResponse = GsonUtils.toJsonString(response);

		} catch (SystemException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;
	}
	
	
	@Override
	public String getCityList() throws SystemException {
		
		String jsonResponse = "";

		try {
			List<CityDetails> cityList = applicationDAO.getCityList();
			ApplicationDetailsResponse response = new ApplicationDetailsResponse();	
			response.setCityList(cityList);

			jsonResponse = GsonUtils.toJsonString(response);

		} catch (SystemException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;
	}
	
	
	@Override
	public String getServiceAreaList(String reqJsonString) throws SystemException {
		
		ApplicationDetailsRequest appDetailsRequest = (ApplicationDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, ApplicationDetailsRequest.class);
		String jsonResponse = "";

		try {
			List<ServiceAreaDetails> serviceAreaList = applicationDAO.getServiceAreaList(appDetailsRequest);
			ApplicationDetailsResponse response = new ApplicationDetailsResponse();	
			response.setServiceAreaList(serviceAreaList);

			jsonResponse = GsonUtils.toJsonString(response);

		} catch (SystemException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;
	}	
	
	
	@Override
	public String addReview(String reqJsonString) throws SystemException {
		
		ApplicationDetailsRequest appDetailsRequest = (ApplicationDetailsRequest) GsonUtils.getObjectFromJsonString(reqJsonString, ApplicationDetailsRequest.class);
		String jsonResponse = "";

		try {
			boolean success = applicationDAO.addReview(appDetailsRequest);
			if(success) {
				jsonResponse = GenericUtils.getDefaultSuccessJsonResponse();
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
	public String getReferralDetails() throws SystemException {
		
		String jsonResponse = "";

		try {
			List<ApplicationPicDetails> picPathList = applicationDAO.getPictureDetailsList(GenericConstant.PIC_TYPE_SHARE);

			ApplicationDetailsResponse response = new ApplicationDetailsResponse();
			response.setAppPicDetails(picPathList);

			response.setReferralHeader("Invite your friend");
			response.setReferralDetails("Refer your friend to let them spend a happy weekend ahead.");
			
			jsonResponse = GsonUtils.toJsonString(response);

		} catch (SystemException be) {
			log.debug(be.getCause() + be.getErrorMessage());
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}	

		return jsonResponse;
	}
}
