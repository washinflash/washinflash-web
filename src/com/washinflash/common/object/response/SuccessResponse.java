package com.washinflash.common.object.response;

import com.washinflash.common.util.GenericConstant;
import com.washinflash.common.util.StatusConstant;


public class SuccessResponse {

	private ResponseStatus responseStatus = new ResponseStatus();

	public SuccessResponse() {
		responseStatus.setStatusCode(StatusConstant.SUCCESS.toString());
		responseStatus.setStatusMessage(GenericConstant.DEFAULT_SUCCESS_MSG);
	}	
	
	public SuccessResponse(String statusMessage) {
		responseStatus.setStatusCode(StatusConstant.SUCCESS.toString());
		responseStatus.setStatusMessage(statusMessage);
	}
	
	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}
	
}
