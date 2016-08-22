package org.dmc.services.data.models;

import java.util.List;

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

	private AddressModel address;

	private List<AwardModel> awards;

	private List<AreaOfExpertiseModel> areasOfExpertise;

	private List<AreaOfExpertiseModel> desiredAreasOfExpertise;

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

	public AddressModel getAddress() {
		return address;
	}

	public void setAddress(AddressModel address) {
		this.address = address;
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

	public List<AwardModel> getAwards() {
		return awards;
	}

	public void setAwards(List<AwardModel> awards) {
		this.awards = awards;
	}

	public List<AreaOfExpertiseModel> getAreasOfExpertise() {
		return areasOfExpertise;
	}

	public void setAreasOfExpertise(List<AreaOfExpertiseModel> areasOfExpertise) {
		this.areasOfExpertise = areasOfExpertise;
	}

	public List<AreaOfExpertiseModel> getDesiredAreasOfExpertise() {
		return desiredAreasOfExpertise;
	}

	public void setDesiredAreasOfExpertise(List<AreaOfExpertiseModel> desiredAreasOfExpertise) {
		this.desiredAreasOfExpertise = desiredAreasOfExpertise;
	}

}
