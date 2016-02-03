package org.dmc.services.company;

import org.dmc.services.sharedattributes.FeatureImage;

public class Company {

    private final String logTag = Company.class.getName();
    
    private final int id;          
    private final int accountId;    
    private final String name;   
    private final String location;
    private final String description;
    private final String division;
    private final String industry;
    private final String NAICSCode;
    private final String RDFocus;
    private final String customers;
    private final String awardsReceived;
    private final String technicalExpertise;
    private final String toolsSoftwareEquipmentMachines;
    private final String postCollaborations;
    private final String collaborationInterests;
    private final String pastProjects;
    private final String upcomingProjectInterests;
    private final String address;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String twitter;
    private final String linkedIn;
    private final String website;
    private final String methodCommunication;
    private final String email;
    private final String phone;
    private final int categoryTier;
    private final String dateJoined;
    private final String reasonJoining;
    private final FeatureImage featureImage;
    private final String logoImage;
    private final boolean follow;
    private final int favoratesCount;
    private final boolean isOwner;
    private final String owner;

    
    private Company(CompanyBuilder builder) {
        this.id = builder.id;
        this.accountId = builder.accountId;
        this.name = builder.name;
        this.location = builder.location;
        this.description = builder.description;
        this.division = builder.division;
        this.industry = builder.industry;
        this.NAICSCode = builder.NAICSCode;
        this.RDFocus = builder.RDFocus;
        this.customers = builder.customers;
        this.awardsReceived = builder.awardsReceived;
        this.technicalExpertise = builder.technicalExpertise;
        this.toolsSoftwareEquipmentMachines = builder.toolsSoftwareEquipmentMachines;
        this.postCollaborations = builder.postCollaborations;
        this.collaborationInterests = builder.collaborationInterests;
        this.pastProjects = builder.pastProjects;
        this.upcomingProjectInterests = builder.upcomingProjectInterests;
        this.address = builder.address;
        this.city = builder.city;
        this.state = builder.state;
        this.zipCode = builder.zipCode;
        this.twitter = builder.twitter;
        this.linkedIn = builder.linkedIn;
        this.website = builder.website;
        this.methodCommunication = builder.methodCommunication;
        this.email = builder.email;
        this.phone = builder.phone;
        this.categoryTier = builder.categoryTier;
        this.dateJoined = builder.dateJoined;
        this.reasonJoining = builder.reasonJoining;
        this.featureImage = builder.featureImage;
        this.logoImage = builder.logoImage;
        this.follow = builder.follow;
        this.favoratesCount = builder.favoratesCount;
        this.isOwner = builder.isOwner;
        this.owner = builder.owner;
    }

    public int getId() {
        return id;
    }
    
    public int getAccountId() {
    	return accountId;
    }
    
    public String getName() {
    	return name;
    }
    
    public String getLocation() {
    	return location;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public String getDivision() {
    	return division;
    }
    
    public String getIndustry() {
    	return industry;
    }
    
    public String getNAICSCode() {
    	return NAICSCode;
    }
   
    public String getRDFocus() {
    	return RDFocus;
    }
    
    public String getCustomers() {
        return customers;
    }
    
    public String getAwardsReceived() {
    	return awardsReceived;
    }
    
    public String getTechnicalExpertise() {
    	return technicalExpertise;
    }
    
    public String getToolsSoftwareEquipmentMachines() {
    	return toolsSoftwareEquipmentMachines;
    }
    
    public String getPostCollaborations() {
    	return postCollaborations;
    }
    
    public String getCollaborationInterests() {
    	return collaborationInterests;
    }
    
    public String getPastProjects() {
    	return pastProjects;
    }
    
    public String getUpcomingProjectInterests() {
    	return upcomingProjectInterests;
    }
    
    public String getAddress() {
    	return address;
    }
    
    public String getCity() {
    	return city;
    }
    
    public String getState() {
    	return state;
    }
    
    public String getZipCode() {
    	return zipCode;
    }
    
    public String getTwitter() {
    	return twitter;
    }
    
    public String getLinkedIn() {
    	return linkedIn;
    }
    
    public String getWebsite() {
    	return website;
    }
    
    public String getMethodCommunication() {
    	return methodCommunication;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public String getPhone() {
    	return phone;
    }
    
    public int getCategoryTier() {
    	return categoryTier;
    }
    
    public String getDateJoined() {
    	return dateJoined;
    }
    
    public String getReasonJoining() {
    	return reasonJoining;
    }
    
    public FeatureImage featureImage() {
    	return featureImage;
    }
    
    public String getLogoImage() {
    	return logoImage;
    }
    
    public boolean getFollow() {
    	return follow;
    }
    
    public int getfavoratesCount() {
    	return favoratesCount;
    }
    
    public boolean getIsOwner() {
    	return isOwner;
    }
    
    public String getOwner() {
    	return owner;
    }

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
}
