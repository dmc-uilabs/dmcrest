package org.dmc.services;

import static com.jayway.restassured.RestAssured.*;
import org.junit.Test;
import org.springframework.http.HttpStatus;


public class EventIT extends BaseIT {
	
	

	/*
	 * test case for GET /events
	 */
	@Test
	public void testGet_NotificationUser() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/events");
	}
	
	
}
