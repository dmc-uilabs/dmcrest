package org.dmc.services.data.entities;


import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "application_submission")
@Where(clause = "status!='SOFT_DELETED'")
@SQLDelete(sql = "UPDATE application_submission SET status = 'SOFT_DELETED' WHERE id = ?")
public class AppSubmission extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "application_id")
	private Document application;
	
	@Column(name = "developer_id")
	private String developerId;
	
	@Column(name = "contact_name")
	private String contactName;
	
	@Column(name = "contact_email")
	private String contactEmail;
	
	@Column(name = "contact_phone")
	private String contactPhone;
	
	@Column(name = "application_name")
	private String appName;
	
	@Column(name = "application_version")
	private String appVersion;
	
	@Column(name = "application_title")
	private String appTitle;
	
	@Column(name = "short_description")
	private String shortDescription;
	
	@Column(name = "full_description")
	private String fullDescription;
	
	@Column(name = "release_notes")
	private String releaseNotes;
	
	@OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
	@JoinTable(name = "application_submission_document",
			joinColumns = @JoinColumn(name = "application_submission_id"),
			inverseJoinColumns = @JoinColumn(name = "document_id"))
	@Where(clause = "doc_class = 'IMAGE'")
	private Set<Document> screenShots;
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "application_icon_id")
	private Document appIcon;
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinTable(name = "application_submission_application_tag",
			joinColumns = @JoinColumn(name = "application_submission_id"),
			inverseJoinColumns = @JoinColumn(name = "application_tag_id"))
	private Set<ApplicationTag> appTags;
	
	@OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
	@JoinTable(name = "application_submission_document",
			joinColumns = @JoinColumn(name = "application_submission_id"),
			inverseJoinColumns = @JoinColumn(name = "document_id"))
	@Where(clause = "doc_class = 'SUPPORT'")
	private Set<Document> appDocuments;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "application_category")
	private ApplicationCategory appCategory;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "application_subcategory")
	private ApplicationSubcategory appSubcategory;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "application_pricing_structure")
	private ApplicationPricingStructure appPricingStructure;
	
	@Column(name = "application_cost")
	private String appCost;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "application_hosting_method")
	private ApplicationHostingMethod appHostingMethod;
	
	@Column(name = "application_hosting_method_notes")
	private String appHostingMethodNotes;
	
	@Column(name = "system_requirements")
	private String systemRequirements;
	
	@Column(name = "application_license")
	private String appLicense;
	
	@Column(name = "standard_license_terms")
	private boolean standardLicenseTerms = false;
	
	@Column(name = "website_url")
	private String websiteUrl;
	
	@Column(name = "support_email")
	private String supportEmail;
	
	@Column(name = "privacy_policy")
	private String privacyPolicy;
	
	@Column(name = "submission_notes")
	private String submissionNotes;
	
	private String copyright;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "submitted_on")
	private Date submittedOn;
	
	@Column(name = "assigned_to")
	private String assignedTo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "review_status")
	private ApplicationReviewStatus reviewStatus = ApplicationReviewStatus.RECEIVED;
	
	@Column(name = "reviewer_notes")
	private String reviewerNotes;
	
	@Enumerated(EnumType.STRING)
	private ApplicationStatus status = ApplicationStatus.IN_REVIEW;
	
	public enum ApplicationCategory {
		DATA, ANALYTIC, BUSINESS;
	}
	
	public enum ApplicationSubcategory {
		FACTORY_DATA_COLLECTION_AND_ANALYSIS,
		SUPPLIER_INTEROPERABILITY,
		MODELING_AND_SIMULATION_ENVIRONMENT,
		DESIGN_VERIFICATION_AND_TESTING,
		COST_ESTIMATION_TOOLS,
		DATA_TRANSLATION;
	}
	
	public enum ApplicationPricingStructure {
		FREE,
		COST_PER_EXECUTION,
		UNLIMITED_EXECUTIONS_PER_MONTH,
		PROPORTIONAL_TO_RESOURCES_USED;
	}
	
	public enum ApplicationHostingMethod {
		PUBLIC,
		PRIVATE,
		THIRD_PARTY
	}
	
	public enum ApplicationReviewStatus {
		RECEIVED,
		PROCESSING,
		NEED_FURTHER_INFORMATION,
		WAITING_ON_USER,
		APPROVED,
		DECLINED,
		CLOSED
	}

	public enum ApplicationStatus {
		IN_REVIEW,
		IN_GOOD_STANDING,
		SUSPENDED,
		SOFT_DELETED
	}
	
	@PrePersist
	protected void onCreate() {
		this.submittedOn = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Document getApplication() {
		return application;
	}

	public void setApplication(Document application) {
		this.application = application;
	}

	public String getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(String developerId) {
		this.developerId = developerId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppTitle() {
		return appTitle;
	}

	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public String getReleaseNotes() {
		return releaseNotes;
	}

	public void setReleaseNotes(String releaseNotes) {
		this.releaseNotes = releaseNotes;
	}

	public Set<Document> getScreenShots() {
		return screenShots;
	}

	public void setScreenShots(Set<Document> screenShots) {
		this.screenShots = screenShots;
	}

	public Document getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Document appIcon) {
		this.appIcon = appIcon;
	}

	public Set<ApplicationTag> getAppTags() {
		return appTags;
	}

	public void setAppTags(Set<ApplicationTag> appTags) {
		this.appTags = appTags;
	}

	public Set<Document> getAppDocuments() {
		return appDocuments;
	}

	public void setAppDocuments(Set<Document> appDocuments) {
		this.appDocuments = appDocuments;
	}

	public ApplicationCategory getAppCategory() {
		return appCategory;
	}

	public void setAppCategory(ApplicationCategory appCategory) {
		this.appCategory = appCategory;
	}

	public ApplicationSubcategory getAppSubcategory() {
		return appSubcategory;
	}

	public void setAppSubcategory(ApplicationSubcategory appSubcategory) {
		this.appSubcategory = appSubcategory;
	}

	public ApplicationPricingStructure getAppPricingStructure() {
		return appPricingStructure;
	}

	public void setAppPricingStructure(ApplicationPricingStructure appPricingStructure) {
		this.appPricingStructure = appPricingStructure;
	}

	public String getAppCost() {
		return appCost;
	}

	public void setAppCost(String appCost) {
		this.appCost = appCost;
	}

	public ApplicationHostingMethod getAppHostingMethod() {
		return appHostingMethod;
	}

	public void setAppHostingMethod(ApplicationHostingMethod appHostingMethod) {
		this.appHostingMethod = appHostingMethod;
	}

	public String getAppHostingMethodNotes() {
		return appHostingMethodNotes;
	}

	public void setAppHostingMethodNotes(String appHostingMethodNotes) {
		this.appHostingMethodNotes = appHostingMethodNotes;
	}

	public String getSystemRequirements() {
		return systemRequirements;
	}

	public void setSystemRequirements(String systemRequirements) {
		this.systemRequirements = systemRequirements;
	}

	public String getAppLicense() {
		return appLicense;
	}

	public void setAppLicense(String appLicense) {
		this.appLicense = appLicense;
	}

	public boolean isStandardLicenseTerms() {
		return standardLicenseTerms;
	}

	public void setStandardLicenseTerms(boolean standardLicenseTerms) {
		this.standardLicenseTerms = standardLicenseTerms;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String getSupportEmail() {
		return supportEmail;
	}

	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}

	public String getPrivacyPolicy() {
		return privacyPolicy;
	}

	public void setPrivacyPolicy(String privacyPolicy) {
		this.privacyPolicy = privacyPolicy;
	}

	public String getSubmissionNotes() {
		return submissionNotes;
	}

	public void setSubmissionNotes(String submissionNotes) {
		this.submissionNotes = submissionNotes;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public Date getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(Date submittedOn) {
		this.submittedOn = submittedOn;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public ApplicationReviewStatus getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(ApplicationReviewStatus reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public String getReviewerNotes() {
		return reviewerNotes;
	}

	public void setReviewerNotes(String reviewerNotes) {
		this.reviewerNotes = reviewerNotes;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
}
