package org.dmc.services;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.junit.Ignore;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class ProfileIT extends BaseIT {
	
	private static final String PROFILE_CREATE_RESOURCE = "/profiles";
	private static final String PROFILE_UPDATE_RESOURCE = "/profiles/{id}";
	private static final String PROFILE_DELETE_RESOURCE = "/profiles/{id}/delete";
	private Integer createdId = -1;
	String randomEPPN = UUID.randomUUID().toString();
		
	// Setup test data
	@Before
	//@Test
	public void testProfileCreate() {
		JSONObject json = createFixture("create");
		this.createdId = given()
				.header("AJP_eppn", randomEPPN)
				.body(json.toString())
				.expect()
				.statusCode(200)
				.when()
				.post(PROFILE_CREATE_RESOURCE)
				.then()
				.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
				.extract()
				.path("id");
	}
	
	@Test
	public void testProfilePatch() {
		JSONObject json = createFixture("update");
			if (this.createdId > 0) {
				given()
				.header("AJP_eppn", randomEPPN)
				.body(json.toString())
				.expect()
				.statusCode(200)
				.when()
				.patch(PROFILE_UPDATE_RESOURCE, this.createdId.toString())
				.then()
				.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
				.extract()
				.path("id");	
			}
	}
	
	// Cleanup
	@After  
	public void testProfileDelete() {
		if (this.createdId > 0) {
			given()
			.header("AJP_eppn", randomEPPN)
			.expect().statusCode(200)
			.when()
			.get(PROFILE_DELETE_RESOURCE, this.createdId.toString())
			.then()
			.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));	
		}
	}
	
	public JSONObject createFixture(String type) {
		
		JSONObject json = new JSONObject();
		ArrayList<String> skills = new ArrayList<String>();
		skills.add("Skill one " + type);
		skills.add("Skill two " + type);
		skills.add("Skill three " + type);
		
		json.put("displayName", "test displayName " + type);
		json.put("jobTitle", "test jobTitle " + type);
		json.put("phone", "test phone " + type);
		json.put("email", "test email " + type);
		json.put("location", "test location " + type);
		json.put("image", "test image " + type);
		json.put("description", "test description " + type);
		json.put("skills", skills);
        
		return json;
	}

}