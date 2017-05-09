package com.washinflash.common.object.model;

public class ServiceDetails {

	private String serviceName;
	private String serviceDesc;
	private double actualPrice;
	private double effectivePrice;
	private String unitDesc;
	private String picPath;
	private String serviceCat;
	private String serviceCatDesc;
	
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	public double getActualPrice() {
		return actualPrice;
	}
	public void setActualPrice(double actualPrice) {
		this.actualPrice = actualPrice;
	}
	public double getEffectivePrice() {
		return effectivePrice;
	}
	public void setEffectivePrice(double effectivePrice) {
		this.effectivePrice = effectivePrice;
	}
	public String getServiceCat() {
		return serviceCat;
	}
	public void setServiceCat(String serviceCat) {
		this.serviceCat = serviceCat;
	}
	public String getUnitDesc() {
		return unitDesc;
	}
	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}
	public String getServiceCatDesc() {
		return serviceCatDesc;
	}
	public void setServiceCatDesc(String serviceCatDesc) {
		this.serviceCatDesc = serviceCatDesc;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	
}
