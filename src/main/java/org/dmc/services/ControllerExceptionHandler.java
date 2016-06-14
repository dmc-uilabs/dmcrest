package org.dmc.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.amazonaws.services.importexport.model.MissingParameterException;

@ResponseBody
@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(MissingParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void handleMissingParameterException() {}
}
