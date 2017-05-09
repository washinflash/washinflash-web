package com.washinflash.admin.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/public/terms")
public class TermsAndConditionAction {

	//private static final Logger log = Logger.getLogger(TermsAndConditionAction.class);
	

	@RequestMapping(value="/showTerms.do", method = RequestMethod.GET)
	public String showTermsAndConditionPage(ModelMap model) {
		
		return "terms";
	}
}