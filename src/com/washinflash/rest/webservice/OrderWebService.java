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
import com.washinflash.rest.businessservice.OrderService;

@RestController
@RequestMapping("/rest/order")
public class OrderWebService {

	private static final Logger log = Logger.getLogger(OrderWebService.class);
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
    private ServletContext context;

	
	@RequestMapping(value="/createOrder", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String createOrder(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = orderService.createOrder(reqJsonString, (Map<String, Object>) context.getAttribute(GenericConstant.APP_PARAM_MAP_KEY));
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}
		
	
	@RequestMapping(value="/getMyOrderDetailsList", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String geMyOrderDetailsList(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = orderService.getMyOrderDetailsList(reqJsonString);
		} catch (AuthenticationException ae) {
			log.error(ae.getCause() + ae.getMessage() + ae);
			jsonResponse = GenericUtils.getAuthFailureJsonResponse(ae);
		} catch (Throwable t) {
			log.error(t.getCause() + t.getMessage() + t);
			jsonResponse = GenericUtils.getSystemFailureJsonResponse();
		}

		return jsonResponse;
	}	
	
	@RequestMapping(value="/cancelMyOrder", method = RequestMethod.POST, consumes = "application/json", produces="application/json")
	public String cancelMyOrder(@RequestBody(required = true) String reqJsonString) {

		String jsonResponse = "";
		try {
			applicationService.checkAuthenticity(reqJsonString);
			jsonResponse = orderService.cancelMyOrder(reqJsonString);
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
