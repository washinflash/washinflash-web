package com.washinflash.admin.businessservice;

import java.util.List;

import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.EmployeeDetails;

public interface UserAdminService {
	
	public List<EmployeeDetails> getEmployeeList(String empType) throws SystemException;
	public EmployeeDetails validateLogin(String userId, String password) throws SystemException;
	
}
