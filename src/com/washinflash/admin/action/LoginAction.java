package com.washinflash.admin.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.washinflash.admin.businessservice.UserAdminService;
import com.washinflash.admin.validator.EmployeeDetailsValidator;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.EmployeeDetails;

@Controller
@RequestMapping("/admin/login")
public class LoginAction {
	
	@Autowired
	private UserAdminService userAdminService;
	@Autowired
	private EmployeeDetailsValidator employeeValidator;

	
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(employeeValidator);
    }
	
	@RequestMapping(value="/showLogin.do", method = RequestMethod.GET)
	public String showLogin(ModelMap model) {
		
		return "login";
	}
		
	@RequestMapping(value="/validateLogin.do", method = RequestMethod.POST)
	public String validateLogin(@Validated @ModelAttribute EmployeeDetails req, BindingResult result, HttpServletRequest request, ModelMap model) {
		
		if(result.hasErrors()) {
	        return "login";           
	    }
		
		EmployeeDetails userData = null;
		try {
			userData  = userAdminService.validateLogin(req.getEmail(), req.getPassword());
		} catch (SystemException se) {}
		
		if(userData != null) {
			model.put("msg", "Welcome to Washinflash admin application!!");
			request.getSession().setAttribute("LOGGEDIN_USER", userData);
			
			return "home";
		} else {
			
			model.put("errorMessage", "Invalid credentials provided");
			return "login";
		}
	
	}
	
	@RequestMapping(value="/gotoHome.do", method = RequestMethod.GET)
	public String gotoHome(ModelMap model) {

		model.put("msg", "Welcome to Washinflash admin application!!");
		
		return "home";
	}
	
	@RequestMapping(value="/logout.do", method = RequestMethod.GET)
	public String logout(HttpSession session, ModelMap model) {

		session.invalidate();
		model.put("infoMessage", "You are successfully logged out");
		
		return "login";
	}
}
