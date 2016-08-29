package org.dmc.services.data.models;

import java.util.ArrayList;
import java.util.List;

public class OrganizationModel extends BaseModel {

	private String name;

	private String location;

	private String description;

	private String email;

	private String phone;

	private String website;

	private String socialMediaLinkedin;

	private String socialMediaTwitter;

	private String socialMediaInthenews;

	private String perferedCommMethod;

	private AddressModel address;

	private String reasonJoining;

	private Integer featureImage;

	private List<AwardModel> awards = new ArrayList<AwardModel>();

	private List<OrganizationContactModel> contacts = new ArrayList<OrganizationContactModel>();

	private List<AreaOfExpertiseModel> areasOfExpertise = new ArrayList<AreaOfExpertiseModel>();

	private List<AreaOfExpertiseModel> desiredAreasOfExpertise = new ArrayList<AreaOfExpertiseModel>();

	private Integer dmdiiMemberId;

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

	public String getSocialMediaLinkedin() {
		return socialMediaLinkedin;
	}

	public void setSocialMediaLinkedin(String socialMediaLinkedin) {
		this.socialMediaLinkedin = socialMediaLinkedin;
	}

	public String getSocialMediaTwitter() {
		return socialMediaTwitter;
	}

	public void setSocialMediaTwitter(String socialMediaTwitter) {
		this.socialMediaTwitter = socialMediaTwitter;
	}

	public String getSocialMediaInthenews() {
		return socialMediaInthenews;
	}

	public void setSocialMediaInthenews(String socialMediaInthenews) {
		this.socialMediaInthenews = socialMediaInthenews;
	}

	public String getPerferedCommMethod() {
		return perferedCommMethod;
	}

	public void setPerferedCommMethod(String perferedCommMethod) {
		this.perferedCommMethod = perferedCommMethod;
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

	public List<OrganizationContactModel> getContacts() {
		return contacts;
	}

	public void setContacts(List<OrganizationContactModel> contacts) {
		this.contacts = contacts;
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

	public Integer getDmdiiMemberId() {
		return dmdiiMemberId;
	}

	public void setDmdiiMemberId(Integer dmdiiMemberId) {
		this.dmdiiMemberId = dmdiiMemberId;
	}

}
