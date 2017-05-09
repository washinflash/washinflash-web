package com.washinflash.rest.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.ServiceDeliveryMapDetails;

public class ServiceDeliveryMapMapper implements RowMapper<ServiceDeliveryMapDetails> {

	public ServiceDeliveryMapDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ServiceDeliveryMapDetails mapDetails = new ServiceDeliveryMapDetails();
		mapDetails.setServiceDelMapId(rs.getInt("SERV_DEL_MAPPING_ID"));
		mapDetails.setServiceTypeId(rs.getInt("SERVICE_TYPE_ID"));
		mapDetails.setDeliveryTypeId(rs.getInt("DELIVERY_TYPE_ID"));
		mapDetails.setDeliveryTime(rs.getInt("DELIVERY_TIME_HOURS"));
		mapDetails.setDeliveryTimeStr(mapDetails.getDeliveryTime() + " hrs");
		
		return mapDetails;
	}
	
}
