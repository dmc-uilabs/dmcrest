package org.dmc.services.data.models;

public class OrganizationContactModel extends BaseModel {

	private OrganizationContactTypeModel contactType;

	private String name;

	private String phoneNumber;

	private String title;

	private String email;

	public OrganizationContactTypeModel getContactType() {
		return contactType;
	}

	public void setContactType(OrganizationContactTypeModel contactType) {
		this.contactType = contactType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
