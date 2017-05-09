package com.washinflash.admin.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.washinflash.common.object.model.EmployeeDetails;


@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {


	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {

		String uri = request.getRequestURI();

		if(uri.contains("/admin/") && !uri.endsWith("Login.do") && !uri.endsWith("logout.do")) {
			
			EmployeeDetails userData = (EmployeeDetails) request.getSession().getAttribute("LOGGEDIN_USER");
			
			if(userData == null) {
				response.sendRedirect("showLogin.do");
				return false;
			}   
		}
		
		return true;
	}
}
