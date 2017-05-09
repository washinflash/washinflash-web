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
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.rest.businessservice.ApplicationService;
import com.washinflash.rest.businessservice.ServiceDetailsService;

@RestController
@RequestMapping("/rest/service")
public class ServiceDetailsWebService {

	private static final Logger log = Logger.getLogger(ServiceDetailsWebService.class);
	
	@Autowired
	private ServiceDetailsService serviceDetailsService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
    private ServletContext context;
		
	
	@RequestMapping(value="/getPriceList", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String getPriceList(@RequestBody(required = true) String reqJsonString) {
		
		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = serviceDetailsService.getPriceList(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
	
	
	@RequestMapping(value="/getServiceDetailsParam", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String getServiceDetailsParam(@RequestBody(required = true) String reqJsonString) {
		
		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = serviceDetailsService.getServiceDetailsParam(reqJsonString, (Map<String, Object>) context.getAttribute(GenericConstant.APP_PARAM_MAP_KEY));
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
