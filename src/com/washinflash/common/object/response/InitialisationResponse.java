package com.washinflash.common.object.response;

import java.util.Map;


public class InitialisationResponse extends SuccessResponse {

	private String verSupportedFlag;
	private String verNotSupportedMessage;
	private boolean isValidSession;
	private Map<String, Object> applicationParamMap;
	
	
	public String getVerSupportedFlag() {
		return verSupportedFlag;
	}

	public void setVerSupportedFlag(String verSupportedFlag) {
		this.verSupportedFlag = verSupportedFlag;
	}

	public String getVerNotSupportedMessage() {
		return verNotSupportedMessage;
	}

	public void setVerNotSupportedMessage(String verNotSupportedMessage) {
		this.verNotSupportedMessage = verNotSupportedMessage;
	}

	public boolean isValidSession() {
		return isValidSession;
	}

	public void setValidSession(boolean isValidSession) {
		this.isValidSession = isValidSession;
	}

	public Map<String, Object> getApplicationParamMap() {
		return applicationParamMap;
	}

	public void setApplicationParamMap(Map<String, Object> applicationParamMap) {
		this.applicationParamMap = applicationParamMap;
	}

}
