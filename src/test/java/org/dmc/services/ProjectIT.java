package org.dmc.services;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;

import org.json.JSONObject;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class ProjectIT extends BaseIT {

	@Test
	public void testProject6() {
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(200).
		when().
			get("/projects/6").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectSchema.json"));
	}
	
	@Test
	public void testProject5(){
		
		//TODO: need to update to another demo project
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(200).
		when().
			get("/projects/6").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectSchema.json"));		
	}
	
	@Test
	public void testProjectList(){
		given().
			header("AJP_eppn", userEPPN).
		expect().
			statusCode(200).
		when().
			get("/projects").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/projectListSchema.json"));
	}


	// leaving this as an example of how to work with parameters to URL
	// instead of json, but json is probably preferable
	@Test
	public void testProjectCreateParameter(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);
		given().
			header("AJP_eppn", userEPPN).
			param("projectname", "junitTestParam" + unique).
			param("unixname", "junitParam" + unique).
		expect().
			statusCode(200).
		when().
			post("/projects/createWithParameter").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
	}
	

	@Test
	public void testProjectCreateJson(){
		JSONObject json = new JSONObject();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);

		String input = "{\"projectname\": " + "junitTest" + unique + ", {\"comment\": \"test " + date.getTime() + "\"}";
		
		json.put("projectname", "junitTest" + unique);
		json.put("unixname", "junit" + unique);
		System.out.println("json = " + json.toString());
		given().
			header("AJP_eppn", userEPPN).
			body(json.toString()).
		expect().
			statusCode(200).
		when().
			post("/projects/create").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));
	}

	@Test
	public void testProjectCreateFailOnDuplicate(){
		JSONObject json = new JSONObject();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String unique = format.format(date);

		String input = "{\"projectname\": " + "junitTestdup" + unique + ", {\"comment\": \"test " + date.getTime() + "\"}";
		
		json.put("projectname", "junitTestdup" + unique);
		json.put("unixname", "junitdup" + unique);

		// first time should work
		given().
			header("AJP_eppn", userEPPN).
			body(json.toString()).
		expect().
			statusCode(200).
		when().
			post("/projects/create").
		then().
			body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"));

		// second time should fail, because unixname is a duplicate
		given().
			header("AJP_eppn", userEPPN).
			body(json.toString()).
		expect().
			statusCode(200).
		when().
			post("/projects/create").
		then().
			log().all().
			body(matchesJsonSchemaInClasspath("Schemas/errorSchema.json"));
	}
}
