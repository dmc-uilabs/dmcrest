package org.dmc.services;

import org.springframework.http.HttpStatus;


public class DMCServiceException extends RuntimeException {

    private static final long serialVersionUID = 3203632117392984971L;
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
			case UnknownUser:
				status = HttpStatus.FORBIDDEN;
				break;

			case UnauthorizedAccessAttempt:
			case InvalidAccountId:
				status = HttpStatus.UNAUTHORIZED;
				break;
			case AWSError:
			case UnexpectedDOMEError:
			case UnexpectedDOMEConnectionError:
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				break;
			case NotDMDIIMember:
			case NotProjectAdmin:
			case OnlyProjectAdmin:
			case MemberNotAssignedToProject:
				status = HttpStatus.FORBIDDEN;
				break;
			case OtherSQLError:
			case CanNotCreateQueue:
			case CanNotCloseActiveMQConnection:
			case CannotPatchDOMEServerEntry:
			case CannotDeleteDOMEServerEntry:
			case CannotCreateDOMEServerEntry:
			case UnknownSQLError:
			case UnableToLogServiceHistory:
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				break;
			case IncorrectType:
			case InvalidCommentId:
			case InvalidDiscussionId:
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
			case DiscussionFollowNotFound:
			default:
				status = HttpStatus.NOT_FOUND;
		}
		return status;
	}
}
