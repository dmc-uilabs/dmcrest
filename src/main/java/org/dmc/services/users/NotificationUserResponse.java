package org.dmc.services.users;

public class NotificationUserResponse {

	String responseDescription;

	public NotificationUserResponse() {}

	public NotificationUserResponse(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

}
