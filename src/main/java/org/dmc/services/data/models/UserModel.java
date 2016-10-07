package org.dmc.services.data.models;

import java.util.List;
import java.util.Map;

import org.dmc.services.users.UserMessages;
import org.dmc.services.users.UserRunningServices;

public class UserModel extends BaseModel {

	private Integer id;
	private String displayName;
	private Integer accountId;
	private Integer profileId;
	private Integer companyId;
	private String companyName;
	private int role;
	private String realname;
	private String title;
	private boolean termsConditions;
	private String firstName;
	private String lastName;
	private String email;
	private String address;
	private String phone;
	private String image;
	private UserRunningServices runningServices = new UserRunningServices();
	private UserMessages messages = new UserMessages();
	private OnboardingStatusModel onboarding;
	private List<UserSkillModel> skills;
	private Map<Integer, String> roles;
	private Boolean isDMDIIMember;
	private UserContactInfoModel userContactInfo;
	private String aboutMe;
	private String resume;
	private int account;
	private List<NotificationModel> notifications;
	private boolean hasUnreadNotifications;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
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

	public List<UserSkillModel> getSkills() {
		return skills;
	}

	public void setSkills(List<UserSkillModel> skills) {
		this.skills = skills;
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

	public void setIsDMDIIMember(Boolean isDMDIIMember) {
		this.isDMDIIMember = isDMDIIMember;
	}

	public UserContactInfoModel getUserContactInfo() {
		return userContactInfo;
	}

	public void setUserContactInfo(UserContactInfoModel userContactInfo) {
		this.userContactInfo = userContactInfo;
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

	public Integer getAccount() {
		return account;
	}

	public void setAccount(Integer account) {
		this.account = account;
	}

	public List<NotificationModel> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<NotificationModel> notifications) {
		this.notifications = notifications;
	}

	public boolean isHasUnreadNotifications() {
		return hasUnreadNotifications;
	}

	public void setHasUnreadNotifications(boolean hasUnreadNotifications) {
		this.hasUnreadNotifications = hasUnreadNotifications;
	}
}
