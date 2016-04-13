package org.dmc.services;

import static com.jayway.restassured.RestAssured.*;
import org.junit.Test;
import org.springframework.http.HttpStatus;


public class AnnouncementIT extends BaseIT {
	
	

	/*
	 * test case for GET /announcements
	 */
	@Test
	public void testGet_NotificationUser() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/announcements");
	}
	
	
}
