package com.washinflash.admin.model;

import java.util.ArrayList;
import java.util.List;


public class AdminSearchResult {
	
	private List<SearchOrderDetails> orderDetailsList = new ArrayList<>();
	
	private boolean disableFirstRecordButton;
	private boolean disablePrevRecordButton;
	private boolean disableNextRecordButton;
	private boolean disableLastRecordButton;

	private int lastRecordIndex;

	public List<SearchOrderDetails> getOrderDetailsList() {
		return orderDetailsList;
	}

	public void setOrderDetailsList(List<SearchOrderDetails> orderDetailsList) {
		this.orderDetailsList = orderDetailsList;
	}

	public boolean isDisableFirstRecordButton() {
		return disableFirstRecordButton;
	}

	public void setDisableFirstRecordButton(boolean disableFirstRecordButton) {
		this.disableFirstRecordButton = disableFirstRecordButton;
	}

	public boolean isDisablePrevRecordButton() {
		return disablePrevRecordButton;
	}

	public void setDisablePrevRecordButton(boolean disablePrevRecordButton) {
		this.disablePrevRecordButton = disablePrevRecordButton;
	}

	public boolean isDisableNextRecordButton() {
		return disableNextRecordButton;
	}

	public void setDisableNextRecordButton(boolean disableNextRecordButton) {
		this.disableNextRecordButton = disableNextRecordButton;
	}

	public boolean isDisableLastRecordButton() {
		return disableLastRecordButton;
	}

	public void setDisableLastRecordButton(boolean disableLastRecordButton) {
		this.disableLastRecordButton = disableLastRecordButton;
	}

	public int getLastRecordIndex() {
		return lastRecordIndex;
	}

	public void setLastRecordIndex(int lastRecordIndex) {
		this.lastRecordIndex = lastRecordIndex;
	}
	
}
