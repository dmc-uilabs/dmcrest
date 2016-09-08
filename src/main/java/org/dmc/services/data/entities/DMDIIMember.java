package org.dmc.services.data.entities;

import org.dmc.services.dmdiitype.DMDIIType;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "organization_dmdii_member")
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

	@OneToMany(mappedBy = "dmdiiMember", cascade = CascadeType.ALL)
	private List<DMDIIMemberContact> contacts;

	@OneToMany(mappedBy = "dmdiiMember", cascade = CascadeType.ALL)
	private List<DMDIIMemberFinance> finances;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_dmdii_member_id")
	private Set<DMDIIProject> projects;

	public DMDIIType getDmdiiType() {
		return this.dmdiiType;
	}

	public void setDmdiiType(DMDIIType dmdiiType) {
		this.dmdiiType = dmdiiType;
	}

	public Organization getOrganization() {
		return organization;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contacts == null) ? 0 : contacts.hashCode());
		result = prime * result + ((dmdiiType == null) ? 0 : dmdiiType.hashCode());
		result = prime * result + ((expireDate == null) ? 0 : expireDate.hashCode());
		result = prime * result + ((finances == null) ? 0 : finances.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}
}
