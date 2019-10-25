package com.souro.file_upload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class LMSControllerException extends RuntimeException{

	public LMSControllerException(String exception) {
		super(exception);
		
	}

}
