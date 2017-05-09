package com.washinflash.common.exception;

import com.washinflash.common.util.StatusConstant;


public class BusinessException extends CustomException {

	private static final long serialVersionUID = 987654321L;	
	
	public BusinessException (Exception ex) {
		super(ex);
	}
	
	public BusinessException (StatusConstant errorCode, String errorMessage) {
		super (errorCode, errorMessage);
	}
	
	public BusinessException (StatusConstant errorCode, String errorMessage, Exception ex) {
		super(errorCode, errorMessage, ex);
	}
}
