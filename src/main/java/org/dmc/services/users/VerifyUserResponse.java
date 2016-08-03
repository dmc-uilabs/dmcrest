package org.dmc.services.users;

public class VerifyUserResponse {

	Integer responseCode;

	String responseDescription;

	public VerifyUserResponse() {}

	public VerifyUserResponse(Integer responseCode, String responseDescription) {
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

}
