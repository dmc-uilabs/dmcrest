package org.dmc.services.data.models;

public class PaymentsStatus {
	
	private String status;
	
	private String reason;
	
	public PaymentsStatus(String status, String reason) {
		this.status = status;
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
