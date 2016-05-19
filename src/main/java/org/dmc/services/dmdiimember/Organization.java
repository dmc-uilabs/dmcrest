package org.dmc.services.dmdiimember;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Organization {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "organization_id")
	private Integer id;
	@Column(name = "accountid")
	private Integer accountId;
	private String name;
	private String location;
	private String description;
	private String division;
	private String industry;
	@Column(name = "naics_code")
	private String naicsCode;
	@Column(name = "rd_focus")
	private String rdFocus;
	private String customers;
	private String awards;
	@Column(name = "tech_expertise")
	private String techExpertise;
	@Column(name = "tools_software_equip_mach")
	private String toolsSoftwareEquipMach;
	@Column(name = "post_collaboration")
	private String postCollaboration;
	@Column(name = "collaboration_interest")
	private String collaborationInterest;
	@Column(name = "past_projects")
	private String pastProjects;
	@Column(name = "upcoming_project_interests")
	private String upcomingProjectInterests;
	@Column(name = "addressid")
	private Integer adressId;
	private String email;
	private String phone;
	private String website;
	@Column(name = "social_media_linkedin")
	private String socialMediaLinkedin;
	@Column(name = "social_media_twitter")
	private String socialMediaTwitter;
	@Column(name = "social_medial_inthenews")
	private String socialMediaInthenews;
	@Column(name = "perfered_comm_method")
	private String perferedCommMethod;
	@Column(name = "category_tier")
	private Integer categoryTier;
	@Column(name = "date_joining")
	private String dateJoining;
	@Column(name = "reason_joining")
	private String reasonJoining;
	@Column(name = "feature_image")
	private Integer featureImage;
	@Column(name = "logo_image")
	private String logoImage;
	private String follow;
	@Column(name = "favorates_count")
	private Integer favoritesCount;
	// TODO make this a boolean (it's a string in the database)
	@Column(name = "is_owner")
	private String isOwner;
	private String owner;


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
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
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getNaicsCode() {
		return naicsCode;
	}
	public void setNaicsCode(String naicsCode) {
		this.naicsCode = naicsCode;
	}
	public String getRdFocus() {
		return rdFocus;
	}
	public void setRdFocus(String rdFocus) {
		this.rdFocus = rdFocus;
	}
	public String getCustomers() {
		return customers;
	}
	public void setCustomers(String customers) {
		this.customers = customers;
	}
	public String getAwards() {
		return awards;
	}
	public void setAwards(String awards) {
		this.awards = awards;
	}
	public String getTechExpertise() {
		return techExpertise;
	}
	public void setTechExpertise(String techExpertise) {
		this.techExpertise = techExpertise;
	}
	public String getToolsSoftwareEquipMach() {
		return toolsSoftwareEquipMach;
	}
	public void setToolsSoftwareEquipMach(String toolsSoftwareEquipMach) {
		this.toolsSoftwareEquipMach = toolsSoftwareEquipMach;
	}
	public String getPostCollaboration() {
		return postCollaboration;
	}
	public void setPostCollaboration(String postCollaboration) {
		this.postCollaboration = postCollaboration;
	}
	public String getCollaborationInterest() {
		return collaborationInterest;
	}
	public void setCollaborationInterest(String collaborationInterest) {
		this.collaborationInterest = collaborationInterest;
	}
	public String getPastProjects() {
		return pastProjects;
	}
	public void setPastProjects(String pastProjects) {
		this.pastProjects = pastProjects;
	}
	public String getUpcomingProjectInterests() {
		return upcomingProjectInterests;
	}
	public void setUpcomingProjectInterests(String upcomingProjectInterests) {
		this.upcomingProjectInterests = upcomingProjectInterests;
	}
	public Integer getAdressId() {
		return adressId;
	}
	public void setAdressId(Integer adressId) {
		this.adressId = adressId;
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
	public Integer getCategoryTier() {
		return categoryTier;
	}
	public void setCategoryTier(Integer categoryTier) {
		this.categoryTier = categoryTier;
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
	public String getLogoImage() {
		return logoImage;
	}
	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
	}
	public Integer getFavoritesCount() {
		return favoritesCount;
	}
	public void setFavoritesCount(Integer favoritesCount) {
		this.favoritesCount = favoritesCount;
	}
	public String getIsOwner() {
		return isOwner;
	}
	public void setIsOwner(String isOwner) {
		this.isOwner = isOwner;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
