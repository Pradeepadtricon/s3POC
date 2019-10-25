package com.souro.file_upload.exception;

@SuppressWarnings("serial")
public class LMSServiceException extends Exception {

	public LMSServiceException() {
		super();
		
	}

	public LMSServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public LMSServiceException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public LMSServiceException(String message) {
		super(message);
	
	}

	public LMSServiceException(Throwable cause) {
		super(cause);
		
	}

}
