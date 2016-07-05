package org.dmc.services;

import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;

import static com.jayway.restassured.RestAssured.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;

import org.dmc.services.users.User;
import org.dmc.services.users.AssignUser;
import org.dmc.services.users.UserOnboarding;
import org.dmc.services.users.UserNotifications;
import org.dmc.services.users.UserRunningServices;
import org.dmc.services.users.UserMessages;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AssignUserIT extends BaseIT {
	
	private static final String logTag = AssignUserIT.class.getName();
	
	@Test
	public void testNewUserWithCompany() throws Exception {
		
		String uniqueUserEPPN1 = TestUserUtil.createNewUser();
		Integer int1 = TestUserUtil.addBasicInfomationToUser(uniqueUserEPPN1);
		ObjectMapper mapper1 = new ObjectMapper();
		
		JsonNode users1 =
		given().
			header("Content-type", "application/json").
			header("AJP_eppn", uniqueUserEPPN1).
			header("projectNumber",-1).
		expect().
			statusCode(HttpStatus.OK.value()).
		when().
		get("/assign_users").as(JsonNode.class);
		
		ArrayList<AssignUser> membersList1 = mapper1.readValue(mapper1.treeAsTokens(users1), new TypeReference<ArrayList<AssignUser>>() {});

		
		String uniqueUserEPPN2 = TestUserUtil.createNewUser();
		Integer int2 = TestUserUtil.addBasicInfomationToUser(uniqueUserEPPN2);
		ObjectMapper mapper2 = new ObjectMapper();
		
		JsonNode users2 =
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", uniqueUserEPPN2).
		header("projectNumber",-1).
		expect().
		statusCode(HttpStatus.OK.value()).
		when().
		get("/assign_users").as(JsonNode.class);
		
		ArrayList<AssignUser> membersList2 = mapper2.readValue(mapper2.treeAsTokens(users2), new TypeReference<ArrayList<AssignUser>>() {});

		assertFalse("Users' EPPNs ( "+uniqueUserEPPN1+" & "+uniqueUserEPPN2+" ) are the same",uniqueUserEPPN1.equals(uniqueUserEPPN2));
		assertTrue("Second list "+membersList2.size()+" in not one larger than first list " + membersList1.size(), membersList2.size() ==  membersList1.size()+1);
	}
}