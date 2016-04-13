package org.dmc.services;

import static com.jayway.restassured.RestAssured.*;
import org.junit.Test;
import org.springframework.http.HttpStatus;


public class NotificationIT extends BaseIT {
	
	

	/*
	 * test case for GET /notifications-user
	 */
	@Test
	public void testGet_NotificationUser() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/notifications-user");
	}
	
	
	/*
	 * test case for GET /notifications-pm
	 */
	@Test
	public void testGet_NotificationPm() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/notifications-pm");
	}
	
	
	/*
	 * test case for GET /notifications-user-statistic
	 */
	@Test
	public void testGet_NotificationUserStatistic() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/notifications-user-statistic");
	}
	
	
	/*
	 * test case for GET /notifications-pm-statistic
	 */
	@Test
	public void testGet_NotificationPmStatistic() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/notifications-pm-statistic");
	}
	
}
