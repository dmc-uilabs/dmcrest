package org.dmc.services;

import org.dmc.services.components.Component;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Test
	public void testComponentList(){
		expect().statusCode(200).when().get("/components").then().
        body(matchesJsonSchemaInClasspath("Schemas/componentListSchema.json"));
	}
	
	/*@Test
	public void testComponents(){
		given().header("AJP_eppn", knownUserEPPN).expect().statusCode(405).when().get("/components");
	}
*/
	
	@Test
	public void testProjectComponents(){
		expect().statusCode(200).when().get("/projects/6/components").then().
		body(matchesJsonSchemaInClasspath("Schemas/componentListSchema.json"));
	}
	
	
	/**
	 * test case for post /components
	 */
	@Test
	public void testServicePost_Component(){
		Component obj = null;
		ObjectMapper mapper = new ObjectMapper();
		String postedcomponentJSONString = null;
		try {
			postedcomponentJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
        header("Content-type", "application/json").
        header("AJP_eppn", knownUserEPPN).
        body(postedcomponentJSONString).
	expect().
        statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
	when().
        post("/components");
	}
	
	
}