package org.dmc.services;

import org.junit.Test;
import org.junit.Ignore;

import static com.jayway.restassured.RestAssured.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;


import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;

import org.dmc.services.users.User;
import org.dmc.services.users.AssignUser;
import org.dmc.services.users.UserOnboarding;
import org.dmc.services.users.UserNotifications;
import org.dmc.services.users.UserRunningServices;
import org.dmc.services.users.UserMessages;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class AssignUserIT extends BaseIT {
	
	private static final String logTag = AssignUserIT.class.getName();
	
	@Test
	public void testNewUserWithCompany() {
		String unique = TestUserUtil.generateTime();
		
		// create user; will not have company
		AssignUser newUser =
		given().
			header("Content-type", "application/json").
			header("AJP_eppn", unique).
			header("projectNumber",-1).
		expect().
			statusCode(HttpStatus.OK.value()).
		when().
		get("/assign_users").as(AssignUser.class);
		
//		// check; does not have have company
//		assertTrue("New user is assigned company " + newUser.getCompanyId() + " when the comapnay should be -1", newUser.getCompanyId() == -1);
		
		
	}
}