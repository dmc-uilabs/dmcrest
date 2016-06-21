package org.dmc.services.data.models;

import java.util.Date;
import java.util.List;

public class DMDIIProjectModel extends BaseModel {
	
	private DMDIIMemberModel primeOrganization;
	
	private UserModel principalInvestigator;
	
	private DMDIIProjectStatusModel projectStatus;
	
	private Date awardedDate;
	
	private Date endDate;
	
	private String projectTitle;
	
	private String projectSummary;
	
	private UserModel principalPointOfContact;
	
	private DMDIIProjectFocusAreaModel projectFocusArea;
	
	private DMDIIProjectThrustModel projectThrust;
	
	private List<DMDIIMemberModel> contributingCompanies;
	

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

	public List<DMDIIMemberModel> getContributingCompanies() {
		return contributingCompanies;
	}

	public void setContributingCompanies(List<DMDIIMemberModel> contributingCompanies) {
		this.contributingCompanies = contributingCompanies;
	}

	public DMDIIProjectStatusModel getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(DMDIIProjectStatusModel projectStatus) {
		this.projectStatus = projectStatus;
	}

	public DMDIIProjectFocusAreaModel getProjectFocusArea() {
		return projectFocusArea;
	}

	public void setProjectFocusArea(DMDIIProjectFocusAreaModel projectFocusArea) {
		this.projectFocusArea = projectFocusArea;
	}

	public DMDIIProjectThrustModel getProjectThrust() {
		return projectThrust;
	}

	public void setProjectThrust(DMDIIProjectThrustModel projectThrust) {
		this.projectThrust = projectThrust;
	}

}
