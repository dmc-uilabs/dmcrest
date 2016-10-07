package org.dmc.services.data.models;

import java.util.Set;

import org.dmc.services.data.entities.AppSubmission.ApplicationCategory;
import org.dmc.services.data.entities.AppSubmission.ApplicationHostingMethod;
import org.dmc.services.data.entities.AppSubmission.ApplicationPricingStructure;
import org.dmc.services.data.entities.AppSubmission.ApplicationSubcategory;

public class AppSubmissionModel extends BaseModel {

	private DocumentModel application;
	private String developerId;
	private String contactName;
	private String contactEmail;
	private String contactPhone;
	private String appName;
	private String appVersion;
	private String appTitle;
	private String shortDescription;
	private String fullDescription;
	private String releaseNotes;
	private Set<DocumentModel> screenShots;
	private DocumentModel appIcon;
	private Set<ApplicationTagModel> appTags;
	private Set<DocumentModel> appDocuments;
	private ApplicationCategory appCategory;
	private ApplicationSubcategory appSubcategory;
	private ApplicationPricingStructure appPricingStructure;
	private String appCost;
	private ApplicationHostingMethod appHostingMethod;
	private String appHostingMethodNotes;
	private String systemRequirements;
	private String appLicense;
	private boolean standardLicenseTerms;
	private String websiteUrl;
	private String supportEmail;
	private String privacyPolicy;
	private String submissionNotes;
	private String copyright;
	
	public DocumentModel getApplication() {
		return application;
	}
	public void setApplication(DocumentModel application) {
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
	public Set<DocumentModel> getScreenShots() {
		return screenShots;
	}
	public void setScreenShots(Set<DocumentModel> screenShots) {
		this.screenShots = screenShots;
	}
	public DocumentModel getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(DocumentModel appIcon) {
		this.appIcon = appIcon;
	}
	public Set<ApplicationTagModel> getAppTags() {
		return appTags;
	}
	public void setAppTags(Set<ApplicationTagModel> appTags) {
		this.appTags = appTags;
	}
	public Set<DocumentModel> getAppDocuments() {
		return appDocuments;
	}
	public void setAppDocuments(Set<DocumentModel> appDocuments) {
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
	
}
