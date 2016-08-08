package org.dmc.services.accounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dmc.services.BaseIT;
import org.dmc.services.users.User;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccountsIT extends BaseIT {
	private final String logTag = AccountsIT.class.getName();
	private String knownUserEPPN = "fforgeadmin";
	private String knownUserID = "102";
	private String notificationSettingID = "1";
	private String serverID = "2";

	/**
	 * Tests for <code> get /accounts/{accountID} </code>
	 **/

	@Test
	public void testAccountGet_UnknownUser() {
		String unknownUserID = Integer.toString(Integer.MAX_VALUE);

		given().header("AJP_eppn", knownUserEPPN).expect().statusCode(401).when().get("/accounts/" + unknownUserID);

		// not json is returned on 401. If it is, then the check below should be
		// perfromed.
		// .then().
		// body(matchesJsonSchemaInClasspath("Schemas/userAccountSchema.json"));
		// check resulting json
	}

	@Test
	public void testAccountGet_KnownUser() {
		given().header("AJP_eppn", knownUserEPPN).expect().statusCode(200).when().get("/accounts/" + knownUserID).then()
				.body(matchesJsonSchemaInClasspath("Schemas/userAccountSchema.json"));

		// check results
		// assertTrue(false);
	}

	/**
	 * <code> testAccountGet_MismatchedUserEPPNAndUserId </code> Tests for
	 * mismatched user EPPN (from header) and user ID (from path)
	 **/
	@Test
	public void testAccountGet_MismatchedUserEPPNAndUserId() {
		String unknownUserID = Integer.toString(Integer.MAX_VALUE);

		given().header("AJP_eppn", knownUserEPPN).expect().statusCode(401).when().get("/accounts/" + unknownUserID);

		// not json is returned on 401. If it is, then the check below should be
		// perfromed.
		// .then().
		// body(matchesJsonSchemaInClasspath("Schemas/userAccountSchema.json"));
		// check resulting json
	}

	/**
	 * Tests for <code> patch /accounts/{accountID} </code>
	 **/

	@Test
	public void testAccountUpdate_MismatchedUserId() {
		String unique = TestUserUtil.generateTime();
		User user = getUser(unique);
		JSONObject userAccountJson = getUserAccountJson(unique, user.getAccountId());

		// perfrom update
		userAccountJson.put("firstName", "NEWGivenName" + unique);
		userAccountJson.put("lastName", "NEWSurname" + unique);
		userAccountJson.put("displayName", "NEWDisplayName" + unique);

		given().header("Content-type", "application/json").header("AJP_eppn", "userEPPN" + unique)
				.body(userAccountJson.toString()).expect().statusCode(401). // Unauthorized
																			// access
				when().patch("/accounts/1" + userAccountJson.getString("id"));

		// check results
	}

	@Test
	public void testAccountUpdateOfNewlyCreatedUser() {
		String unique = TestUserUtil.generateTime();
		User user = getUser(unique);
		JSONObject userAccountJson = getUserAccountJson(unique, user.getAccountId());

		// perfrom update
		userAccountJson.put("firstName", "NEWGivenName" + unique);
		userAccountJson.put("lastName", "NEWSurname" + unique);
		userAccountJson.put("displayName", "NEWDisplayName" + unique);

		// ServiceLogger.log(logTag, "testAccountUpdateOfNewlyCreatedUser,
		// userAccountJson: " + userAccountJson.toString());

		UserAccount patchedUserAccount = given().header("Content-type", "application/json")
				.header("AJP_eppn", "userEPPN" + unique).body(userAccountJson.toString()).expect().statusCode(200)
				.when().patch("/accounts/" + userAccountJson.getString("id")).as(UserAccount.class);

		// check results
		assertTrue(
				"New and patched user's id are equal " + userAccountJson.getString("id") + " "
						+ patchedUserAccount.getId(),
				userAccountJson.getString("id").equals(patchedUserAccount.getId()));

		assertFalse("Orginal and patched user's display names are different",
				user.getDisplayName().equals(patchedUserAccount.getDisplayName()));

		assertTrue("New and patched user's fistname are different",
				userAccountJson.getString("firstName").equals(patchedUserAccount.getFirstName()));

		assertTrue("New and patched user's lastname are different",
				userAccountJson.getString("lastName").equals(patchedUserAccount.getLastName()));

		assertTrue("New and patched user's display name are different",
				userAccountJson.getString("displayName").equals(patchedUserAccount.getDisplayName()));

	}

	private User getUser(String uniqueString) {
		given().header("Content-type", "text/plain").header("AJP_eppn", "userEPPN" + uniqueString)
				.header("AJP_givenName", "userGivenName" + uniqueString).header("AJP_sn", "userSurname" + uniqueString)
				.header("AJP_displayName", "userDisplayName" + uniqueString)
				.header("AJP_mail", "userEmail" + uniqueString).expect().statusCode(200).when().post("/users/create");
				
		User user = given().header("Content-type", "text/plain").header("AJP_eppn", "userEPPN" + uniqueString)
				.header("AJP_givenName", "userGivenName" + uniqueString).header("AJP_sn", "userSurname" + uniqueString)
				.header("AJP_displayName", "userDisplayName" + uniqueString)
				.header("AJP_mail", "userEmail" + uniqueString).expect().statusCode(200).when().get("/user")
				.as(User.class);

		return user;
	}

	private JSONObject getUserAccountJson(String uniqueString, int userAccountId) {

		String userAccountJsonString = given().header("AJP_eppn", "userEPPN" + uniqueString).expect().statusCode(200)
				.when().get("/accounts/" + userAccountId).body().asString();

		JSONObject userAccountJson = new JSONObject(userAccountJsonString);

		return userAccountJson;
	}

	private UserAccount getUserAccount(String uniqueString, int userAccountId) {
		UserAccount userAccount = given().header("AJP_eppn", "userEPPN" + uniqueString).expect().statusCode(200).when()
				.get("/accounts/" + userAccountId).as(UserAccount.class);

		return userAccount;
	}

	/**
	 * Tests for <code> get /accounts/{accountID}/account_servers </code>
	 **/
	@Test
	public void testAccountGet_AccountServers() {
		String accountID = "102"; // This is the accountID of alias=baseDOME in the servers table (first two entries)

		List<UserAccountServer> receivedAccountServers = Arrays.asList(given().header("Content-type", "application/json").header("AJP_eppn", userEPPN)
				.expect().statusCode(HttpStatus.OK.value()).when().get("/accounts/" + accountID + "/account_servers").as(UserAccountServer[].class));

		assertTrue("testAccountGet_AccountServers: Account server user_id values are not equal",
				(receivedAccountServers.get(0).getAccountId().equals(accountID)));
		assertTrue("testAccountGet_AccountServers: Account server server_id values are not equal",
				(receivedAccountServers.get(0).getId().equals("1")));
		assertTrue("testAccountGet_AccountServers: Account server url values are not equal",
				(receivedAccountServers.get(0).getIp().equals("http://ec2-52-88-73-23.us-west-2.compute.amazonaws.com:8080/DOMEApiServicesV7/")));
		assertTrue("testAccountGet_AccountServers: Account server user_id values are not equal",
				(receivedAccountServers.get(0).getName().equals("baseDOME")));
		assertTrue("testAccountGet_AccountServers: Account server status values are not equal",
				(receivedAccountServers.get(0).getStatus().equals("offline")));

	}
	
	@Test
	public void testAccountGet_AccountServersWithParameters() {
		String accountID = "102"; // This is the accountID of alias=baseDOME in the servers table (first two entries)

		List<UserAccountServer> receivedAccountServers = Arrays.asList(
				given().header("Content-type", "application/json").header("AJP_eppn", userEPPN).param("_limit", 1).param("_order", "DESC").expect()
						.statusCode(HttpStatus.OK.value()).when().get("/accounts/" + accountID + "/account_servers").as(UserAccountServer[].class));

		assertTrue("testAccountGet_AccountServers: Account server user_id values are not equal",
				(receivedAccountServers.get(0).getAccountId().equals(accountID)));
		assertTrue("testAccountGet_AccountServers: Limit parameter did not work", (receivedAccountServers.size() == 1));

	}

	/**
	 * Tests for
	 * <code> get /accounts/{accountID}/account-notification-settings </code>
	 **/

	@Test
	public void testAccount_account_Notification_Settings() {
		given().header("AJP_eppn", knownUserEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
				.get("/accounts/" + knownUserID + "/account-notification-settings");
	}

	/**
	 * Tests for
	 * <code> GET /accounts/{accountID}/account-notification-settings/NotificationSettingID </code>
	 **/

	@Test
	public void testAccountNotificationSettingGet_NotificationSettingID() {
		given().header("AJP_eppn", knownUserEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
				.get("/account-notification-settings/" + notificationSettingID);

	}
	
	
	/**
	 * Tests for
	 * <code> PATCH /accounts/{accountID}/account-notification-settings/NotificationSettingID </code>
	 **/

	@Test
	public void testAccountNotificationSettingPatch_NotificationSettingID() {
		AccountNotificationSetting obj = new AccountNotificationSetting();
		ObjectMapper mapper = new ObjectMapper();
		String postedNotificationSetting = null;
		try {
			postedNotificationSetting = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", knownUserEPPN).
		body(postedNotificationSetting).
		expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
				.patch("/account-notification-settings/" + notificationSettingID);

	}

	/**
	 * Tests for <code> get /account-notification-categories </code>
	 **/

	@Test
	public void testAccount_accountNotificationCategories() {
		given().header("AJP_eppn", knownUserEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
				.get("/account-notification-categories");
	}

	
	/**
	 * Tests for <code> get /accounts/{accountID}/favorite_products </code>
	 **/

	@Test
	public void testAccount_FavoriteProducts() {
		given().header("AJP_eppn", knownUserEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
				.get("/accounts/" + knownUserID + "/favorite_products");
	}

	/**
	 * Tests for <code> get /accounts/{accountID}/following_companies </code>
	 **/

	@Test
	public void testAccount_FollowingCompanies() {
		given().header("AJP_eppn", knownUserEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
				.get("/accounts/" + knownUserID + "/following_companies");
	}


	/**
	 * Tests for <code> get /accounts/{accountID}/following_members </code>
	 **/

	@Test
	public void testAccount_FollowingMembers() {
		given().header("AJP_eppn", knownUserEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
				.get("/accounts/" + knownUserID + "/following_members");
	}

}