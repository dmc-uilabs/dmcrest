package org.dmc.services.data.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.Formula;


@Entity
@Table(name="dmdii_project")
@Where(clause = "is_deleted = 'FALSE'")
@SQLDelete(sql="UPDATE dmdii_project SET is_deleted = 'TRUE' WHERE id = ?")
public class DMDIIProject extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "organization_dmdii_member_id", nullable = false)
	private DMDIIMember primeOrganization;

	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "principal_investigator_id")
	private DMDIIProjectContact principalInvestigator;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private DMDIIProjectStatus projectStatus;

	@Column(name = "awarded_date")
	@Temporal(TemporalType.DATE)
	private Date awardedDate;

	@Column(name = "end_date")
	@Temporal(TemporalType.DATE)
	private Date endDate;

	@Column(name = "project_title")
	private String projectTitle;

	@Column(name = "project_summary")
	private String projectSummary;

	@ManyToOne
	@JoinColumn(name = "principal_point_of_contact_id")
	private User principalPointOfContact;

	@ManyToOne
	@JoinColumn(name = "focus_area_id")
	private DMDIIProjectFocusArea projectFocusArea;

	@ManyToOne
	@JoinColumn(name = "thrust_id")
	private DMDIIProjectThrust projectThrust;

	@ManyToMany
	@JoinTable(name = "dmdii_project_contributing_company",
				joinColumns = @JoinColumn(name = "dmdii_project_id"),
				inverseJoinColumns = @JoinColumn(name = "contributing_company_id"))
	@Where(clause = "is_deleted = 'FALSE'")
	private List<DMDIIMember> contributingCompanies;

	@Column(name = "project_root_number")
	private Integer rootNumber;

	@Column(name = "project_call_number")
	private Integer callNumber;

	@Column(name = "project_number")
	private Integer projectNumber;

	@Formula("concat(project_root_number, '-', project_call_number, '-', project_number)")
	private String projectNumberString;

	// @Transient
	// private String projectNumberString = String.format("%02d", rootNumber) + "-" + String.format("%02d", callNumber) + "-" + String.format("%02d", projectNumber);

	@Column(name = "cost_share")
	private BigDecimal costShare;

	@Column(name = "dmdii_funding")
	private BigDecimal dmdiiFunding;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	public DMDIIProject () {

	}

	public DMDIIMember getPrimeOrganization() {
		return primeOrganization;
	}

	public void setPrimeOrganization(DMDIIMember primeOrganization) {
		this.primeOrganization = primeOrganization;
	}

	public DMDIIProjectContact getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(DMDIIProjectContact principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public DMDIIProjectStatus getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(DMDIIProjectStatus projectStatus) {
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DMDIIProjectFocusArea getProjectFocusArea() {
		return projectFocusArea;
	}

	public void setProjectFocusArea(DMDIIProjectFocusArea projectFocusArea) {
		this.projectFocusArea = projectFocusArea;
	}

	public DMDIIProjectThrust getProjectThrust() {
		return projectThrust;
	}

	public void setProjectThrust(DMDIIProjectThrust projectThrust) {
		this.projectThrust = projectThrust;
	}

	public List<DMDIIMember> getContributingCompanies() {
		return contributingCompanies;
	}

	public void setContributingCompanies(List<DMDIIMember> contributingCompanies) {
		this.contributingCompanies = contributingCompanies;
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

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((awardedDate == null) ? 0 : awardedDate.hashCode());
		result = prime * result + ((callNumber == null) ? 0 : callNumber.hashCode());
		result = prime * result + ((contributingCompanies == null) ? 0 : contributingCompanies.hashCode());
		result = prime * result + ((costShare == null) ? 0 : costShare.hashCode());
		result = prime * result + ((dmdiiFunding == null) ? 0 : dmdiiFunding.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isDeleted == null) ? 0 : isDeleted.hashCode());
		result = prime * result + ((primeOrganization == null) ? 0 : primeOrganization.hashCode());
		result = prime * result + ((principalInvestigator == null) ? 0 : principalInvestigator.hashCode());
		result = prime * result + ((principalPointOfContact == null) ? 0 : principalPointOfContact.hashCode());
		result = prime * result + ((projectFocusArea == null) ? 0 : projectFocusArea.hashCode());
		result = prime * result + ((projectNumber == null) ? 0 : projectNumber.hashCode());
		result = prime * result + ((projectStatus == null) ? 0 : projectStatus.hashCode());
		result = prime * result + ((projectSummary == null) ? 0 : projectSummary.hashCode());
		result = prime * result + ((projectThrust == null) ? 0 : projectThrust.hashCode());
		result = prime * result + ((projectTitle == null) ? 0 : projectTitle.hashCode());
		result = prime * result + ((rootNumber == null) ? 0 : rootNumber.hashCode());
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
		if (callNumber == null) {
			if (other.callNumber != null)
				return false;
		} else if (!callNumber.equals(other.callNumber))
			return false;
		if (contributingCompanies == null) {
			if (other.contributingCompanies != null)
				return false;
		} else if (!contributingCompanies.equals(other.contributingCompanies))
			return false;
		if (costShare == null) {
			if (other.costShare != null)
				return false;
		} else if (!costShare.equals(other.costShare))
			return false;
		if (dmdiiFunding == null) {
			if (other.dmdiiFunding != null)
				return false;
		} else if (!dmdiiFunding.equals(other.dmdiiFunding))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isDeleted == null) {
			if (other.isDeleted != null)
				return false;
		} else if (!isDeleted.equals(other.isDeleted))
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
		if (projectFocusArea == null) {
			if (other.projectFocusArea != null)
				return false;
		} else if (!projectFocusArea.equals(other.projectFocusArea))
			return false;
		if (projectNumber == null) {
			if (other.projectNumber != null)
				return false;
		} else if (!projectNumber.equals(other.projectNumber))
			return false;
		if (projectStatus == null) {
			if (other.projectStatus != null)
				return false;
		} else if (!projectStatus.equals(other.projectStatus))
			return false;
		if (projectSummary == null) {
			if (other.projectSummary != null)
				return false;
		} else if (!projectSummary.equals(other.projectSummary))
			return false;
		if (projectThrust == null) {
			if (other.projectThrust != null)
				return false;
		} else if (!projectThrust.equals(other.projectThrust))
			return false;
		if (projectTitle == null) {
			if (other.projectTitle != null)
				return false;
		} else if (!projectTitle.equals(other.projectTitle))
			return false;
		if (rootNumber == null) {
			if (other.rootNumber != null)
				return false;
		} else if (!rootNumber.equals(other.rootNumber))
			return false;
		return true;
	}

}
