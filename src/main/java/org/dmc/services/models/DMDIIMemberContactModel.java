package org.dmc.services.models;

import javax.persistence.Column;

import org.dmc.services.entities.DMDIIContactType;

public class DMDIIMemberContactModel extends BaseModel {

	private DMDIIContactType contactType;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	public DMDIIContactType getContactType() {
		return contactType;
	}

	public void setContactType(DMDIIContactType contactType) {
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
