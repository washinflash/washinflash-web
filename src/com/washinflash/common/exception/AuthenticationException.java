package com.washinflash.common.exception;

import com.washinflash.common.util.StatusConstant;


public class AuthenticationException extends CustomException {

	private static final long serialVersionUID = 736342628363L;
	
	public AuthenticationException (Exception ex) {
		super(ex);
	}
	
	public AuthenticationException (StatusConstant errorCode, String errorMessage) {
		super (errorCode, errorMessage);
	}
	
	public AuthenticationException (StatusConstant errorCode, String errorMessage, Exception ex) {
		super(errorCode, errorMessage, ex);
	}
	
}
