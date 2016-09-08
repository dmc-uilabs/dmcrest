package org.dmc.services.data.models;

import org.dmc.services.users.UserMessages;
import org.dmc.services.users.UserNotifications;
import org.dmc.services.users.UserRunningServices;

import java.util.Map;

public class UserModel extends BaseModel {

	//	private String username;
	//	private boolean
	//	private String firstName;
	//	private String lastName;
	//	private String email;
	//	private String address;
	//	private String phone;
	//	private Map<Integer, String> roles;
	//	private UserContactInfoModel userContactInfo;
	//	private boolean isDMDIIMember;
	//	private Integer organization;
	//	private String aboutMe;

	private String displayName;
	private int accountId;
	private int profileId;
	private int companyId;
	private int role;
	private String username;
	private String realname;
	private String title;
	private boolean termsConditions;
	private String firstName;
	private String lastName;
	private String email;
	private String address;
	private String phone;
	private String image;
	private UserNotifications notifications;
	private UserRunningServices runningServices;
	private UserMessages messages;
	private OnboardingStatusModel onboarding;
	private Map<Integer, String> roles;
	private Boolean isDMDIIMember;
	private UserContactInfoModel userContactInfo;
	private Integer organization;
	private String aboutMe;
	private String resume;
	private int account;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getProfileId() {
		return profileId;
	}

	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean getTermsConditions() {
		return termsConditions;
	}

	public void setTermsConditions(boolean termsConditions) {
		this.termsConditions = termsConditions;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public UserNotifications getNotifications() {
		return notifications;
	}

	public void setNotifications(UserNotifications notifications) {
		this.notifications = notifications;
	}

	public UserRunningServices getRunningServices() {
		return runningServices;
	}

	public void setRunningServices(UserRunningServices runningServices) {
		this.runningServices = runningServices;
	}

	public UserMessages getMessages() {
		return messages;
	}

	public void setMessages(UserMessages messages) {
		this.messages = messages;
	}

	public OnboardingStatusModel getOnboarding() {
		return onboarding;
	}

	public void setOnboarding(OnboardingStatusModel onboarding) {
		this.onboarding = onboarding;
	}

	public Map<Integer, String> getRoles() {
		return roles;
	}

	public void setRoles(Map<Integer, String> roles) {
		this.roles = roles;
	}

	public Boolean getIsDMDIIMember() {
		return isDMDIIMember;
	}

	public void setIsDMDIIMember(Boolean DMDIIMember) {
		isDMDIIMember = DMDIIMember;
	}

	public UserContactInfoModel getUserContactInfo() {
		return userContactInfo;
	}

	public void setUserContactInfo(UserContactInfoModel userContactInfo) {
		this.userContactInfo = userContactInfo;
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

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public void setIsDMDIIMember(boolean isDMDIIMember) {
		this.isDMDIIMember = isDMDIIMember;
	}
}
