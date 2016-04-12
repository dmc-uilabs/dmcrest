package org.dmc.services;


public class DMCServiceException extends Exception {
	public static int NotAdminUser = 1;
	private int errorcode;
	private String errorMessage;
	public DMCServiceException(int c, String m)
	{
		this.errorcode = c;
		this.errorMessage = m;
	}
	public int getErrorcode() {
		return this.errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrorMessage() {
		return this.errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
