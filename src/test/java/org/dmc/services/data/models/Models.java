package org.dmc.services.data.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Models {

	public static final String PROJECT_TITLE = "ProjectTitle";
	public static final String PROJECT_SUMMARY = "ProjectSummary";
	public static final String THRUST_CODE = "ThrustCode";
	public static final String FOCUS_AREA_NAME = "FocusAreaName";
	public static final String STATUS_NAME = "StatusName";
	public static final String USER_NAME = "UserName";
	public static final String FIRST_NAME = "FirstName";
	public static final String LAST_NAME = "LastName";
	public static final String EMAIL = "email@email.com";
	public static final String ADDRESS = "Address";
	public static final String PHONE = "Phone";
	public static final String LOCATION = "Location";
	public static final String DATE = "2016/07/04";
	public static final String PROJECT_NEWS_TITLE = "Project News Title";
	public static final String PROJECT_NEWS_CONTENT = "Project News Content";
	public static final String PROJECT_EVENT_TITLE = "Project Event Title";
	public static final String PROJECT_EVENT_DESCRIPTION = "Project Event Description";

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

	public static DMDIIProjectModel dmdiiProjectModel() throws Exception {
		DMDIIProjectModel dmdiiProjectModel = new DMDIIProjectModel();

		List<Integer> contributingCompanyIds = new ArrayList<Integer>();
		contributingCompanyIds.add(dmdiiMemberModel().getId());

		dmdiiProjectModel.setId(2000);
		dmdiiProjectModel.setPrimeOrganization(dmdiiMemberModel().getId());
		dmdiiProjectModel.setPrincipalInvestigator(user());
		dmdiiProjectModel.setProjectStatus(projectStatus());
		dmdiiProjectModel.setAwardedDate(format.parse("2016/07/01"));
		dmdiiProjectModel.setEndDate(format.parse("2017/01/01"));
		dmdiiProjectModel.setProjectTitle(PROJECT_TITLE);
		dmdiiProjectModel.setProjectSummary(PROJECT_SUMMARY);
		dmdiiProjectModel.setPrincipalPointOfContact(user());
		dmdiiProjectModel.setProjectFocusArea(projectFocusArea());
		dmdiiProjectModel.setProjectThrust(projectThrust());
		dmdiiProjectModel.setContributingCompanies(contributingCompanyIds);
		dmdiiProjectModel.setRootNumber(2016);
		dmdiiProjectModel.setCallNumber(07);
		dmdiiProjectModel.setProjectNumber(01);
		dmdiiProjectModel.setCostShare(new BigDecimal("50000"));
		dmdiiProjectModel.setDmdiiFunding(new BigDecimal("5000"));

		return dmdiiProjectModel;
	}

	private static DMDIIProjectThrustModel projectThrust() {
		DMDIIProjectThrustModel dmdiiProjectThrustModel = new DMDIIProjectThrustModel();

		dmdiiProjectThrustModel.setId(2000);
		dmdiiProjectThrustModel.setCode(THRUST_CODE);

		return dmdiiProjectThrustModel;
	}

	private static DMDIIProjectFocusAreaModel projectFocusArea() {
		DMDIIProjectFocusAreaModel dmdiiProjectFocusAreaModel = new DMDIIProjectFocusAreaModel();

		dmdiiProjectFocusAreaModel.setId(2000);
		dmdiiProjectFocusAreaModel.setName(FOCUS_AREA_NAME);

		return dmdiiProjectFocusAreaModel;
	}

	private static DMDIIProjectStatusModel projectStatus() {
		DMDIIProjectStatusModel dmdiiProjectStatusModel = new DMDIIProjectStatusModel();

		dmdiiProjectStatusModel.setId(2000);
		dmdiiProjectStatusModel.setName(STATUS_NAME);

		return dmdiiProjectStatusModel;
	}

	public static DMDIIMemberModel dmdiiMemberModel() throws Exception {
		DMDIIMemberModel dmdiiMember = new DMDIIMemberModel();

		List<DMDIIAreaOfExpertiseModel> areasOfExpertise = new ArrayList<DMDIIAreaOfExpertiseModel>();
		areasOfExpertise.add(dmdiiAreaOfExpertise());

		List<DMDIIMemberContactModel> contacts = new ArrayList<DMDIIMemberContactModel>();
		contacts.add(dmdiiMemberContact());

		List<DMDIIMemberCustomerModel> customers = new ArrayList<DMDIIMemberCustomerModel>();
		customers.add(dmdiiMemberCustomer());

		List<DMDIIMemberUserModel> users = new ArrayList<DMDIIMemberUserModel>();
		users.add(dmdiiMemberUser());

		dmdiiMember.setId(1000);
		dmdiiMember.setDmdiiType(dmdiiType());
		dmdiiMember.setOrganization(organization());
		dmdiiMember.setStartDate(new Date(format.parse("2015/11/25").getTime()));
		dmdiiMember.setExpireDate(new Date(format.parse("2017/11/25").getTime()));
		dmdiiMember.setAreasOfExpertise(areasOfExpertise);
		dmdiiMember.setContacts(contacts);
		dmdiiMember.setCustomers(customers);
		dmdiiMember.setUsers(users);
		return dmdiiMember;
	}

	public static DMDIITypeModel dmdiiType() {
		DMDIITypeModel dmdiiType = new DMDIITypeModel();
		dmdiiType.setId(1);
		dmdiiType.setTier(1);
		dmdiiType.setDmdiiTypeCategory(dmdiiTypeCategory());
		return dmdiiType;
	}

	public static DMDIITypeCategoryModel dmdiiTypeCategory() {
		DMDIITypeCategoryModel dmdiiTypeCategory = new DMDIITypeCategoryModel();
		dmdiiTypeCategory.setId(1);
		dmdiiTypeCategory.setCategory("Industry");
		return dmdiiTypeCategory;
	}

	public static OrganizationModel organization() {
		OrganizationModel organization = new OrganizationModel();
		organization.setId(1000);
		organization.setName("Test Company");
		organization.setLocation("Chicago, IL");
		organization.setAddress(address());
		return organization;
	}

	public static AddressModel address() {
		AddressModel address = new AddressModel();
		address.setId(1000);
		address.setStreetAddress1("123 Street st.");
		address.setStreetAddress2("Apt. 2");
		address.setCity("Chicago");
		address.setState("IL");
		address.setZip("45732");
		return address;
	}

	public static DMDIIAreaOfExpertiseModel dmdiiAreaOfExpertise() {
		DMDIIAreaOfExpertiseModel dmdiiAreaOfExpertise = new DMDIIAreaOfExpertiseModel();
		dmdiiAreaOfExpertise.setId(1);
		dmdiiAreaOfExpertise.setName("Additive Manufacturing");
		return dmdiiAreaOfExpertise;
	}

	public static DMDIIMemberContactModel dmdiiMemberContact() throws Exception {
		DMDIIMemberContactModel dmdiiMemberContact = new DMDIIMemberContactModel();
		dmdiiMemberContact.setId(1);
		dmdiiMemberContact.setContactType(dmdiiContactType());
		dmdiiMemberContact.setFirstName("Thor");
		dmdiiMemberContact.setLastName("Odinson");
		dmdiiMemberContact.setEmail("todinson@gmail.com");
		return dmdiiMemberContact;
	}

	public static DMDIIContactTypeModel dmdiiContactType() {
		DMDIIContactTypeModel dmdiiContactType = new DMDIIContactTypeModel();
		dmdiiContactType.setId(1);
		dmdiiContactType.setType("primary point of contact");
		return dmdiiContactType;
	}

	public static DMDIIMemberCustomerModel dmdiiMemberCustomer() {
		DMDIIMemberCustomerModel dmdiiMemberCustomer = new DMDIIMemberCustomerModel();
		dmdiiMemberCustomer.setId(1);
		dmdiiMemberCustomer.setName("Google");
		dmdiiMemberCustomer.setLink("www.google.com");
		return dmdiiMemberCustomer;
	}

	public static DMDIIMemberFinanceModel dmdiiMemberFinance() throws Exception {
		DMDIIMemberFinanceModel dmdiiMemberFinance = new DMDIIMemberFinanceModel();
		dmdiiMemberFinance.setId(1);
		dmdiiMemberFinance.setName("finance");
		dmdiiMemberFinance.setAssetUrl("some_url");
		// TODO add user here:
		return dmdiiMemberFinance;
	}

	public static DMDIIInstituteInvolvementModel dmdiiInstituteInvolvement() throws Exception {
		DMDIIInstituteInvolvementModel instituteInvolvement = new DMDIIInstituteInvolvementModel();
		instituteInvolvement.setId(1);
		instituteInvolvement.setStaticLineItem("some static line item");
		instituteInvolvement.setDate(new Date(format.parse("2016/05/25").getTime()));
		return instituteInvolvement;
	}

	public static DMDIISkillModel dmdiiSkill() {
		DMDIISkillModel skill = new DMDIISkillModel();
		skill.setId(1);
		skill.setTagLink("tag1");
		skill.setTagName("taglink1");
		return skill;
	}

	public static DMDIIMemberUserModel dmdiiMemberUser() {
		DMDIIMemberUserModel dmdiiMemberUser = new DMDIIMemberUserModel();
		dmdiiMemberUser.setId(1);
		dmdiiMemberUser.setUser(user());
		dmdiiMemberUser.setRole(dmdiiRole());
		return dmdiiMemberUser;
	}

	public static DMDIIRoleModel dmdiiRole() {
		DMDIIRoleModel dmdiiRole = new DMDIIRoleModel();
		dmdiiRole.setId(1);
		dmdiiRole.setRole("Role");
		return dmdiiRole;
	}

	public static UserModel user() {
		UserModel user = new UserModel();
		user.setId(1000);
		user.setUsername("eppn");
		user.setFirstName("fname");
		user.setLastName("lname");
		user.setEmail("test@email.com");
		user.setAddress("123 Street St.");
		user.setPhone("1562236654");
		user.setUserContactInfo(userContactInfo());
		return user;
	}

	public static UserContactInfoModel userContactInfo() {
		UserContactInfoModel contactInfo = new UserContactInfoModel();
		contactInfo.setId(1);
		contactInfo.setUserPublicContactInfo(userPublicContactInfo());
		contactInfo.setUserPrivateContactInfo(userPrivateContactInfo());
		contactInfo.setUserMemberPortalContactInfo(userMemberPortalContactInfo());
		return contactInfo;
	}

	public static UserPublicContactInfoModel userPublicContactInfo() {
		UserPublicContactInfoModel publicContactInfo = new UserPublicContactInfoModel();
		publicContactInfo.setId(1);
		publicContactInfo.setEmail("public@email.com");
		publicContactInfo.setPhone("4444444444");
		publicContactInfo.setLocation("Ann Arbor, MI");
		return publicContactInfo;
	}

	public static UserPrivateContactInfoModel userPrivateContactInfo() {
		UserPrivateContactInfoModel privateContactInfo = new UserPrivateContactInfoModel();
		privateContactInfo.setId(1);
		privateContactInfo.setEmail("private@email.com");
		privateContactInfo.setPhone("4444444444");
		privateContactInfo.setLocation("Ann Arbor, MI");
		return privateContactInfo;
	}

	public static UserMemberPortalContactInfoModel userMemberPortalContactInfo() {
		UserMemberPortalContactInfoModel memberPortalContactInfo = new UserMemberPortalContactInfoModel();
		memberPortalContactInfo.setId(1);
		memberPortalContactInfo.setEmail("memberportal@email.com");
		memberPortalContactInfo.setPhone("4444444444");
		memberPortalContactInfo.setLocation("Ann Arbor, MI");
		return memberPortalContactInfo;
	}

	public static DMDIIMemberEventModel dmdiiMemberEvent() throws Exception {
		DMDIIMemberEventModel event = new DMDIIMemberEventModel();
		event.setId(1);
		event.setName("Member Event");
		event.setDescription("Short description of the event");
		event.setLocation("Chicago, IL");
		event.setDate("2016/05/25");
		return event;
	}

	public static DMDIIMemberNewsModel dmdiiMemberNews() throws Exception {
		DMDIIMemberNewsModel news = new DMDIIMemberNewsModel();
		news.setId(1);
		news.setTitle("News Title");
		news.setContent("Content of the news article");
		news.setDateCreated("2016/05/25");
		return news;
	}

	public static DMDIIProjectNewsModel dmdiiProjectNews() {
		DMDIIProjectNewsModel news = new DMDIIProjectNewsModel();
		news.setId(1);
		news.setTitle(PROJECT_NEWS_TITLE);
		news.setContent(PROJECT_NEWS_CONTENT);
		news.setDateCreated(DATE);
		return news;
	}

	public static DMDIIProjectEventModel dmdiiProjectEvent() {
		DMDIIProjectEventModel event = new DMDIIProjectEventModel();
		event.setId(1);
		event.setEventName(PROJECT_EVENT_TITLE);
		event.setEventDescription(PROJECT_EVENT_DESCRIPTION);
		event.setEventDate(DATE);
		return event;
	}
}
