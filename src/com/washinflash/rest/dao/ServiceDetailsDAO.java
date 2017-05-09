package com.washinflash.rest.dao;

import java.util.List;

import com.washinflash.common.exception.SystemException;
import com.washinflash.common.object.model.ServiceDetails;

public interface ServiceDetailsDAO {

	public List<ServiceDetails> getPriceList(String serviceCat) throws SystemException;
	public List<String> getNextHolidays(int limit) throws SystemException;
	
}
