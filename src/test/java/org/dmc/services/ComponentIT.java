package org.dmc.services;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static com.jayway.restassured.RestAssured.*;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class ComponentIT extends BaseIT {
	private String knownUserEPPN = "fforgeadmin";
    

	@Test
	public void testComponent1() {
	  expect().
	    statusCode(200).
	    when().
	    get("/components/1").then().
        body(matchesJsonSchemaInClasspath("Schemas/componentSchema.json")); 
	}
	
	/*@Test
	public void testComponentList(){
		expect().statusCode(200).when().get("/components").then().
        body(matchesJsonSchemaInClasspath("Schemas/componentListSchema.json"));
	}*/
	
	@Test
	public void testComponents(){
		given().header("AJP_eppn", knownUserEPPN).expect().statusCode(405).when().get("/components");
	}

	
	@Test
	public void testProjectComponents(){
		expect().statusCode(200).when().get("/projects/6/components").then().
		body(matchesJsonSchemaInClasspath("Schemas/componentListSchema.json"));
	}
	
}