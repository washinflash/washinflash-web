package com.washinflash.common.helper;

import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

import com.washinflash.common.util.GenericConstant;
import com.washinflash.rest.dao.ApplicationDAO;


public class ServletContextInitializer implements ServletContextAware, InitializingBean {
	
	@Autowired
	private ApplicationDAO applicationDAO;
	
	private ServletContext context;
	
	@Override
	public void setServletContext(ServletContext servletContext) { 
		context = servletContext; 	
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {

		Map<String, Object> applicationParamMap = applicationDAO.getApplicationParamMap();
		context.setAttribute(GenericConstant.APP_PARAM_MAP_KEY, applicationParamMap);
		
	}

}