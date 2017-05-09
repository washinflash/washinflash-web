package com.washinflash.rest.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.ServiceCategoryDetails;

public class ServiceCategoryDetailsMapper implements RowMapper<ServiceCategoryDetails> {

	public ServiceCategoryDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ServiceCategoryDetails serviceCategoryDetails = new ServiceCategoryDetails();
		serviceCategoryDetails.setServiceCatId(rs.getInt("CAT.SERVICE_CAT_ID"));
		serviceCategoryDetails.setServiceCat(rs.getString("CAT.SERVICE_CAT"));
		serviceCategoryDetails.setServiceCatDesc(rs.getString("CAT.SERVICE_CAT_DESC"));
		serviceCategoryDetails.setServiceTypeId(rs.getInt("CAT.SERVICE_TYPE_ID"));
		
		return serviceCategoryDetails;
	}
	
}
