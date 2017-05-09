package com.washinflash.rest.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.ServiceTypeDetails;

public class ServiceTypeDetailsMapper implements RowMapper<ServiceTypeDetails> {

	public ServiceTypeDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ServiceTypeDetails serviceTypeDetails = new ServiceTypeDetails();
		serviceTypeDetails.setServiceTypeId(rs.getInt("SERVICE_TYPE_ID"));
		serviceTypeDetails.setServiceType(rs.getString("SERVICE_TYPE"));
		serviceTypeDetails.setServiceTypeDesc(rs.getString("SERVICE_TYPE_DESC"));
		serviceTypeDetails.setMinOrderValue(rs.getInt("MIN_ORDER_VALUE"));
		
		return serviceTypeDetails;
	}
	
}
