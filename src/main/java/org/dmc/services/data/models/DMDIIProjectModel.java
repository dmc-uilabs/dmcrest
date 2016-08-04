package org.dmc.services.data.models;

import java.math.BigDecimal;
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
	
	private List<Integer> contributingCompanyIds;
	
	private Integer rootNumber;
	
	private Integer callNumber;
	
	private Integer projectNumber;
	
	private BigDecimal costShare;
	
	private BigDecimal dmdiiFunding;
	

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

	public List<Integer> getContributingCompanyIds() {
		return contributingCompanyIds;
	}

	public void setContributingCompanies(List<Integer> contributingCompanyIds) {
		this.contributingCompanyIds = contributingCompanyIds;
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

	public Integer getRootNumber() {
		return rootNumber;
	}

	public void setRootNumber(Integer rootNumber) {
		this.rootNumber = rootNumber;
	}

	public Integer getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(Integer callNumber) {
		this.callNumber = callNumber;
	}

	public Integer getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(Integer projectNumber) {
		this.projectNumber = projectNumber;
	}
	
	public String getProjectIdentifier() {
		return rootNumber + "-" + callNumber + "-" + projectNumber;
	}

	public BigDecimal getCostShare() {
		return costShare;
	}

	public void setCostShare(BigDecimal costShare) {
		this.costShare = costShare;
	}

	public BigDecimal getDmdiiFunding() {
		return dmdiiFunding;
	}

	public void setDmdiiFunding(BigDecimal dmdiiFunding) {
		this.dmdiiFunding = dmdiiFunding;
	}

}
