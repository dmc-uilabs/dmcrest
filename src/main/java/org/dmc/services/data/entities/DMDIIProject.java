package org.dmc.services.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.dmc.services.data.models.DMDIIUserModel;

@Entity
@Table(name="dmdii_project")
public class DMDIIProject extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "prime_organization_id", nullable = false)
	private DMDIIMember primeOrganization;
	
	@Column(name = "principal_investigator")
	private DMDIIUserModel principalInvestigator;
	
	@Column(name = "project_status_id")
	private Integer projectStatusId;
	
	@Column(name = "awarded_date")
	@Temporal(TemporalType.DATE)
	private Date awardedDate;
	
	@Column(name = "project_title")
	private String projectTitle;
	
	@Column(name = "project_summary")
	private String projectSummary;
	
	@JoinColumn(name = "principal_point_of_contact_id")
	private DMDIIUserModel principalPointOfContact;
	
	@Column(name = "focus_area_id")
	private Integer focusAreaId;
	
	@Column(name = "thrust_id")
	private Integer thrustId;
	
	public DMDIIProject () {
		
	}
	
	public DMDIIProject (DMDIIMember primeOrganization, DMDIIUserModel principalInvestigator,
			Integer projectStatusId, Date awardedDate, String projectTitle, String projectSummary,
			DMDIIUserModel principalPointOfContact) {
		
		this.primeOrganization = primeOrganization;
		this.principalInvestigator = principalInvestigator;
		this.projectStatusId = projectStatusId;
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

	public DMDIIUserModel getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(DMDIIUserModel principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public Integer getProjectStatusId() {
		return projectStatusId;
	}

	public void setProjectStatusId(Integer projectStatusId) {
		this.projectStatusId = projectStatusId;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((awardedDate == null) ? 0 : awardedDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((primeOrganization == null) ? 0 : primeOrganization.hashCode());
		result = prime * result + ((principalInvestigator == null) ? 0 : principalInvestigator.hashCode());
		result = prime * result + ((principalPointOfContact == null) ? 0 : principalPointOfContact.hashCode());
		result = prime * result + ((projectStatusId == null) ? 0 : projectStatusId.hashCode());
		result = prime * result + ((projectSummary == null) ? 0 : projectSummary.hashCode());
		result = prime * result + ((projectTitle == null) ? 0 : projectTitle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DMDIIProject other = (DMDIIProject) obj;
		if (awardedDate == null) {
			if (other.awardedDate != null)
				return false;
		} else if (!awardedDate.equals(other.awardedDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (primeOrganization == null) {
			if (other.primeOrganization != null)
				return false;
		} else if (!primeOrganization.equals(other.primeOrganization))
			return false;
		if (principalInvestigator == null) {
			if (other.principalInvestigator != null)
				return false;
		} else if (!principalInvestigator.equals(other.principalInvestigator))
			return false;
		if (principalPointOfContact == null) {
			if (other.principalPointOfContact != null)
				return false;
		} else if (!principalPointOfContact.equals(other.principalPointOfContact))
			return false;
		if (projectStatusId == null) {
			if (other.projectStatusId != null)
				return false;
		} else if (!projectStatusId.equals(other.projectStatusId))
			return false;
		if (projectSummary == null) {
			if (other.projectSummary != null)
				return false;
		} else if (!projectSummary.equals(other.projectSummary))
			return false;
		if (projectTitle == null) {
			if (other.projectTitle != null)
				return false;
		} else if (!projectTitle.equals(other.projectTitle))
			return false;
		return true;
	}
}
