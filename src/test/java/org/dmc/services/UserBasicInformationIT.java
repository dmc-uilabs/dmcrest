package org.dmc.services;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.junit.Ignore;

import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class UserBasicInformationIT extends BaseIT {  
	
	private static final String USER_CREATE_RESOURCE = "/users/create";
	private static final String UPDATE_RESOURCE = "/user-basic-information";
	private Integer createdId = -1;
	String randomEPPN = UUID.randomUUID().toString();

    @Before
	public void testUserCreate(){
    	
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);
        
        createdId =
        given().
        header("Content-type", "text/plain").
        header("AJP_eppn", randomEPPN).
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
			.header("AJP_eppn", randomEPPN)
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
			.header("AJP_eppn", randomEPPN)
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
			.header("AJP_eppn", randomEPPN)
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
			.header("AJP_eppn", randomEPPN)
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
			.statusCode(200)
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