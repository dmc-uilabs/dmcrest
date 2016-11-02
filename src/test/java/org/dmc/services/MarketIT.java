package org.dmc.services;

import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.dmc.services.utility.TestUserUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.ValidatableResponse;

public class MarketIT extends BaseIT {
    private final String logTag = MarketIT.class.getName();
    
    private String knownEPPN;
    
    @Before
    public void before() {
    	if (knownEPPN == null) {
    		knownEPPN = TestUserUtil.createNewUser();
    	}
    }
	
	/*
	 * test case for GET /market/services
	 */
	@Test
	public void testMarketGet_Service() {
        ValidatableResponse response =
                given().
                header("AJP_eppn", knownEPPN).
                parameter("dates", "1y").
                expect().
                statusCode(HttpStatus.OK.value()).
                when().
                get("/market/services").
                then().
                body(matchesJsonSchemaInClasspath("Schemas/serviceListSchema.json"));

        String jsonResponse = response.extract().asString();
        ServiceLogger.log(logTag, "In marketServicesGet: " + jsonResponse);
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<?> serviceList = null;
        try {
            serviceList = mapper.readValue(jsonResponse, ArrayList.class);
            // this condition is true given sample data expected to load in Bamboo, change if different data is loaded.
            // this could fail if tests change (e.g. create some services for the marketplace) and are run in a different order.
            // may want to make the criteria more stringent...
            assertEquals(1, serviceList.size());
        } catch (Exception e) {
            fail("unable to map response to Service object: " + e.getMessage());
        }

	}
	
	
	/*
	 * test case for GET /market/new_services
	 */
	@Test
	public void testMarketGet_NewService() {

        ValidatableResponse response =
    		given().
    		header("AJP_eppn", knownEPPN).
    		expect().
    		statusCode(HttpStatus.OK.value()).
    		when().
    		get("/market/new_services").
            then().
            body(matchesJsonSchemaInClasspath("Schemas/serviceListSchema.json"));
        String jsonResponse = response.extract().asString();
        ServiceLogger.log(logTag, "In marketNewServicesGet: " + jsonResponse);
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<?> serviceList = null;
        try {
            serviceList = mapper.readValue(jsonResponse, ArrayList.class);
            // this condition is true given sample data expected to load in Bamboo, change if different data is loaded.
            //^^ Totally flawed assumption
            // this could fail if tests change (e.g. create some services for the marketplace) and are run in a different order.
            // may want to make the criteria more stringent...
            //^^ No kidding
            assertThat(Integer.valueOf(2), lessThanOrEqualTo(Integer.valueOf(serviceList.size())));
        } catch (Exception e) {
            fail("unable to map response to Service object: " + e.getMessage());
        }

	}

}
