package com.washinflash.rest.businessservice;

import java.util.Map;

import com.washinflash.common.exception.SystemException;

public interface ServiceDetailsService {

	public String getPriceList(String reqJsonString) throws SystemException;
	public String getServiceDetailsParam(String reqJsonString, Map<String, Object> appParam) throws SystemException;
}
