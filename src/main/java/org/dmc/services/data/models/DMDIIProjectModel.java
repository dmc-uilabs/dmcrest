package org.dmc.services.data.models;

import java.util.Date;

public class DMDIIProjectModel extends BaseModel {
	
	private DMDIIMemberModel primeOrganization;
	
	private UserModel principalInvestigator;
	
	private Integer statusId;
	
	private Date awardedDate;
	
	private Date endDate;
	
	private String projectTitle;
	
	private String projectSummary;
	
	private UserModel principalPointOfContact;
	
	private Integer focusAreaId;
	
	private Integer thrustId;	
	

	public DMDIIMemberModel getPrimeOrganization() {
		return primeOrganization;
	}

	public void setPrimeOrganization(DMDIIMemberModel primeOrganization) {
		this.primeOrganization = primeOrganization;
	}

	public UserModel getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(UserModel principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Date getAwardedDate() {
		return awardedDate;
	}

	public void setAwardedDate(Date awardedDate) {
		this.awardedDate = awardedDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public String getProjectSummary() {
		return projectSummary;
	}

	public void setProjectSummary(String projectSummary) {
		this.projectSummary = projectSummary;
	}

	public UserModel getPrincipalPointOfContact() {
		return principalPointOfContact;
	}

	public void setPrincipalPointOfContact(UserModel principalPointOfContact) {
		this.principalPointOfContact = principalPointOfContact;
	}

	public Integer getFocusAreaId() {
		return focusAreaId;
	}

	public void setFocusAreaId(Integer focusAreaId) {
		this.focusAreaId = focusAreaId;
	}

	public Integer getThrustId() {
		return thrustId;
	}

	public void setThrustId(Integer thrustId) {
		this.thrustId = thrustId;
	}

}
