package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.dmc.services.company.Company;
import org.dmc.services.company.CompanyImage;
import org.dmc.services.company.CompanyReview;
import org.dmc.services.company.CompanyReviewFlagged;
import org.dmc.services.company.CompanyReviewHelpful;
import org.dmc.services.company.CompanySkillImage;
import org.dmc.services.company.CompanyVideo;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.users.User;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CompanyIT extends BaseIT {

	private final String logTag = CompanyIT.class.getName();

	// company
	private static final String COMPANY_GET_RESOURCE = "/companies/{id}";
	private static final String COMPANY_CREATE_RESOURCE = "/companies/create";
	private static final String COMPANY_UPDATE_RESOURCE = "/companies/{id}";
	private static final String COMPANY_DELETE_RESOURCE = "/companies/{id}/delete";
	private static final String ALL_COMPANY_GET_RESOURCE = "/companies";
	private static final String COMPANY_GET_MEMBERS = "/companies/{companyID}/company_members";
	private static final String COMPANY_ADD_MEMBER_RESOURCE = "/companies/{id}/member/{userId}";

	// skills
	private static final String COMPANY_CREATE_SKILLS = "/company_skills";
	private static final String COMPANY_GET_SKILLS = "/companies/{companyID}/company_skills";

	// videos
	private static final String COMPANY_VIDEOS_GET_RESOURCE = "/companies/{companyID}/company_videos";
	private static final String COMPANY_VIDEO_CREATE_RESOURCE = "/company_videos";
	private static final String COMPANY_VIDEO_UPDATE_RESOURCE = "/company_videos/{id}";
	private static final String COMPANY_VIDEO_DELETE_RESOURCE = "/company_videos/{id}";

	private ArrayList<CompanyVideo> videos = null;
	private Integer createdId = null;
	private String imageId = "1";
	private String reviewId = "1";
	private String accountId = "1";
	private String helpfulId = "1";
	private String order = "1";
	private String sort = "1";
	private String limit = "1";
	private String companyId = "1";
	private String followed_companyId = "1";
	private String position = "1";
	private String company_featuredId = "1";
	private String serviceId = "1";
	String randomEPPN = "fforgeadmin";
	private String randomNonOwnerEPPN;

	@Before
	public void testCompanyCreate() {
		String json = createFixture();
		ServiceLogger.log(logTag, "Creating company");
		this.createdId =
			given().
				body(json).
				header("AJP_eppn", randomEPPN).
				header("Content-type", "application/json").
			expect().
				statusCode(200).
			when().
				post(COMPANY_CREATE_RESOURCE).
			then().
				body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).
			extract().
				path("id");
		ServiceLogger.log(logTag, "Company created " + this.createdId);
		if (randomNonOwnerEPPN == null) {
			randomNonOwnerEPPN = TestUserUtil.createNewUser();
		}
	}

	@Test
	public void testCompaniesGetJSON() {
		if (this.createdId != null) {
			given().header("Content-type", "application/json").header("AJP_eppn", randomEPPN).expect().statusCode(200)
					.when().get(ALL_COMPANY_GET_RESOURCE).then()
					.body(matchesJsonSchemaInClasspath("Schemas/companiesSchema.json")); // checks
																							// JSON
																							// syntax
		} else {
			assertTrue("Could not create new user", false);
		}
	}

	@Test
	public void testCompaniesGet() {
		if (this.createdId != null) {
		    Integer saveCreatedId = createdId;
			ArrayList<Company> orginalCompanyList = given().header("Content-type", "application/json")
					.header("AJP_eppn", randomEPPN).expect().statusCode(200).when().get(ALL_COMPANY_GET_RESOURCE)
					.as(ArrayList.class);

			// add one company
			testCompanyCreate();

			ArrayList<Company> newCompanyList = given().header("Content-type", "application/json")
					.header("AJP_eppn", randomEPPN).expect().statusCode(200).when().get(ALL_COMPANY_GET_RESOURCE)
					.as(ArrayList.class);

			assertTrue("", newCompanyList.size() == orginalCompanyList.size() + 1);
			testCompanyDelete();

			this.createdId = saveCreatedId;
		} else {
			assertTrue("Could not create new company", false);
		}
	}

	@Test
	public void testCompanyGet() {
		if (this.createdId != null) {
			Company returnedCompany =
			given().
				header("Content-type", "application/json").
				header("AJP_eppn", randomEPPN).
			expect().
				statusCode(200).  //HttpStatus.OK.value()
			when().
				get(COMPANY_GET_RESOURCE, this.createdId.toString()).
				as(Company.class);
		}
	}

	@Test
	public void testCompanyUpdate() {
		String json = updateFixture();
		given().
			body(json).
			header("AJP_eppn", randomEPPN).
			header("Content-type", "application/json").
		expect().
			statusCode(200).
		when().
			patch(COMPANY_UPDATE_RESOURCE, this.createdId.toString()).
		then().
			body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
	}

	@Test
	public void testCompanyUpdateNotOwner() {
		String json = updateFixture();
		given().
			body(json).
			header("AJP_eppn", randomNonOwnerEPPN).
			header("Content-type", "application/json").
		expect().
			statusCode(403).
		when().
			patch(COMPANY_UPDATE_RESOURCE, this.createdId.toString());
	}

	@Test
	public void testCompanyVideoCreate() {

		if (this.createdId != null) {
			JSONObject json = new JSONObject();
			json.put("title", "test video title");
			json.put("link", "test video link");
			json.put("companyId", this.createdId);

			given().header("Content-type", "application/json").header("AJP_eppn", randomEPPN).body(json.toString())
					.expect().statusCode(200).when().post(COMPANY_VIDEO_CREATE_RESOURCE).then()
					.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).extract().path("id");
		}

	}

	@Test
	public void testCompanyVideoUpdate() {

		Integer videoId = null;
		testCompanyVideosGet();
		if (this.videos != null && this.videos.size() > 0) {
			videoId = this.videos.get(0).getId();
			JSONObject json = new JSONObject();
			json.put("title", "test video title update");
			json.put("link", "test video link update");
			json.put("companyId", this.createdId);

			given().header("Content-type", "application/json").header("AJP_eppn", randomEPPN).body(json.toString())
					.expect().statusCode(200).when().patch(COMPANY_VIDEO_UPDATE_RESOURCE, videoId).then()
					.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).extract().path("id");
		}
	}

	@Test
	public void testCompanyVideoUpdateNotOwner() {

		Integer videoId = null;
		testCompanyVideosGet();
		if (this.videos != null && this.videos.size() > 0) {
			videoId = this.videos.get(0).getId();
			JSONObject json = new JSONObject();
			json.put("title", "test video title update");
			json.put("link", "test video link update");
			json.put("companyId", this.createdId);

			given().header("Content-type", "application/json").header("AJP_eppn", randomNonOwnerEPPN)
					.body(json.toString()).expect().statusCode(403).when()
					.patch(COMPANY_VIDEO_UPDATE_RESOURCE, videoId);
		}
	}

	@Test
	public void testCompanyVideosGet() {

		ObjectMapper mapper = new ObjectMapper();

		if (this.createdId != null) {
			testCompanyVideoCreate();
			JsonNode vs = given().header("Content-type", "application/json").header("AJP_eppn", randomEPPN).expect()
					.statusCode(200).when().get(COMPANY_VIDEOS_GET_RESOURCE, this.createdId).as(JsonNode.class);

			try {
				this.videos = mapper.readValue(mapper.treeAsTokens(vs), new TypeReference<ArrayList<CompanyVideo>>() {
				});
			} catch (Exception e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
		}
	}

	@Test
	public void testCompanyVideoDelete() {
		if (this.createdId != null) {
			testCompanyVideosGet();
			if (this.videos != null && this.videos.size() > 0) {
				int videoId = this.videos.get(0).getId();
				given().header("Content-type", "application/json").header("AJP_eppn", randomEPPN).expect()
						.statusCode(200).when().delete(COMPANY_VIDEO_DELETE_RESOURCE, videoId).then()
						.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
			}
		}
	}

	@After
	public void testCompanyDelete() {
		given().header("Content-type", "application/json").header("AJP_eppn", randomEPPN).expect().statusCode(200)
				.when().delete(COMPANY_DELETE_RESOURCE, this.createdId.toString()).then()
				.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
	}

	public String createFixture() {
		Company company = new Company();
		
//		company.setId(Integer.toString(id));
		company.setAccountId(Integer.toString(1001));
		company.setName("test name");
		company.setLocation("test location");
		company.setDescription("test description");
		company.setDivision("test division");
		company.setIndustry("test industry");
		company.setNAICSCode("test NAICSCode");
		company.setRDFocus("test RDFocus");
		company.setCustomers("test customers");
		company.setAwardsReceived("test awardsReceived");
		company.setTechnicalExpertise("test technicalExpertise");
		company.setToolsSoftwareEquipmentMachines("test toolsSoftwareEquipmentMachines");
		company.setPastCollaborations("test postCollaborations");
		company.setPastProjects("test pastProjects");
		company.setUpcomingProjectInterests("test upcomingProjectInterests");
		company.setCollaborationInterests("test collaborationInterests");
		company.setAddress("test address , test address");
		company.setCity("test city");

		// Todo:				company.setState(state);
// 		json.put("state", "test state");

		company.setZipCode("test zipCode");
		company.setTwitter("test twitter");
		company.setLinkedIn("test linkedIn");
		company.setWebsite("test website");
		company.setMethodCommunication("test methodCommunication");
		company.setEmail("test email");
		company.setPhone("test phone");
		company.setCategoryTier(30);
		company.setDateJoined("test dateJoined");
		company.setReasonJoining("test reasonJoining");
		company.setFeatureImage(new FeatureImage("feature_image_thumb.jpg", "feature_image_large.jpg"));
		company.setLogoImage("test logoImage");
		company.setFollow(true);
		company.setFavoritesCount(1002);
		company.setIsOwner(false);
		company.setOwner("test owner");
		
		ObjectMapper mapper = new ObjectMapper();
		String companyJSONString = null;
		try {
			companyJSONString = mapper.writeValueAsString(company);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return companyJSONString;
	}

	public String updateFixture() {

		Company company = new Company();
		
		//		company.setId(Integer.toString(id));
		company.setAccountId(Integer.toString(1001));
		company.setName("test updated name");
		company.setLocation("test updated location");
		company.setDescription("test updated description");
		company.setDivision("test updated division");
		company.setIndustry("test updated industry");
		company.setNAICSCode("test updated NAICSCode");
		company.setRDFocus("test updated RDFocus");
		company.setCustomers("test updated customers");
		company.setAwardsReceived("test updated awardsReceived");
		company.setTechnicalExpertise("test updated technicalExpertise");
		company.setToolsSoftwareEquipmentMachines("test updated toolsSoftwareEquipmentMachines");
		company.setPastCollaborations("test updated postCollaborations");
		company.setPastProjects("test updated pastProjects");
		company.setUpcomingProjectInterests("test updated upcomingProjectInterests");
		company.setCollaborationInterests("test updated collaborationInterests");
		company.setAddress("test updated address");
		company.setCity("test updated city");
		
		// Todo:				company.setState(state);
		// 		json.put("state", "test updated state");
		
		company.setZipCode("test updated zipCode");
		company.setTwitter("test updated twitter");
		company.setLinkedIn("test updated linkedIn");
		company.setWebsite("test updated website");
		company.setMethodCommunication("test updated methodCommunication");
		company.setEmail("test updated email");
		company.setPhone("test updated phone");
		company.setCategoryTier(30);
		company.setDateJoined("test updated dateJoined");
		company.setReasonJoining("test updated reasonJoining");
		company.setFeatureImage(new FeatureImage("feature_image_thumb_updated.jpg", "feature_image_large_updated.jpg"));
		company.setLogoImage("test updated logoImage");
		company.setFollow(true);
		company.setFavoritesCount(1002);
		company.setIsOwner(false);
		company.setOwner("test updated owner");
		
		ObjectMapper mapper = new ObjectMapper();
		String companyJSONString = null;
		try {
			companyJSONString = mapper.writeValueAsString(company);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return companyJSONString;
	}

	public JSONArray createSkills() {

		JSONArray skills = new JSONArray();
		JSONObject skill = new JSONObject();
		int org_id = 1; // int org_id = this.createId();
		skill.put("id", 1);
		// skill.put("companyId", this.createdId);
		skill.put("companyId", org_id);
		skill.put("skill", "Company Skill new test");
		skills.put(skill);
		JSONObject skill2 = new JSONObject();
		skill2.put("id", 2);
		// skill2.put("companyId", this.createdId);
		skill.put("companyId", org_id);
		skill2.put("skill", "Company Skill new test");
		skills.put(skill2);
		return skills;
	}

	@Test
	public void testCompanyGetMembers() {

		// Add 2 members to the company
		String user1 = addMember(randomEPPN, this.createdId);
		String user2 = addMember(randomEPPN, this.createdId);

		// Test get all members using the first member id
		String user1_eppn = "userEPPN" + user1;

		ArrayList<User> allUsers =
		given().
			param("companyID", this.createdId.toString()).
			header("AJP_eppn", user1_eppn).
		expect().
			statusCode(200).
		when().
			get(COMPANY_GET_MEMBERS, this.createdId.toString()).
		andReturn().
			as(ArrayList.class);

		int numUsers = 3;  // 3rd user is the one who created the company
		Assert.assertTrue("Company members list cannot be null", allUsers != null);
		Assert.assertTrue("Expected " + numUsers + " company members", allUsers.size() == numUsers);

	}

	@Test
	public void testCompanyGetMembersNonMember() {

		// Add 2 members to the company
		String user1 = addMember(randomEPPN, this.createdId);
		String user2 = addMember(randomEPPN, this.createdId);

		String nonMemberEPPN = "testUser";

		// testUser not a member of company, so expect to get 401 response
		// (UNAUTHORIZED)
		given().param("companyID", this.createdId.toString()).header("AJP_eppn", nonMemberEPPN).expect().statusCode(401)
				.when().get(COMPANY_GET_MEMBERS, this.createdId.toString());
	}

	public static int createUser(String userEPPN, String userGivenName, String userSurName, String userDisplayName,
			String userEmail) {

		Integer id = given().header("Content-type", "text/plain").header("AJP_eppn", userEPPN)
				.header("AJP_givenName", userGivenName).header("AJP_sn", userSurName)
				.header("AJP_displayName", userDisplayName).header("AJP_mail", userEmail).expect().statusCode(200)
				.when().post("/users/create").then().body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
				.extract().path("id");

		// check return value > 0
		assertTrue("Added user: " + "userEPPN" + userEPPN + " Valid user ID must be greater then zero.  Value is " + id
				+ ".", id > 0);
		// could also check email syntax

		return id.intValue();
	}

	public JSONObject createAddMemberFixture() {

		JSONObject json = new JSONObject();

		// EMPTY

		return json;
	}

	public String addMember(String ownerEPPN, int companyId) {

		// Create a member user
		String unique2 = TestUserUtil.generateTime();
		String memberEPPN = "userEPPN" + unique2;
		String memberGivenName = "userGivenName" + unique2;
		String memberSurName = "userSurName" + unique2;
		String memberDisplayName = "userDisplayName" + unique2;
		String memberEmail = "userEmail" + unique2;

		int memberUserId = createUser(memberEPPN, memberGivenName, memberSurName, memberDisplayName, memberEmail);

		given().header("Content-type", "application/json").header("AJP_eppn", ownerEPPN).expect().statusCode(200).when()
				.post(COMPANY_ADD_MEMBER_RESOURCE, Integer.toString(companyId), Integer.toString(memberUserId));

		return unique2;

	}

	/**
	 * test case for PATCH /campany_images/{imageID}
	 */
	@Test
	public void testPath_CompanyImageById() {
		CompanyImage object = new CompanyImage();
		ObjectMapper mapper = new ObjectMapper();
		String patchedCompanyImageJSONString = null;
		try {
			patchedCompanyImageJSONString = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.body(patchedCompanyImageJSONString).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
				.patch("/company_images/" + imageId);
	}

	/**
	 * test case for GET /companies/{companyID}/company_images
	 */
	@Test
	public void testCompanyGet_CompanyImage() {
		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
				.get("/companies/" + companyId + "/company_images");
	}

	/**
	 * test case for GET /companies/{companyID}/company_history
	 */
	@Test
	public void testCompanyGet_CompanyHistory() {
		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
				.get("/companies/" + companyId + "/company_history");
	}

	/**
	 * test case for GET /companies/{companyID}/company_skill_images
	 */
	@Test
	public void testCompanyGet_CompanySkillImage() {
		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
				.get("/companies/" + companyId + "/company_skill_images");
	}

	/**
	 * test case for PATCH /company_skill_images/{imageID}
	 */
	@Test
	public void testCompanyPatch_CompanySkillImageById() {
		CompanySkillImage obj = new CompanySkillImage();
		ObjectMapper mapper = new ObjectMapper();
		String patchedCompanySkillImageJSONString = null;
		try {
			patchedCompanySkillImageJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.body(patchedCompanySkillImageJSONString).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
				.patch("/company_skill_images/" + imageId);
	}

	/**
	 * test case for GET /companies/{companyID}/company_reviews
	 * See CompanyReviewIT
	 */

	/**
	 * test case for POST /company_reviews/
	 * See CompanyReviewIT
	 */


	/**
	 * test case for GET /company_reviews_helpful
	 */
	@Test
	public void testCompanyGet_CompanyReviewsHelpful() {
		given().param("reviewId", reviewId).param("accountId", accountId).header("AJP_eppn", userEPPN).expect()
				.statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when().get("/company_reviews_helpful");
	}

	/**
	 * test case for POST /company_reviews_helpful
	 */
	@Test
	public void testCompanyPost_CompanyReviewHelpful() {
		CompanyReviewHelpful obj = new CompanyReviewHelpful();
		ObjectMapper mapper = new ObjectMapper();
		String postedCompanyReviewHelpfulJSONString = null;
		try {
			postedCompanyReviewHelpfulJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.body(postedCompanyReviewHelpfulJSONString).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value())
				.when().post("/company_reviews_helpful");
	}

	/**
	 * test case for PATCH /company_reviews_helpful/{helpfulID}
	 */
	@Test
	public void testCompanyPatch_CompanyReviewHelpfulById() {
		CompanyReviewHelpful obj = new CompanyReviewHelpful();
		ObjectMapper mapper = new ObjectMapper();
		String patchedCompanyReviewHelpfulJSONString = null;
		try {
			patchedCompanyReviewHelpfulJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.body(patchedCompanyReviewHelpfulJSONString).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value())
				.when().patch("/company_reviews_helpful/" + helpfulId);
	}

	/**
	 * test case for GET /company_reviews_flagged
	 */
	@Test
	public void testCompanyGet_CompanyReviewsFlagged() {
		given().param("reviewId", reviewId).param("accountId", accountId).header("AJP_eppn", userEPPN).expect()
				.statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when().get("/company_reviews_flagged");
	}

	/**
	 * test case for POST /company_reviews_flagged
	 */
	@Test
	public void testCompanyPost_CompanyReviewFlagged() {
		CompanyReviewFlagged obj = new CompanyReviewFlagged();
		ObjectMapper mapper = new ObjectMapper();
		String postedCompanyReviewFlaggedJSONString = null;
		try {
			postedCompanyReviewFlaggedJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.body(postedCompanyReviewFlaggedJSONString).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value())
				.when().post("/company_reviews_flagged");
	}
	
	
	/**
	 * test case for GET /companies/{companyID}/company_services
	 */
	@Test
	public void testCompanyGet_CompanyServices() {
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.OK.value()).
		when().
		get("/companies/" + companyId + "/company_services");
	}
	
	/**
	 * test case for GET /companies/{companyID}/company_components
	 */
	@Test
	public void testCompanyGet_CompanyComponent() {
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/companies/" + companyId + "/company_components");
	}
	
	/**
	 * test case for GET /companies/{companyID}/company_featured
	 */
	@Test
	public void testCompanyGet_CompanyFeatured() {
		given().param("order", order).param("sort", sort).
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/companies/" + companyId + "/company_featured");
	}
	
	
	/**
	 * test case for GET /companies/{companyID}/new
	 */
	@Test
	public void testCompanyGet_CompanyByIdNew() {
		given().
		param("order", order).
		param("sort", sort).
		param("limit", limit).
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/companies/" + companyId + "/new");
	}

	
	/**
	 * test case for POST /company/follow
	 */
	@Test
	public void testCompanyPost_Follow() {

		given().
		param("accountId", accountId).
		param("companyId", companyId).
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		post("/company/follow");
	}
	
	
	/**
	 * test case for DELETE /company/unfollow/{followed_companiId}
	 */
	@Test
	public void testCompanyDelete_UnFollowCompanyId() {
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(400).
		when().
		delete("/company/unfollow/" + followed_companyId);
	}
	
	
	/**
	 * test case for PATCH /company_featured/{company_featuredId}/position
	 */
	@Test
	public void testCompanyPatch_CompanyFeaturedPosition() {
		given().
		param("position", position).
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(400).
		when().
		patch("/company_featured/" + company_featuredId + "/position");
	}
	
	
	/**
	 * test case for POST /company_featured/add
	 */
	@Test
	public void testCompanyPatch_CompanyFeaturedAdd() {
		given().
		param("companyId", companyId).
		param("position", position).
		param("serviceId", serviceId).
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		post("/company_featured/add");
	}
	
	
	/**
	 * test case for DELETE /company_featured/{company_featuredId}
	 */
	@Test
	public void testCompanyDelete_CompanyFeaturedById() {
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(400).
		when().
		delete("/company_featured/" + company_featuredId);
	}
	
	
}