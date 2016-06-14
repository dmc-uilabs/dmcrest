package org.dmc.services.data.models;

import java.util.Date;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.User;

public class DMDIIProjectModel extends BaseModel {
	
	private DMDIIMember primeOrganization;
	
	private User principalInvestigator;
	
	private String projectStatus;
	
	private Date awardedDate;
	
	private Date endDate;
	
	private String projectTitle;
	
	private String projectSummary;
	
	private User principalPointOfContact;
	
	private Integer focusAreaId;
	
	private Integer thrustId;	
	

	public DMDIIMember getPrimeOrganization() {
		return primeOrganization;
	}

	public void setPrimeOrganization(DMDIIMember primeOrganization) {
		this.primeOrganization = primeOrganization;
	}

	public User getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(User principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
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

	public User getPrincipalPointOfContact() {
		return principalPointOfContact;
	}

	public void setPrincipalPointOfContact(User principalPointOfContact) {
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
