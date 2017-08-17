package org.dmc.services.exceptions;

public class TooManyAttemptsException extends Exception {
	public TooManyAttemptsException() {
		super("Number of daily payment failures exceeded!");
	}
}
