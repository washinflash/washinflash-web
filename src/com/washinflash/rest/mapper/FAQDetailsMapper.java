package com.washinflash.rest.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.washinflash.common.object.model.FAQDetails;

public class FAQDetailsMapper implements RowMapper<FAQDetails> {

	public FAQDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		FAQDetails faqDetails = new FAQDetails();
		faqDetails.setFaqDetailsId(rs.getInt("FAQ_DETAILS_ID"));
		faqDetails.setQuestion(rs.getString("FAQ_QUESTION"));
		faqDetails.setAnswer(rs.getString("FAQ_ANSWER"));
		
		return faqDetails;
	}
	
}
