package com.washinflash.rest.webservice;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.washinflash.common.exception.AuthenticationException;
import com.washinflash.common.object.model.AuthenticationDetails;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.GsonUtils;
import com.washinflash.rest.businessservice.ApplicationService;
import com.washinflash.rest.businessservice.UserService;

@RestController
@RequestMapping("/rest/user")
public class UserWebService {

	private static final Logger log = Logger.getLogger(UserWebService.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private ApplicationService applicationService;


	@RequestMapping(value="/registerUser", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String registerUser(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			AuthenticationDetails authDetails = (AuthenticationDetails) GsonUtils.getObjectFromJsonString(reqJsonString, AuthenticationDetails.class);
			GenericUtils.validateToken(authDetails);
			jsonResponse = userService.registerUser(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
	
	@RequestMapping(value="/registerFacebookUser", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String registerFacebookUser(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			AuthenticationDetails authDetails = (AuthenticationDetails) GsonUtils.getObjectFromJsonString(reqJsonString, AuthenticationDetails.class);
			GenericUtils.validateToken(authDetails);
			jsonResponse = userService.registerFacebookUser(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
	
	@RequestMapping(value="/resendFreshOTP", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String resendFreshOTP(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			AuthenticationDetails authDetails = (AuthenticationDetails) GsonUtils.getObjectFromJsonString(reqJsonString, AuthenticationDetails.class);
			GenericUtils.validateToken(authDetails);
			jsonResponse = userService.resendFreshOTP(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
	
	
	@RequestMapping(value="/sendOTPForFacebookReg", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String sendOTPForFacebookReg(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			AuthenticationDetails authDetails = (AuthenticationDetails) GsonUtils.getObjectFromJsonString(reqJsonString, AuthenticationDetails.class);
			GenericUtils.validateToken(authDetails);
			jsonResponse = userService.sendOTPForFacebookReg(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
	
	@RequestMapping(value="/updateMobileVerified", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String updateMobileVerified(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			AuthenticationDetails authDetails = (AuthenticationDetails) GsonUtils.getObjectFromJsonString(reqJsonString, AuthenticationDetails.class);
			GenericUtils.validateToken(authDetails);
			jsonResponse = userService.updateMobileVerified(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
	
	@RequestMapping(value="/validateUserDetails", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String validateUserDetails(@RequestBody(required = true) String reqJsonString) {
	
		String jsonResponse = "";
		try {
			AuthenticationDetails authDetails = (AuthenticationDetails) GsonUtils.getObjectFromJsonString(reqJsonString, AuthenticationDetails.class);
			GenericUtils.validateToken(authDetails);
			jsonResponse = userService.validateUserDetails(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
	
	@RequestMapping(value="/validateFacebookUserDetails", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String validateFacebookUserDetails(@RequestBody(required = true) String reqJsonString) {
	
		String jsonResponse = "";
		try {
			AuthenticationDetails authDetails = (AuthenticationDetails) GsonUtils.getObjectFromJsonString(reqJsonString, AuthenticationDetails.class);
			GenericUtils.validateToken(authDetails);
			jsonResponse = userService.validateFacebookUserDetails(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
	
	@RequestMapping(value="/updateAddress", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String updateAddress(@RequestBody(required = true) String reqJsonString) {
		
		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = userService.updateAddress(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}

	@RequestMapping(value="/getAddressList", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String getAddressList(@RequestBody(required = true) String reqJsonString) {
		
		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = userService.getActiveAddressList(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
	
	@RequestMapping(value="/getAddress", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String getAddress(@RequestBody(required = true) String reqJsonString) {
		
		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = userService.getAddress(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}	
	
	
	@RequestMapping(value="/updateUserDetails", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String updateUserDetails(@RequestBody(required = true) String reqJsonString) {
		
		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = userService.updateUserDetails(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}	
	
	@RequestMapping(value="/updateUserPassword", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String updateUserPassword(@RequestBody(required = true) String reqJsonString) {
		
		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = userService.updateUserPassword(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
	
	@RequestMapping(value="/forgotPassword", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String forgotPassword(@RequestBody(required = true) String reqJsonString) {
		
		String jsonResponse = "";
		try {
			AuthenticationDetails authDetails = (AuthenticationDetails) GsonUtils.getObjectFromJsonString(reqJsonString, AuthenticationDetails.class);
			GenericUtils.validateToken(authDetails);
			jsonResponse = userService.forgotPassword(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}

	@RequestMapping(value="/logoutUserSession", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String logoutUserSession(@RequestBody(required = true) String reqJsonString) {
		
		String jsonResponse = "";
		try {
			AuthenticationDetails authDetails = (AuthenticationDetails) GsonUtils.getObjectFromJsonString(reqJsonString, AuthenticationDetails.class);
			GenericUtils.validateToken(authDetails);
			jsonResponse = userService.logoutUserSession(reqJsonString);
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
