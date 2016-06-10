package org.dmc.services;

import org.springframework.http.HttpStatus;


public class DMCServiceException extends Exception {

	private DMCError error;
	private String errorMessage;
	
	public DMCServiceException(){
		this.error = DMCError.Generic;
		this.errorMessage = "";
	}

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

		HttpStatus status;

		switch(error) {
			case NotAdminUser:
			case UnknownUser:
				status = HttpStatus.FORBIDDEN;
				break;

			case UnauthorizedAccessAttempt:
				status = HttpStatus.UNAUTHORIZED;
				break;
			case AWSError:
			case UnexpectedDOMEError:
			case UnexceptedDOMEConnectionError:
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				break;
			case NotDMDIIMember:
			case NotProjectAdmin:
			case OnlyProjectAdmin:
				status = HttpStatus.FORBIDDEN;
				break;
			case OtherSQLError:
			case CanNotCreateQueue:
			case CanNotCloseActiveMQConnection:
			case CannotPatchDOMEServerEntry:
			case CannotDeleteDOMEServerEntry:
			case CannotCreateDOMEServerEntry:
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				break;
			case IncorrectType:
				status = HttpStatus.BAD_REQUEST;
				break;
			case CannotConnectToDome:
				status = HttpStatus.GATEWAY_TIMEOUT;
				break;
			case BadURL:
				status = HttpStatus.UNPROCESSABLE_ENTITY;
				break;
			case NoContentInQuery:
				status = HttpStatus.NO_CONTENT;
				break;
			default:
				status = HttpStatus.NOT_FOUND;
		}
		return status;
	}
}
