package org.dmc.services.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "service_run")
public class ServiceRun extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "run_id")
	private Integer id;

	@Column (name = "service_id")
	private Integer serviceId;
	
	@Column(name = "account_id")
	private Integer accountId;
	
	@Column(name = "run_by")
	private Integer runBy;
	
	@Column(name = "status") 
	private Integer status;
	
	@Column(name = "percent_complete")
	private Integer percentCompleted;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;

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

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
