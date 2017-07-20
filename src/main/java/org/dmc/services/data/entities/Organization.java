package org.dmc.services.data.entities;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Entity
@Where(clause = "is_deleted='FALSE'")
@SQLDelete(sql="UPDATE organization SET is_deleted = 'TRUE' WHERE organization_id = ?")
@Table(name = "organization")
public class Organization extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "organization_id")
	private Integer id;

	@Column(name = "accountid")
	private Integer accountId;

	@Column(name = "name")
	private String name;

	@Column(name = "location")
	private String location;

	@Column(name = "description")
	private String description;

	@Column(name = "division")
	private String division;

	@Column(name = "industry")
	private String industry;

	@Column(name = "naics_code")
	private String naicsCode;

	@Column(name = "rd_focus")
	private String rdFocus;

	@Column(name = "customers")
	private String customers;

	@ManyToMany
	@JoinTable(name = "organization_area_of_expertise",
			   joinColumns = @JoinColumn(name="organization_id"),
			   inverseJoinColumns = @JoinColumn(name="area_of_expertise_id"))
	private List<AreaOfExpertise> areasOfExpertise;

	@ManyToMany
	@JoinTable(name = "organization_desired_area_of_expertise",
			   joinColumns = @JoinColumn(name="organization_id"),
			   inverseJoinColumns = @JoinColumn(name="area_of_expertise_id"))
	private List<AreaOfExpertise> desiredAreasOfExpertise;

	@OneToMany(mappedBy="organization", cascade={CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval=true)
	private List<Award> awards;

	@OneToMany(mappedBy="organization", cascade={CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval=true)
	private List<OrganizationContact> contacts;

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

	@OneToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "address_id")
	private Address address;

	@Column(name = "email")
	private String email;

	@Column(name = "phone")
	private String phone;

	@Column(name = "website")
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

	@Column(name = "reason_joining")
	private String reasonJoining;

	@Column(name = "feature_image")
	private Integer featureImage;

	@Column(name = "logo_image")
	private String logoImage;

	@Column(name = "follow")
	private String follow;

	@Column(name = "favorites_count")
	private Integer favoritesCount;

	@Column(name = "is_owner")
	private String isOwner;

	@Column(name = "owner")
	private String owner;

	@OneToOne(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private DMDIIMember dmdiiMember;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	@OneToMany(mappedBy = "organization", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Set<UserRoleAssignment> userRoleAssignments;

	@OneToMany(mappedBy = "organization", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Set<OrganizationUser> organizationUsers;

	@Column(name="production_capabilities")
	private String productionCapabilities;
	
	@Column(name="dmdii_membership_info")
	private String dmdiiMembershipInfo;
	
	@Column(name="is_paid")
	private Boolean isPaid = false;

	@Column(name="other_organization_tags")
	private String otherOrganizationTags;

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

	public List<AreaOfExpertise> getAreasOfExpertise() {
		return areasOfExpertise;
	}

	public void setAreasOfExpertise(List<AreaOfExpertise> areasOfExpertise) {
		this.areasOfExpertise = areasOfExpertise;
	}

	public List<AreaOfExpertise> getDesiredAreasOfExpertise() {
		return desiredAreasOfExpertise;
	}

	public void setDesiredAreasOfExpertise(List<AreaOfExpertise> desiredAreasOfExpertise) {
		this.desiredAreasOfExpertise = desiredAreasOfExpertise;
	}

	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		awards.stream().forEach((a) -> a.setOrganization(this));
		this.awards = awards;
	}

	public List<OrganizationContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<OrganizationContact> contacts) {
		contacts.stream().forEach((a) -> a.setOrganization(this));
		this.contacts = contacts;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
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

	public Integer getCategoryTier() {
		return categoryTier;
	}

	public void setCategoryTier(Integer categoryTier) {
		this.categoryTier = categoryTier;
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

	public DMDIIMember getDmdiiMember() {
		return dmdiiMember;
	}

	public void setDmdiiMember(DMDIIMember dmdiiMember) {
		this.dmdiiMember = dmdiiMember;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Set<UserRoleAssignment> getUserRoleAssignments() {
		return userRoleAssignments;
	}

	public void setUserRoleAssignments(Set<UserRoleAssignment> userRoleAssignments) {
		this.userRoleAssignments = userRoleAssignments;
	}

	public Set<OrganizationUser> getOrganizationUsers() {
		return organizationUsers;
	}

	public void setOrganizationUsers(Set<OrganizationUser> organizationUsers) {
		this.organizationUsers = organizationUsers;
	}

	public String getProductionCapabilities() { 
		return productionCapabilities; 
	}

	public void setProductionCapabilities(String productionCapabilities) { 
		this.productionCapabilities = productionCapabilities; 
	}
	
	public String getDmdiiMembershipInfo() {
		return dmdiiMembershipInfo;
	}
	
	public void setDmdiiMembershipInfo(String dmdiiMembershipInfo) {
		this.dmdiiMembershipInfo = dmdiiMembershipInfo;
	}
	
	public Boolean getIsPaid() {
		return isPaid;
	}
	
	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public String getOtherOrganizationTags() { return otherOrganizationTags; }

	public void setOtherOrganizationTags(String otherOrganizationTags) { this.otherOrganizationTags = otherOrganizationTags; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((areasOfExpertise == null) ? 0 : areasOfExpertise.hashCode());
		result = prime * result + ((awards == null) ? 0 : awards.hashCode());
		result = prime * result + ((categoryTier == null) ? 0 : categoryTier.hashCode());
		result = prime * result + ((collaborationInterest == null) ? 0 : collaborationInterest.hashCode());
		result = prime * result + ((contacts == null) ? 0 : contacts.hashCode());
		result = prime * result + ((customers == null) ? 0 : customers.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((desiredAreasOfExpertise == null) ? 0 : desiredAreasOfExpertise.hashCode());
		result = prime * result + ((division == null) ? 0 : division.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((favoritesCount == null) ? 0 : favoritesCount.hashCode());
		result = prime * result + ((featureImage == null) ? 0 : featureImage.hashCode());
		result = prime * result + ((follow == null) ? 0 : follow.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((industry == null) ? 0 : industry.hashCode());
		result = prime * result + ((isDeleted == null) ? 0 : isDeleted.hashCode());
		result = prime * result + ((isOwner == null) ? 0 : isOwner.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((logoImage == null) ? 0 : logoImage.hashCode());
		result = prime * result + ((naicsCode == null) ? 0 : naicsCode.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((pastProjects == null) ? 0 : pastProjects.hashCode());
		result = prime * result + ((perferedCommMethod == null) ? 0 : perferedCommMethod.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((postCollaboration == null) ? 0 : postCollaboration.hashCode());
		result = prime * result + ((rdFocus == null) ? 0 : rdFocus.hashCode());
		result = prime * result + ((reasonJoining == null) ? 0 : reasonJoining.hashCode());
		result = prime * result + ((socialMediaInthenews == null) ? 0 : socialMediaInthenews.hashCode());
		result = prime * result + ((socialMediaLinkedin == null) ? 0 : socialMediaLinkedin.hashCode());
		result = prime * result + ((socialMediaTwitter == null) ? 0 : socialMediaTwitter.hashCode());
		result = prime * result + ((techExpertise == null) ? 0 : techExpertise.hashCode());
		result = prime * result + ((toolsSoftwareEquipMach == null) ? 0 : toolsSoftwareEquipMach.hashCode());
		result = prime * result + ((upcomingProjectInterests == null) ? 0 : upcomingProjectInterests.hashCode());
		result = prime * result + ((website == null) ? 0 : website.hashCode());
		result = prime * result + ((productionCapabilities == null) ? 0 : productionCapabilities.hashCode());
		result = prime * result + ((dmdiiMembershipInfo == null) ? 0 : dmdiiMembershipInfo.hashCode());
		result = prime * result + ((isPaid == null) ? 0 : isPaid.hashCode());
		result = prime * result + ((otherOrganizationTags == null) ? 0 : otherOrganizationTags.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Organization other = (Organization) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (areasOfExpertise == null) {
			if (other.areasOfExpertise != null)
				return false;
		} else if (!areasOfExpertise.equals(other.areasOfExpertise))
			return false;
		if (awards == null) {
			if (other.awards != null)
				return false;
		} else if (!awards.equals(other.awards))
			return false;
		if (categoryTier == null) {
			if (other.categoryTier != null)
				return false;
		} else if (!categoryTier.equals(other.categoryTier))
			return false;
		if (collaborationInterest == null) {
			if (other.collaborationInterest != null)
				return false;
		} else if (!collaborationInterest.equals(other.collaborationInterest))
			return false;
		if (contacts == null) {
			if (other.contacts != null)
				return false;
		} else if (!contacts.equals(other.contacts))
			return false;
		if (customers == null) {
			if (other.customers != null)
				return false;
		} else if (!customers.equals(other.customers))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (desiredAreasOfExpertise == null) {
			if (other.desiredAreasOfExpertise != null)
				return false;
		} else if (!desiredAreasOfExpertise.equals(other.desiredAreasOfExpertise))
			return false;
		if (division == null) {
			if (other.division != null)
				return false;
		} else if (!division.equals(other.division))
			return false;
		if (dmdiiMember == null) {
			if (other.dmdiiMember != null)
				return false;
		} else if (!dmdiiMember.equals(other.dmdiiMember))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (favoritesCount == null) {
			if (other.favoritesCount != null)
				return false;
		} else if (!favoritesCount.equals(other.favoritesCount))
			return false;
		if (featureImage == null) {
			if (other.featureImage != null)
				return false;
		} else if (!featureImage.equals(other.featureImage))
			return false;
		if (follow == null) {
			if (other.follow != null)
				return false;
		} else if (!follow.equals(other.follow))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (industry == null) {
			if (other.industry != null)
				return false;
		} else if (!industry.equals(other.industry))
			return false;
		if (isDeleted == null) {
			if (other.isDeleted != null)
				return false;
		} else if (!isDeleted.equals(other.isDeleted))
			return false;
		if (isOwner == null) {
			if (other.isOwner != null)
				return false;
		} else if (!isOwner.equals(other.isOwner))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (logoImage == null) {
			if (other.logoImage != null)
				return false;
		} else if (!logoImage.equals(other.logoImage))
			return false;
		if (naicsCode == null) {
			if (other.naicsCode != null)
				return false;
		} else if (!naicsCode.equals(other.naicsCode))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (organizationUsers == null) {
			if (other.organizationUsers != null)
				return false;
		} else if (!organizationUsers.equals(other.organizationUsers))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (pastProjects == null) {
			if (other.pastProjects != null)
				return false;
		} else if (!pastProjects.equals(other.pastProjects))
			return false;
		if (perferedCommMethod == null) {
			if (other.perferedCommMethod != null)
				return false;
		} else if (!perferedCommMethod.equals(other.perferedCommMethod))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (postCollaboration == null) {
			if (other.postCollaboration != null)
				return false;
		} else if (!postCollaboration.equals(other.postCollaboration))
			return false;
		if (rdFocus == null) {
			if (other.rdFocus != null)
				return false;
		} else if (!rdFocus.equals(other.rdFocus))
			return false;
		if (reasonJoining == null) {
			if (other.reasonJoining != null)
				return false;
		} else if (!reasonJoining.equals(other.reasonJoining))
			return false;
		if (socialMediaInthenews == null) {
			if (other.socialMediaInthenews != null)
				return false;
		} else if (!socialMediaInthenews.equals(other.socialMediaInthenews))
			return false;
		if (socialMediaLinkedin == null) {
			if (other.socialMediaLinkedin != null)
				return false;
		} else if (!socialMediaLinkedin.equals(other.socialMediaLinkedin))
			return false;
		if (socialMediaTwitter == null) {
			if (other.socialMediaTwitter != null)
				return false;
		} else if (!socialMediaTwitter.equals(other.socialMediaTwitter))
			return false;
		if (techExpertise == null) {
			if (other.techExpertise != null)
				return false;
		} else if (!techExpertise.equals(other.techExpertise))
			return false;
		if (toolsSoftwareEquipMach == null) {
			if (other.toolsSoftwareEquipMach != null)
				return false;
		} else if (!toolsSoftwareEquipMach.equals(other.toolsSoftwareEquipMach))
			return false;
		if (upcomingProjectInterests == null) {
			if (other.upcomingProjectInterests != null)
				return false;
		} else if (!upcomingProjectInterests.equals(other.upcomingProjectInterests))
			return false;
		if (userRoleAssignments == null) {
			if (other.userRoleAssignments != null)
				return false;
		} else if (!userRoleAssignments.equals(other.userRoleAssignments))
			return false;
		if (website == null) {
			if (other.website != null)
				return false;
		} else if (!website.equals(other.website))
			return false;
		if (productionCapabilities == null) {
			if (other.productionCapabilities != null)
				return false;
		} else if (!productionCapabilities.equals(other.productionCapabilities))
			return false;
		if (dmdiiMembershipInfo == null) {
			if (other.dmdiiMembershipInfo != null)
				return false;
		} else if (!dmdiiMembershipInfo.equals(other.dmdiiMembershipInfo))
			return false;
		if (isPaid == null) {
			if (other.isPaid != null)
				return false;
		} else if (!isPaid.equals(other.isPaid))
		if (otherOrganizationTags == null) {
			if (other.otherOrganizationTags != null)
				return false;
		} else if (!otherOrganizationTags.equals(other.otherOrganizationTags))
			return false;
		return true;
	}

}
