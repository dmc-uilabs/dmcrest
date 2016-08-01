package org.dmc.services.data;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.dmc.services.data.entities.*;
import org.dmc.services.dmdiitype.DMDIIType;

public class Entities {
	
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

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	
	public static DMDIIProject dmdiiProject() throws Exception {
		DMDIIProject dmdiiProject = new DMDIIProject();
		
		List<DMDIIMember> contributingCompanies = new ArrayList<DMDIIMember>();
		contributingCompanies.add(dmdiiMember());
		
		dmdiiProject.setId(2000);
		dmdiiProject.setPrimeOrganization(dmdiiMember());
		dmdiiProject.setPrincipalInvestigator(user());
		dmdiiProject.setProjectStatus(projectStatus());
		dmdiiProject.setAwardedDate(format.parse("2016/07/04"));
		dmdiiProject.setEndDate(format.parse("2017/01/01"));
		dmdiiProject.setProjectTitle(PROJECT_TITLE);
		dmdiiProject.setProjectSummary(PROJECT_SUMMARY);
		dmdiiProject.setPrincipalPointOfContact(user());
		dmdiiProject.setProjectFocusArea(projectFocusArea());
		dmdiiProject.setProjectThrust(projectThrust());
		dmdiiProject.setContributingCompanies(contributingCompanies);
		dmdiiProject.setRootNumber(2016);
		dmdiiProject.setCallNumber(07);
		dmdiiProject.setProjectNumber(01);
		dmdiiProject.setCostShare(new BigDecimal("50000"));
		dmdiiProject.setDmdiiFunding(new BigDecimal("5000"));
		
		return dmdiiProject;
	}
	
	public static DMDIIProject dmdiiProject1() throws Exception {
		DMDIIProject dmdiiProject = new DMDIIProject();
		
		List<DMDIIMember> contributingCompanies = new ArrayList<DMDIIMember>();
		contributingCompanies.add(dmdiiMember());
		
		dmdiiProject.setId(2001);
		dmdiiProject.setPrimeOrganization(dmdiiMember());
		dmdiiProject.setPrincipalInvestigator(user());
		dmdiiProject.setProjectStatus(projectStatus());
		dmdiiProject.setAwardedDate(format.parse("2015/07/04"));
		dmdiiProject.setEndDate(format.parse("2016/01/01"));
		dmdiiProject.setProjectTitle(PROJECT_TITLE);
		dmdiiProject.setProjectSummary(PROJECT_SUMMARY);
		dmdiiProject.setPrincipalPointOfContact(user());
		dmdiiProject.setProjectFocusArea(projectFocusArea());
		dmdiiProject.setProjectThrust(projectThrust());
		dmdiiProject.setContributingCompanies(contributingCompanies);
		dmdiiProject.setRootNumber(2015);
		dmdiiProject.setCallNumber(07);
		dmdiiProject.setProjectNumber(04);
		dmdiiProject.setCostShare(new BigDecimal("50000"));
		dmdiiProject.setDmdiiFunding(new BigDecimal("5000"));
		
		return dmdiiProject;
	}
	
	public static DMDIIProject dmdiiProject2() throws Exception {
		DMDIIProject dmdiiProject = new DMDIIProject();
		
		List<DMDIIMember> contributingCompanies = new ArrayList<DMDIIMember>();
		contributingCompanies.add(dmdiiMember());
		
		dmdiiProject.setId(2002);
		dmdiiProject.setPrimeOrganization(dmdiiMember1());
		dmdiiProject.setPrincipalInvestigator(user());
		dmdiiProject.setProjectStatus(projectStatus());
		dmdiiProject.setAwardedDate(format.parse("2016/07/04"));
		dmdiiProject.setEndDate(format.parse("2017/01/01"));
		dmdiiProject.setProjectTitle(PROJECT_TITLE);
		dmdiiProject.setProjectSummary(PROJECT_SUMMARY);
		dmdiiProject.setPrincipalPointOfContact(user());
		dmdiiProject.setProjectFocusArea(projectFocusArea());
		dmdiiProject.setProjectThrust(projectThrust());
		dmdiiProject.setContributingCompanies(contributingCompanies);
		dmdiiProject.setRootNumber(2016);
		dmdiiProject.setCallNumber(07);
		dmdiiProject.setProjectNumber(04);
		dmdiiProject.setCostShare(new BigDecimal("75000"));
		dmdiiProject.setDmdiiFunding(new BigDecimal("7500"));
		
		return dmdiiProject;
	}

	public static DMDIIProjectThrust projectThrust() {
		DMDIIProjectThrust dmdiiProjectThrust = new DMDIIProjectThrust();
		
		dmdiiProjectThrust.setId(2000);
		dmdiiProjectThrust.setCode(THRUST_CODE);
		
		return dmdiiProjectThrust;
	}

	public static DMDIIProjectFocusArea projectFocusArea() {
		DMDIIProjectFocusArea dmdiiProjectFocusArea = new DMDIIProjectFocusArea();
		
		dmdiiProjectFocusArea.setId(2000);
		dmdiiProjectFocusArea.setName(FOCUS_AREA_NAME);
		
		return dmdiiProjectFocusArea;
	}

	public static DMDIIProjectStatus projectStatus() {
		DMDIIProjectStatus dmdiiProjectStatus = new DMDIIProjectStatus();
		
		dmdiiProjectStatus.setId(2000);
		dmdiiProjectStatus.setName(STATUS_NAME);
		
		return dmdiiProjectStatus;
	}

	public static User user() {
		User user = new User();
		
		user.setId(2000);
		user.setUsername(USER_NAME);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setEmail(EMAIL);
		user.setAddress(ADDRESS);
		user.setPhone(PHONE);
		user.setUserContactInfo(userContactInfo());
		
		return user;
	}

	public static UserContactInfo userContactInfo() {
		UserContactInfo userContactInfo = new UserContactInfo();
		
		userContactInfo.setId(2000);
		userContactInfo.setUserPrivateContactInfo(userPrivateContactInfo());
		userContactInfo.setUserPublicContactInfo(userPublicContactInfo());
		userContactInfo.setUserMemberPortalContactInfo(userMemberPortalContactInfo());
		
		return userContactInfo;
	}

	public static UserPublicContactInfo userPublicContactInfo() {
		UserPublicContactInfo publicContactInfo = new UserPublicContactInfo();
		publicContactInfo.setId(1);
		publicContactInfo.setEmail("public@email.com");
		publicContactInfo.setPhone("4444444444");
		publicContactInfo.setLocation("Ann Arbor, MI");
		return publicContactInfo;
	}

	public static UserPrivateContactInfo userPrivateContactInfo() {
		UserPrivateContactInfo privateContactInfo = new UserPrivateContactInfo();
		privateContactInfo.setId(1);
		privateContactInfo.setEmail("private@email.com");
		privateContactInfo.setPhone("4444444444");
		privateContactInfo.setLocation("Ann Arbor, MI");
		return privateContactInfo;
	}

	public static UserMemberPortalContactInfo userMemberPortalContactInfo() {
		UserMemberPortalContactInfo memberPortalContactInfo = new UserMemberPortalContactInfo();
		memberPortalContactInfo.setId(1);
		memberPortalContactInfo.setEmail("memberportal@email.com");
		memberPortalContactInfo.setPhone("4444444444");
		memberPortalContactInfo.setLocation("Ann Arbor, MI");
		return memberPortalContactInfo;
	}

	public static DMDIIMember dmdiiMember() throws Exception {
		DMDIIMember dmdiiMember = new DMDIIMember();

		List<DMDIIAreaOfExpertise> areasOfExpertise = new ArrayList<DMDIIAreaOfExpertise>();
		areasOfExpertise.add(dmdiiAreaOfExpertise());

		List<DMDIIMemberContact> contacts = new ArrayList<DMDIIMemberContact>();
		contacts.add(dmdiiMemberContact());

		List<DMDIIMemberCustomer> customers = new ArrayList<DMDIIMemberCustomer>();
		customers.add(dmdiiMemberCustomer());

		List<DMDIIMemberUser> users = new ArrayList<DMDIIMemberUser>();
		users.add(dmdiiMemberUser());

		dmdiiMember.setId(1000);
		dmdiiMember.setDmdiiType(dmdiiType());
		dmdiiMember.setOrganization(organization());
		dmdiiMember.setStartDate(new Date(format.parse("2015/11/25").getTime()));
		dmdiiMember.setExpireDate(new Date(format.parse("2017/11/25").getTime()));
		dmdiiMember.setAreasOfExpertise(areasOfExpertise);
		dmdiiMember.setDesiredAreasOfExpertise(areasOfExpertise);
		dmdiiMember.setContacts(contacts);
		dmdiiMember.setCustomers(customers);
		dmdiiMember.setUsers(users);
		return dmdiiMember;
	}

	public static DMDIIMember dmdiiMember1() throws Exception {
		DMDIIMember dmdiiMember = new DMDIIMember();

		List<DMDIIAreaOfExpertise> areasOfExpertise = new ArrayList<DMDIIAreaOfExpertise>();
		areasOfExpertise.add(dmdiiAreaOfExpertise());

		List<DMDIIMemberContact> contacts = new ArrayList<DMDIIMemberContact>();
		contacts.add(dmdiiMemberContact());

		List<DMDIIMemberCustomer> customers = new ArrayList<DMDIIMemberCustomer>();
		customers.add(dmdiiMemberCustomer());

		List<DMDIIMemberUser> users = new ArrayList<DMDIIMemberUser>();
		users.add(dmdiiMemberUser());

		dmdiiMember.setId(1001);
		dmdiiMember.setDmdiiType(dmdiiType());
		dmdiiMember.setOrganization(organization());
		dmdiiMember.setStartDate(new Date(format.parse("2015/11/25").getTime()));
		dmdiiMember.setExpireDate(new Date(format.parse("2017/11/25").getTime()));
		dmdiiMember.setAreasOfExpertise(areasOfExpertise);
		dmdiiMember.setDesiredAreasOfExpertise(areasOfExpertise);
		dmdiiMember.setContacts(contacts);
		dmdiiMember.setCustomers(customers);
		dmdiiMember.setUsers(users);
		return dmdiiMember;
	}

	public static DMDIIType dmdiiType() {
		DMDIIType dmdiiType = new DMDIIType();
		dmdiiType.setId(1);
		dmdiiType.setTier(1);
		dmdiiType.setDmdiiTypeCategory(dmdiiTypeCategory());
		return dmdiiType;
	}

	public static DMDIITypeCategory dmdiiTypeCategory() {
		DMDIITypeCategory dmdiiTypeCategory = new DMDIITypeCategory();
		dmdiiTypeCategory.setId(1);
		dmdiiTypeCategory.setCategory("Industry");
		return dmdiiTypeCategory;
	}

	public static Organization organization() {
		Organization organization = new Organization();
		organization.setId(1000);
		organization.setAccountId(1000);
		organization.setName("Test Company");
		organization.setLocation("Chicago, IL");
		organization.setDivision("Test Division");
		organization.setAddress(address());
		return organization;
	}

	public static Address address() {
		Address address = new Address();
		address.setId(1000);
		address.setStreetAddress1("123 Street st.");
		address.setStreetAddress2("Apt. 2");
		address.setCity("Chicago");
		address.setState("IL");
		address.setZip("45732");
		return address;
	}

	public static DMDIIAreaOfExpertise dmdiiAreaOfExpertise() {
		DMDIIAreaOfExpertise dmdiiAreaOfExpertise = new DMDIIAreaOfExpertise();
		dmdiiAreaOfExpertise.setId(1);
		dmdiiAreaOfExpertise.setName("Additive Manufacturing");
		return dmdiiAreaOfExpertise;
	}

	public static DMDIIMemberContact dmdiiMemberContact() throws Exception {
		DMDIIMemberContact dmdiiMemberContact = new DMDIIMemberContact();
		dmdiiMemberContact.setId(1);
		dmdiiMemberContact.setContactType(dmdiiContactType());
		dmdiiMemberContact.setFirstName("Thor");
		dmdiiMemberContact.setLastName("Odinson");
		dmdiiMemberContact.setEmail("todinson@gmail.com");
		return dmdiiMemberContact;
	}

	public static DMDIIContactType dmdiiContactType() {
		DMDIIContactType dmdiiContactType = new DMDIIContactType();
		dmdiiContactType.setId(1);
		dmdiiContactType.setType("primary point of contact");
		return dmdiiContactType;
	}

	public static DMDIIMemberCustomer dmdiiMemberCustomer() {
		DMDIIMemberCustomer dmdiiMemberCustomer = new DMDIIMemberCustomer();
		dmdiiMemberCustomer.setId(1);
		dmdiiMemberCustomer.setName("Google");
		dmdiiMemberCustomer.setLink("www.google.com");
		return dmdiiMemberCustomer;
	}

	public static DMDIIMemberFinance dmdiiMemberFinance() throws Exception {
		DMDIIMemberFinance dmdiiMemberFinance = new DMDIIMemberFinance();
		dmdiiMemberFinance.setId(1);
		dmdiiMemberFinance.setName("finance");
		dmdiiMemberFinance.setAssetUrl("some_url");
		// TODO add user here:
		return dmdiiMemberFinance;
	}

	public static DMDIIInstituteInvolvement dmdiiInstituteInvolvement() throws Exception {
		DMDIIInstituteInvolvement instituteInvolvement = new DMDIIInstituteInvolvement();
		instituteInvolvement.setId(1);
		instituteInvolvement.setStaticLineItem("some static line item");
		instituteInvolvement.setDate(new Date(format.parse("2016/05/25").getTime()));
		return instituteInvolvement;
	}

	public static DMDIISkill dmdiiSkill() {
		DMDIISkill skill = new DMDIISkill();
		skill.setId(1);
		skill.setTagLink("tag1");
		skill.setTagName("taglink1");
		return skill;
	}

	public static DMDIIMemberUser dmdiiMemberUser() {
		DMDIIMemberUser dmdiiMemberUser = new DMDIIMemberUser();
		dmdiiMemberUser.setId(1);
		dmdiiMemberUser.setUser(user());
		dmdiiMemberUser.setRole(dmdiiRole());
		return dmdiiMemberUser;
	}

	public static DMDIIRole dmdiiRole() {
		DMDIIRole dmdiiRole = new DMDIIRole();
		dmdiiRole.setId(1);
		dmdiiRole.setRole("Role");
		return dmdiiRole;
	}


}
