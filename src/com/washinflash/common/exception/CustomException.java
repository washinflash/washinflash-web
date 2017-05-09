package com.washinflash.common.exception;

import com.washinflash.common.util.StatusConstant;

public abstract class CustomException extends Exception {

	private static final long serialVersionUID = 876353421112L;
	private StatusConstant errorCode;
	private String errorMessage;
	
	public CustomException (Exception ex) {
		super(ex);
	}
	public CustomException (StatusConstant errorCode, Exception ex) {
		super(ex);
		this.errorCode = errorCode;
	}
	public CustomException (StatusConstant errorCode, String errorMessage) {
		super (errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	public CustomException (StatusConstant errorCode, String errorMessage, Exception ex) {
		super(errorMessage, ex);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}	

	public StatusConstant getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(StatusConstant errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
