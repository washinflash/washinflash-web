package com.washinflash.rest.businessservice;

import java.util.Map;

import com.washinflash.common.exception.SystemException;

public interface OrderService {

	public String createOrder(String reqJsonString, Map<String, Object> appParam) throws SystemException;
	public String getMyOrderDetailsList(String reqJsonString) throws SystemException;
	public String cancelMyOrder(String reqJsonString) throws SystemException;
}
