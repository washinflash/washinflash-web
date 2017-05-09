package com.washinflash.admin.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.UserAddress;

public class UserAddressMapper implements RowMapper<UserAddress> {

	public UserAddress mapRow(ResultSet rs, int rowNum) throws SQLException {
			
		UserAddress address = new UserAddress();
		address.setAddressId(rs.getInt("ADDR.ADDRESS_ID"));
		address.setName(rs.getString("ADDR.NAME"));
		address.setMobileNo(rs.getString("ADDR.MOBILE_NO"));
		address.setLandmark(rs.getString("ADDR.LANDMARK"));
		address.setAddress(rs.getString("ADDR.ADDRESS"));
		address.setPinCode(rs.getInt("ADDR.PIN_CODE"));

		return address;
	}
	
}
