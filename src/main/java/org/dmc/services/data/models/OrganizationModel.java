package org.dmc.services.data.models;

public class OrganizationModel extends BaseModel {

	private String name;

	private String location;

	private String description;

	private String email;

	private String phone;

	private String website;

	private String dateJoining;

	private String reasonJoining;

	private Integer featureImage;

	private Integer addressId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer adressId) {
		this.addressId = adressId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDateJoining() {
		return dateJoining;
	}

	public void setDateJoining(String dateJoining) {
		this.dateJoining = dateJoining;
	}

	public String getReasonJoining() {
		return reasonJoining;
	}

	public void setReasonJoining(String reasonJoining) {
		this.reasonJoining = reasonJoining;
	}

	public Integer getFeatureImage() {
		return featureImage;
	}

	public void setFeatureImage(Integer featureImage) {
		this.featureImage = featureImage;
	}

}
