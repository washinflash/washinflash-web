package com.washinflash.common.object.response;

import java.util.List;

public class ServiceDetailsParamResponse extends SuccessResponse {

	private int noOfPickupDays;
	private int noOfDeliveryDays;
	private int minServiceDays;
	private int disablePickupSlotCountForFirstDay;
	private int disableDeliverySlotCountForFirstDay;
	private List<String> pickupTimeSlotList;
	private List<String> deliveryTimeSlotList;
	private List<String> availableDaysList;
	private String todayDateStr;
	private String tomorrowDateStr;
	

	public int getDisablePickupSlotCountForFirstDay() {
		return disablePickupSlotCountForFirstDay;
	}
	public void setDisablePickupSlotCountForFirstDay(
			int disablePickupSlotCountForFirstDay) {
		this.disablePickupSlotCountForFirstDay = disablePickupSlotCountForFirstDay;
	}
	public int getNoOfPickupDays() {
		return noOfPickupDays;
	}
	public void setNoOfPickupDays(int noOfPickupDays) {
		this.noOfPickupDays = noOfPickupDays;
	}
	public int getNoOfDeliveryDays() {
		return noOfDeliveryDays;
	}
	public void setNoOfDeliveryDays(int noOfDeliveryDays) {
		this.noOfDeliveryDays = noOfDeliveryDays;
	}
	public int getMinServiceDays() {
		return minServiceDays;
	}
	public void setMinServiceDays(int minServiceDays) {
		this.minServiceDays = minServiceDays;
	}
	public List<String> getPickupTimeSlotList() {
		return pickupTimeSlotList;
	}
	public void setPickupTimeSlotList(List<String> pickupTimeSlotList) {
		this.pickupTimeSlotList = pickupTimeSlotList;
	}
	public List<String> getDeliveryTimeSlotList() {
		return deliveryTimeSlotList;
	}
	public void setDeliveryTimeSlotList(List<String> deliveryTimeSlotList) {
		this.deliveryTimeSlotList = deliveryTimeSlotList;
	}
	public List<String> getAvailableDaysList() {
		return availableDaysList;
	}
	public void setAvailableDaysList(List<String> availableDaysList) {
		this.availableDaysList = availableDaysList;
	}
	public String getTodayDateStr() {
		return todayDateStr;
	}
	public void setTodayDateStr(String todayDateStr) {
		this.todayDateStr = todayDateStr;
	}
	public String getTomorrowDateStr() {
		return tomorrowDateStr;
	}
	public void setTomorrowDateStr(String tomorrowDateStr) {
		this.tomorrowDateStr = tomorrowDateStr;
	}
	public int getDisableDeliverySlotCountForFirstDay() {
		return disableDeliverySlotCountForFirstDay;
	}
	public void setDisableDeliverySlotCountForFirstDay(
			int disableDeliverySlotCountForFirstDay) {
		this.disableDeliverySlotCountForFirstDay = disableDeliverySlotCountForFirstDay;
	}
	
}
