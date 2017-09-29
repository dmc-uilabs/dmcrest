package org.dmc.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.DMCError;

@ResponseBody
@ControllerAdvice
public class ControllerExceptionHandler {
	private final String logTag = ControllerExceptionHandler.class.getName();

	@ExceptionHandler(Exception.class)
	public ResponseEntity handleUncaughtException(Exception e) {
		ServiceLogger.logException(logTag, new DMCServiceException(DMCError.Generic, e.getMessage()));
		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder("REST Internal Error").build();
		return new ResponseEntity<ErrorMessage>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
