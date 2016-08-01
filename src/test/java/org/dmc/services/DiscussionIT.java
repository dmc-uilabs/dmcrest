package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dmc.services.discussions.Discussion;
import org.dmc.services.discussions.DiscussionController;
import org.dmc.services.discussions.FollowingIndividualDiscussion;
import org.dmc.services.discussions.IndividualDiscussion;
import org.dmc.services.discussions.IndividualDiscussionComment;
import org.dmc.services.discussions.IndividualDiscussionCommentFlagged;
import org.dmc.services.discussions.IndividualDiscussionCommentHelpful;
import org.dmc.services.discussions.IndividualDiscussionTag;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Ignore
public class DiscussionIT extends BaseIT {
	
	private final String logTag = DiscussionController.class.getName();
	private static final String PROFILE_CREATE_RESOURCE = "/profiles";
	private static final String ALL_DISCUSSIONS_RESOURCE = "/all-discussions";
	private static final String DISCUSSION_CREATE_RESOURCE = "/discussions/create";
	private String followId = "1";
	private String individualDiscussionID = "1";
	private String knownCommentId = "1";
	private String helpfulId = "1";
	private String disscusionTagId = "1";
	private String accountId = "1";
	
	private Integer createdId = null;
	private String knownEPPN;
	
	@Before
	public void testDiscussionCreate() {
		if (knownEPPN == null) {
			knownEPPN = TestUserUtil.createNewUser();
		}
		this.createdId = createDiscussion(null);
	}
	
    @Test
	public void testAllDiscussions() {
    	if (this.createdId != null) {
			ArrayList<Discussion> originalDiscussionList =
		        given().
		            header("Content-type", "application/json").
		            header("AJP_eppn", knownEPPN).
		        expect().
		            statusCode(200).
		        when().
		            get(ALL_DISCUSSIONS_RESOURCE).as(ArrayList.class);
			
		        // add a discussion
		        String savedRandomEPPN = knownEPPN;
		        knownEPPN = TestUserUtil.createNewUser();
		        testDiscussionCreate();
		        knownEPPN = savedRandomEPPN;
		        
		        ArrayList<Discussion> newDiscussionList =
		        given().
		            header("Content-type", "application/json").
		            header("AJP_eppn", knownEPPN).
		        expect().
		            statusCode(200).
		        when().
		            get(ALL_DISCUSSIONS_RESOURCE).as(ArrayList.class);
            int LIMIT = 100;
		       assertTrue("Returned discussion lists do not have acceptable sizes", newDiscussionList.size() == originalDiscussionList.size()+1 || newDiscussionList.size() == LIMIT);
    	}
	}

	public int createDiscussion(Integer projectId) {
		
		JSONObject json = createFixture(projectId);
		
		this.createdId = given()
				.header("Content-type", "application/json")
				.header("AJP_eppn", knownEPPN)
				.body(json.toString())
				.expect()
				.statusCode(200)
				.when()
				.post(DISCUSSION_CREATE_RESOURCE)
				.then()
				.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
				.extract()
				.path("id");
		
		return createdId;
	}
	
	public JSONObject createFixture(Integer projectId) {
			
		String projId = (projectId != null) ? projectId.toString() : "123";
		JSONObject json = new JSONObject();
		json.put("title", "test discussion title");
		json.put("message", "test discussion message");
		json.put("createdBy", "test-disc-created-by");
		json.put("createdAt", 1232000);
		json.put("accountId", "123");
		json.put("projectId", projId);
		
		return json;
	}
	
	
	/**
	 * test case for get /popular_discussions
	 */
	@Test
	public void testGet_PopularDiscussions(){
		given().
		header("AJP_eppn", knownEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/popular_discussions");
	}
	
	/*
	 * test case for get /follow_people_discussions
	 */
	@Test
	public void testGet_FollowPeopleDiscussions(){
		given().
		header("AJP_eppn", knownEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/follow_people_discussions");
	}
	
	
	/*
	 * test case for get /following_discussions
	 */
	@Test
	public void testGet_FollowingDiscussions(){
		given().
		header("AJP_eppn", knownEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/following_discussions");
	}
	
	/*
	 * test case 1 for GET /accounts/{accountID}/follow_discussions
	 */

	@Test
	public void testGet_FollowDiscussionsFromAccountIdWithIndividualDiscussionId() {
		String individualDiscussionId = "1";
		String accountId = "550";
		String userEppn = "joeengineer";

		List<FollowingIndividualDiscussion> followedDiscussions = Arrays.asList(given().header("AJP_eppn", userEppn).param("individual-discussionId", individualDiscussionId)
				.expect().statusCode(HttpStatus.OK.value()).when().get("/accounts/" + accountId + "/follow_discussions").as(FollowingIndividualDiscussion[].class));

		assertTrue(
				"testGet_FollowDiscussionsFromAccountIdWithIndividualDiscussionId: there should only be one item returned when both accountId and individualDiscussionId are specified",
				followedDiscussions.size() == 1);
		assertTrue("testGet_FollowDiscussionsFromAccountIdWithIndividualDiscussionId: accountId values are not equal", followedDiscussions.get(0).getAccountId().equals(accountId));
		assertTrue("testGet_FollowDiscussionsFromAccountIdWithIndividualDiscussionId: individualDiscussionId values are not equal",
				followedDiscussions.get(0).getIndividualDiscussionId().equals(individualDiscussionId));
		assertTrue("testGet_FollowDiscussionsFromAccountIdWithIndividualDiscussionId: id values are not equal", followedDiscussions.get(0).getId().equals("1"));
	}

	/*
	 * test case 2 for GET /accounts/{accountID}/follow_discussions
	 */

	@Test
	public void testGet_FollowDiscussionsFromAccountIdWithoutIndividualDiscussionId() {
		String accountId = "550";
		String userEppn = "joeengineer";

		List<FollowingIndividualDiscussion> followedDiscussions = Arrays.asList(given().header("AJP_eppn", userEppn).param("limit", 2).expect().statusCode(HttpStatus.OK.value())
				.when().get("/accounts/" + accountId + "/follow_discussions").as(FollowingIndividualDiscussion[].class));

		assertTrue("testGet_FollowDiscussionsFromAccountIdWithoutIndividualDiscussionId: limit parameter didn't work", followedDiscussions.size() == 2);
		assertTrue("testGet_FollowDiscussionsFromAccountIdWithoutIndividualDiscussionId: accountId values are not equal",
				followedDiscussions.get(0).getAccountId().equals(accountId));
		assertTrue("testGet_FollowDiscussionsFromAccountIdWithoutIndividualDiscussionId: individualDiscussionId values are not equal",
				followedDiscussions.get(0).getIndividualDiscussionId().equals("1"));
		assertTrue("testGet_FollowDiscussionsFromAccountIdWithoutIndividualDiscussionId: id values are not equal", followedDiscussions.get(0).getId().equals("1"));
	}

	/*
	 * test case 1 for POST /follow_discussions
	 */
	@Test
	public void testPost_followDiscussionWithValidObject() {
		FollowingIndividualDiscussion followToPost = new FollowingIndividualDiscussion();
		ObjectMapper mapper = new ObjectMapper();
		String postedFollowDiscussionsJSONString = null;

		String accountId = "550";
		String individualDiscussionId = "3";
		String userEPPN = "joeengineer";

		followToPost.setIndividualDiscussionId(individualDiscussionId);
		followToPost.setAccountId(accountId);

		try {
			postedFollowDiscussionsJSONString = mapper.writeValueAsString(followToPost);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		FollowingIndividualDiscussion postedFollow = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).body(postedFollowDiscussionsJSONString)
				.expect().statusCode(HttpStatus.CREATED.value()).when().post("/follow_discussions").as(FollowingIndividualDiscussion.class);

		assertTrue("testPost_followDiscussionWithValidObject: individual discussion id values are not equal",
				postedFollow.getIndividualDiscussionId().equals(individualDiscussionId));
		assertTrue("testPost_followDiscussionWithValidObject: account id values are not equal", postedFollow.getAccountId().equals(accountId));

	}

	/*
	 * test case 2 for POST /follow_discussions
	 */
	@Test
	public void testPost_followDiscussionWithInvalidAccountId() {
		FollowingIndividualDiscussion followToPost = new FollowingIndividualDiscussion();
		ObjectMapper mapper = new ObjectMapper();
		String postedFollowDiscussionsJSONString = null;

		String accountId = "0";
		String individualDiscussionId = "3";

		followToPost.setIndividualDiscussionId(individualDiscussionId);
		followToPost.setAccountId(accountId);

		try {
			postedFollowDiscussionsJSONString = mapper.writeValueAsString(followToPost);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(postedFollowDiscussionsJSONString).expect().statusCode(HttpStatus.UNAUTHORIZED.value())
				.when().post("/follow_discussions");

	}

	/*
	 * test case 3 for POST /follow_discussions
	 */
	@Test
	public void testPost_followDiscussionWithInvalidDiscussionId() {
		FollowingIndividualDiscussion followToPost = new FollowingIndividualDiscussion();
		ObjectMapper mapper = new ObjectMapper();
		String postedFollowDiscussionsJSONString = null;

		String accountId = "550";
		String individualDiscussionId = "0";
		String userEPPN = "joeengineer";

		followToPost.setIndividualDiscussionId(individualDiscussionId);
		followToPost.setAccountId(accountId);

		try {
			postedFollowDiscussionsJSONString = mapper.writeValueAsString(followToPost);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).body(postedFollowDiscussionsJSONString).expect().statusCode(HttpStatus.BAD_REQUEST.value())
				.when().post("/follow_discussions");

	}

	/*
	 * test case 1 for DELETE /follow_discussions/{followID}
	 */
	@Test
	public void testDelete_FollowDiscussionsWithValidId() {
		FollowingIndividualDiscussion followToPost = new FollowingIndividualDiscussion();
		ObjectMapper mapper = new ObjectMapper();
		String postedFollowDiscussionsJSONString = null;

		String accountId = "102";
		String individualDiscussionId = "4";
		String userEPPN = "fforgeadmin";

		followToPost.setIndividualDiscussionId(individualDiscussionId);
		followToPost.setAccountId(accountId);

		try {
			postedFollowDiscussionsJSONString = mapper.writeValueAsString(followToPost);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		FollowingIndividualDiscussion postedFollow = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).body(postedFollowDiscussionsJSONString)
				.expect().statusCode(HttpStatus.CREATED.value()).when().post("/follow_discussions").as(FollowingIndividualDiscussion.class);

		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.OK.value()).when().delete("/follow_discussions/" + postedFollow.getId());
	}

	/*
	 * test case 2 for DELETE /follow_discussions/{followID}
	 */
	@Test
	public void testDelete_FollowDiscussionsWithInvalidId() {
		FollowingIndividualDiscussion followToPost = new FollowingIndividualDiscussion();
		ObjectMapper mapper = new ObjectMapper();
		String postedFollowDiscussionsJSONString = null;

		String accountId = "102";
		String individualDiscussionId = "4";
		String userEPPN = "fforgeadmin";

		followToPost.setIndividualDiscussionId(individualDiscussionId);
		followToPost.setAccountId(accountId);

		try {
			postedFollowDiscussionsJSONString = mapper.writeValueAsString(followToPost);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		FollowingIndividualDiscussion postedFollow = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).body(postedFollowDiscussionsJSONString)
				.expect().statusCode(HttpStatus.CREATED.value()).when().post("/follow_discussions").as(FollowingIndividualDiscussion.class);

		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.NOT_FOUND.value()).when().delete("/follow_discussions/" + 0);
	}

	/*
	 * test case for GET /projects/{id}/individual-discussion
	 */
	@Test
	public void testGet_IndividualDiscussionFromProjectId() {
		List<IndividualDiscussion> received = Arrays.asList(given().header("AJP_eppn", knownEPPN).expect().statusCode(HttpStatus.OK.value()).when()
				.get("/projects/" + 2 + "/individual-discussion").as(IndividualDiscussion[].class));

		assertTrue("testGet_IndividualDiscussionFromProjectId: id values are not equal", (received.get(0).getId().equals("3")));
		assertTrue("testGet_IndividualDiscussionFromProjectId: title values are not equal", (received.get(0).getTitle().equals("For Project")));
		assertTrue("testGet_IndividualDiscussionFromProjectId: createdBy values are not equal", (received.get(0).getCreatedBy().equals("Google")));
		assertTrue("testGet_IndividualDiscussionFromProjectId: createdAt values are not equal", (received.get(0).getCreatedAt().equals(new BigDecimal("12345"))));
		assertTrue("testGet_IndividualDiscussionFromProjectId: accountId values are not equal", (received.get(0).getAccountId().equals(new BigDecimal(550))));
	}

	/*
	 * test case for GET /individual-discussion
	 */
	@Test
	public void testGet_IndividualDiscussion() {
		List<IndividualDiscussion> received = Arrays
				.asList(given().header("AJP_eppn", knownEPPN).expect().statusCode(HttpStatus.OK.value()).when().get("/individual-discussion").as(IndividualDiscussion[].class));

		assertTrue("testGet_IndividualDiscussion: title values are not equal", (received.get(0).getTitle().equals("For Community")));
		assertTrue("testGet_IndividualDiscussion: createdBy values are not equal", (received.get(0).getCreatedBy().equals("John")));
		assertTrue("testGet_IndividualDiscussion: createdAt values are not equal", (received.get(0).getCreatedAt().equals(new BigDecimal("12345"))));
		assertTrue("testGet_IndividualDiscussion: accountId values are not equal", (received.get(0).getAccountId().equals(new BigDecimal(550))));
		for (int i = 0; i < received.size(); i++) {
			assertTrue("testGet_IndividualDiscussion: projectId values are not equal", (received.get(i).getProjectId() == null));
		}
	}

	/*
	 * test case 1 for POST /individual-discussion
	 */
	@Test
	public void testPost_IndividualDiscussionWithProjectId() {
		IndividualDiscussion obj = new IndividualDiscussion();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussion = null;

		String title = "For POST /individual-discussion With ProjectId";
		String createdBy = "Eminem";
		BigDecimal createdAt = new BigDecimal(12301293);
		BigDecimal accountId = new BigDecimal(550);
		BigDecimal projectId = new BigDecimal(2);

		obj.setTitle(title);
		obj.setCreatedBy(createdBy);
		obj.setCreatedAt(createdAt);
		obj.setAccountId(accountId);
		obj.setProjectId(projectId);

		try {
			postedIndividualDiscussion = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		IndividualDiscussion posted = given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(postedIndividualDiscussion).expect()
				.statusCode(HttpStatus.CREATED.value()).when().post("/individual-discussion").as(IndividualDiscussion.class);

		assertTrue("testPost_IndividualDiscussionWithProjectId: title values are not equal", (posted.getTitle().equals(title)));
		assertTrue("testPost_IndividualDiscussionWithProjectId: createdBy values are not equal", (posted.getCreatedBy().equals(createdBy)));
		assertTrue("testPost_IndividualDiscussionWithProjectId: createdAt values are not equal", (posted.getCreatedAt().equals(createdAt)));
		assertTrue("testPost_IndividualDiscussionWithProjectId: accountId values are not equal", (posted.getAccountId().equals(accountId)));
		assertTrue("testPost_IndividualDiscussionWithProjectId: projectId values are not equal", (posted.getProjectId().equals(projectId)));
	}

	/*
	 * test case 2 for POST /individual-discussion
	 */
	@Test
	public void testPost_IndividualDiscussionWithoutProjectId() {
		IndividualDiscussion obj = new IndividualDiscussion();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussion = null;

		String title = "For POST /individual-discussion Without projectId";
		String createdBy = "Eminem";
		BigDecimal createdAt = new BigDecimal(12301293);
		BigDecimal accountId = new BigDecimal(550);

		obj.setTitle(title);
		obj.setCreatedBy(createdBy);
		obj.setCreatedAt(createdAt);
		obj.setAccountId(accountId);

		try {
			postedIndividualDiscussion = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		IndividualDiscussion posted = given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(postedIndividualDiscussion).expect()
				.statusCode(HttpStatus.CREATED.value()).when().post("/individual-discussion").as(IndividualDiscussion.class);

		assertTrue("testPost_IndividualDiscussionWithoutProjectId: title values are not equal", (posted.getTitle().equals(title)));
		assertTrue("testPost_IndividualDiscussionWithoutProjectId: createdBy values are not equal", (posted.getCreatedBy().equals(createdBy)));
		assertTrue("testPost_IndividualDiscussionWithoutProjectId: createdAt values are not equal", (posted.getCreatedAt().equals(createdAt)));
		assertTrue("testPost_IndividualDiscussionWithoutProjectId: accountId values are not equal", (posted.getAccountId().equals(accountId)));
	}

	/*
	 * test case 3 for POST /individual-discussion
	 */
	@Test
	public void testPost_IndividualDiscussionWithInvalidAccountId() {
		IndividualDiscussion obj = new IndividualDiscussion();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussion = null;

		String title = "For POST /individual-discussion Without projectId";
		String createdBy = "Eminem";
		BigDecimal createdAt = new BigDecimal(12301293);
		BigDecimal accountId = BigDecimal.ZERO;

		obj.setTitle(title);
		obj.setCreatedBy(createdBy);
		obj.setCreatedAt(createdAt);
		obj.setAccountId(accountId);

		try {
			postedIndividualDiscussion = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(postedIndividualDiscussion).expect().statusCode(HttpStatus.UNAUTHORIZED.value()).when()
				.post("/individual-discussion");

	}

	/*
	 * test case for GET /individual-discussion/{individualDiscussionID}
	 */
	@Test
	@Ignore("Test requires db data which isn't set up, gives param that isn't used in controller. Needs to be cleaned up")
	public void testGet_IndividualDiscussionFromId() {
		IndividualDiscussion read = given().param("commentId", knownCommentId).header("AJP_eppn", knownEPPN).expect().statusCode(HttpStatus.OK.value()).when()
				.get("/individual-discussion/" + 3).as(IndividualDiscussion.class);

		assertTrue("testGet_IndividualDiscussionFromId: title values are not equal", (read.getTitle().equals("For Project")));
		assertTrue("testGet_IndividualDiscussionFromId: createdBy values are not equal", (read.getCreatedBy().equals("Google")));
		assertTrue("testGet_IndividualDiscussionFromId: createdAt values are not equal", (read.getCreatedAt().equals(new BigDecimal(12345))));
		assertTrue("testGet_IndividualDiscussionFromId: accountId values are not equal", (read.getAccountId().equals(new BigDecimal(550))));
		assertTrue("testGet_IndividualDiscussionFromId: projectId values are not equal", (read.getProjectId().equals(new BigDecimal(2))));
	}

	/*
	 * test case for GET /individual-discussion/{individualDiscussionID}/individual-discussion-comments
	 */
	@Test
	public void testGet_IndividualDiscussionCommentsFromIndividualDiscussionId() {
		List<IndividualDiscussionComment> listOfComments = Arrays.asList(given().header("AJP_eppn", knownEPPN).param("commentId", "1").param("_limit", 2).expect()
				.statusCode(HttpStatus.OK.value()).when().get("/individual-discussion/" + 1 + "/individual-discussion-comments").as(IndividualDiscussionComment[].class));

		assertTrue("testGet_IndividualDiscussionCommentsFromIndividualDiscussionId: limit parameter did not work", (listOfComments.size() == 2));

		assertTrue("testGet_IndividualDiscussionCommentsFromIndividualDiscussionId: id values are not equal", (listOfComments.get(0).getId().equals("3")));
		assertTrue("testGet_IndividualDiscussionCommentsFromIndividualDiscussionId: individual_discussion_id values are not equal",
				(listOfComments.get(0).getIndividualDiscussionId().equals("1")));
		assertTrue("testGet_IndividualDiscussionCommentsFromIndividualDiscussionId: fullName values are not equal", (listOfComments.get(0).getFullName().equals("Joe")));
		assertTrue("testGet_IndividualDiscussionCommentsFromIndividualDiscussionId: accountId values are not equal",
				(listOfComments.get(0).getAccountId().equals(new BigDecimal(550))));
		assertTrue("testGet_IndividualDiscussionCommentsFromIndividualDiscussionId: commentId values are not equal",
				(listOfComments.get(0).getCommentId().equals(new BigDecimal(1))));
		assertTrue("testGet_IndividualDiscussionCommentsFromIndividualDiscussionId: likes values are not equal", (listOfComments.get(0).getLike().equals(new BigDecimal(1))));
	}

	/*
	 * test case for GET /individual-discussion/{individualDiscussionID}/individual-discussion-tags
	 */
	@Test
	public void testGet_IndividualDiscussionTags() {
		List<IndividualDiscussionTag> tags = Arrays.asList(given().header("AJP_eppn", knownEPPN).param("_limit", 2).expect().statusCode(HttpStatus.OK.value()).when()
				.get("/individual-discussion/" + 1 + "/individual-discussion-tags").as(IndividualDiscussionTag[].class));

		assertTrue("testGet_IndividualDiscussionTags: limit parameter didn't work", tags.size() == 2);
		assertTrue("testGet_IndividualDiscussionTags: id value is not correct", tags.get(0).getId().equals("1"));
		assertTrue("testGet_IndividualDiscussionTags: individual discussion id value is not correct", tags.get(0).getIndividualDiscussionId().equals("1"));
		assertTrue("testGet_IndividualDiscussionTags: name value is not correct", tags.get(0).getName().equals("tag"));
	}

	/*
	 * test case for GET /individual-discussion-comments/{id}
	 */
	@Test
	public void testGet_IndividualDiscussionCommentsFromId() {
		IndividualDiscussionComment readObj = given().header("AJP_eppn", knownEPPN).expect().statusCode(HttpStatus.OK.value()).when().get("/individual-discussion-comments/" + 1)
				.as(IndividualDiscussionComment.class);

		assertTrue("testGet_IndividualDiscussionCommentsFromId: id values are not equal", (readObj.getId().equals("1")));
		assertTrue("testGet_IndividualDiscussionCommentsFromId: individual_discussion_id values are not equal", (readObj.getIndividualDiscussionId().equals("1")));
		assertTrue("testGet_IndividualDiscussionCommentsFromId: fullName values are not equal", (readObj.getFullName().equals("Joe")));
		assertTrue("testGet_IndividualDiscussionCommentsFromId: accountId values are not equal", (readObj.getAccountId().equals(new BigDecimal(550))));
		assertTrue("testGet_IndividualDiscussionCommentsFromId: commentId values are not equal", (readObj.getCommentId().equals(new BigDecimal(0))));
		assertTrue("testGet_IndividualDiscussionCommentsFromId: likes values are not equal", (readObj.getLike().equals(new BigDecimal(30))));
	}

	/*
	 * test case 1 for GET /individual-discussion-comments
	 */
	@Test
	public void testGet_IndividualDiscussionCommentsWithIndividualDiscussionId() {
		List<IndividualDiscussionComment> listOfComments = Arrays.asList(given().header("AJP_eppn", knownEPPN).param("_limit", 2).param("_order", "ASC").param("commentId", 0)
				.param("individual-discussionId", 1).param("individual-discussionId", 3).expect().statusCode(HttpStatus.OK.value()).when().get("/individual-discussion-comments")
				.as(IndividualDiscussionComment[].class));

		for (int i = 0; i < listOfComments.size(); i++) {
			assertTrue("testGet_IndividualDiscussionCommentsWithIndividualDiscussionId: individualDiscussionId values are not equal",
					(listOfComments.get(i).getIndividualDiscussionId().equals("1")));
		}

		assertTrue("testGet_IndividualDiscussionCommentsWithIndividualDiscussionId: id values are not equal", (listOfComments.get(0).getId().equals("1")));
		assertTrue("testGet_IndividualDiscussionCommentsWithIndividualDiscussionId: fullName values are not equal", (listOfComments.get(0).getFullName().equals("Joe")));
		assertTrue("testGet_IndividualDiscussionCommentsWithIndividualDiscussionId: accountId values are not equal",
				(listOfComments.get(0).getAccountId().equals(new BigDecimal(550))));
		assertTrue("testGet_IndividualDiscussionCommentsWithIndividualDiscussionId: commentId values are not equal",
				(listOfComments.get(0).getCommentId().equals(new BigDecimal(0))));
		assertTrue("testGet_IndividualDiscussionCommentsWithIndividualDiscussionId: likes values are not equal", listOfComments.get(0).getLike().equals(new BigDecimal(30)));
	}

	/*
	 * test case 2 for GET /individual-discussion-comments
	 */
	@Test
	public void testGet_IndividualDiscussionCommentsWithoutIndividualDiscussionId() {
		List<IndividualDiscussionComment> listOfComments = Arrays.asList(given().header("AJP_eppn", knownEPPN).param("_limit", 2).param("_order", "ASC").param("commentId", 1)
				.expect().statusCode(HttpStatus.OK.value()).when().get("/individual-discussion-comments").as(IndividualDiscussionComment[].class));

		for (int i = 0; i < listOfComments.size(); i++) {
			assertTrue("testGet_IndividualDiscussionCommentsWithoutIndividualDiscussionId: individualDiscussionId values are not equal",
					(listOfComments.get(i).getIndividualDiscussionId().equals("1")));
		}

		assertTrue("testGet_IndividualDiscussionCommentsWithoutIndividualDiscussionId: id values are not equal", (listOfComments.get(0).getId().equals("3")));
		assertTrue("testGet_IndividualDiscussionCommentsWithoutIndividualDiscussionId: fullName values are not equal", (listOfComments.get(0).getFullName().equals("Joe")));
		assertTrue("testGet_IndividualDiscussionCommentsWithoutIndividualDiscussionId: accountId values are not equal",
				(listOfComments.get(0).getAccountId().equals(new BigDecimal(550))));
		assertTrue("testGet_IndividualDiscussionCommentsWithoutIndividualDiscussionId: commentId values are not equal",
				(listOfComments.get(0).getCommentId().equals(new BigDecimal(1))));
		assertTrue("testGet_IndividualDiscussionCommentsWithoutIndividualDiscussionId: likes values are not equal", listOfComments.get(0).getLike().equals(new BigDecimal(1)));
	}

	/*
	 * test case 1 for POST /individual-discussion-comments
	 */
	@Test
	public void testPost_IndividualDiscussionCommentsWithValidUser() {
		IndividualDiscussionComment obj = new IndividualDiscussionComment();
		String postedCommentStr = null;
		ObjectMapper mapper = new ObjectMapper();

		String individualDiscussionId = "1";
		String fullName = "Marshall Mathers";
		BigDecimal commentId = new BigDecimal(0);
		String avatar = "For POST /individual-discussion-comments";
		Boolean reply = false;
		String text = "TEXT";
		BigDecimal accountId = new BigDecimal(550);
		BigDecimal createdAt = new BigDecimal(12301293);
		BigDecimal like = new BigDecimal(2);
		BigDecimal dislike = new BigDecimal(1);

		obj.setIndividualDiscussionId(individualDiscussionId);
		obj.setFullName(fullName);
		obj.setAccountId(accountId);
		obj.setCommentId(commentId);
		obj.setAvatar(avatar);
		obj.setReply(reply);
		obj.setText(text);
		obj.setCreatedAt(createdAt);
		obj.setLike(like);
		obj.setDislike(dislike);

		try {
			postedCommentStr = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		IndividualDiscussionComment postedCommentObj = given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(postedCommentStr).expect()
				.statusCode(HttpStatus.OK.value()).when().post("/individual-discussion-comments").as(IndividualDiscussionComment.class);

		assertTrue("testPost_IndividualDiscussionCommentsWithValidUser: individualDiscussionId values are not equal",
				(postedCommentObj.getIndividualDiscussionId().equals(individualDiscussionId)));
		assertTrue("testPost_IndividualDiscussionCommentsWithValidUser: fullName values are not equal", (postedCommentObj.getFullName().equals(fullName)));
		assertTrue("testPost_IndividualDiscussionCommentsWithValidUser: accountId values are not equal", (postedCommentObj.getAccountId().equals(accountId)));
		assertTrue("testPost_IndividualDiscussionCommentsWithValidUser: commentId values are not equal", (postedCommentObj.getCommentId().equals(commentId)));
		assertTrue("testPost_IndividualDiscussionCommentsWithValidUser: avatar values are not equal", (postedCommentObj.getAvatar().equals(avatar)));
		assertTrue("testPost_IndividualDiscussionCommentsWithValidUser: reply values are not equal", (postedCommentObj.getReply().equals(reply)));
		assertTrue("testPost_IndividualDiscussionCommentsWithValidUser: text values are not equal", (postedCommentObj.getText().equals(text)));
		assertTrue("testPost_IndividualDiscussionCommentsWithValidUser: createdAt values are not equal", (postedCommentObj.getCreatedAt().equals(createdAt)));
		assertTrue("testPost_IndividualDiscussionCommentsWithValidUser: like values are not equal", (postedCommentObj.getLike().equals(like)));
		assertTrue("testPost_IndividualDiscussionCommentsWithValidUser: dislike values are not equal", (postedCommentObj.getDislike().equals(dislike)));
	}

	/*
	 * test case 2 for POST /individual-discussion-comments
	 */
	@Test
	public void testPost_IndividualDiscussionCommentsWithInvalidUser() {
		IndividualDiscussionComment obj = new IndividualDiscussionComment();
		String postedCommentStr = null;
		ObjectMapper mapper = new ObjectMapper();

		String individualDiscussionId = "1";
		String fullName = "Marshall Mathers";
		BigDecimal commentId = new BigDecimal(0);
		String avatar = "For POST /individual-discussion-comments";
		Boolean reply = false;
		String text = "TEXT";
		BigDecimal accountId = BigDecimal.ZERO;
		BigDecimal createdAt = new BigDecimal(12301293);
		BigDecimal like = new BigDecimal(2);
		BigDecimal dislike = new BigDecimal(1);

		obj.setIndividualDiscussionId(individualDiscussionId);
		obj.setFullName(fullName);
		obj.setAccountId(accountId);
		obj.setCommentId(commentId);
		obj.setAvatar(avatar);
		obj.setReply(reply);
		obj.setText(text);
		obj.setCreatedAt(createdAt);
		obj.setLike(like);
		obj.setDislike(dislike);

		try {
			postedCommentStr = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(postedCommentStr).expect().statusCode(HttpStatus.UNAUTHORIZED.value()).when()
				.post("/individual-discussion-comments");

	}

	/*
	 * test case for PATCH /individual-discussion-comments/{commentID}
	 */

	@Test
	public void testPatch_IndividualDiscussionComments() {
		IndividualDiscussionComment obj = new IndividualDiscussionComment();
		ObjectMapper mapper = new ObjectMapper();
		String patchedIndividualDiscussionCommentJSONString = null;

		String individualDiscussionId = "1";
		String fullName = "Marshall Mathers";
		BigDecimal commentId = new BigDecimal(0);
		String avatar = "For PATCH /individual-discussion-comments/{id}";
		Boolean reply = false;
		String text = "TEXT";
		BigDecimal accountId = new BigDecimal(550);
		BigDecimal createdAt = new BigDecimal(12301293);
		BigDecimal like = new BigDecimal(2);
		BigDecimal dislike = new BigDecimal(1);

		obj.setIndividualDiscussionId(individualDiscussionId);
		obj.setFullName(fullName);
		obj.setAccountId(accountId);
		obj.setCommentId(commentId);
		obj.setAvatar(avatar);
		obj.setReply(reply);
		obj.setText(text);
		obj.setCreatedAt(createdAt);
		obj.setLike(like);
		obj.setDislike(dislike);

		try {
			patchedIndividualDiscussionCommentJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		IndividualDiscussionComment received = given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(patchedIndividualDiscussionCommentJSONString)
				.expect().statusCode(HttpStatus.OK.value()).when().patch("/individual-discussion-comments/" + 5).as(IndividualDiscussionComment.class);

		assertTrue("testPatch_IndividualDiscussionComments: id values are not equal", (received.getId().equals("5")));
		assertTrue("testPatch_IndividualDiscussionComments: individualDiscussionId values are not equal", (received.getIndividualDiscussionId().equals(individualDiscussionId)));
		assertTrue("testPatch_IndividualDiscussionComments: fullName values are not equal", (received.getFullName().equals(fullName)));
		assertTrue("testPatch_IndividualDiscussionComments: accountId values are not equal", (received.getAccountId().equals(accountId)));
		assertTrue("testPatch_IndividualDiscussionComments: commentId values are not equal", (received.getCommentId().equals(commentId)));
		assertTrue("testPatch_IndividualDiscussionComments: avatar values are not equal", (received.getAvatar().equals(avatar)));
		assertTrue("testPatch_IndividualDiscussionComments: reply values are not equal", (received.getReply().equals(reply)));
		assertTrue("testPatch_IndividualDiscussionComments: text values are not equal", (received.getText().equals(text)));
		assertTrue("testPatch_IndividualDiscussionComments: createdAt values are not equal", (received.getCreatedAt().equals(createdAt)));
		assertTrue("testPatch_IndividualDiscussionComments: like values are not equal", (received.getLike().equals(like)));
		assertTrue("testPatch_IndividualDiscussionComments: dislike values are not equal", (received.getDislike().equals(dislike)));
	}

	/*
	 * test case for GET /individual-discussion-comments-helpful
	 */
	@Test
	public void testGet_IndividualDiscussionCommentHelpful() {
		String accountId = "550";
		String commentId = "2";
		Boolean helpful = true;

		IndividualDiscussionCommentHelpful received = given().param("accountId", accountId).param("commentId", commentId).header("AJP_eppn", knownEPPN).expect()
				.statusCode(HttpStatus.OK.value()).when().get("/individual-discussion-comments-helpful").as(IndividualDiscussionCommentHelpful.class);

		assertTrue("testGet_IndividualDiscussionCommentHelpful: id values are not equal", (received.getId().equals("1")));
		assertTrue("testGet_IndividualDiscussionCommentHelpful: accountId values are not equal", (received.getAccountId().equals(accountId)));
		assertTrue("testGet_IndividualDiscussionCommentHelpful: commentId values are not equal", (received.getCommentId().equals(commentId)));
		assertTrue("testGet_IndividualDiscussionCommentHelpful: helpful values are not equal", (received.getHelpful().equals(helpful)));
	}

	/*
	 * test case 1 for POST /individual-discussion-comments-helpful
	 */
	@Test
	public void testPost_IndividualDiscussionCommentHelpfulWithValidAttributes() {
		IndividualDiscussionCommentHelpful commentHelpful = new IndividualDiscussionCommentHelpful();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussionCommentHelpfulJSONString = null;
		String accountId = "550";
		String commentId = "2";
		Boolean helpful = true;

		commentHelpful.setAccountId(accountId);
		commentHelpful.setCommentId(commentId);
		commentHelpful.setHelpful(helpful);

		try {
			postedIndividualDiscussionCommentHelpfulJSONString = mapper.writeValueAsString(commentHelpful);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		IndividualDiscussionCommentHelpful postedHelpful = given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN)
				.body(postedIndividualDiscussionCommentHelpfulJSONString).expect().statusCode(HttpStatus.CREATED.value()).when().post("/individual-discussion-comments-helpful")
				.as(IndividualDiscussionCommentHelpful.class);

		assertTrue("testPost_IndividualDiscussionCommentHelpfulWithValidAttributes: accountId values are not equal", (postedHelpful.getAccountId().equals(accountId)));
		assertTrue("testPost_IndividualDiscussionCommentHelpfulWithValidAttributes: commentId values are not equal", (postedHelpful.getCommentId().equals(commentId)));
		assertTrue("testPost_IndividualDiscussionCommentHelpfulWithValidAttributes: helpful values are not equal", (postedHelpful.getHelpful().equals(helpful)));

	}

	/*
	 * test case 2 for POST /individual-discussion-comments-helpful
	 */
	@Test
	public void testPost_IndividualDiscussionCommentHelpfulWithInvalidAccountId() {
		IndividualDiscussionCommentHelpful commentHelpful = new IndividualDiscussionCommentHelpful();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussionCommentHelpfulJSONString = null;
		String accountId = "0";
		String commentId = "2";
		Boolean helpful = true;

		commentHelpful.setAccountId(accountId);
		commentHelpful.setCommentId(commentId);
		commentHelpful.setHelpful(helpful);

		try {
			postedIndividualDiscussionCommentHelpfulJSONString = mapper.writeValueAsString(commentHelpful);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(postedIndividualDiscussionCommentHelpfulJSONString).expect()
				.statusCode(HttpStatus.UNAUTHORIZED.value()).when().post("/individual-discussion-comments-helpful");

	}

	/*
	 * test case 3 for POST /individual-discussion-comments-helpful
	 */
	@Test
	public void testPost_IndividualDiscussionCommentHelpfulWithInvalidCommentId() {
		IndividualDiscussionCommentHelpful commentHelpful = new IndividualDiscussionCommentHelpful();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussionCommentHelpfulJSONString = null;
		String accountId = "550";
		String commentId = "0";
		Boolean helpful = true;

		commentHelpful.setAccountId(accountId);
		commentHelpful.setCommentId(commentId);
		commentHelpful.setHelpful(helpful);

		try {
			postedIndividualDiscussionCommentHelpfulJSONString = mapper.writeValueAsString(commentHelpful);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(postedIndividualDiscussionCommentHelpfulJSONString).expect()
				.statusCode(HttpStatus.BAD_REQUEST.value()).when().post("/individual-discussion-comments-helpful");

	}

	/*
	 * test case 1 for PATCH /individual-discussion-comments-helpful/{helpfulID}
	 */
	@Test
	public void testPatch_IndividualDiscussionCommentHelpfulByIdWithGoodObject() {
		IndividualDiscussionCommentHelpful commentHelpful = new IndividualDiscussionCommentHelpful();
		ObjectMapper mapper = new ObjectMapper();
		String patchedIndividualDiscussionCommentHelpfulJSONString = null;
		String accountId = "550";
		String commentId = "3";
		Boolean helpfulBool = true;

		commentHelpful.setAccountId(accountId);
		commentHelpful.setCommentId(commentId);
		commentHelpful.setHelpful(helpfulBool);

		try {
			patchedIndividualDiscussionCommentHelpfulJSONString = mapper.writeValueAsString(commentHelpful);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		IndividualDiscussionCommentHelpful helpful = given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN)
				.body(patchedIndividualDiscussionCommentHelpfulJSONString).expect().statusCode(HttpStatus.OK.value()).when().patch("/individual-discussion-comments-helpful/" + 2)
				.as(IndividualDiscussionCommentHelpful.class);

		assertTrue("testPatch_IndividualDiscussionCommentHelpfulByIdWithGoodObject: id values are not equal", (helpful.getId().equals("2")));
		assertTrue("testPatch_IndividualDiscussionCommentHelpfulByIdWithGoodObject: accountId values are not equal", (helpful.getAccountId().equals(accountId)));
		assertTrue("testPatch_IndividualDiscussionCommentHelpfulByIdWithGoodObject: commentId values are not equal", (helpful.getCommentId().equals(commentId)));
		assertTrue("testPatch_IndividualDiscussionCommentHelpfulByIdWithGoodObject: helpful values are not equal", (helpful.getHelpful().equals(helpfulBool)));

	}

	/*
	 * test case 2 for PATCH /individual-discussion-comments-helpful/{helpfulID}
	 */
	@Test
	public void testPatch_IndividualDiscussionCommentHelpfulByIdWithInvalidAccount() {
		IndividualDiscussionCommentHelpful commentHelpful = new IndividualDiscussionCommentHelpful();
		ObjectMapper mapper = new ObjectMapper();
		String patchedIndividualDiscussionCommentHelpfulJSONString = null;
		String accountId = "0";
		String commentId = "3";
		Boolean helpful = true;

		commentHelpful.setAccountId(accountId);
		commentHelpful.setCommentId(commentId);
		commentHelpful.setHelpful(helpful);

		try {
			patchedIndividualDiscussionCommentHelpfulJSONString = mapper.writeValueAsString(commentHelpful);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(patchedIndividualDiscussionCommentHelpfulJSONString).expect()
				.statusCode(HttpStatus.UNAUTHORIZED.value()).when().patch("/individual-discussion-comments-helpful/" + 2);
	}

	/*
	 * test case 3 for PATCH /individual-discussion-comments-helpful/{helpfulID}
	 */
	@Test
	public void testPatch_IndividualDiscussionCommentHelpfulById() {
		IndividualDiscussionCommentHelpful commentHelpful = new IndividualDiscussionCommentHelpful();
		ObjectMapper mapper = new ObjectMapper();
		String patchedIndividualDiscussionCommentHelpfulJSONString = null;
		String accountId = "550";
		String commentId = "0";
		Boolean helpful = true;

		commentHelpful.setAccountId(accountId);
		commentHelpful.setCommentId(commentId);
		commentHelpful.setHelpful(helpful);

		try {
			patchedIndividualDiscussionCommentHelpfulJSONString = mapper.writeValueAsString(commentHelpful);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(patchedIndividualDiscussionCommentHelpfulJSONString).expect()
				.statusCode(HttpStatus.BAD_REQUEST.value()).when().patch("/individual-discussion-comments-helpful/" + 2);
	}

	/*
	 * test case for GET /individual-discussion-comments-flagged
	 */
	@Test
	public void testGet_IndividualDiscussionCommentFlagged() {
		IndividualDiscussionCommentFlagged flagInTable = new IndividualDiscussionCommentFlagged();
		String accountId = "550";
		String commentId = "1";
		String reason = "Bad";
		String comment = "Inappropriate";
		String userEPPN = "joeengineer";

		flagInTable.setId("1");
		flagInTable.setAccountId(accountId);
		flagInTable.setCommentId(commentId);
		flagInTable.setReason(reason);
		flagInTable.setComment(comment);

		IndividualDiscussionCommentFlagged receivedFlag = given().param("accountId", accountId).param("commentId", commentId).header("AJP_eppn", userEPPN).expect()
				.statusCode(HttpStatus.OK.value()).when().get("/individual-discussion-comments-flagged").as(IndividualDiscussionCommentFlagged.class);

		assertTrue("testGet_IndividualDiscussionCommentFlagged: expected flag in table doesn't match flag read with GET method", flagInTable.equals(receivedFlag));
	}

	/*
	 * test case 1 for POST /individual-discussion-comments-flagged
	 */
	@Test
	public void testPost_IndividualDiscussionCommentFlaggedWithValidObj() {
		IndividualDiscussionCommentFlagged flagToPost = new IndividualDiscussionCommentFlagged();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussionCommentFlaggedJSONString = null;
		String userEPPN = "fforgeadmin";
		String accountId = "102";
		String commentId = "2";
		String reason = "Bad";
		String comment = "Inappropriate";

		flagToPost.setAccountId(accountId);
		flagToPost.setCommentId(commentId);
		flagToPost.setReason(reason);
		flagToPost.setComment(comment);

		try {
			postedIndividualDiscussionCommentFlaggedJSONString = mapper.writeValueAsString(flagToPost);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		IndividualDiscussionCommentFlagged postedFlag = given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.body(postedIndividualDiscussionCommentFlaggedJSONString).expect().statusCode(HttpStatus.CREATED.value()).when().post("/individual-discussion-comments-flagged")
				.as(IndividualDiscussionCommentFlagged.class);

		assertTrue("testPost_IndividualDiscussionCommentFlaggedWithValidObj: accountId values are not equal", postedFlag.getAccountId().equals(accountId));
		assertTrue("testPost_IndividualDiscussionCommentFlaggedWithValidObj: commentId values are not equal", postedFlag.getCommentId().equals(commentId));
		assertTrue("testPost_IndividualDiscussionCommentFlaggedWithValidObj: reason values are not equal", postedFlag.getReason().equals(reason));
		assertTrue("testPost_IndividualDiscussionCommentFlaggedWithValidObj: comment values are not equal", postedFlag.getComment().equals(comment));

	}

	/*
	 * test case 2 for POST /individual-discussion-comments-flagged
	 */
	@Test
	public void testPost_IndividualDiscussionCommentFlaggedWithInvalidAccount() {
		IndividualDiscussionCommentFlagged flagToPost = new IndividualDiscussionCommentFlagged();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussionCommentFlaggedJSONString = null;
		String userEPPN = "joeengineer";
		String accountId = "0";
		String commentId = "2";
		String reason = "Bad";
		String comment = "Inappropriate";

		flagToPost.setAccountId(accountId);
		flagToPost.setCommentId(commentId);
		flagToPost.setReason(reason);
		flagToPost.setComment(comment);

		try {
			postedIndividualDiscussionCommentFlaggedJSONString = mapper.writeValueAsString(flagToPost);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).body(postedIndividualDiscussionCommentFlaggedJSONString).expect()
				.statusCode(HttpStatus.UNAUTHORIZED.value()).when().post("/individual-discussion-comments-flagged");

	}

	/*
	 * test case 3 for POST /individual-discussion-comments-flagged
	 */
	@Test
	public void testPost_IndividualDiscussionCommentFlaggedWithInvalidCommentId() {
		IndividualDiscussionCommentFlagged flagToPost = new IndividualDiscussionCommentFlagged();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussionCommentFlaggedJSONString = null;
		String userEPPN = "fforgeadmin";
		String accountId = "102";
		String commentId = "0";
		String reason = "Bad";
		String comment = "Inappropriate";

		flagToPost.setAccountId(accountId);
		flagToPost.setCommentId(commentId);
		flagToPost.setReason(reason);
		flagToPost.setComment(comment);

		try {
			postedIndividualDiscussionCommentFlaggedJSONString = mapper.writeValueAsString(flagToPost);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).body(postedIndividualDiscussionCommentFlaggedJSONString).expect()
				.statusCode(HttpStatus.BAD_REQUEST.value()).when().post("/individual-discussion-comments-flagged");

	}

	/*
	 * test case 1 for POST /individual-discussion-tags
	 */
	@Test
	public void testPost_IndividualDiscussionTagWithValidDiscussionId() {
		IndividualDiscussionTag tagToPost = new IndividualDiscussionTag();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussionTagJSONString = null;

		String individualDiscussionId = "3";
		String name = "tag3";

		tagToPost.setIndividualDiscussionId(individualDiscussionId);
		tagToPost.setName(name);

		try {
			postedIndividualDiscussionTagJSONString = mapper.writeValueAsString(tagToPost);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		IndividualDiscussionTag postedTag = given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(postedIndividualDiscussionTagJSONString).expect()
				.statusCode(HttpStatus.CREATED.value()).when().post("/individual-discussion-tags").as(IndividualDiscussionTag.class);

		assertTrue("testPost_IndividualDiscussionTagWithValidDiscussionId: individual discussion id values are not equal",
				(postedTag.getIndividualDiscussionId().equals(individualDiscussionId)));
		assertTrue("testPost_IndividualDiscussionTagWithValidDiscussionId: name values are not equal", (postedTag.getName().equals(name)));
	}

	/*
	 * test case 2 for POST /individual-discussion-tags
	 */
	@Test
	public void testPost_IndividualDiscussionTagWithInvalidDiscussionId() {
		IndividualDiscussionTag tagToPost = new IndividualDiscussionTag();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussionTagJSONString = null;

		String individualDiscussionId = "0";
		String name = "tag3";

		tagToPost.setIndividualDiscussionId(individualDiscussionId);
		tagToPost.setName(name);

		try {
			postedIndividualDiscussionTagJSONString = mapper.writeValueAsString(tagToPost);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		given().header("Content-type", "application/json").header("AJP_eppn", knownEPPN).body(postedIndividualDiscussionTagJSONString).expect()
				.statusCode(HttpStatus.BAD_REQUEST.value()).when().post("/individual-discussion-tags");

	}
	
	
	/*
	 * test case for DELETE /individual-discussion-tags/{disscusionTagID}
	 */
	@Test
	public void testDelete_IndividualDiscussionTag(){
		given().
		header("AJP_eppn", knownEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().delete("/individual-discussion-tags/" + disscusionTagId);
	}
	
}