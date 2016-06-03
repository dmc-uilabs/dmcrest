package org.dmc.services.models;

import javax.persistence.Column;

public class DMDIIMemberContactModel extends BaseModel {

	private DMDIIContactTypeModel contactType;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	public DMDIIContactTypeModel getContactType() {
		return contactType;
	}

	public void setContactType(DMDIIContactTypeModel contactType) {
		this.contactType = contactType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
