package org.dmc.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;

import com.amazonaws.services.importexport.model.MissingParameterException;

@ResponseBody
@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity handleUncaughtException(Exception e) {
		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder("REST Internal Error").build();
		return new ResponseEntity<ErrorMessage>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
