package org.dmc.services;

import org.springframework.http.HttpStatus;


public class DMCServiceException extends Exception {
	
	public static final int NotAdminUser = 1;
	public static final int NotDMDIIMember = 2;
	public static final int CanNotInsertChangeLog = 3;
	public static final int OtherSQLError = 4;
	public static final int CompanySkillSetNotExist = 5;
	
	private int errorCode;
	private String errorMessage;
	
	public DMCServiceException(int c, String m)
	{
		this.errorCode = c;
		this.errorMessage = m;
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}
	
	public void setErrorCode(int errorcode) {
		this.errorCode = errorcode;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public HttpStatus getHttpStatusCode() {
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		switch(errorCode) {
			case NotAdminUser:
				status = HttpStatus.FORBIDDEN; 
				break;	
			case NotDMDIIMember:
				status = HttpStatus.FORBIDDEN;
				break;
			case OtherSQLError:
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				break;
		}
		return status;
	}
}
