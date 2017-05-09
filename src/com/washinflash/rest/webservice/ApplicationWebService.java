package com.washinflash.rest.webservice;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.washinflash.common.exception.AuthenticationException;
import com.washinflash.common.object.model.AuthenticationDetails;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.GsonUtils;
import com.washinflash.rest.businessservice.ApplicationService;

@RestController
@RequestMapping("/rest/application")
public class ApplicationWebService {

	private static final Logger log = Logger.getLogger(ApplicationWebService.class); 
	
	@Autowired
	private ApplicationService applicationService;
    @Autowired
    private ServletContext context;
	
	@RequestMapping(value="/initialise", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String initialise(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			AuthenticationDetails authDetails = (AuthenticationDetails) GsonUtils.getObjectFromJsonString(reqJsonString, AuthenticationDetails.class);
			GenericUtils.validateToken(authDetails);
			jsonResponse = applicationService.initialise(reqJsonString, (Map<String, Object>) context.getAttribute(GenericConstant.APP_PARAM_MAP_KEY));
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}	
	
	@RequestMapping(value="/getApplicationDetails", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String getApplicationDetails(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = applicationService.getApplicationDetails((Map<String, Object>) context.getAttribute(GenericConstant.APP_PARAM_MAP_KEY));
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}		
	
	@RequestMapping(value="/getAboutUsDetails", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String getAboutUsDetails(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = applicationService.getAboutUsDetails((Map<String, Object>) context.getAttribute(GenericConstant.APP_PARAM_MAP_KEY));
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}	
	
	@RequestMapping(value="/getOfferDetails", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String getOfferDetails(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = applicationService.getOfferDetails();
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}	
	
	@RequestMapping(value="/getCityList", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String getCityList(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = applicationService.getCityList();
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}	
	
	@RequestMapping(value="/getServiceAreaList", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String getServiceAreaList(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = applicationService.getServiceAreaList(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}	
	
	@RequestMapping(value="/addReview", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String addReview(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = applicationService.addReview(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
	
	@RequestMapping(value="/getFaqDetails", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String getFaqDetails(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = applicationService.getFaqDetails();
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
	
	
	@RequestMapping(value="/getReferralDetails", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String getReferralDetails(@RequestBody(required = true) String reqJsonString) {
		
		String jsonResponse = "";
		try {
			AuthenticationDetails authDetails = (AuthenticationDetails) GsonUtils.getObjectFromJsonString(reqJsonString, AuthenticationDetails.class);
			GenericUtils.validateToken(authDetails);
			jsonResponse = applicationService.getReferralDetails();
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
}
