package org.dmc.services.partnerdmdiiprojects;

import java.util.Date;

import org.dmc.services.dmdiimember.DMDIIMember;
import org.dmc.services.models.BaseModel;

public class DMDIIProject extends BaseModel {
	
	private DMDIIMember primeCompany;
	
	private String projectStatus;
	
	private Date awardedDate;
	
	private String projectTitle;
	
	private String projectSummary;
	
	private DMDIIUser principalPointOfContact;

	public DMDIIMember getPrimeCompany() {
		return primeCompany;
	}

	public void setPrimeCompany(DMDIIMember primeCompany) {
		this.primeCompany = primeCompany;
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
