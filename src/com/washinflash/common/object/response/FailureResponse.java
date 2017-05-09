package com.washinflash.common.object.response;

import com.washinflash.common.util.StatusConstant;


public class FailureResponse {

	private ResponseStatus responseStatus = new ResponseStatus();
	
	public FailureResponse(String statusMessage) {
		responseStatus.setStatusCode(StatusConstant.FAILURE.toString());
		responseStatus.setErrorCode(StatusConstant.FAILED.toString());
		responseStatus.setStatusMessage(statusMessage);
	}
	
	public FailureResponse(String statusMessage, StatusConstant errorCode) {
		responseStatus.setStatusCode(StatusConstant.FAILURE.toString());
		responseStatus.setErrorCode(errorCode.toString());
		responseStatus.setStatusMessage(statusMessage);
	}
	
	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}
	
}
