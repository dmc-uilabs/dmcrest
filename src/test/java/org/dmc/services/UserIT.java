package org.dmc.services;

import org.junit.Test;

import java.util.Date;
import java.text.SimpleDateFormat;

import static com.jayway.restassured.RestAssured.*;
import static org.junit.Assert.*;

import org.json.JSONObject;

/*
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.ResponseSpecification;
import static com.jayway.restassured.RestAssured.given;
*/
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class UserIT extends BaseIT {

    @Test
	public void testUserIncorrectInvocation(){
		JSONObject json = new JSONObject();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);
		
        // callin enpoint with content is payload, which is not used when received.
		json.put("user_name", "username " + unique);
		json.put("email", "testemail" + unique + "@ge.com");
		json.put("password", "pwd " + unique);
		json.put("name", "usertester " + unique);
		
		
		Integer id = given().body(json.toString()).expect().statusCode(200).when().post("/users/create").then()
		.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).extract().path("id");
        
        assertTrue("Valid user ID return.  Value is " + id + ", but should be -99999.", id == -99999);
	}
    
    @Test
	public void testUserCreate(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);
        
        Integer id =
        given().
        header("Content-type", "text/plain").
        header("AJP_eppn", "userEPPN" + unique).
        header("AJP_givenName", "userGivenName" + unique).
        header("AJP_sn", "userSurname" + unique).
        header("AJP_displayName", "userDisplayName" + unique).
        header("AJP_mail", "userEmail" + unique).
        expect().
        statusCode(200).
		when().
        post("/users/create").
		then().
        body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).
        extract().path("id");

        // check return value > 0
        assertTrue("Added user: " + "userEPPN" + unique + " Valid user ID must be greater then zero.  Value is " + id + ".", id > 0);
        // could also check email syntax
        
	}

	
    @Test
	public void testUserGet_UnknownUser(){
        String unknownUser = "unknown";
        
		given().
        header("AJP_eppn", unknownUser).
		expect().
        statusCode(200).
		when().
        get("/user").
		then().
        body(matchesJsonSchemaInClasspath("Schemas/userSchema.json"));
	}

    @Test
	public void testUserGet_KnownUser(){
        String knownUser = "fforgeadmin";
        
		given().
        header("AJP_eppn", knownUser).
		expect().
        statusCode(200).
		when().
        get("/user").
		then().
        body(matchesJsonSchemaInClasspath("Schemas/userSchema.json"));
	}
}