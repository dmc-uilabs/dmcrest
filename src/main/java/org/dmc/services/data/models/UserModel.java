package org.dmc.services.data.models;

import java.util.List;

public class UserModel extends BaseModel {

	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private String address;
	private String phone;
	private List<UserRoleAssignmentModel> roles;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<UserRoleAssignmentModel> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRoleAssignmentModel> roles) {
		this.roles = roles;
	}
	
}
