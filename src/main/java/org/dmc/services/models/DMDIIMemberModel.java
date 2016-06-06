package org.dmc.services.models;

import java.sql.Date;
import java.util.List;

public class DMDIIMemberModel extends BaseModel {

	private DMDIITypeModel dmdiiType;

	private OrganizationModel organization;

	private Date startDate;

	private Date expireDate;

	private List<DMDIIAwardModel> awards;

	private List<DMDIIAreaOfExpertiseModel> areasOfExpertise;

	private List<DMDIIMemberContactModel> contacts;

	private List<DMDIIMemberCustomerModel> customers;

	private List<DMDIIMemberFinanceModel> finances;

	private List<DMDIIInstituteInvolvementModel> instituteInvolvement;

	private List<DMDIIRndFocusModel> rndFocus;

	private List<DMDIISkillModel> skills;

	private List<DMDIIMemberUserModel> users;

	public DMDIITypeModel getDmdiiType() {
		return dmdiiType;
	}

	public void setDmdiiType(DMDIITypeModel dmdiiType) {
		this.dmdiiType = dmdiiType;
	}

	public OrganizationModel getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationModel organization) {
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

	public List<DMDIIAwardModel> getAwards() {
		return awards;
	}

	public void setAwards(List<DMDIIAwardModel> awards) {
		this.awards = awards;
	}

	public List<DMDIIAreaOfExpertiseModel> getAreasOfExpertise() {
		return areasOfExpertise;
	}

	public void setAreasOfExpertise(List<DMDIIAreaOfExpertiseModel> areasOfExpertise) {
		this.areasOfExpertise = areasOfExpertise;
	}

	public List<DMDIIMemberContactModel> getContacts() {
		return contacts;
	}

	public void setContacts(List<DMDIIMemberContactModel> contacts) {
		this.contacts = contacts;
	}

	public List<DMDIIMemberCustomerModel> getCustomers() {
		return customers;
	}

	public void setCustomers(List<DMDIIMemberCustomerModel> customers) {
		this.customers = customers;
	}

	public List<DMDIIMemberFinanceModel> getFinances() {
		return finances;
	}

	public void setFinances(List<DMDIIMemberFinanceModel> finances) {
		this.finances = finances;
	}

	public List<DMDIIInstituteInvolvementModel> getInstituteInvolvement() {
		return instituteInvolvement;
	}

	public void setInstituteInvolvement(List<DMDIIInstituteInvolvementModel> instituteInvolvement) {
		this.instituteInvolvement = instituteInvolvement;
	}

	public List<DMDIIRndFocusModel> getRndFocus() {
		return rndFocus;
	}

	public void setRndFocus(List<DMDIIRndFocusModel> rndFocus) {
		this.rndFocus = rndFocus;
	}

	public List<DMDIISkillModel> getSkills() {
		return skills;
	}

	public void setSkills(List<DMDIISkillModel> skills) {
		this.skills = skills;
	}

	public List<DMDIIMemberUserModel> getUsers() {
		return users;
	}

	public void setUsers(List<DMDIIMemberUserModel> users) {
		this.users = users;
	}
}
