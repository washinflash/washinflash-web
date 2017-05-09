package com.washinflash.common.object.request;

import java.util.List;

import com.washinflash.common.object.model.OrderDetails;


public class OrderDetailsRequest extends OrderDetails {

	private String token;
	private CommonInfo commonInfo;
	
	private String historyComments;
	private List<String> serviceTypeList;
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getHistoryComments() {
		return historyComments;
	}

	public void setHistoryComments(String historyComments) {
		this.historyComments = historyComments;
	}

	public List<String> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<String> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}
	
	public CommonInfo getCommonInfo() {
		return commonInfo;
	}

	public void setCommonInfo(CommonInfo commonInfo) {
		this.commonInfo = commonInfo;
	}
}