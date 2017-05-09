package com.washinflash.rest.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.ServiceAreaDetails;

public class ServiceAreaDetailsMapper implements RowMapper<ServiceAreaDetails> {

	public ServiceAreaDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ServiceAreaDetails serviceAreaDetails = new ServiceAreaDetails();
		serviceAreaDetails.setServiceAreaId(rs.getInt("SERVICE_AREA_ID"));
		serviceAreaDetails.setServiceAreaCode(rs.getString("SERVICE_AREA_CODE"));
		serviceAreaDetails.setServiceAreaName(rs.getString("SERVICE_AREA_NAME"));
		serviceAreaDetails.setServiceCityId(rs.getInt("SERVICE_CITY_ID"));
		
		return serviceAreaDetails;
	}
	
}
