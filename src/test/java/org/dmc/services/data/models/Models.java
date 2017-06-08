package org.dmc.services.data.models;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.dmc.services.data.entities.DocumentClass;
import org.dmc.services.data.entities.DocumentParentType;

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
	public static final String DOCUMENT_NAME = "DocumentName";
	public static final String DOCUMENT_URL = "https://test-final-verify.s3.amazonaws.com/ProjectOfDMDII/103552215657713056245%40google.com/Documents/1473359761-343968-sanitized-football.jpg?AWSAccessKeyId=AKIAJDE3BJULBHCYEX4Q&Expires=1475951762&Signature=p3U7tV%2Bk9rAx6jdNe5XGOzJz7ME%3D";
	public static final String TAG_NAME = "TagName";
	public static final String ACCESS_LEVEL = "AccessLevel";
	public static final String PARENT_TYPE = "ParentType";

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public static DMDIIProjectModel dmdiiProjectModel() throws Exception {
		DMDIIProjectModel dmdiiProjectModel = new DMDIIProjectModel();

		List<Integer> contributingCompanyIds = new ArrayList<Integer>();
		contributingCompanyIds.add(dmdiiMemberModel().getId());

		dmdiiProjectModel.setId(2000);
		dmdiiProjectModel.setPrimeOrganization(new DMDIIPrimeOrganizationModel(dmdiiMemberModel().getId(), dmdiiMemberModel().getOrganization().getName()));
		dmdiiProjectModel.setPrincipalInvestigator(dmdiiProjectContact1());
		dmdiiProjectModel.setProjectStatus(projectStatus());
		dmdiiProjectModel.setAwardedDate("2016/07/01");
		dmdiiProjectModel.setEndDate("2017/01/01");
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

		List<AreaOfExpertiseModel> areasOfExpertise = new ArrayList<AreaOfExpertiseModel>();
		areasOfExpertise.add(dmdiiAreaOfExpertise());

		List<DMDIIMemberContactModel> contacts = new ArrayList<DMDIIMemberContactModel>();
		contacts.add(dmdiiMemberContact());

		dmdiiMember.setId(1000);
		dmdiiMember.setDmdiiType(dmdiiType());
		dmdiiMember.setOrganization(organization());
		dmdiiMember.setStartDate("2015-11-25");
		dmdiiMember.setExpireDate("2017-11-25");
		dmdiiMember.setContacts(contacts);
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

	public static AreaOfExpertiseModel dmdiiAreaOfExpertise() {
		AreaOfExpertiseModel dmdiiAreaOfExpertise = new AreaOfExpertiseModel();
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

	public static DMDIIMemberFinanceModel dmdiiMemberFinance() throws Exception {
		DMDIIMemberFinanceModel dmdiiMemberFinance = new DMDIIMemberFinanceModel();
		dmdiiMemberFinance.setId(1);
		dmdiiMemberFinance.setName("finance");
		dmdiiMemberFinance.setAssetUrl("some_url");
		// TODO add user here:
		return dmdiiMemberFinance;
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
		user.setDisplayName(RandomStringUtils.randomAlphanumeric(10));
		user.setRealname(RandomStringUtils.randomAlphanumeric(10));
		user.setTitle(RandomStringUtils.randomAlphanumeric(10));
		user.setTermsConditions(true);
		user.setFirstName(RandomStringUtils.randomAlphanumeric(10));
		user.setLastName(RandomStringUtils.randomAlphanumeric(10));
		user.setEmail(RandomStringUtils.randomAlphanumeric(10));
		user.setAddress(RandomStringUtils.randomAlphanumeric(10));
		user.setPhone(RandomStringUtils.randomAlphanumeric(10));
		user.setImage(RandomStringUtils.randomAlphanumeric(10));
		user.setAboutMe(RandomStringUtils.randomAlphanumeric(10));
		user.setResume(RandomStringUtils.randomAlphanumeric(10));
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

	public static DMDIIProjectContactModel dmdiiProjectContact1() {
		DMDIIProjectContactModel contact = new DMDIIProjectContactModel();
		contact.setId(200);
		contact.setFirstName(FIRST_NAME);
		contact.setLastName(LAST_NAME);
		contact.setEmail(EMAIL);
		return contact;
	}

	public static DMDIIProjectContactModel dmdiiProjectContact2() {
		DMDIIProjectContactModel contact = new DMDIIProjectContactModel();
		contact.setId(201);
		contact.setFirstName(FIRST_NAME + "2");
		contact.setLastName(LAST_NAME + "2");
		contact.setEmail(EMAIL + "2");
		return contact;
	}

	public static DMDIIDocumentTagModel dmdiiDocumentTagModel() {
		DMDIIDocumentTagModel dmdiiDocumentTagModel = new DMDIIDocumentTagModel();
		dmdiiDocumentTagModel.setId(1000);
		dmdiiDocumentTagModel.setTagName(TAG_NAME);
		return dmdiiDocumentTagModel;
	}

	public static DMDIIDocumentModel dmdiiDocumentModel() {
		DMDIIDocumentModel dmdiiDocumentModel = new DMDIIDocumentModel();

		List<DMDIIDocumentTagModel> tags = new ArrayList<>();
		tags.add(dmdiiDocumentTagModel());

		dmdiiDocumentModel.setId(1000);
		dmdiiDocumentModel.setDocumentName(DOCUMENT_NAME);
		dmdiiDocumentModel.setDocumentUrl(DOCUMENT_URL);
		dmdiiDocumentModel.setDmdiiProjectId(1000);
		dmdiiDocumentModel.setOwnerId(1000);
		dmdiiDocumentModel.setTags(tags);
		dmdiiDocumentModel.setModified(new Date());
		dmdiiDocumentModel.setExpires(new Date());
		dmdiiDocumentModel.setAccessLevel(ACCESS_LEVEL);
		dmdiiDocumentModel.setFileType(2);
		dmdiiDocumentModel.setVerified(true);

		return dmdiiDocumentModel;
	}

	public static DocumentTagModel documentTagModel() {
		DocumentTagModel documentTagModel = new DocumentTagModel();
		documentTagModel.setId(1000);
		documentTagModel.setTagName(TAG_NAME);
		return documentTagModel;
	}

	public static DocumentModel documentModel() {
		DocumentModel documentModel = new DocumentModel();

		List<DocumentTagModel> tags = new ArrayList<>();
		tags.add(documentTagModel());

		documentModel.setId(1000);
		documentModel.setDocumentName(DOCUMENT_NAME);
		documentModel.setDocumentUrl(DOCUMENT_URL);
		documentModel.setParentType(DocumentParentType.ORGANIZATION);
		documentModel.setParentId(1000);
		documentModel.setOwnerId(1000);
		documentModel.setTags(tags);
		documentModel.setModified(new Date());
		documentModel.setExpires(new Date());
		documentModel.setDocClass(DocumentClass.LOGO);

		return documentModel;
	}
}
