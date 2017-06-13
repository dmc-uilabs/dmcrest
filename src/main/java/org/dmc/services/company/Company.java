package org.dmc.services.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Objects;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.utils.RestViews;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-05-03T15:13:20.207Z")

public class Company {

    private final String logTag = Company.class.getName();

	@JsonView(RestViews.CompaniesShortView.class)
	private String id;
  @JsonView(RestViews.CompaniesShortView.class)
	private String accountId;
  @JsonView(RestViews.CompaniesShortView.class)
	private String name;
	private String location;
	private String description;
	private String division;
	private String industry;
	private String nAICSCode;
	private String rDFocus;
	private String customers;
	private String awardsReceived;
	private String technicalExpertise;
	private String toolsSoftwareEquipmentMachines;
	private String pastCollaborations;
	private String collaborationInterests;
	private String pastProjects;
	private String upcomingProjectInterests;
	private String address;
	private String city;
	private int state;
	private String zipCode;
	private String twitter;
	private String linkedIn;
	private String website;
	private String methodCommunication;
	private String email;
	private String phone;
	private int categoryTier;
	private String dateJoined;
	private String reasonJoining;
	private FeatureImage featureImage;
	private String logoImage;
	private boolean follow;
	private int favoritesCount;
	private boolean isOwner;
	private String owner;


	/**
	 **/
	@JsonProperty("id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	/**
	 **/
	@JsonProperty("accountId")
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}


	/**
	 **/
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	/**
	 **/
	@JsonProperty("location")
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}


	/**
	 **/
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 **/
	@JsonProperty("division")
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}


	/**
	 **/
	@JsonProperty("industry")
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}


	/**
	 **/
	@JsonProperty("NAICSCode")
	public String getNAICSCode() {
		return nAICSCode;
	}
	public void setNAICSCode(String nAICSCode) {
		this.nAICSCode = nAICSCode;
	}


	/**
	 **/
	@JsonProperty("RDFocus")
	public String getRDFocus() {
		return rDFocus;
	}
	public void setRDFocus(String rDFocus) {
		this.rDFocus = rDFocus;
	}


	/**
	 **/
	@JsonProperty("customers")
	public String getCustomers() {
		return customers;
	}
	public void setCustomers(String customers) {
		this.customers = customers;
	}


	/**
	 **/
	@JsonProperty("awardsReceived")
	public String getAwardsReceived() {
		return awardsReceived;
	}
	public void setAwardsReceived(String awardsReceived) {
		this.awardsReceived = awardsReceived;
	}


	/**
	 **/
	@JsonProperty("technicalExpertise")
	public String getTechnicalExpertise() {
		return technicalExpertise;
	}
	public void setTechnicalExpertise(String technicalExpertise) {
		this.technicalExpertise = technicalExpertise;
	}


	/**
	 **/
	@JsonProperty("toolsSoftwareEquipmentMachines")
	public String getToolsSoftwareEquipmentMachines() {
		return toolsSoftwareEquipmentMachines;
	}
	public void setToolsSoftwareEquipmentMachines(String toolsSoftwareEquipmentMachines) {
		this.toolsSoftwareEquipmentMachines = toolsSoftwareEquipmentMachines;
	}


	/**
	 **/
	@JsonProperty("pastCollaborations")
	public String getPastCollaborations() {
		return pastCollaborations;
	}
	public void setPastCollaborations(String pastCollaborations) {
		this.pastCollaborations = pastCollaborations;
	}


	/**
	 **/
	@JsonProperty("collaborationInterests")
	public String getCollaborationInterests() {
		return collaborationInterests;
	}
	public void setCollaborationInterests(String collaborationInterests) {
		this.collaborationInterests = collaborationInterests;
	}


	/**
	 **/
	@JsonProperty("pastProjects")
	public String getPastProjects() {
		return pastProjects;
	}
	public void setPastProjects(String pastProjects) {
		this.pastProjects = pastProjects;
	}


	/**
	 **/
	@JsonProperty("upcomingProjectInterests")
	public String getUpcomingProjectInterests() {
		return upcomingProjectInterests;
	}
	public void setUpcomingProjectInterests(String upcomingProjectInterests) {
		this.upcomingProjectInterests = upcomingProjectInterests;
	}


	/**
	 **/
	@JsonProperty("address")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}


	/**
	 **/
	@JsonProperty("city")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}


	/**
	 **/
	@JsonProperty("state")
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}


	/**
	 **/
	@JsonProperty("zipCode")
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	/**
	 **/
	@JsonProperty("twitter")
	public String getTwitter() {
		return twitter;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}


	/**
	 **/
	@JsonProperty("linkedIn")
	public String getLinkedIn() {
		return linkedIn;
	}
	public void setLinkedIn(String linkedIn) {
		this.linkedIn = linkedIn;
	}


	/**
	 **/
	@JsonProperty("website")
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}


	/**
	 **/
	@JsonProperty("methodCommunication")
	public String getMethodCommunication() {
		return methodCommunication;
	}
	public void setMethodCommunication(String methodCommunication) {
		this.methodCommunication = methodCommunication;
	}


	/**
	 **/
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 **/
	@JsonProperty("phone")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}


	/**
	 **/
	@JsonProperty("categoryTier")
	public int getCategoryTier() {
		return categoryTier;
	}
	public void setCategoryTier(int categoryTier) {
		this.categoryTier = categoryTier;
	}


	/**
	 **/
	@JsonProperty("dateJoined")
	public String getDateJoined() {
		return dateJoined;
	}
	public void setDateJoined(String dateJoined) {
		this.dateJoined = dateJoined;
	}


	/**
	 **/
	@JsonProperty("reasonJoining")
	public String getReasonJoining() {
		return reasonJoining;
	}
	public void setReasonJoining(String reasonJoining) {
		this.reasonJoining = reasonJoining;
	}


	/**
	 **/
	@JsonProperty("featureImage")
	public FeatureImage getFeatureImage() {
		return featureImage;
	}
	public void setFeatureImage(FeatureImage featureImage) {
		this.featureImage = featureImage;
	}


	/**
	 **/
	@JsonProperty("logoImage")
	public String getLogoImage() {
		return logoImage;
	}
	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}


	/**
	 **/
	@JsonProperty("follow")
	public boolean getFollow() {
		return follow;
	}
	public void setFollow(boolean follow) {
		this.follow = follow;
	}


	/**
	 **/
	@JsonProperty("favoritesCount")
	public int getFavoritesCount() {
		return favoritesCount;
	}
	public void setFavoritesCount(int favoritesCount) {
		this.favoritesCount = favoritesCount;
	}


	/**
	 **/
	@JsonProperty("isOwner")
	public boolean getIsOwner() {
		return isOwner;
	}
	public void setIsOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}


	/**
	 **/
	@JsonProperty("owner")
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}



	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Company company = (Company) o;
		return Objects.equals(id, company.id) &&
        Objects.equals(accountId, company.accountId) &&
        Objects.equals(name, company.name) &&
        Objects.equals(location, company.location) &&
        Objects.equals(description, company.description) &&
        Objects.equals(division, company.division) &&
        Objects.equals(industry, company.industry) &&
        Objects.equals(nAICSCode, company.nAICSCode) &&
        Objects.equals(rDFocus, company.rDFocus) &&
        Objects.equals(customers, company.customers) &&
        Objects.equals(awardsReceived, company.awardsReceived) &&
        Objects.equals(technicalExpertise, company.technicalExpertise) &&
        Objects.equals(toolsSoftwareEquipmentMachines, company.toolsSoftwareEquipmentMachines) &&
        Objects.equals(pastCollaborations, company.pastCollaborations) &&
        Objects.equals(collaborationInterests, company.collaborationInterests) &&
        Objects.equals(pastProjects, company.pastProjects) &&
        Objects.equals(upcomingProjectInterests, company.upcomingProjectInterests) &&
        Objects.equals(address, company.address) &&
        Objects.equals(city, company.city) &&
        Objects.equals(state, company.state) &&
        Objects.equals(zipCode, company.zipCode) &&
        Objects.equals(twitter, company.twitter) &&
        Objects.equals(linkedIn, company.linkedIn) &&
        Objects.equals(website, company.website) &&
        Objects.equals(methodCommunication, company.methodCommunication) &&
        Objects.equals(email, company.email) &&
        Objects.equals(phone, company.phone) &&
        Objects.equals(categoryTier, company.categoryTier) &&
        Objects.equals(dateJoined, company.dateJoined) &&
        Objects.equals(reasonJoining, company.reasonJoining) &&
        Objects.equals(featureImage, company.featureImage) &&
        Objects.equals(logoImage, company.logoImage) &&
        Objects.equals(follow, company.follow) &&
        Objects.equals(favoritesCount, company.favoritesCount) &&
        Objects.equals(isOwner, company.isOwner) &&
        Objects.equals(owner, company.owner);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, accountId, name, location, description, division, industry, nAICSCode, rDFocus, customers, awardsReceived, technicalExpertise, toolsSoftwareEquipmentMachines, pastCollaborations, collaborationInterests, pastProjects, upcomingProjectInterests, address, city, state, zipCode, twitter, linkedIn, website, methodCommunication, email, phone, categoryTier, dateJoined, reasonJoining, featureImage, logoImage, follow, favoritesCount, isOwner, owner);
	}

	@Override
	public String toString()  {
		StringBuilder sb = new StringBuilder();
		sb.append("class Company {\n");

		sb.append("  id: ").append(id).append("\n");
		sb.append("  accountId: ").append(accountId).append("\n");
		sb.append("  name: ").append(name).append("\n");
		sb.append("  location: ").append(location).append("\n");
		sb.append("  description: ").append(description).append("\n");
		sb.append("  division: ").append(division).append("\n");
		sb.append("  industry: ").append(industry).append("\n");
		sb.append("  nAICSCode: ").append(nAICSCode).append("\n");
		sb.append("  rDFocus: ").append(rDFocus).append("\n");
		sb.append("  customers: ").append(customers).append("\n");
		sb.append("  awardsReceived: ").append(awardsReceived).append("\n");
		sb.append("  technicalExpertise: ").append(technicalExpertise).append("\n");
		sb.append("  toolsSoftwareEquipmentMachines: ").append(toolsSoftwareEquipmentMachines).append("\n");
		sb.append("  pastCollaborations: ").append(pastCollaborations).append("\n");
		sb.append("  collaborationInterests: ").append(collaborationInterests).append("\n");
		sb.append("  pastProjects: ").append(pastProjects).append("\n");
		sb.append("  upcomingProjectInterests: ").append(upcomingProjectInterests).append("\n");
		sb.append("  address: ").append(address).append("\n");
		sb.append("  city: ").append(city).append("\n");
		sb.append("  state: ").append(state).append("\n");
		sb.append("  zipCode: ").append(zipCode).append("\n");
		sb.append("  twitter: ").append(twitter).append("\n");
		sb.append("  linkedIn: ").append(linkedIn).append("\n");
		sb.append("  website: ").append(website).append("\n");
		sb.append("  methodCommunication: ").append(methodCommunication).append("\n");
		sb.append("  email: ").append(email).append("\n");
		sb.append("  phone: ").append(phone).append("\n");
		sb.append("  categoryTier: ").append(categoryTier).append("\n");
		sb.append("  dateJoined: ").append(dateJoined).append("\n");
		sb.append("  reasonJoining: ").append(reasonJoining).append("\n");
		sb.append("  featureImage: ").append(featureImage).append("\n");
		sb.append("  logoImage: ").append(logoImage).append("\n");
		sb.append("  follow: ").append(follow).append("\n");
		sb.append("  favoritesCount: ").append(favoritesCount).append("\n");
		sb.append("  isOwner: ").append(isOwner).append("\n");
		sb.append("  owner: ").append(owner).append("\n");
		sb.append("}\n");
		return sb.toString();
	}
/*
    //company Builder
    public static class CompanyBuilder {

        private final int id;
        private final int accountId;
        private final String name;
        private String location;
        private String description;
        private String division;
        private String industry;
        private String NAICSCode;
        private String RDFocus;
        private String customers;
        private String awardsReceived;
        private String technicalExpertise;
        private String toolsSoftwareEquipmentMachines;
        private String postCollaborations;
        private String collaborationInterests;
        private String pastProjects;
        private String upcomingProjectInterests;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private String twitter;
        private String linkedIn;
        private String website;
        private String methodCommunication;
        private String email;
        private String phone;
        private int categoryTier;
        private String dateJoined;
        private String reasonJoining;
        private FeatureImage featureImage;
        private String logoImage;
        private boolean follow;
        private int favoratesCount;
        private boolean isOwner;
        private String owner;

    	public CompanyBuilder(int id, int accountId, String name) {
    		this.id = id;
    		this.accountId = accountId;
    		this.name = name;
    	}

        public CompanyBuilder location(String location) {
        	this.location = location;
        	return this;
        }

        public CompanyBuilder description(String description) {
        	this.description = description;
        	return this;
        }

        public CompanyBuilder division(String division) {
        	this.division = division;
        	return this;
        }

        public CompanyBuilder industry(String industry) {
        	this.industry = industry;
        	return this;
        }

        public CompanyBuilder NAICSCode(String NAICSCode) {
        	this.NAICSCode = NAICSCode;
        	return this;
        }

        public CompanyBuilder RDFocus(String RDFocus) {
        	this.RDFocus = RDFocus;
        	return this;
        }

        public CompanyBuilder customers(String customers) {
        	this.customers = customers;
            return this;
        }

        public CompanyBuilder awardsReceived(String awardsReceived) {
        	this.awardsReceived = awardsReceived;
        	return this;
        }

        public CompanyBuilder technicalExpertise(String technicalExpertise) {
        	this.technicalExpertise = technicalExpertise;
        	return this;
        }

        public CompanyBuilder toolsSoftwareEquipmentMachines(String toolsSoftwareEquipmentMachines) {
        	this.toolsSoftwareEquipmentMachines = toolsSoftwareEquipmentMachines;
        	return this;
        }

        public CompanyBuilder postCollaborations(String postCollaborations) {
        	this.postCollaborations = postCollaborations;
        	return this;
        }

        public CompanyBuilder collaborationInterests(String collaborationInterests) {
        	this.collaborationInterests = collaborationInterests;
        	return this;
        }

        public CompanyBuilder pastProjects(String pastProjects) {
        	this.pastProjects = pastProjects;
        	return this;
        }

        public CompanyBuilder upcomingProjectInterests(String upcomingProjectInterests) {
        	this.upcomingProjectInterests = upcomingProjectInterests;
        	return this;
        }

        public CompanyBuilder address(String address) {
        	this.address = address;
        	return this;
        }

        public CompanyBuilder city(String city) {
        	this.city = city;
        	return this;
        }

        public CompanyBuilder state(String state) {
        	this.state = state;
        	return this;
        }

        public CompanyBuilder zipCode(String zipCode) {
        	this.zipCode = zipCode;
        	return this;
        }

        public CompanyBuilder twitter(String twitter) {
        	this.twitter = twitter;
        	return this;
        }

        public CompanyBuilder linkedIn(String linkedIn) {
        	this.linkedIn = linkedIn;
        	return this;
        }

        public CompanyBuilder website(String website) {
        	this.website = website;
        	return this;
        }

        public CompanyBuilder methodCommunication(String methodCommunication) {
        	this.methodCommunication = methodCommunication;
        	return this;
        }

        public CompanyBuilder email(String email) {
        	this.email = email;
        	return this;
        }

        public CompanyBuilder phone(String phone) {
        	this.phone = phone;
        	return this;
        }

        public CompanyBuilder categoryTier(int categoryTier) {
        	this.categoryTier = categoryTier;
        	return this;
        }

        public CompanyBuilder dateJoined(String dateJoined) {
        	this.dateJoined = dateJoined;
        	return this;
        }

        public CompanyBuilder reasonJoining(String reasonJoining) {
        	this.reasonJoining = reasonJoining;
        	return this;
        }

        public CompanyBuilder featureImage(FeatureImage featureImage) {
        	this.featureImage = featureImage;
        	return this;
        }

        public CompanyBuilder logoImage(String logoImage) {
        	this.logoImage = logoImage;
        	return this;
        }

        public CompanyBuilder follow(boolean follow) {
        	this.follow = follow;
        	return this;
        }

        public CompanyBuilder favoratesCount(int favoratesCount) {
        	this.favoratesCount = favoratesCount;
        	return this;
        }

        public CompanyBuilder isOwner(boolean isOwner) {
        	this.isOwner = isOwner;
        	return this;
        }

        public CompanyBuilder owner(String owner) {
        	this.owner = owner;
        	return this;
        }

        public Company build() {
        	return new Company(this);
        }
    }
 */
}
