package org.dmc.services.data.entities;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.dmc.services.dmdiitype.DMDIIType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "organization_dmdii_member")
@Where(clause = "is_deleted='FALSE'")
@SQLDelete(sql="UPDATE organization_dmdii_member SET is_deleted = 'TRUE' WHERE id = ?")
public class DMDIIMember extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "dmdii_type_id")
	private DMDIIType dmdiiType;

	@OneToOne
	@JoinColumn(name = "organization_id")
	private Organization organization;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "expire_date")
	private Date expireDate;

	@OneToMany(mappedBy="dmdiiMember", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DMDIIMemberContact> contacts;

	@OneToMany(mappedBy="dmdiiMember", cascade={CascadeType.MERGE, CascadeType.PERSIST})
	private List<DMDIIMemberFinance> finances;

	@OneToMany(mappedBy="primeOrganization", fetch = FetchType.LAZY)
	@Where(clause = "is_deleted = 'FALSE'")
	private Set<DMDIIProject> projects;

	@ManyToMany(mappedBy="contributingCompanies", fetch = FetchType.LAZY)
	private Set<DMDIIProject> contributingProjects;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	public DMDIIType getDmdiiType() {
		return this.dmdiiType;
	}

	public void setDmdiiType(DMDIIType dmdiiType) {
		this.dmdiiType = dmdiiType;
	}

	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public List<DMDIIMemberContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<DMDIIMemberContact> contacts) {
		contacts.stream().forEach((a) -> a.setDmdiiMember(this));
		this.contacts = contacts;
	}

	public List<DMDIIMemberFinance> getFinances() {
		return finances;
	}

	public void setFinances(List<DMDIIMemberFinance> finances) {
		finances.stream().forEach((a) -> a.setDmdiiMember(this));
		this.finances = finances;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<DMDIIProject> getProjects() {
		return projects;
	}

	public void setProjects(Set<DMDIIProject> projects) {
		this.projects = projects;
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
		result = prime * result + ((contacts == null) ? 0 : contacts.hashCode());
		result = prime * result + ((dmdiiType == null) ? 0 : dmdiiType.hashCode());
		result = prime * result + ((expireDate == null) ? 0 : expireDate.hashCode());
		result = prime * result + ((finances == null) ? 0 : finances.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isDeleted == null) ? 0 : isDeleted.hashCode());
		result = prime * result + ((organization == null) ? 0 : organization.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		DMDIIMember other = (DMDIIMember) obj;
		if (contacts == null) {
			if (other.contacts != null)
				return false;
		} else if (!contacts.equals(other.contacts))
			return false;
		if (dmdiiType == null) {
			if (other.dmdiiType != null)
				return false;
		} else if (!dmdiiType.equals(other.dmdiiType))
			return false;
		if (expireDate == null) {
			if (other.expireDate != null)
				return false;
		} else if (!expireDate.equals(other.expireDate))
			return false;
		if (finances == null) {
			if (other.finances != null)
				return false;
		} else if (!finances.equals(other.finances))
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
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		if (projects == null) {
			if (other.projects != null)
				return false;
		} else if (!projects.equals(other.projects))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}
}
