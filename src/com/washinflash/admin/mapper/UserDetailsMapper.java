package com.washinflash.admin.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.UserDetails;
import com.washinflash.common.util.GenericUtils;

public class UserDetailsMapper implements RowMapper<UserDetails> {

	public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		UserDetails userDetails = new UserDetails();
		userDetails.setUserDetailsId(rs.getInt("USER_DETAILS_ID"));
		userDetails.setName(rs.getString("NAME"));
		userDetails.setEmail(rs.getString("EMAIL"));
		userDetails.setMobileNo(rs.getString("MOBILE_NO"));
		userDetails.setMobVerified(rs.getString("MOB_VERIFIED"));
		userDetails.setReferralCode(rs.getString("REFERRAL_CODE"));
		userDetails.setForcePassReset(rs.getString("FORCE_PASS_RESET"));
		userDetails.setUserType(rs.getString("USER_TYPE"));
		userDetails.setFacebookId(GenericUtils.replaceEmptyString(rs.getString("FACEBOOK_ID"), "-"));
		
		return userDetails;
	}
	
}
