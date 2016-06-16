package org.dmc.services;

import org.dmc.services.discussions.Discussion;
import org.dmc.services.discussions.DiscussionController;
import org.dmc.services.discussions.FollowingIndividualDiscussion;
import org.dmc.services.discussions.IndividualDiscussion;
import org.dmc.services.discussions.IndividualDiscussionComment;
import org.dmc.services.discussions.IndividualDiscussionCommentFlagged;
import org.dmc.services.discussions.IndividualDiscussionCommentHelpful;
import org.dmc.services.discussions.IndividualDiscussionTag;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.jayway.restassured.RestAssured.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;

//@Ignore
public class DiscussionIT extends BaseIT {
	
	private final String logTag = DiscussionController.class.getName();
	private static final String PROFILE_CREATE_RESOURCE = "/profiles";
	private static final String ALL_DISCUSSIONS_RESOURCE = "/all-discussions";
	private static final String DISCUSSION_CREATE_RESOURCE = "/discussions/create";
	private String followId = "1";
	private String individualDiscussionID = "1";
	private String commentId = "1";
	private String helpfulId = "1";
	private String disscusionTagId = "1";
	private String accountId = "1";
	
	private Integer createdId = null;
	String randomEPPN = UUID.randomUUID().toString();
	
	@Before
	public void testDiscussionCreate() {
		this.createdId = createDiscussion(null);
	}
	
    @Test
	public void testAllDiscussions() {
    	if (this.createdId != null) {
			ArrayList<Discussion> originalDiscussionList =
		        given().
		            header("Content-type", "application/json").
		            header("AJP_eppn", randomEPPN).
		        expect().
		            statusCode(200).
		        when().
		            get(ALL_DISCUSSIONS_RESOURCE).as(ArrayList.class);
			
		        // add a discussion
		        String savedRandomEPPN = randomEPPN;
		        randomEPPN = UUID.randomUUID().toString();
		        testDiscussionCreate();
		        randomEPPN = savedRandomEPPN;
		        
		        ArrayList<Discussion> newDiscussionList =
		        given().
		            header("Content-type", "application/json").
		            header("AJP_eppn", randomEPPN).
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
				.header("AJP_eppn", randomEPPN)
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
		header("AJP_eppn", userEPPN).
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
		header("AJP_eppn", userEPPN).
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
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/following_discussions");
	}
	
	
	/*
	 * test case for PATCH /follow_discussions
	 */
	@Test
	public void testPatch_followDiscussion(){
		FollowingIndividualDiscussion obj = new FollowingIndividualDiscussion();
		ObjectMapper mapper = new ObjectMapper();
		String patchedFollowDiscussionsJSONString = null;
		try {
			patchedFollowDiscussionsJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
        header("Content-type", "application/json").
        header("AJP_eppn", userEPPN).
        body(patchedFollowDiscussionsJSONString).
	expect().
        statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
	when().
        patch("/follow_discussions");
	}

	
	/*
	 * test case for DELETE /follow_discussions/{followID}
	 */
	@Test
	public void testDelete_FollowDiscussions(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().delete("/follow_discussions/" + followId);
	}
	
	
	/*
	 * test case for GET /individual-discussion
	 */
	@Test
	public void testGet_IndividualDiscussion() {
		List<IndividualDiscussion> received = Arrays.asList(given().header("AJP_eppn", userEPPN).param("limit", 3).param("order", "ASC").expect().statusCode(HttpStatus.OK.value())
				.when().get("/individual-discussion").as(IndividualDiscussion[].class));

		assertTrue("testGet_IndividualDiscussion: limit parameter did not work", received.size() == 3);

		assertTrue("testGet_IndividualDiscussion: title values are not equal", (received.get(0).getTitle().equals("For Community")));
		assertTrue("testGet_IndividualDiscussion: createdBy values are not equal", (received.get(0).getCreatedBy().equals("John")));
		assertTrue("testGet_IndividualDiscussion: createdAt values are not equal", (received.get(0).getCreatedAt().equals(new BigDecimal("12345"))));
		assertTrue("testGet_IndividualDiscussion: accountId values are not equal", (received.get(0).getAccountId().equals(new BigDecimal(550))));
	}

	/*
	 * test case for POST /individual-discussion
	 */
	@Test
	public void testPost_IndividualDiscussion() {
		IndividualDiscussion obj = new IndividualDiscussion();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussion = null;

		String title = "For POST /individual-discussion";
		String createdBy = "Eminem";
		BigDecimal createdAt = new BigDecimal(12301293);
		BigDecimal accountId = new BigDecimal(550);
		BigDecimal projectId = new BigDecimal(12);

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

		IndividualDiscussion posted = given().header("Content-type", "application/json").header("AJP-eppn", userEPPN).body(postedIndividualDiscussion).expect()
				.statusCode(HttpStatus.OK.value()).when().post("/individual-discussion").as(IndividualDiscussion.class);

		assertTrue("testPost_IndividualDiscussion: title values are not equal", (posted.getTitle().equals(title)));
		assertTrue("testPost_IndividualDiscussion: createdBy values are not equal", (posted.getCreatedBy().equals(createdBy)));
		assertTrue("testPost_IndividualDiscussion: createdAt values are not equal", (posted.getCreatedAt().equals(createdAt)));
		assertTrue("testPost_IndividualDiscussion: accountId values are not equal", (posted.getAccountId().equals(accountId)));
		assertTrue("testPost_IndividualDiscussion: projectId values are not equal", (posted.getProjectId().equals(projectId)));
	}
	
	/*
	 * test case for GET /individual-discussion/{individualDiscussionID}
	 */
	@Test
	public void testGet_IndividualDiscussionFromId(){
		IndividualDiscussion read = given().param("commentId", commentId).header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.OK.value()).when()
				.get("/individual-discussion/" + 3).as(IndividualDiscussion.class);

		assertTrue("testGet_IndividualDiscussionFromId: title values are not equal", (read.getTitle().equals("For Project")));
		assertTrue("testGet_IndividualDiscussionFromId: createdBy values are not equal", (read.getCreatedBy().equals("Google")));
		assertTrue("testGet_IndividualDiscussionFromId: createdAt values are not equal", (read.getCreatedAt().equals(new BigDecimal(12345))));
		assertTrue("testGet_IndividualDiscussionFromId: accountId values are not equal", (read.getAccountId().equals(new BigDecimal(550))));
		assertTrue("testGet_IndividualDiscussionFromId: projectId values are not equal", (read.getProjectId().equals(new BigDecimal(12))));
	}
	
	
	/*
	 * test case for GET /individual-discussion/{individualDiscussionID}/individual-discussion-comments
	 */
	@Test
	public void testGet_IndividualDiscussionCommentsFromIndividualDiscussionId(){
		given().param("commentId", commentId).
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/individual-discussion/" + individualDiscussionID + "/individual-discussion-comments");
	}
	
	
	/*
	 * test case for GET /individual-discussion/{individualDiscussionID}/individual-discussion-tags
	 */
	@Test
	public void testGet_IndividualDiscussionTag(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/individual-discussion/" + individualDiscussionID + "/individual-discussion-tags");
	}
	
	
	/*
	 * test case for GET /individual-discussion-comments
	 */
	@Test
	public void testGet_IndividualDiscussionComments(){
		List <IndividualDiscussionComment> listOfComments = Arrays.asList(given().
		header("AJP_eppn", userEPPN).param("_limit",  2).param("_order", "ASC").param("commentId", 0).param("individual-discussionId", 1).param("individual-discussionId", 3).
		expect().
		statusCode(HttpStatus.OK.value()).
		when().get("/individual-discussion-comments").as(IndividualDiscussionComment[].class));
		
		for (int i = 0; i < listOfComments.size(); i++) {
			System.out.println(listOfComments.get(i).toString());
			assertTrue("testGet_IndividualDiscussionComments: individualDiscussionId values are not equal", (listOfComments.get(i).getIndividualDiscussionId().equals("1")));
		}
		
		assertTrue("testGet_IndividualDiscussionComments: id values are not equal", (listOfComments.get(0).getId().equals("1")));
		assertTrue("testGet_IndividualDiscussionComments: fullName values are not equal", (listOfComments.get(0).getFullName().equals("Joe")));
		assertTrue("testGet_IndividualDiscussionComments: accountId values are not equal", (listOfComments.get(0).getAccountId().equals(new BigDecimal(550))));
		assertTrue("testGet_IndividualDiscussionComments: commentId values are not equal", (listOfComments.get(0).getCommentId().equals(new BigDecimal(0))));
	}
	
	/*
	 * test case for POST /individual-discussion-comments
	 */
	@Test
	public void testPost_IndividualDiscussionComments(){
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

		IndividualDiscussionComment postedCommentObj = given().header("Content-type", "application/json").header("AJP-eppn", userEPPN).body(postedCommentStr).expect()
				.statusCode(HttpStatus.OK.value()).when().post("/individual-discussion-comments").as(IndividualDiscussionComment.class);

		assertTrue("testPost_IndividualDiscussionComment: individualDiscussionId values are not equal", (postedCommentObj.getIndividualDiscussionId().equals(individualDiscussionId)));
		assertTrue("testPost_IndividualDiscussionComment: fullName values are not equal", (postedCommentObj.getFullName().equals(fullName)));
		assertTrue("testPost_IndividualDiscussionComment: accountId values are not equal", (postedCommentObj.getAccountId().equals(accountId)));
		assertTrue("testPost_IndividualDiscussionComment: commentId values are not equal", (postedCommentObj.getCommentId().equals(commentId)));
		assertTrue("testPost_IndividualDiscussionComment: avatar values are not equal", (postedCommentObj.getAvatar().equals(avatar)));
		assertTrue("testPost_IndividualDiscussionComment: reply values are not equal", (postedCommentObj.getReply().equals(reply)));
		assertTrue("testPost_IndividualDiscussionComment: text values are not equal", (postedCommentObj.getText().equals(text)));
		assertTrue("testPost_IndividualDiscussionComment: createdAt values are not equal", (postedCommentObj.getCreatedAt().equals(createdAt)));
		assertTrue("testPost_IndividualDiscussionComment: like values are not equal", (postedCommentObj.getLike().equals(like)));
		assertTrue("testPost_IndividualDiscussionComment: dislike values are not equal", (postedCommentObj.getDislike().equals(dislike)));
	}
	
	
	/*
	 * test case for PATCH /individual-discussion-comments/{commentID}
	 */
	
	@Test
	public void testPatch_IndividualDiscussionComments(){
		IndividualDiscussionComment obj = new IndividualDiscussionComment();
		ObjectMapper mapper = new ObjectMapper();
		String patchedIndividualDiscussionCommentJSONString = null;
		
		try {
			patchedIndividualDiscussionCommentJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
		header("Content-type", "application/json").
		header("AJP-eppn", userEPPN).
		body(patchedIndividualDiscussionCommentJSONString).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		patch("/individual-discussion-comments/" + commentId);
	}
	
	
	
	/*
	 * test case for DELETE /individual-discussion-comments/{commentID}
	 */
	@Test
	public void testDelete_IndividualDiscussionComment(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().delete("/individual-discussion-comments/" + commentId);
	}
	
	
	/*
	 * test case for GET /individual-discussion-comments-helpful
	 */
	@Test
	public void testGet_IndividualDiscussionCommentHelpful(){
		given().
		param("accountId", accountId).
		param("commentId", commentId).
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/individual-discussion-comments-helpful");
	}
	
	
	/*
	 * test case for POST /individual-discussion-comments-helpful
	 */
	@Test
	public void testPost_IndividualDiscussionCommentHelpful(){
		IndividualDiscussionCommentHelpful obj = new IndividualDiscussionCommentHelpful();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussionCommentHelpfulJSONString = null;
		
		try {
			postedIndividualDiscussionCommentHelpfulJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", userEPPN).
		body(postedIndividualDiscussionCommentHelpfulJSONString).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().post("/individual-discussion-comments-helpful");
	}
	
	
	/*
	 * test case for PATCH /individual-discussion-comments-helpful/{helpfulID}
	 */
	@Test
	public void testPatch_IndividualDiscussionCommentHelpfulById(){
		IndividualDiscussionCommentHelpful obj = new IndividualDiscussionCommentHelpful();
		ObjectMapper mapper = new ObjectMapper();
		String patchedIndividualDiscussionCommentHelpfulJSONString = null;
		
		try {
			patchedIndividualDiscussionCommentHelpfulJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", userEPPN).
		body(patchedIndividualDiscussionCommentHelpfulJSONString).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().patch("/individual-discussion-comments-helpful/" + helpfulId);
	}
	
	
	/*
	 * test case for GET /individual-discussion-comments-flagged
	 */
	@Test
	public void testGet_IndividualDiscussionCommentflagged(){
		given().
		param("accountId", accountId).
		param("commentId", commentId).
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/individual-discussion-comments-flagged");
	}
	
	
	/*
	 * test case for POST /individual-discussion-comments-flagged
	 */
	@Test
	public void testPost_IndividualDiscussionCommentflagged(){
		IndividualDiscussionCommentFlagged obj = new IndividualDiscussionCommentFlagged();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussionCommentFlaggedJSONString = null;
		
		try {
			postedIndividualDiscussionCommentFlaggedJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", userEPPN).
		body(postedIndividualDiscussionCommentFlaggedJSONString).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().post("/individual-discussion-comments-flagged");
	}
	
	
	
	/*
	 * test case for POST /individual-discussion-tags
	 */
	@Test
	public void testPost_IndividualDiscussionTag(){
		IndividualDiscussionTag obj = new IndividualDiscussionTag();
		ObjectMapper mapper = new ObjectMapper();
		String postedIndividualDiscussionTagJSONString = null;
		
		try {
			postedIndividualDiscussionTagJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", userEPPN).
		body(postedIndividualDiscussionTagJSONString).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().post("/individual-discussion-tags");
	}
	
	
	/*
	 * test case for DELETE /individual-discussion-tags/{disscusionTagID}
	 */
	@Test
	public void testDelete_IndividualDiscussionTag(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().delete("/individual-discussion-tags/" + disscusionTagId);
	}
	
}