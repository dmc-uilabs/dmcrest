package org.dmc.services;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class SpecificationIT extends BaseIT {
	
	@Test
	public void testSpecification13() {
	  expect().
	    statusCode(200).
	    when().
	    get("/services/12/specifications").then().
        body(matchesJsonSchemaInClasspath("Schemas/specificationSchema.json"));
	}
	
	@Test
	public void testSpecification1() {
		expect().statusCode(200).when().get("/services/1/specifications").then().
        body(matchesJsonSchemaInClasspath("Schemas/specificationSchema.json"));
	}
}