package com.washinflash.admin.businessservice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.washinflash.admin.businessservice.UserAdminService;
import com.washinflash.admin.dao.UserAdminDAO;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.EmployeeDetails;

@Service
public class UserAdminServiceImpl implements UserAdminService {

	@Autowired
	private UserAdminDAO userAdminDAO;


	@Override
	public List<EmployeeDetails> getEmployeeList(String empType) throws SystemException {
		
		return userAdminDAO.getEmployeeList(empType);
	}
	
	@Override
	public EmployeeDetails validateLogin(String userId, String password) throws SystemException {
		
		return userAdminDAO.validateLogin(userId, password);
	}
}
