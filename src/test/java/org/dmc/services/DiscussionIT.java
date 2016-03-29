package org.dmc.services;

import org.dmc.services.discussions.Discussion;
import org.dmc.services.discussions.DiscussionController;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.Date;
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
	
	private Integer createdId = null;
	String randomEPPN = UUID.randomUUID().toString();
	
	@Before
	public void testDiscussionCreate() {
		JSONObject json = createFixture();
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

		       assertTrue("", newDiscussionList.size() == originalDiscussionList.size()+1);
    	}

	}

	public JSONObject createFixture() {
			
		JSONObject json = new JSONObject();
		json.put("title", "test discussion title");
		json.put("message", "test discussion message");
		json.put("createdBy", "test-disc-created-by");
		json.put("createdAt", 1232000);
		json.put("accountId", "123");
		json.put("projectId", "123");
		
		return json;
	}

}