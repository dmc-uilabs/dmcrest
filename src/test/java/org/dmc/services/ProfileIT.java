package org.dmc.services;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.junit.Before; 
import org.junit.After;
import static org.junit.Assert.*;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.dmc.services.utility.TestUserUtil;


public class ProfileIT extends BaseIT {
	
	private static final String PROFILE_CREATE_RESOURCE = "/profiles";
	private static final String PROFILE_READ_RESOURCE   = "/profiles/{id}";
	private static final String PROFILE_UPDATE_RESOURCE = "/profiles/{id}";
	private static final String PROFILE_DELETE_RESOURCE = "/profiles/{id}/delete";
	private String profileId = "1";
	
	private Integer createdId = -1;
//	String randomEPPN = UUID.randomUUID().toString();
    String unique = null;
		
	// Setup test data
	@Before
	//@Test
	public void testProfileCreate() {
//        ServiceLogger.log(logTag, "starting testUserCreate");
        unique = TestUserUtil.generateTime();
        
//        Integer id =
        given().
        header("Content-type", "text/plain").
        header("AJP_eppn", "userEPPN" + unique).
        header("AJP_givenName", "userGivenName" + unique).
        header("AJP_sn", "userSurname" + unique).
        header("AJP_displayName", unique).
        header("AJP_mail", "userEmail" + unique).
        expect().
        statusCode(200).
		when().
        get("/user");
//		then().
//        body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).
//        extract().path("id");

        
		JSONObject json = createFixture("create");
		this.createdId = given()
            .header("Content-type", "application/json")
				.header("AJP_eppn", "userEPPN" + unique)
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
	public void testProfileGet() {
        
		JSONObject json = createFixture("update");
        if (this.createdId > 0) {
            Integer retrivedId = given()
                .header("AJP_eppn", "userEPPN" + unique)
            .expect()
                .statusCode(200)
            .when()
                .get(PROFILE_READ_RESOURCE, this.createdId.toString())
            .then()
                .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                .extract()
                .path("id");
            assertTrue("Retrieved Id is not the same as newly created user's id", this.createdId.equals(retrivedId));
            assertTrue("Retrieved Id is " + retrivedId, retrivedId > 0);
        }
	}
    
	@Test
	public void testProfilePatch() {
        
		JSONObject json = createFixture("update");
			if (this.createdId > 0) {
				Integer retrivedId =
                given()
                    .header("Content-type", "application/json")
                    .header("AJP_eppn", "userEPPN" + unique)
                    .body(json.toString())
				.expect()
                    .statusCode(200)
				.when()
                    .patch(PROFILE_UPDATE_RESOURCE, this.createdId.toString())
				.then()
                    .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                    .extract()
                    .path("id");
                
                assertTrue("Retrieved Id is not the same as newly created user's id", this.createdId.equals(retrivedId));
                assertTrue("Retrieved Id is " + retrivedId, retrivedId > 0);
			}
	}
	
	// Cleanup
	@After  
	public void testProfileDelete() {
		if (this.createdId > 0) {
			given()
			.header("AJP_eppn", "userEPPN" + unique)
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
		json.put("company", "1");
		json.put("jobTitle", "test jobTitle " + type);
		json.put("phone", "test phone " + type);
		json.put("email", "test email " + type);
		json.put("location", "test location " + type);
		json.put("image", "test image " + type);
		json.put("description", "test description " + type);
		json.put("skills", skills);
        
		return json;
	}
	
	
	/**
	 * test case for GET /profiles
	 */
	@Test
	public void testProfileGet_Profiles(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get(PROFILE_CREATE_RESOURCE);
	}
	
	
	/**
	 * test case for GET /profiles/{profileID}/profile_history
	 */
	@Test
	public void testProfileGet_ProfileHistory(){
		given().
		header("AJP_eppn", userEPPN).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/profiles/" + profileId +"/profile_history");
	}
	
	/**
	 * test case for GET /profiles/{profileID}/profile_reviews
	 */
	@Test
	public void testProfileGet_ProfileReview(){
		given().
		header("AJP_eppn", userEPPN).
		param("reviewId", "1").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/profiles/" + profileId +"/profile_reviews");
	}

	
	/**
	 * test case for GET /profiles/{profileID}/following_members
	 */
	@Test
	public void testProfileGet_ProfileFollowingMember(){
		given().
		header("AJP_eppn", userEPPN).
		param("reviewId", "1").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().get("/profiles/" + profileId +"/following_members");
	}
	
	
}