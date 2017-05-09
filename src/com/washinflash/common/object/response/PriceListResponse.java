package com.washinflash.common.object.response;

import java.util.List;

import com.washinflash.common.object.model.ServiceDetails;

public class PriceListResponse extends SuccessResponse {

	private List<List<ServiceDetails>> regularPriceCatList;
	private List<List<ServiceDetails>> premiumPriceCatList;
	private List<List<ServiceDetails>> dryCleaningPriceCatList;

	public List<List<ServiceDetails>> getRegularPriceCatList() {
		return regularPriceCatList;
	}

	public void setRegularPriceCatList(List<List<ServiceDetails>> regularPriceCatList) {
		this.regularPriceCatList = regularPriceCatList;
	}

	public List<List<ServiceDetails>> getPremiumPriceCatList() {
		return premiumPriceCatList;
	}

	public void setPremiumPriceCatList(List<List<ServiceDetails>> premiumPriceCatList) {
		this.premiumPriceCatList = premiumPriceCatList;
	}

	public List<List<ServiceDetails>> getDryCleaningPriceCatList() {
		return dryCleaningPriceCatList;
	}

	public void setDryCleaningPriceCatList(List<List<ServiceDetails>> dryCleaningPriceCatList) {
		this.dryCleaningPriceCatList = dryCleaningPriceCatList;
	}
	
}
