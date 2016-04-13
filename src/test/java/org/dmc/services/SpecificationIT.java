package org.dmc.services;


import org.dmc.services.services.specifications.ArraySpecifications;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	/*
	 * test GET /array_specifications
	 */
	@Test
	public void testGet_ArraySpecification(){
		given().
		header("AJP_eppn","user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("array_specifications");	
	}
	
	
	/*
	 * test case for POST /array_specifications
	 */
	@Test
	public void testProductPost_ProductReview() {
		ArraySpecifications obj = new ArraySpecifications();
		ObjectMapper mapper = new ObjectMapper();
		String postedArraySpecificationJSONString = null;
		try {
			postedArraySpecificationJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", "user_EPPN").
		body(postedArraySpecificationJSONString).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		post("/array_specifications");
	}
	
	
	
}