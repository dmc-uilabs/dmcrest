package org.dmc.services;

import org.springframework.http.HttpStatus;


public class DMCServiceException extends Exception {

	private DMCError error;
	public DMCServiceException(DMCError e, String m)
	{
	    super(m);
		this.error = e;
	}

	public DMCError getError() {
		return this.error;
	}

	public void setError(DMCError error) {
		this.error = error;
	}

	public HttpStatus getHttpStatusCode() {

		HttpStatus status = HttpStatus.NOT_FOUND;

		switch(error) {
			case NotAdminUser:
				status = HttpStatus.FORBIDDEN;
				break;
			case AWSError:
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
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				break;
			case IncorrectType:
				status = HttpStatus.BAD_REQUEST;
				break;
		}
		return status;
	}
}
