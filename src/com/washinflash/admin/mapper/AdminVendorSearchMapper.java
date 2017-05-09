package com.washinflash.admin.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.admin.model.SearchOrderDetails;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;

public class AdminVendorSearchMapper implements RowMapper<SearchOrderDetails> {

	public SearchOrderDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SearchOrderDetails searchResult = new SearchOrderDetails();
		searchResult.setOrderId(rs.getInt("DET.ORDER_ID"));
		searchResult.setOrderServiceMappingId(rs.getInt("MAP.ORDER_SERVICE_MAPPING_ID"));
		searchResult.setOrderRef(rs.getString("DET.ORDER_REFERENCE"));		
		searchResult.setLatestStatus(rs.getString("HIST.STATUS"));
		searchResult.setServiceStatus(rs.getString("MAP.ORDER_STATUS"));
		searchResult.setName(rs.getString("MAP.VENDOR_NAME"));
		searchResult.setMobileNo(rs.getString("MAP.MOBILE_NO"));
		searchResult.setPinCode(rs.getInt("ADDR.PIN_CODE"));
		searchResult.setPickupDate(GenericUtils.getFormattedStringFromDate(rs.getDate("DET.PICKUP_DATE"), GenericConstant.DEFAULT_DATE_FORMAT));
		searchResult.setVendorPickupDate(GenericUtils.getISTFormattedStringFromDate(rs.getTimestamp("MAP.ASSIGNED_VENDOR_ON"), GenericConstant.DEFAULT_DATE_TIME_FORMAT));	
		searchResult.setDeliveryDate(GenericUtils.getFormattedStringFromDate(rs.getDate("DET.DELIVERY_DATE"), GenericConstant.DEFAULT_DATE_FORMAT));		
		searchResult.setServiceType(rs.getString("SERV.SERVICE_TYPE_DESC"));
		
		return searchResult;
	}
	
}
