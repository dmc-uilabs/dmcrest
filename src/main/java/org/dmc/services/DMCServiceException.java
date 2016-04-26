package org.dmc.services;

import org.springframework.http.HttpStatus;


public class DMCServiceException extends Exception {

	private DMCError error;
	private String errorMessage;
	
	public DMCServiceException(DMCError e, String m)
	{
		this.error = e;
		this.errorMessage = m;
	}
	
	public DMCError getError() {
		return this.error;
	}
	
	public void setError(DMCError error) {
		this.error = error;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public HttpStatus getHttpStatusCode() {
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		switch(error) {
			case NotAdminUser:
				status = HttpStatus.FORBIDDEN; 
				break;	
			case NotDMDIIMember:
			case NotProjectAdmin:
			case OnlyProjectAdmin:
				status = HttpStatus.FORBIDDEN;
				break;
			case OtherSQLError:
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				break;
		}
		return status;
	}
}
