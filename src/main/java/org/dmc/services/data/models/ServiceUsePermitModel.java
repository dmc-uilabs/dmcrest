package org.dmc.services.data.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ServiceUsePermitModel extends BaseModel {

	private Integer serviceId;
	
	private Integer remainingUses;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss")
	private Date expirationDate;
	
	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getUses() {
		return remainingUses;
	}

	public void setUses(Integer uses) {
		this.remainingUses = uses;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
}
