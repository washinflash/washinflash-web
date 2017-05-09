package com.washinflash.rest.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.ServiceDetails;
import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.GenericUtils;
import com.washinflash.common.util.StatusConstant;
import com.washinflash.rest.dao.ServiceDetailsDAO;
import com.washinflash.rest.dao.jdbctemplate.SpringJDBCTemplate;
import com.washinflash.rest.mapper.ServiceDetailsMapper;

@Component
public class ServiceDetailsDAOImpl extends SpringJDBCTemplate implements ServiceDetailsDAO {

	@Override
	public List<ServiceDetails> getPriceList(String serviceCat) throws SystemException {
		
		String selectSQL = "SELECT SERVLIST.SERVICE_NAME, SERVLIST.SERVICE_DESC, SERVLIST.ACTUAL_PRICE, SERVLIST.EFFECTIVE_PRICE, SERVLIST.UNIT_DESC, SERVLIST.PIC_PATH, " +
				"SERVCAT.SERVICE_CAT, SERVCAT.SERVICE_CAT_DESC FROM SERVICE_LIST AS SERVLIST, SERVICE_CATEGORY AS SERVCAT " +
				"WHERE SERVLIST.SERVICE_CAT_ID = SERVCAT.SERVICE_CAT_ID AND SERVLIST.STATUS = ? AND SERVCAT.STATUS = ? AND SERVCAT.SERVICE_CAT = ? " +
				"ORDER BY SERVLIST.SERVICE_LIST_ID";
		
		List<ServiceDetails> priceList = getJDBCTemplateObject().query(selectSQL, new ServiceDetailsMapper(), 
				StatusConstant.ACTIVE.toString(), StatusConstant.ACTIVE.toString(), serviceCat);
		
		return priceList;
	}
	
	public List<String> getNextHolidays(int limit) throws SystemException {
		
		String selectSQL = "SELECT DATE_FORMAT(CLOSE_ON, '%d-%m-%Y') CLOSE_DAY FROM SERVICE_CLOSE_DAYS WHERE CLOSE_ON >= STR_TO_DATE(?,'%d-%m-%Y') " +
				"AND CLOSE_ON <= DATE_ADD(STR_TO_DATE(?,'%d-%m-%Y'), INTERVAL ? DAY) AND STATUS = ?";
		List<String> nextHolidayList = new ArrayList<>();

		String currDateStr = GenericUtils.getCurrentISTFormatedDate(GenericConstant.DEFAULT_DATE_FORMAT);
		
		List<Map<String, Object>> returnList = getJDBCTemplateObject().queryForList(selectSQL, currDateStr, currDateStr, limit, StatusConstant.ACTIVE.toString());

		for (Map<String, Object> row : returnList) {
			nextHolidayList.add((String) row.get("CLOSE_DAY"));
		}
		
		return nextHolidayList;
	}

}
