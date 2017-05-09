package com.washinflash.common.object.model;

import java.util.List;

public class ServiceTypeDetails {

	private int serviceTypeId;
	private String serviceType;
	private String serviceTypeDesc;
	private int minOrderValue;
	
	private List<ServiceDeliveryMapDetails> serviceDeliveryMapList;
	
	
	public int getServiceTypeId() {
		return serviceTypeId;
	}
	public void setServiceTypeId(int serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceTypeDesc() {
		return serviceTypeDesc;
	}
	public void setServiceTypeDesc(String serviceTypeDesc) {
		this.serviceTypeDesc = serviceTypeDesc;
	}
	public int getMinOrderValue() {
		return minOrderValue;
	}
	public void setMinOrderValue(int minOrderValue) {
		this.minOrderValue = minOrderValue;
	}
	public List<ServiceDeliveryMapDetails> getServiceDeliveryMapList() {
		return serviceDeliveryMapList;
	}
	public void setServiceDeliveryMapList(
			List<ServiceDeliveryMapDetails> serviceDeliveryMapList) {
		this.serviceDeliveryMapList = serviceDeliveryMapList;
	}

}
