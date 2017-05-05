package org.dmc.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;

import org.dmc.services.ServiceLogger;

import com.amazonaws.services.importexport.model.MissingParameterException;

@ResponseBody
@ControllerAdvice
public class ControllerExceptionHandler {
	private final String logTag = ControllerExceptionHandler.class.getName();

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity handleUncaughtException(Exception e) {
		ServiceLogger.log(logTag, e.getMessage());
		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder("REST Internal Error").build();
		return new ResponseEntity<ErrorMessage>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
