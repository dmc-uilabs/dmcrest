package org.dmc.services.data.models;

import java.util.Date;

public class ServiceRunModel extends BaseModel {
	
	private Integer id;
	private Integer serviceId;
	private Integer accountId;
	private Integer runBy;
	private Integer status;
	private Integer percentCompleted;
	private Date startDate;
	private Date stopDate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public Integer getRunBy() {
		return runBy;
	}
	public void setRunBy(Integer runBy) {
		this.runBy = runBy;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPercentCompleted() {
		return percentCompleted;
	}
	public void setPercentCompleted(Integer percentCompleted) {
		this.percentCompleted = percentCompleted;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getStopDate() {
		return stopDate;
	}
	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}
	
}
