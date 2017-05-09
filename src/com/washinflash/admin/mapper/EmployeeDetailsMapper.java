package com.washinflash.admin.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.EmployeeDetails;
import com.washinflash.common.util.GenericUtils;

public class EmployeeDetailsMapper implements RowMapper<EmployeeDetails> {

	public EmployeeDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		EmployeeDetails employee = new EmployeeDetails();
		employee.setEmployeeId(rs.getInt("EMPLOYEE_DETAILS_ID"));
		employee.setName(rs.getString("EMPLOYEE_NAME"));
		employee.setEmpType(rs.getString("EMPLOYEE_TYPE"));
		employee.setMobileNo(rs.getString("MOBILE_NO"));
		employee.setEmail(GenericUtils.replaceEmptyString(rs.getString("EMAIL_ID"), "-"));
		employee.setAddress(rs.getString("ADDRESS"));
		employee.setStatus(rs.getString("STATUS"));
		
		return employee;
	}
	
}
