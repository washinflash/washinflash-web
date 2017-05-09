package com.washinflash.rest.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.DeliveryTypeDetails;

public class DeliveryTypeDetailsMapper implements RowMapper<DeliveryTypeDetails> {

	public DeliveryTypeDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

		DeliveryTypeDetails deliveryTypeDetails = new DeliveryTypeDetails();
		deliveryTypeDetails.setDeliveryTypeId(rs.getInt("DELIVERY_TYPE_ID"));
		deliveryTypeDetails.setDeliveryType(rs.getString("DELIVERY_TYPE"));
		deliveryTypeDetails.setDeliveryTypeDesc(rs.getString("DELIVERY_TYPE_DESC"));
		deliveryTypeDetails.setTotalBillMulti(rs.getDouble("TOTAL_BILL_MULTI"));
		
		return deliveryTypeDetails;
	}
	
}
