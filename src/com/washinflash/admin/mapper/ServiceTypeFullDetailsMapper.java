package com.washinflash.admin.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.ServiceTypeFullDetails;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;


public class ServiceTypeFullDetailsMapper implements RowMapper<ServiceTypeFullDetails> {

	public ServiceTypeFullDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

		ServiceTypeFullDetails details = new ServiceTypeFullDetails();
		details.setServiceStatus(GenericUtils.replaceEmptyString(rs.getString("SERV.ORDER_STATUS"), "-"));
		details.setServiceType(rs.getString("SERV.SERVICE_TYPE"));
		details.setServiceTypeDesc(rs.getString("SERV.SERVICE_TYPE_DESC"));
		details.setVendorName(GenericUtils.replaceEmptyString(rs.getString("VEND.VENDOR_NAME"), "-"));
		details.setVendorType(GenericUtils.replaceEmptyString(rs.getString("VEND.VENDOR_TYPE"), "-"));
		details.setVendorMobileNo(GenericUtils.replaceEmptyString(rs.getString("VEND.MOBILE_NO"), "-"));
		details.setVendorEmail(GenericUtils.replaceEmptyString(rs.getString("VEND.EMAIL_ID"), "-"));
		details.setVendorAddress(GenericUtils.replaceEmptyString(rs.getString("VEND.ADDRESS"), "-"));
		details.setVendorPickupDate(GenericUtils.replaceEmptyString(
				GenericUtils.getISTFormattedStringFromDate(rs.getTimestamp("SERV.ASSIGNED_VENDOR_ON"), GenericConstant.DISPLAY_DATE_YEAR_FORMAT), "-"));

		return details;
	}
	
}
