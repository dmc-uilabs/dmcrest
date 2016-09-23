package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

import java.util.UUID;

import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;


public class UserBasicInformationIT extends BaseIT {  
	
	private static final String USER_CREATE_RESOURCE = "/users/create";
	private static final String UPDATE_RESOURCE = "/user-basic-information";
	private Integer createdId = -1;
	private String testUser = null;
	String randomEPPN = UUID.randomUUID().toString();

    @Before
	public void testUserCreate(){
    	
		String unique = TestUserUtil.generateTime();
		testUser = randomEPPN + "-" + unique;
        createdId =
        given().
        header("Content-type", "text/plain").
        header("AJP_eppn", testUser).
        header("AJP_givenName", "userGivenName" + unique).
        header("AJP_sn", "userSurname" + unique).
        header("AJP_displayName", "userDisplayName" + unique).
        header("AJP_mail", "userEmail" + unique).
        expect().
        statusCode(200).
		when().
        post(USER_CREATE_RESOURCE).
		then().
        body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).
        extract().path("id");
	}

	@Test
	public void basicInformation() {
		if (this.createdId != -1) {
			JSONObject json = createFixture("normal");
			given()
			.header("Content-type", "application/json")
			.header("AJP_eppn", testUser)
			.body(json.toString())
			.expect()
			.statusCode(200)
			.when()
			.post(UPDATE_RESOURCE)
			.then()
			.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
			// Be sure we've updated the correct user
			.body("id", equalTo(this.createdId));
		}	
	}
	
	@Test
	public void basicInformationMissingFields() {
		if (this.createdId != -1) {
			JSONObject json = createFixture("missing");
			given()
			.header("Content-type", "application/json")
			.header("AJP_eppn", testUser)
			.body(json.toString())
			.expect()
			.statusCode(200)
			.when()
			.post(UPDATE_RESOURCE)
			.then()
			.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
			// Be sure we've updated the correct user
			.body("id", equalTo(this.createdId));
		}	
	}

    @Test
	public void basicInformationMissingCompanyField() {
		if (this.createdId != -1) {
			JSONObject json = createFixture("missingCompany");
			given()
			.header("Content-type", "application/json")
			.header("AJP_eppn", testUser)
			.body(json.toString())
			.expect()
			.statusCode(200)
			.when()
			.post(UPDATE_RESOURCE)
			.then()
			.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
			// Be sure we've updated the correct user
			.body("id", equalTo(-99999));
		}
	}

    
	@Test
	public void basicInformationEmptyFields() {
		if (this.createdId != -1) {
			JSONObject json = createFixture("empty");
			given()
			.header("Content-type", "application/json")
			.header("AJP_eppn", testUser)
			.body(json.toString())
			.expect()
			.statusCode(200)
			.when()
			.post(UPDATE_RESOURCE)
			.then()
			.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
			// Be sure we've updated the correct user
			.body("id", equalTo(this.createdId));
		}	
	}
	
	@Test
	public void basicInformationMissingEPPNHeader() {
		if (this.createdId != -1) {
			JSONObject json = createFixture("normal");
			given()
			.header("Content-type", "application/json")
			.body(json.toString())
			.expect()
			.statusCode(403)
			.when()
			.post(UPDATE_RESOURCE)
			.then()
			.body(matchesJsonSchemaInClasspath("Schemas/errorSchema.json"));
		}	
	}

	public JSONObject createFixture(String type) {
		
		JSONObject json = new JSONObject();
		
		if (type.equals("normal")) {
			json.put("email", "test basic info email");
			json.put("firstName", "test basic info first name");
			json.put("lastName", "test basic info last name");
			json.put("company", "1");
		} else if (type.equals("missing")) {
			json.put("email", "test basic info email");
			json.put("company", "1");
        } else if (type.equals("missingCompany")) {
			json.put("email", "test basic info email");
		} else if(type.equals("empty")) {
			json.put("email", "test basic info email");
			json.put("firstName", "");
			json.put("lastName", "test basic info last name");
			json.put("company", "1");
		}
		
		return json;
	}

}