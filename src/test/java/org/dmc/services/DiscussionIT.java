package org.dmc.services;

import org.dmc.services.discussions.Discussion;
import org.dmc.services.discussions.DiscussionController;
import org.dmc.services.discussions.FollowingIndividualDiscussion;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.jayway.restassured.RestAssured.*;

import java.util.ArrayList;
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
	
	/**
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
	
	
	/**
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
	
	
	/**
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

	
	
	/**
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
}