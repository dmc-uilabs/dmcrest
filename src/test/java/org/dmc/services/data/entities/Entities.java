package org.dmc.services.data.entities;

import org.apache.commons.lang3.RandomStringUtils;
import org.dmc.services.dmdiitype.DMDIIType;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class Entities {
	
	private static final Calendar today = GregorianCalendar.getInstance();
	private static final Random RANDOM = new Random();

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

	private static final String ADDRESS = "Address";
	private static final String EMAIL = "email@email.com";
	private static final String FIRST_NAME = "FirstName";
	private static final String FOCUS_AREA_NAME = "FocusAreaName";
	private static final String INDUSTRY = "Industry";
	private static final String LAST_NAME = "LastName";
	private static final String LOCATION = "Location";
	private static final String PHONE = "Phone";
	private static final String PROJECT_SUMMARY = "ProjectSummary";
	private static final String PROJECT_TITLE = "ProjectTitle";
	private static final String STATUS_NAME = "StatusName";
	private static final String THRUST_CODE = "ThrustCode";
	private static final String USER_NAME = "UserName";
	public static final String DOCUMENT_NAME = "DocumentName";
	public static final String DOCUMENT_URL = "https://test-final-verify.s3.amazonaws.com/ProjectOfDMDII/103552215657713056245%40google.com/Documents/1473359761-343968-sanitized-football.jpg?AWSAccessKeyId=AKIAJDE3BJULBHCYEX4Q&Expires=1475951762&Signature=p3U7tV%2Bk9rAx6jdNe5XGOzJz7ME%3D";
	public static final String TAG_NAME = "TagName";

	public static DMDIIProject dmdiiProject() throws Exception {
		DMDIIProject dmdiiProject = new DMDIIProject();

		List<DMDIIMember> contributingCompanies = new ArrayList<DMDIIMember>();
		contributingCompanies.add(dmdiiMember());

		//		dmdiiProject.setId(2000);
		dmdiiProject.setPrimeOrganization(dmdiiMember());
		dmdiiProject.setPrincipalInvestigator(dmdiiProjectContact1());
		dmdiiProject.setProjectStatus(projectStatus());
		dmdiiProject.setAwardedDate(format.parse("2016/07/04"));
		dmdiiProject.setEndDate(format.parse("2017/01/01"));
		dmdiiProject.setProjectTitle(PROJECT_TITLE);
		dmdiiProject.setProjectSummary(PROJECT_SUMMARY);
		dmdiiProject.setPrincipalPointOfContact(dmdiiProjectContact2());
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

		//		dmdiiProject.setId(2001);
		dmdiiProject.setPrimeOrganization(dmdiiMember());
		dmdiiProject.setPrincipalInvestigator(dmdiiProjectContact1());
		dmdiiProject.setProjectStatus(projectStatus());
		dmdiiProject.setAwardedDate(format.parse("2015/07/04"));
		dmdiiProject.setEndDate(format.parse("2016/01/01"));
		dmdiiProject.setProjectTitle(PROJECT_TITLE);
		dmdiiProject.setProjectSummary(PROJECT_SUMMARY);
		dmdiiProject.setPrincipalPointOfContact(dmdiiProjectContact2());
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

		//		dmdiiProject.setId(2002);
		dmdiiProject.setPrimeOrganization(dmdiiMember1());
		dmdiiProject.setPrincipalInvestigator(dmdiiProjectContact1());
		dmdiiProject.setProjectStatus(projectStatus());
		dmdiiProject.setAwardedDate(format.parse("2016/07/04"));
		dmdiiProject.setEndDate(format.parse("2017/01/01"));
		dmdiiProject.setProjectTitle(PROJECT_TITLE);
		dmdiiProject.setProjectSummary(PROJECT_SUMMARY);
		dmdiiProject.setPrincipalPointOfContact(dmdiiProjectContact2());
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

		//		dmdiiProjectThrust.setId(2000);
		dmdiiProjectThrust.setCode(THRUST_CODE);

		return dmdiiProjectThrust;
	}

	public static DMDIIProjectFocusArea projectFocusArea() {
		DMDIIProjectFocusArea dmdiiProjectFocusArea = new DMDIIProjectFocusArea();

		//		dmdiiProjectFocusArea.setId(2000);
		dmdiiProjectFocusArea.setName(FOCUS_AREA_NAME);

		return dmdiiProjectFocusArea;
	}

	public static DMDIIProjectStatus projectStatus() {
		DMDIIProjectStatus dmdiiProjectStatus = new DMDIIProjectStatus();

		//		dmdiiProjectStatus.setId(2000);
		dmdiiProjectStatus.setName(STATUS_NAME);

		return dmdiiProjectStatus;
	}

	public static User user() {
		User user = new User();

		user.setUsername(RandomStringUtils.randomAlphabetic(10));
		user.setTermsAndCondition(termsAndConditions());
		user.setPassword(RandomStringUtils.randomAlphabetic(10));
		user.setRealname(RandomStringUtils.randomAlphabetic(10));
		user.setTitle(RandomStringUtils.randomAlphabetic(10));
		user.setFirstName(RandomStringUtils.randomAlphabetic(10));
		user.setLastName(RandomStringUtils.randomAlphabetic(10));
		user.setEmail(RandomStringUtils.randomAlphabetic(10));
		user.setAddress(RandomStringUtils.randomAlphabetic(10));
		user.setPhone(RandomStringUtils.randomAlphabetic(10));
		user.setImage(RandomStringUtils.randomAlphabetic(10));
		user.setAboutMe(RandomStringUtils.randomAlphabetic(10));
		user.setResume(RandomStringUtils.randomAlphabetic(10));
		user.setAddDate(RANDOM.nextLong());
		//		user.setUserContactInfo(userContactInfo());

		//		final OrganizationUser organizationUser = organizationUser(organization(), user);
		//		user.setOrganizationUser(organizationUser);
		//		user.setRoles(userRoleAssignments(organizationUser));

		return user;
	}

	public static User user(UserContactInfo userContactInfo) {
		User user = user();
		user.setUserContactInfo(userContactInfo);
		return user;
	}

	public static OrganizationUser organizationUser(Organization organization, User user) {
		OrganizationUser ou = new OrganizationUser();
		ou.setUser(user);
		ou.setOrganization(organization);
		ou.setIsVerified(true);
		return ou;
	}

	private static List<UserRoleAssignment> userRoleAssignments(OrganizationUser organizationUser) {
		List<UserRoleAssignment> roles = new ArrayList<>();
		roles.add(userRoleAssignment(organizationUser));
		return roles;
	}

	private static UserRoleAssignment userRoleAssignment(OrganizationUser organizationUser) {
		UserRoleAssignment assignment = new UserRoleAssignment();
		assignment.setUser(organizationUser.getUser());
		assignment.setOrganization(organizationUser.getOrganization());
		assignment.setRole(role());
		return assignment;
	}

	private static Role role() {
		Role role = new Role();
		role.setRole(RandomStringUtils.randomAlphabetic(5));
		return role;
	}

	private static java.util.Date termsAndConditions() {
		LocalDate localDate = LocalDate.now().minusDays(RANDOM.nextInt(100));
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static UserContactInfo userContactInfo() {
		UserContactInfo userContactInfo = new UserContactInfo();

		//		userContactInfo.setId(2000);
		userContactInfo.setUserPrivateContactInfo(userPrivateContactInfo());
		userContactInfo.setUserPublicContactInfo(userPublicContactInfo());
		userContactInfo.setUserMemberPortalContactInfo(userMemberPortalContactInfo());

		return userContactInfo;
	}

	public static UserPublicContactInfo userPublicContactInfo() {
		UserPublicContactInfo publicContactInfo = new UserPublicContactInfo();
		//		publicContactInfo.setId(1);
		publicContactInfo.setEmail("public@email.com");
		publicContactInfo.setPhone("4444444444");
		publicContactInfo.setLocation("Ann Arbor, MI");
		return publicContactInfo;
	}

	public static UserPrivateContactInfo userPrivateContactInfo() {
		UserPrivateContactInfo privateContactInfo = new UserPrivateContactInfo();
		//		privateContactInfo.setId(1);
		privateContactInfo.setEmail("private@email.com");
		privateContactInfo.setPhone("4444444444");
		privateContactInfo.setLocation("Ann Arbor, MI");
		return privateContactInfo;
	}

	public static UserMemberPortalContactInfo userMemberPortalContactInfo() {
		UserMemberPortalContactInfo memberPortalContactInfo = new UserMemberPortalContactInfo();
		//		memberPortalContactInfo.setId(1);
		memberPortalContactInfo.setEmail("memberportal@email.com");
		memberPortalContactInfo.setPhone("4444444444");
		memberPortalContactInfo.setLocation("Ann Arbor, MI");
		return memberPortalContactInfo;
	}

	public static DMDIIMember dmdiiMember() throws ParseException {
		DMDIIMember dmdiiMember = new DMDIIMember();

		List<DMDIIMemberUser> users = new ArrayList<DMDIIMemberUser>();
		users.add(dmdiiMemberUser());

		dmdiiMember.setDmdiiType(dmdiiType());
		dmdiiMember.setOrganization(organization());
		dmdiiMember.setStartDate(new Date(format.parse("2015/11/25").getTime()));
		dmdiiMember.setExpireDate(new Date(format.parse("2017/11/25").getTime()));
		return dmdiiMember;
	}

	public static DMDIIMember dmdiiMember1() throws Exception {
		DMDIIMember dmdiiMember = new DMDIIMember();

		List<DMDIIMemberContact> contacts = new ArrayList<DMDIIMemberContact>();
		//		contacts.add(dmdiiMemberContact());

		dmdiiMember.setDmdiiType(dmdiiType());
		dmdiiMember.setOrganization(organization());
		dmdiiMember.setStartDate(new Date(format.parse("2015/11/25").getTime()));
		dmdiiMember.setExpireDate(new Date(format.parse("2017/11/25").getTime()));
		dmdiiMember.setContacts(contacts);
		return dmdiiMember;
	}

	public static DMDIIMember dmdiiMember(Organization org, DMDIIType dmdiiType, DMDIIMemberUser dmdiiMemberUser)
			throws ParseException {
		List<DMDIIMemberUser> users = new ArrayList<DMDIIMemberUser>();
		users.add(dmdiiMemberUser);

		final DMDIIMember dmdiiMember = dmdiiMember();
		dmdiiMember.setOrganization(org);
		dmdiiMember.setDmdiiType(dmdiiType);
		dmdiiMember.setStartDate(getDateOneMonthAgo());
		dmdiiMember.setExpireDate(getDateOneMonthInFuture());
		return dmdiiMember;
	}

	public static DMDIIType dmdiiType() {
		DMDIIType dmdiiType = new DMDIIType();
		//		dmdiiType.setId(1);
		dmdiiType.setTier(1);
		dmdiiType.setDmdiiTypeCategory(dmdiiTypeCategory());
		return dmdiiType;
	}

	public static DMDIIType dmdiiType(DMDIITypeCategory dmdiiTypeCategory) {
		DMDIIType dmdiiType = dmdiiType();
		dmdiiType.setDmdiiTypeCategory(dmdiiTypeCategory);
		return dmdiiType;
	}

	public static DMDIITypeCategory dmdiiTypeCategory() {
		DMDIITypeCategory dmdiiTypeCategory = new DMDIITypeCategory();
		//		dmdiiTypeCategory.setId(1);
		dmdiiTypeCategory.setCategory("Industry");
		return dmdiiTypeCategory;
	}

	public static Organization organization() {
		Organization o = new Organization();
		o.setAccountId(RANDOM.nextInt());
		o.setName(RandomStringUtils.randomAlphabetic(10));
		o.setLocation(RandomStringUtils.randomAlphabetic(10));
		o.setDescription(RandomStringUtils.randomAlphabetic(10));
		o.setDivision(RandomStringUtils.randomAlphabetic(10));
		o.setIndustry(RandomStringUtils.randomAlphabetic(10));
		o.setNaicsCode(RandomStringUtils.randomAlphabetic(10));
		o.setRdFocus(RandomStringUtils.randomAlphabetic(10));
		o.setCustomers(RandomStringUtils.randomAlphabetic(10));
		o.setTechExpertise(RandomStringUtils.randomAlphabetic(10));
		o.setToolsSoftwareEquipMach(RandomStringUtils.randomAlphabetic(10));
		o.setPostCollaboration(RandomStringUtils.randomAlphabetic(10));
		o.setCollaborationInterest(RandomStringUtils.randomAlphabetic(10));
		o.setPastProjects(RandomStringUtils.randomAlphabetic(10));
		o.setUpcomingProjectInterests(RandomStringUtils.randomAlphabetic(10));
		o.setEmail(RandomStringUtils.randomAlphabetic(10));
		o.setPhone(RandomStringUtils.randomAlphabetic(10));
		o.setWebsite(RandomStringUtils.randomAlphabetic(10));
		o.setSocialMediaLinkedin(RandomStringUtils.randomAlphabetic(10));
		o.setSocialMediaTwitter(RandomStringUtils.randomAlphabetic(10));
		o.setSocialMediaInthenews(RandomStringUtils.randomAlphabetic(10));
		o.setPerferedCommMethod(RandomStringUtils.randomAlphabetic(10));
		o.setCategoryTier(RANDOM.nextInt());
		o.setReasonJoining(RandomStringUtils.randomAlphabetic(10));
		o.setFeatureImage(RANDOM.nextInt());
		o.setLogoImage(RandomStringUtils.randomAlphabetic(10));
		o.setFollow(RandomStringUtils.randomAlphabetic(10));
		o.setFavoritesCount(RANDOM.nextInt());
		o.setIsOwner(RandomStringUtils.randomAlphabetic(10));
		o.setOwner(RandomStringUtils.randomAlphabetic(10));
		return o;
	}

	public static Address address() {
		Address address = new Address();
		//		address.setId(1000);
		address.setStreetAddress1("123 Street st.");
		address.setStreetAddress2("Apt. 2");
		address.setCity("Chicago");
		address.setState("IL");
		address.setZip("45732");
		return address;
	}

	public static DMDIIMemberContact dmdiiMemberContact() {
		DMDIIMemberContact dmdiiMemberContact = new DMDIIMemberContact();
		//		dmdiiMemberContact.setId(1);
		dmdiiMemberContact.setContactType(dmdiiContactType());
		dmdiiMemberContact.setFirstName("Thor");
		dmdiiMemberContact.setLastName("Odinson");
		dmdiiMemberContact.setEmail("todinson@gmail.com");
		return dmdiiMemberContact;
	}

	public static DMDIIContactType dmdiiContactType() {
		DMDIIContactType dmdiiContactType = new DMDIIContactType();
		//		dmdiiContactType.setId(1);
		dmdiiContactType.setType("primary point of contact");
		return dmdiiContactType;
	}

	public static DMDIIMemberFinance dmdiiMemberFinance() {
		DMDIIMemberFinance dmdiiMemberFinance = new DMDIIMemberFinance();
		//		dmdiiMemberFinance.setId(1);
		dmdiiMemberFinance.setName("finance");
		dmdiiMemberFinance.setAssetUrl("some_url");
		// TODO add user here:
		return dmdiiMemberFinance;
	}

	public static DMDIIMemberUser dmdiiMemberUser() {
		DMDIIMemberUser dmdiiMemberUser = new DMDIIMemberUser();
		//		dmdiiMemberUser.setId(1);
		dmdiiMemberUser.setUser(user());
		dmdiiMemberUser.setRole(dmdiiRole());
		return dmdiiMemberUser;
	}

	public static DMDIIMemberUser dmdiiMemberUser(DMDIIMember dmdiiMember, User user, DMDIIRole dmdiiRole) {
		DMDIIMemberUser dmdiiMemberUser = new DMDIIMemberUser();
		dmdiiMemberUser.setDmdiiMember(dmdiiMember);
		dmdiiMemberUser.setUser(user);
		dmdiiMemberUser.setRole(dmdiiRole);
		return dmdiiMemberUser;
	}

	public static DMDIIRole dmdiiRole() {
		DMDIIRole dmdiiRole = new DMDIIRole();
		//		dmdiiRole.setId(1);
		dmdiiRole.setRole(RandomStringUtils.randomAlphabetic(6));
		return dmdiiRole;
	}

	public static DMDIIProjectContact dmdiiProjectContact1() {
		DMDIIProjectContact contact = new DMDIIProjectContact();
		//		contact.setId(200);
		contact.setFirstName(FIRST_NAME);
		contact.setLastName(LAST_NAME);
		contact.setEmail(EMAIL);
		return contact;
	}

	public static DMDIIProjectContact dmdiiProjectContact2() {
		DMDIIProjectContact contact = new DMDIIProjectContact();
		//		contact.setId(201);
		contact.setFirstName(FIRST_NAME + "2");
		contact.setLastName(LAST_NAME + "2");
		contact.setEmail(EMAIL + "2");
		return contact;
	}

	public static OnboardingStatus onboardingStatus() {
		OnboardingStatus status = new OnboardingStatus();
		status.setAccount(false);
		status.setCompany(false);
		status.setProfile(false);
		status.setStorefront(false);
		return status;
	}

	public static OnboardingStatus onboardingStatus(Integer userId) {
		OnboardingStatus status = onboardingStatus();
		status.setId(userId);
		return status;
	}

	private static Date getDateOneMonthAgo() {
		return getSomeOtherDate(Calendar.MONTH, -1);
	}

	private static Date getDateOneMonthInFuture() {
		return getSomeOtherDate(Calendar.MONTH, 1);
	}

	private static Date getSomeOtherDate(int field, int amount) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(field, amount);
		return calendar.getTime();
	}
	
	public static DMDIIDocumentTag dmdiiDocumentTag() {
		DMDIIDocumentTag dmdiiDocumentTag = new DMDIIDocumentTag();
		dmdiiDocumentTag.setId(1000);
		dmdiiDocumentTag.setTagName(TAG_NAME);
		return dmdiiDocumentTag;
	}

	public static DMDIIDocument dmdiiDocument() throws Exception {
		DMDIIDocument dmdiiDocument = new DMDIIDocument();
		
		List<DMDIIDocumentTag> tagList = new ArrayList<>();
		tagList.add(dmdiiDocumentTag());
		
		dmdiiDocument.setId(1000);
		dmdiiDocument.setDocumentName(DOCUMENT_NAME);
		dmdiiDocument.setDocumentUrl(DOCUMENT_URL);
		dmdiiDocument.setDmdiiProject(dmdiiProject());
		dmdiiDocument.setOwner(user());
		dmdiiDocument.setTags(tagList);
		dmdiiDocument.setModified(new Date());
		dmdiiDocument.setExpires(new Timestamp(System.currentTimeMillis()));
		dmdiiDocument.setIsDeleted(false);
		dmdiiDocument.setAccessLevel(DMDIIProjectItemAccessLevel.PROJECT_PARTICIPANT_VIPS);
		dmdiiDocument.setFileType(2);
		dmdiiDocument.setVerified(true);
		
		return dmdiiDocument;
	}
	
	public static DocumentTag documentTag() {
		DocumentTag documentTag = new DocumentTag();
		documentTag.setId(1000);
		documentTag.setTagName(TAG_NAME);
		return documentTag;
	}
	
	public static Document document() throws Exception {
		Document document = new Document();
		
		List<DocumentTag> tagList = new ArrayList<>();
		tagList.add(documentTag());
		
		document.setId(1000);
		document.setDocumentName(DOCUMENT_NAME);
		document.setDocumentUrl(DOCUMENT_URL);
		document.setParentType(DocumentParentType.ORGANIZATION);
		document.setParentId(1000);
		document.setOwner(user());
		document.setTags(tagList);
		document.setModified(new Timestamp(System.currentTimeMillis()));
		document.setExpires(new Timestamp(getDateOneMonthInFuture().getTime()));
		document.setIsDeleted(false);
		document.setDocClass(DocumentClass.LOGO);
		document.setVerified(true);
		
		return document;
	}
}
