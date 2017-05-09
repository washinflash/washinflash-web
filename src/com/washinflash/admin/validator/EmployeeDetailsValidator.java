package com.washinflash.admin.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.washinflash.common.object.model.EmployeeDetails;

@Component
public class EmployeeDetailsValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return EmployeeDetails.class.equals(clazz);
	}
	
	@Override
	public void validate(Object obj, Errors errors) {
	
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.email.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.password.required");
        
        EmployeeDetails emp = (EmployeeDetails) obj;
        
        if(emp.getPassword().length() < 6) {
        	errors.reject("password", "error.password.min.length");
        }
        
        //To call other validator
        //ValidationUtils.invokeValidator(validatorInstanceToCall, objectToValidate, errors);
	}
	
}
