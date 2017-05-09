package com.washinflash.admin.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.VendorDetails;

public class VendorDetailsMapper implements RowMapper<VendorDetails> {

	public VendorDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		VendorDetails vendor = new VendorDetails();
		vendor.setVendorId(rs.getInt("VENDOR_DETAILS_ID"));
		vendor.setName(rs.getString("VENDOR_NAME"));
		vendor.setVenType(rs.getString("VENDOR_TYPE"));
		vendor.setMobileNo(rs.getString("MOBILE_NO"));
		vendor.setEmail(rs.getString("EMAIL_ID"));
		vendor.setAddress(rs.getString("ADDRESS"));
		vendor.setStatus(rs.getString("STATUS"));
		
		return vendor;
	}
	
}
