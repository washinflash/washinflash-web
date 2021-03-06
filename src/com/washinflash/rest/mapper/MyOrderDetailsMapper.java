package com.washinflash.rest.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.OrderDetails;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;

public class MyOrderDetailsMapper implements RowMapper<OrderDetails> {

	public OrderDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setOrderId(rs.getInt("DET.ORDER_ID"));
		String deliveryType = rs.getString("DET.DELIVERY_TYPE");
		if(deliveryType == null) {
			deliveryType = GenericConstant.SHOW_NA;
		}
		orderDetails.setDeliveryType(deliveryType);
		orderDetails.setOrderRef(rs.getString("DET.ORDER_REFERENCE"));
		orderDetails.setOrderDate(GenericUtils.getISTFormattedStringFromDate(rs.getTimestamp("ORDER_DATE"), GenericConstant.DISPLAY_DATE_YEAR_FORMAT));
		orderDetails.setPickupDate(GenericUtils.getFormattedStringFromDate(rs.getDate("DET.PICKUP_DATE"), GenericConstant.DEFAULT_DATE_FORMAT));
		orderDetails.setPickupTime(rs.getString("DET.PICKUP_TIME"));		
		orderDetails.setDeliveryDate(GenericUtils.getFormattedStringFromDate(rs.getDate("DET.DELIVERY_DATE"), GenericConstant.DEFAULT_DATE_FORMAT));		
		orderDetails.setDeliveryTime(rs.getString("DET.DELIVERY_TIME"));
		orderDetails.setTotalPriceMultiplier(rs.getDouble("DET.TOTAL_PRICE_MULTIPLIER"));
		orderDetails.setTotalPrice(rs.getDouble("DET.TOTAL_PRICE"));
		orderDetails.setMinimumOrderValue(rs.getInt("DET.MINIMUM_ORDER_VALUE"));
		orderDetails.setAddressId(rs.getInt("DET.ADDRESS_ID"));
		orderDetails.setApproxNoClothes(rs.getInt("DET.APPROX_NO_CLOTHES"));
		orderDetails.setActualNoClothes(rs.getInt("DET.ACTUAL_NO_CLOTHES"));
		orderDetails.setCouponCode(rs.getString("DET.COUPON_CODE"));
		orderDetails.setDonateOnly(rs.getString("DET.DONATE_ONLY"));
		orderDetails.setLatestStatus(rs.getString("HIST.STATUS"));
		
		return orderDetails;
	}
	
}
