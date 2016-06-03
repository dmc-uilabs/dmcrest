package org.dmc.services.entities;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.dmc.services.dmdiitype.DMDIIType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "organization_dmdii_member")
public class DMDIIMember {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "dmdii_type_id")
	private DMDIIType dmdiiType;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "organization_id")
	private Organization organization;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "expire_date")
	private Date expireDate;

	@OneToMany
	@JoinColumn(name = "organization_dmdii_member_id")
	private List<DMDIIAward> awards;

	@ManyToMany(cascade = {CascadeType.DETACH})
	@JoinTable(name = "dmdii_member_area_of_expertise",
			   joinColumns = @JoinColumn(name="organization_dmdii_member_id"),
			   inverseJoinColumns = @JoinColumn(name="dmdii_area_of_expertise_id"))
	private List<DMDIIAreaOfExpertise> areasOfExpertise;

	@OneToMany
	@JoinColumn(name = "organization_dmdii_member_id")
	private List<DMDIIMemberContact> contacts;

	@OneToMany
	@JoinColumn(name = "organization_dmdii_member_id")
	private List<DMDIIMemberCustomer> customers;

	@OneToMany
	@JoinColumn(name = "organization_dmdii_member_id")
	private List<DMDIIMemberFinance> finances;

	@OneToMany
	@JoinColumn(name = "organization_dmdii_member_id")
	private List<DMDIIInstituteInvolvement> instituteInvolvement;

	@ManyToMany
	@JoinTable(name = "dmdii_member_rnd_focus",
			   joinColumns = @JoinColumn(name="organization_dmdii_member_id"),
			   inverseJoinColumns = @JoinColumn(name="id"))
	private List<DMDIIRndFocus> rndFocus;

	@ManyToMany
	@JoinTable(name = "dmdii_member_skill",
			   joinColumns = @JoinColumn(name="organization_dmdii_member_id"),
			   inverseJoinColumns = @JoinColumn(name="id"))
	private List<DMDIISkill> skills;

	@ManyToMany
	@JoinTable(name = "dmdii_member_user",
			   joinColumns = @JoinColumn(name="organization_dmdii_member_id"),
			   inverseJoinColumns = @JoinColumn(name="id"))
	private List<DMDIIMemberUser> users;


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


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

	public List<DMDIIAward> getAwards() {
		return awards;
	}

	public void setAwards(List<DMDIIAward> awards) {
		this.awards = awards;
	}

	public List<DMDIIAreaOfExpertise> getAreasOfExpertise() {
		return areasOfExpertise;
	}

	public void setAreasOfExpertise(List<DMDIIAreaOfExpertise> areasOfExpertise) {
		this.areasOfExpertise = areasOfExpertise;
	}

	public List<DMDIIMemberContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<DMDIIMemberContact> contacts) {
		this.contacts = contacts;
	}

	public List<DMDIIMemberCustomer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<DMDIIMemberCustomer> customers) {
		this.customers = customers;
	}

	public List<DMDIIMemberFinance> getFinances() {
		return finances;
	}

	public void setFinances(List<DMDIIMemberFinance> finances) {
		this.finances = finances;
	}

	public List<DMDIIInstituteInvolvement> getInstituteInvolvement() {
		return instituteInvolvement;
	}

	public void setInstituteInvolvement(List<DMDIIInstituteInvolvement> instituteInvolvement) {
		this.instituteInvolvement = instituteInvolvement;
	}

	public List<DMDIIRndFocus> getRndFocus() {
		return rndFocus;
	}

	public void setRndFocus(List<DMDIIRndFocus> rndFocus) {
		this.rndFocus = rndFocus;
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

}
