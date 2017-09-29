package org.dmc.services.data.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DMDIIProjectModel extends BaseModel {

	private DMDIIPrimeOrganizationModel primeOrganization;

	private DMDIIProjectContactModel principalInvestigator;

	private DMDIIProjectStatusModel projectStatus;

	private String awardedDate;

	private String endDate;

	private String projectTitle;

	private String projectSummary;

	private SimpleUserModel principalPointOfContact;

	private DMDIIProjectFocusAreaModel projectFocusArea;

	private DMDIIProjectThrustModel projectThrust;

	private List<Integer> contributingCompanyIds;

	private Integer rootNumber;

	private Integer callNumber;

	private Integer projectNumber;

	// private String projectNumberString;

	private BigDecimal costShare;

	private BigDecimal dmdiiFunding;

	private Boolean isEvent;


	public DMDIIPrimeOrganizationModel getPrimeOrganization() {
		return primeOrganization;
	}

	public void setPrimeOrganization(DMDIIPrimeOrganizationModel primeOrganization) {
		this.primeOrganization = primeOrganization;
	}

	public DMDIIProjectContactModel getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(DMDIIProjectContactModel principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public String getAwardedDate() {
		return awardedDate;
	}

	public void setAwardedDate(String awardedDate) {
		this.awardedDate = awardedDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
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

	public SimpleUserModel getPrincipalPointOfContact() {
		return principalPointOfContact;
	}

	public void setPrincipalPointOfContact(SimpleUserModel principalPointOfContact) {
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

	// public String getProjectNumberString() {
	// 	return projectNumberString;
	// }
	//
	// public void setProjectNumberString(String projectNumberString) {
	// 	this.projectNumberString = projectNumberString;
	// }

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

	public Boolean getIsEvent() {
		return isEvent == null ? false : isEvent;
	}

	public void setIsEvent(Boolean isEvent) {
		this.isEvent = isEvent;
	}

}
