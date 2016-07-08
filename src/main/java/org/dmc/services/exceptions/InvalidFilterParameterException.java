package org.dmc.services.exceptions;

public class InvalidFilterParameterException extends Exception {
	
	public InvalidFilterParameterException(String param, Class expectedType) {
		super("Invalid filter parameter! Param " + param + " should be of type " + expectedType.getName());
	}

}
