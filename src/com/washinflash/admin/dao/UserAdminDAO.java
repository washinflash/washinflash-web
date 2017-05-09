package com.washinflash.admin.dao;

import java.util.List;

import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.EmployeeDetails;
import com.washinflash.common.object.model.UserAddress;
import com.washinflash.common.object.model.UserDetails;

public interface UserAdminDAO {

	public List<EmployeeDetails> getEmployeeList(String empType) throws SystemException;
	public EmployeeDetails validateLogin(String userId, String password) throws SystemException;
	public UserAddress getAddress(int addressId) throws SystemException;
	public UserDetails getUserDetails(int userDetailsId) throws SystemException;
	public EmployeeDetails getEmployeeDetails(int empId) throws SystemException;
		
}
