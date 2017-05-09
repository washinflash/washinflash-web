package com.washinflash.rest.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.CityDetails;

public class CityDetailsMapper implements RowMapper<CityDetails> {

	public CityDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		CityDetails cityDetails = new CityDetails();
		cityDetails.setServiceCityId(rs.getInt("SERVICE_CITY_ID"));
		cityDetails.setCityCode(rs.getString("CITY_CODE"));
		cityDetails.setCityName(rs.getString("CITY_NAME"));
		
		return cityDetails;
	}
	
}
