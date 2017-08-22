package org.dmc.services.data.models;

import java.util.Date;

public class ServiceUsePermitModel extends BaseModel {

	private Integer serviceId;
	
	private Integer remainingUses;
	
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

	public String getExpirationDate() {
		return expirationDate.toString();
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
}
