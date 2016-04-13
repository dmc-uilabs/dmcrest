package org.dmc.services;

import static com.jayway.restassured.RestAssured.*;

import org.junit.Test;
import org.springframework.http.HttpStatus;

public class MarketIT extends BaseIT {

	/*
	 * test case for GET /market/components
	 */
	@Test
	public void testMarketGet_Component() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/market/components");
	}
	
	
	/*
	 * test case for GET /market/services
	 */
	@Test
	public void testMarketGet_Service() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/market/services");
	}
	
	
	/*
	 * test case for GET /market/new_services
	 */
	@Test
	public void testMarketGet_NewService() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/market/new_services");
	}
	
	
	/*
	 * test case for GET /market/popular_services
	 */
	@Test
	public void testMarketGet_PopularService() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/market/popular_services");
	}

}
