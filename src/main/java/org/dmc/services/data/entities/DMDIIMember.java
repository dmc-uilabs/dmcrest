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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.dmc.services.dmdiitype.DMDIIType;

@Entity
@Table(name = "organization_dmdii_member")
public class DMDIIMember extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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

	@ManyToMany
	@JoinTable(name = "dmdii_member_area_of_expertise",
			   joinColumns = @JoinColumn(name="organization_dmdii_member_id"),
			   inverseJoinColumns = @JoinColumn(name="dmdii_area_of_expertise_id"))
	private List<DMDIIAreaOfExpertise> areasOfExpertise;

	@ManyToMany
	@JoinTable(name = "dmdii_member_desired_area_of_expertise",
			   joinColumns = @JoinColumn(name="organization_dmdii_member_id"),
			   inverseJoinColumns = @JoinColumn(name="dmdii_area_of_expertise_id"))
	private List<DMDIIAreaOfExpertise> desiredAreasOfExpertise;

	@OneToMany(mappedBy="dmdiiMember", cascade=CascadeType.ALL)
	private List<DMDIIMemberContact> contacts;

	@OneToMany(mappedBy="dmdiiMember", cascade=CascadeType.ALL)
	private List<DMDIIMemberCustomer> customers;

	@OneToMany(mappedBy="dmdiiMember", cascade=CascadeType.ALL)
	private List<DMDIIMemberFinance> finances;

	@OneToMany(mappedBy="dmdiiMember", cascade=CascadeType.ALL)
	private List<DMDIIInstituteInvolvement> instituteInvolvement;

	@ManyToMany
	@JoinTable(name = "dmdii_member_skill",
			   joinColumns = @JoinColumn(name="organization_dmdii_member_id"),
			   inverseJoinColumns = @JoinColumn(name="dmdii_skill_id"))
	private List<DMDIISkill> skills;

	@ManyToMany
	@JoinTable(name = "dmdii_member_user",
			   joinColumns = @JoinColumn(name="organization_dmdii_member_id"),
			   inverseJoinColumns = @JoinColumn(name="id"))
	private List<DMDIIMemberUser> users;

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

	public List<DMDIIAreaOfExpertise> getAreasOfExpertise() {
		return areasOfExpertise;
	}

	public void setAreasOfExpertise(List<DMDIIAreaOfExpertise> areasOfExpertise) {
		this.areasOfExpertise = areasOfExpertise;
	}

	public List<DMDIIAreaOfExpertise> getDesiredAreasOfExpertise() {
		return desiredAreasOfExpertise;
	}

	public void setDesiredAreasOfExpertise(List<DMDIIAreaOfExpertise> desiredAreasOfExpertise) {
		this.desiredAreasOfExpertise = desiredAreasOfExpertise;
	}

	public List<DMDIIMemberContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<DMDIIMemberContact> contacts) {
		contacts.stream().forEach((a) -> a.setDmdiiMember(this));
		this.contacts = contacts;
	}

	public List<DMDIIMemberCustomer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<DMDIIMemberCustomer> customers) {
		customers.stream().forEach((a) -> a.setDmdiiMember(this));
		this.customers = customers;
	}

	public List<DMDIIMemberFinance> getFinances() {
		return finances;
	}

	public void setFinances(List<DMDIIMemberFinance> finances) {
		finances.stream().forEach((a) -> a.setDmdiiMember(this));
		this.finances = finances;
	}

	public List<DMDIIInstituteInvolvement> getInstituteInvolvement() {
		return instituteInvolvement;
	}

	public void setInstituteInvolvement(List<DMDIIInstituteInvolvement> instituteInvolvement) {
		instituteInvolvement.stream().forEach((a) -> a.setDmdiiMember(this));
		this.instituteInvolvement = instituteInvolvement;
	}

	public List<DMDIISkill> getSkills() {
		return skills;
	}

	public void setSkills(List<DMDIISkill> skills) {
		this.skills = skills;
	}

	public List<DMDIIMemberUser> getUsers() {
		return users;
	}

	public void setUsers(List<DMDIIMemberUser> users) {
		this.users = users;
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
		result = prime * result + ((areasOfExpertise == null) ? 0 : areasOfExpertise.hashCode());
		result = prime * result + ((desiredAreasOfExpertise == null) ? 0 : desiredAreasOfExpertise.hashCode());
		result = prime * result + ((contacts == null) ? 0 : contacts.hashCode());
		result = prime * result + ((customers == null) ? 0 : customers.hashCode());
		result = prime * result + ((dmdiiType == null) ? 0 : dmdiiType.hashCode());
		result = prime * result + ((expireDate == null) ? 0 : expireDate.hashCode());
		result = prime * result + ((finances == null) ? 0 : finances.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((instituteInvolvement == null) ? 0 : instituteInvolvement.hashCode());
		result = prime * result + ((organization == null) ? 0 : organization.hashCode());
		result = prime * result + ((skills == null) ? 0 : skills.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
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
		if (areasOfExpertise == null) {
			if (other.areasOfExpertise != null)
				return false;
		} else if (!areasOfExpertise.equals(other.areasOfExpertise))
			return false;
		if (desiredAreasOfExpertise == null) {
			if (other.desiredAreasOfExpertise != null)
				return false;
		} else if (!desiredAreasOfExpertise.equals(other.desiredAreasOfExpertise))
			return false;
		if (contacts == null) {
			if (other.contacts != null)
				return false;
		} else if (!contacts.equals(other.contacts))
			return false;
		if (customers == null) {
			if (other.customers != null)
				return false;
		} else if (!customers.equals(other.customers))
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
		if (instituteInvolvement == null) {
			if (other.instituteInvolvement != null)
				return false;
		} else if (!instituteInvolvement.equals(other.instituteInvolvement))
			return false;
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		if (skills == null) {
			if (other.skills != null)
				return false;
		} else if (!skills.equals(other.skills))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}

}
