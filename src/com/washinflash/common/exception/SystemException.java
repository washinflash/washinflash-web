package com.washinflash.common.exception;

import com.washinflash.common.util.StatusConstant;

public class SystemException extends CustomException {

	private static final long serialVersionUID = 123456789L;
	
	public SystemException (StatusConstant errorCode, Exception ex) {
		super(errorCode, ex);
	}
	
	public SystemException (StatusConstant errorCode, String errorMessage) {
		super (errorCode, errorMessage);
	}
	
	public SystemException (StatusConstant errorCode, String errorMessage, Exception ex) {
		super(errorCode, errorMessage, ex);
	}

}
