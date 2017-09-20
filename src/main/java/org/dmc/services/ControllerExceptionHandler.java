package org.dmc.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.DMCError;

import com.amazonaws.services.importexport.model.MissingParameterException;

@ResponseBody
@ControllerAdvice
public class ControllerExceptionHandler {
	private final String logTag = ControllerExceptionHandler.class.getName();
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorMessage> handleSpringAccessError(AccessDeniedException e) {
		ServiceLogger.logException(logTag, new DMCServiceException(DMCError.UnauthorizedAccessAttempt, e.getMessage()));
		return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleUncaughtException(Exception e) {
		ServiceLogger.logException(logTag, new DMCServiceException(DMCError.Generic, e.getMessage()));
		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder("REST Internal Error").build();
		return new ResponseEntity<ErrorMessage>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
