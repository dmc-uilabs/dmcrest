package org.dmc.services.accounts;

import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;

import static org.junit.Assert.*;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import org.json.JSONObject;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.dmc.services.BaseIT;
import org.dmc.services.utility.TestUserUtil;
import org.dmc.services.accounts.UserAccount;
import org.dmc.services.accounts.UserAccountServer;
import org.dmc.services.users.User;
import org.dmc.services.users.UserDao;
import org.dmc.services.ServiceLogger;

@SuppressWarnings("unused")
public class AccountServerIT extends BaseIT {
	private final String logTag = AccountsIT.class.getName();

	private String newKnownUser;
	private String uniqueID;
	private String JSON = org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
	private final String validURL = System.getenv("RunningDOMEip");

	//These can be used when the Apache http client is used to test connections instead of the Sun/Oracle HTTP client
	//private final String urlOff = System.getenv("ServerIsOff");
	//private final String tomcatIsOff = System.getenv("TestDOMEnotRunning");


	private int user_id_lookedup;

	@Before
	public void setupData() {
		newKnownUser = TestUserUtil.createNewUser();
		uniqueID = TestUserUtil.uniqueID();

		try{
			user_id_lookedup = UserDao.getUserID(newKnownUser);
		} catch (SQLException e) {
			assertTrue("Error looking up new user", false);
		}
	}


	/**
	 * Tests for
	 * <code> post /account-servers </code>
	 **/

	@Test
	public void testAccountPost_NewServer_valid_server() {
		UserAccountServer userAccountServer = new UserAccountServer();

		// setup userAccountServer
		userAccountServer.setAccountId(Integer.toString(user_id_lookedup));
		userAccountServer.setName("ServerName" + uniqueID);
		userAccountServer.setIp(validURL);
		userAccountServer.setStatus("online");
		// ToDo: add server status to database
		//		userAccountServer.setStatus("ServerStatus" + uniqueID);

		UserAccountServer returnedUserAccountServer = createNewServer(userAccountServer);

		// check that the orginal UserAccountServer object does not have an id
		assertTrue("Orginal server id is not null",
				userAccountServer.getId() == null);

		// set if of orginal to id of returned
		userAccountServer.setId(returnedUserAccountServer.getId());

		if (!userAccountServer.getIp().contains("DOMEApiServicesV7/"))
			userAccountServer.setIp(validURL + "/DOMEApiServicesV7/");

		// check returned and orginal UserAccountServer object is equal
		ServiceLogger.log(logTag, "original IP: " + userAccountServer.getIp() + "\tReturned IP: " + returnedUserAccountServer.getIp());
		ServiceLogger.log(logTag, "original server: " + userAccountServer.toString() + "\tReturned server: " + returnedUserAccountServer.toString());
		
		assertTrue("Orginal and returned UserAccountServer objects are not equal",
				returnedUserAccountServer.equals(userAccountServer));
	}

	@Test
	public void testAccountPost_NewServer_invalid_server() {
		UserAccountServer userAccountServer = new UserAccountServer();

		// setup userAccountServer
		userAccountServer.setAccountId(Integer.toString(user_id_lookedup));
		userAccountServer.setName("ServerName" + uniqueID);
		userAccountServer.setIp("ServerURL" + uniqueID);
		// ToDo: add server status to database
		//		userAccountServer.setStatus("ServerStatus" + uniqueID);
		ObjectMapper mapper = new ObjectMapper();
		String userAccountServerString = null;

		try {
			userAccountServerString = mapper.writeValueAsString(userAccountServer);
		} catch (JsonProcessingException e) {

			ServiceLogger.log(logTag, e.getMessage());
		}


		given().
		header("Content-type", JSON).
		header("AJP_eppn", newKnownUser).
		body(userAccountServerString).
		expect().
		statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()). 
		post("/account_servers");
	}

	/**
	 * Tests for
	 * <code> get /account-servers/{serverID} </code>
	 **/
	@Ignore //this test requires some extra functionality allowing a tester to enter invalid data into the database
	//due to the existing POST requests performing checks that disallow this, this test must currently be ignored 

	@Test
	public void testAccountGet_ServerID_invalid_Server() {
		UserAccountServer returnedUserAccountServer = createNewServerNoCheck();

		given().
		header("Content-type", JSON).
		header("AJP_eppn", newKnownUser).
		expect().
		statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()). 
		when().
		get("/account_servers/" + returnedUserAccountServer.getId());
	}

	@Test
	public void testAccountGet_ServerID_valid_server() {
		UserAccountServer userAccountServer = new UserAccountServer();

		// setup userAccountServer
		userAccountServer.setAccountId(Integer.toString(user_id_lookedup));
		userAccountServer.setName("ServerName" + uniqueID);
		userAccountServer.setIp(validURL); 
		userAccountServer.setStatus("online");

		UserAccountServer returnedUserAccountServer = createNewServer(userAccountServer);

		UserAccountServer getReturnedUserAccountServer =
				given().
				header("Content-type", JSON).
				header("AJP_eppn", newKnownUser).
				expect().
				statusCode(HttpStatus.OK.value()).  
				when().
				get("/account_servers/" + returnedUserAccountServer.getId()).
				as(UserAccountServer.class);

		// check returned and orginal UserAccountServer object is equal
		ServiceLogger.log(logTag, "status for original server: " + returnedUserAccountServer.getStatus()
		+ "\tstatus for getObject: " + getReturnedUserAccountServer.getStatus());
		assertTrue("Orginal and returned UserAccountServer objects are not equal",
				returnedUserAccountServer.equals(getReturnedUserAccountServer));
	}


	/**
	 * Tests for
	 * <code> DELETE /account-servers/{serverID} </code>
	 **/

	@Test
	public void testAccountDelete_ServerID() {
		UserAccountServer returnedUserAccountServer = createNewServer();

		// delete server record
		given().
		header("Content-type", JSON).
		header("AJP_eppn", newKnownUser).
		expect().
		statusCode(HttpStatus.OK.value()).
		when().
		delete("/account_servers/" + returnedUserAccountServer.getId());

		// attempt to retrieve deleted record; fails
		given().
		header("Content-type", JSON).
		header("AJP_eppn", newKnownUser).
		expect().
		statusCode(HttpStatus.NO_CONTENT.value()).
		when().
		get("/account_servers/" + returnedUserAccountServer.getId());

	}

	/**
	 * Tests for
	 * <code> PATCH /account-servers/{serverID} </code>
	 **/

	@Test
	public void testAccountPatch_ServerID_invalid_server() {
		UserAccountServer returnedUserAccountServer = createNewServer();
		ObjectMapper mapper = new ObjectMapper();
		String userAccountServerString = null;

		returnedUserAccountServer.setName("patchedName");
		returnedUserAccountServer.setIp("patchedIP");

		try {
			userAccountServerString = mapper.writeValueAsString(returnedUserAccountServer);
		} catch (JsonProcessingException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}

		given().
		header("Content-type", JSON).
		header("AJP_eppn", newKnownUser).
		body(userAccountServerString).
		expect().
		statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()).
		when().
		patch("/account_servers/" + returnedUserAccountServer.getId());
	}

	@Test
	public void testAccountPatch_ServerID_valid_server() {
		UserAccountServer returnedUserAccountServer = createNewServer();
		ObjectMapper mapper = new ObjectMapper();
		String userAccountServerString = null;

		returnedUserAccountServer.setName("updatedWorkingName");
		returnedUserAccountServer.setIp(validURL);
		returnedUserAccountServer.setStatus("online");

		try {
			userAccountServerString = mapper.writeValueAsString(returnedUserAccountServer);
		} catch (JsonProcessingException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}

		UserAccountServer patchedUserAccountServer =
				given().
				header("Content-type", JSON).
				header("AJP_eppn", newKnownUser).
				body(userAccountServerString).
				expect().
				statusCode(HttpStatus.OK.value()).
				when().
				patch("/account_servers/" + returnedUserAccountServer.getId()).
				as(UserAccountServer.class);
		if (!returnedUserAccountServer.getIp().contains("DOMEApiServicesV7/"))
			returnedUserAccountServer.setIp(validURL + "/DOMEApiServicesV7/");
		// check returned and orginal UserAccountServer object is equal
		
		ServiceLogger.log(logTag, "Prepatched IP: " + returnedUserAccountServer.getIp() + "\tPostpatched IP: " + patchedUserAccountServer.getIp());
		ServiceLogger.log(logTag, "Prepatched server: " + returnedUserAccountServer.toString() + "\tPostpatched server: " + patchedUserAccountServer.toString());
		
		assertTrue("Orginal modified UserAccountServer object is not equal to patched",
				returnedUserAccountServer.equals(patchedUserAccountServer));
	}


	private UserAccountServer createNewServer(UserAccountServer userAccountServer) {
		ObjectMapper mapper = new ObjectMapper();
		String userAccountServerString = null;
		String jsonString = "unset", error = "unset";

		try {
			userAccountServerString = mapper.writeValueAsString(userAccountServer);
			jsonString = userAccountServer.toString();
		} catch (JsonProcessingException e) {
			error = e.getMessage();
			ServiceLogger.log(logTag, e.getMessage());
		}



		UserAccountServer returnedUserAccountServer =
				given().
				header("Content-type", JSON).
				header("AJP_eppn", newKnownUser).
				body(userAccountServerString).
				expect().
				statusCode(HttpStatus.CREATED.value()). 
				when().
				post("/account_servers"). //IMPROVEMENT allow forced user input for testing purposes
				as(UserAccountServer.class);

		return returnedUserAccountServer;
	}

	private UserAccountServer createNewServer() {
		UserAccountServer userAccountServer = new UserAccountServer();

		// setup userAccountServer
		userAccountServer.setAccountId(Integer.toString(user_id_lookedup));
		userAccountServer.setName("ServerName" + uniqueID);
		userAccountServer.setIp(validURL);
		return createNewServer(userAccountServer);
	}

	private UserAccountServer createNewServerNoCheck(){
		UserAccountServer userAccountServer = new UserAccountServer();

		// setup userAccountServer
		userAccountServer.setAccountId(Integer.toString(user_id_lookedup));
		userAccountServer.setName("ServerName" + uniqueID);
		userAccountServer.setIp("invalidIP");

		ObjectMapper mapper = new ObjectMapper();
		String userAccountServerString = null;

		try {
			userAccountServerString = mapper.writeValueAsString(userAccountServer);
		} catch (JsonProcessingException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}


		given().
		header("Content-type", JSON).
		header("AJP_eppn", newKnownUser).
		body(userAccountServerString).
		expect().
		statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()).  
		when().
		post("/account_servers"); //IMPROVEMENT allow forced user input for testing purposes


		return userAccountServer;
	}
}