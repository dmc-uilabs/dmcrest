package org.dmc.services.partnerdmdiiprojects;

import java.util.Date;

import org.dmc.services.dmdiimember.DMDIIMember;

public class DMDIIProject extends BaseModel {
	
	private DMDIIMember primeOrganization;
	
	private DMDIIUser principalInvestigator;
	
	private String projectStatus;
	
	private Date awardedDate;
	
	private String projectTitle;
	
	private String projectSummary;
	
	private DMDIIUser principalPointOfContact;

	public DMDIIMember getPrimeOrganization() {
		return primeOrganization;
	}

	public void setPrimeOrganization(DMDIIMember primeOrganization) {
		this.primeOrganization = primeOrganization;
	}

	public DMDIIUser getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(DMDIIUser principalInvestigator) {
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

	public DMDIIUser getPrincipalPointOfContact() {
		return principalPointOfContact;
	}

	public void setPrincipalPointOfContact(DMDIIUser principalPointOfContact) {
		this.principalPointOfContact = principalPointOfContact;
	}

}
