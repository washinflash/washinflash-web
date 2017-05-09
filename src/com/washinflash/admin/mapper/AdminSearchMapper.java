package com.washinflash.admin.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.admin.model.SearchOrderDetails;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;

public class AdminSearchMapper implements RowMapper<SearchOrderDetails> {

	public SearchOrderDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SearchOrderDetails searchResult = new SearchOrderDetails();
		searchResult.setOrderId(rs.getInt("DET.ORDER_ID"));
		searchResult.setOrderRef(rs.getString("DET.ORDER_REFERENCE"));		
		searchResult.setLatestStatus(rs.getString("HIST.STATUS"));
		searchResult.setName(rs.getString("ADDR.NAME"));
		searchResult.setMobileNo(rs.getString("ADDR.MOBILE_NO"));
		searchResult.setPinCode(rs.getInt("ADDR.PIN_CODE"));
		searchResult.setPickupDate(GenericUtils.getFormattedStringFromDate(rs.getDate("DET.PICKUP_DATE"), GenericConstant.DEFAULT_DATE_FORMAT));
		searchResult.setPickupTime(rs.getString("DET.PICKUP_TIME"));		
		searchResult.setDeliveryDate(GenericUtils.getFormattedStringFromDate(rs.getDate("DET.DELIVERY_DATE"), GenericConstant.DEFAULT_DATE_FORMAT));		
		searchResult.setDeliveryTime(rs.getString("DET.DELIVERY_TIME"));	
		
		String deliveryType = rs.getString("DET.DELIVERY_TYPE");
		if(!GenericUtils.isEmpty(deliveryType))  {
			deliveryType = deliveryType.replace("Delivery", "");
		}
		searchResult.setDeliveryType(deliveryType);
		
		String serviceType = rs.getString("SERV.SERVICE_TYPE");
		serviceType = serviceType.replaceAll(" Wash", "");
		serviceType = serviceType.replaceAll(" Charity", "");
		serviceType = serviceType.replaceAll(" Cleaning", "");
		searchResult.setServiceType(serviceType);
		
		return searchResult;
	}
	
}
