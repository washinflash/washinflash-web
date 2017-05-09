package com.washinflash.rest.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.ApplicationPicDetails;

public class ApplicationPicDetailsMapper implements RowMapper<ApplicationPicDetails> {

	public ApplicationPicDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ApplicationPicDetails applicationPicDetails = new ApplicationPicDetails();
		applicationPicDetails.setPicId(rs.getInt("APP_PIC_ID"));
		applicationPicDetails.setPicPath(rs.getString("PIC_PATH"));
		applicationPicDetails.setPicType(rs.getString("PIC_TYPE"));
		applicationPicDetails.setPicTitle(rs.getString("PIC_TITLE"));
		applicationPicDetails.setPicDetails(rs.getString("PIC_DETAILS"));
		
		return applicationPicDetails;
	}
	
}
