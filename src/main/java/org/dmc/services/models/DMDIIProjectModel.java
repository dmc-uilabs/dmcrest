package org.dmc.services.models;

import java.util.Date;

import org.dmc.services.dmdiimember.DMDIIMember;

public class DMDIIProjectModel extends BaseModel {
	
	private DMDIIMember primeOrganization;
	
	private DMDIIUserModel principalInvestigator;
	
	private String projectStatus;
	
	private Date awardedDate;
	
	private String projectTitle;
	
	private String projectSummary;
	
	private DMDIIUserModel principalPointOfContact;

	public DMDIIMember getPrimeOrganization() {
		return primeOrganization;
	}

	public void setPrimeOrganization(DMDIIMember primeOrganization) {
		this.primeOrganization = primeOrganization;
	}

	public DMDIIUserModel getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(DMDIIUserModel principalInvestigator) {
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

	public DMDIIUserModel getPrincipalPointOfContact() {
		return principalPointOfContact;
	}

	public void setPrincipalPointOfContact(DMDIIUserModel principalPointOfContact) {
		this.principalPointOfContact = principalPointOfContact;
	}

}
