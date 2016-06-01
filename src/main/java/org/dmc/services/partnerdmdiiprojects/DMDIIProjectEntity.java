package org.dmc.services.partnerdmdiiprojects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.dmc.services.dmdiimember.DMDIIMember;

@Entity
@Table(name="dmdii_project")
public class DMDIIProjectEntity extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "prime_organization_id", nullable = false)
	private DMDIIMember primeOrganization;
	
	@Column(name = "principal_investigator")
	private DMDIIUser principalInvestigator;
	
	@Column(name = "project_status")
	@Enumerated(EnumType.STRING)
	private String projectStatus;
	
	@Column(name = "awarded_date")
	@Temporal(TemporalType.DATE)
	private Date awardedDate;
	
	@Column(name = "project_title")
	@Enumerated(EnumType.STRING)
	private String projectTitle;
	
	@Column(name = "project_summary")
	@Enumerated(EnumType.STRING)
	private String projectSummary;
	
	@JoinColumn(name = "principal_point_of_contact_id")
	private DMDIIUser principalPointOfContact;
	
	public DMDIIProjectEntity () {
		
	}
	
	public DMDIIProjectEntity (DMDIIMember primeOrganization, DMDIIUser principalInvestigator,
			String projectStatus, Date awardedDate, String projectTitle, String projectSummary,
			DMDIIUser principalPointOfContact) {
		
		this.primeOrganization = primeOrganization;
		this.principalInvestigator = principalInvestigator;
		this.projectStatus = projectStatus;
		this.awardedDate = awardedDate;
		this.projectTitle = projectTitle;
		this.projectSummary = projectSummary;
		this.principalPointOfContact = principalPointOfContact;
	}

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
