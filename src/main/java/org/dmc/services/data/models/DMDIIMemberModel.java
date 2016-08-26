package org.dmc.services.data.models;

import java.util.List;

public class DMDIIMemberModel extends BaseModel {

	private DMDIITypeModel dmdiiType;

	private OrganizationModel organization;

	private String startDate;

	private String expireDate;

	private List<DMDIIMemberContactModel> contacts;

	private List<DMDIIMemberFinanceModel> finances;

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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public List<DMDIIMemberContactModel> getContacts() {
		return contacts;
	}

	public void setContacts(List<DMDIIMemberContactModel> contacts) {
		this.contacts = contacts;
	}

	public List<DMDIIMemberFinanceModel> getFinances() {
		return finances;
	}

	public void setFinances(List<DMDIIMemberFinanceModel> finances) {
		this.finances = finances;
	}
}
