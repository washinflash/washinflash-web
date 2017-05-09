package com.washinflash.rest.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.ServiceDetails;

public class ServiceDetailsMapper implements RowMapper<ServiceDetails> {

	public ServiceDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ServiceDetails serviceDetails = new ServiceDetails();
		serviceDetails.setServiceName(rs.getString("SERVLIST.SERVICE_NAME"));
		serviceDetails.setServiceDesc(rs.getString("SERVLIST.SERVICE_DESC"));
		serviceDetails.setActualPrice(rs.getDouble("SERVLIST.ACTUAL_PRICE"));
		serviceDetails.setEffectivePrice(rs.getDouble("SERVLIST.EFFECTIVE_PRICE"));
		serviceDetails.setUnitDesc(rs.getString("SERVLIST.UNIT_DESC"));
		serviceDetails.setPicPath(rs.getString("SERVLIST.PIC_PATH"));
		serviceDetails.setServiceCat(rs.getString("SERVCAT.SERVICE_CAT"));
		serviceDetails.setServiceCatDesc(rs.getString("SERVCAT.SERVICE_CAT_DESC"));
				
		return serviceDetails;
	}
	
}
