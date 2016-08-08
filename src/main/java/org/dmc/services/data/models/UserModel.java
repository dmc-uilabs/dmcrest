package org.dmc.services.data.models;

import java.util.Map;

public class UserModel extends BaseModel {

	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String address;
	private String phone;
	private Map<Integer, String> roles;
	private UserContactInfoModel userContactInfo;
	private boolean isDMDIIMember;
	private Integer organization;
	private String aboutMe;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstname) {
		this.firstName = firstname;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastname) {
		this.lastName = lastname;
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

	public Map<Integer, String> getRoles() {
		return roles;
	}

	public void setRoles(Map<Integer, String> roles) {
		this.roles = roles;
	}

	public UserContactInfoModel getUserContactInfo() {
		return userContactInfo;
	}

	public void setUserContactInfo(UserContactInfoModel userContactInfo) {
		this.userContactInfo = userContactInfo;
	}

	public boolean isDMDIIMember() {
		return isDMDIIMember;
	}

	public void setDMDIIMember(boolean isDMDIIMember) {
		this.isDMDIIMember = isDMDIIMember;
	}

	public Integer getOrganization() {
		return organization;
	}

	public void setOrganization(Integer organization) {
		this.organization = organization;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

}
