package com.washinflash.admin.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.washinflash.admin.dao.UserAdminDAO;
import com.washinflash.admin.dao.jdbctemplate.SpringJDBCTemplate;
import com.washinflash.admin.mapper.EmployeeDetailsMapper;
import com.washinflash.admin.mapper.UserAddressMapper;
import com.washinflash.admin.mapper.UserDetailsMapper;
import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.EmployeeDetails;
import com.washinflash.common.object.model.UserAddress;
import com.washinflash.common.object.model.UserDetails;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.StatusConstant;


@Component
public class UserAdminDAOImpl extends SpringJDBCTemplate implements UserAdminDAO {

	@Override
	public List<EmployeeDetails> getEmployeeList(String empType) throws SystemException {
		
		StringBuffer query = new StringBuffer("SELECT * FROM EMPLOYEE_DETAILS WHERE STATUS = ? ");
		List<Object> queryParam = new ArrayList<>();
		queryParam.add(StatusConstant.ACTIVE.toString());
		
		if(!GenericUtils.isEmpty(empType)) {
			query.append(" AND EMPLOYEE_TYPE = ?");
			queryParam.add(empType);
		}
		
		List<EmployeeDetails> employeeList = getJDBCTemplateObject().query(query.toString(), queryParam.toArray(),
				new EmployeeDetailsMapper());
		
		
		return employeeList;
	}
	
	
	@Override
	public EmployeeDetails getEmployeeDetails(int empId) throws SystemException {
		
		StringBuffer query = new StringBuffer("SELECT * FROM EMPLOYEE_DETAILS WHERE EMPLOYEE_DETAILS_ID = ? ");
		
		List<EmployeeDetails> employeeList = getJDBCTemplateObject().query(query.toString(), new EmployeeDetailsMapper(), empId);
		
		EmployeeDetails employeeDetails = null;
		if(employeeList != null && employeeList.size() > 0) {
			employeeDetails = employeeList.get(0);
		}
		
		return employeeDetails;
	}
	
	@Override
	public EmployeeDetails validateLogin(String userId, String password) throws SystemException {
		
		StringBuffer query = new StringBuffer("SELECT * FROM EMPLOYEE_DETAILS WHERE EMAIL_ID = ? AND PASSWORD = ? AND EMPLOYEE_TYPE = ? AND STATUS = ? ");
				
		List<EmployeeDetails> employeeList = getJDBCTemplateObject().query(query.toString(), new EmployeeDetailsMapper(), 
				userId, password, GenericConstant.EMP_TYPE_ADMIN, StatusConstant.ACTIVE.toString());
		
		EmployeeDetails employeeDetails = null;
		if(employeeList != null && employeeList.size() > 0) {
			employeeDetails = employeeList.get(0);
		}
		
		return employeeDetails;
	}
	
	
	@Override
	public UserDetails getUserDetails(int userDetailsId) throws SystemException {

		
		String selectSQL = "SELECT USER_DETAILS_ID, NAME, EMAIL, MOBILE_NO, MOB_VERIFIED, REFERRAL_CODE, FORCE_PASS_RESET, USER_TYPE, " +
				"FACEBOOK_ID FROM USER_DETAILS WHERE USER_DETAILS_ID = ?";

		List<UserDetails> userDetailsList = getJDBCTemplateObject().query(selectSQL, new UserDetailsMapper(), userDetailsId);

		UserDetails userDetails = null;
		if(userDetailsList != null && userDetailsList.size() > 0) {
			userDetails = userDetailsList.get(0);
		}

		return userDetails;
	}
	
	
	@Override
	public UserAddress getAddress(int addressId) throws SystemException {

		String selectSQL = "SELECT ADDR.ADDRESS_ID, ADDR.NAME, ADDR.MOBILE_NO, ADDR.LANDMARK, ADDR.ADDRESS, ADDR.PIN_CODE " +
				"FROM USER_ADDRESS AS ADDR WHERE ADDR.ADDRESS_ID = ?";

		List<UserAddress> addressList = getJDBCTemplateObject().query(selectSQL, new UserAddressMapper(), addressId);

		UserAddress userAddress = null;
		if(addressList != null && addressList.size() > 0) {
			userAddress = addressList.get(0);
		}

		return userAddress;
	}
	
}
