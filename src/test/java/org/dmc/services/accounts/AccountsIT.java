package org.dmc.services.accounts;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dmc.services.BaseIT;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.member.FollowingMember;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.ValidatableResponse;

public class AccountsIT extends BaseIT {
    private static final String LOGTAG = AccountsIT.class.getName();

    private static final String ACCOUNTS_BY_ID = "/accounts/{id}";
    private static final String ACCOUNT_SERVERS = "/accounts/{id}/account_servers";
    private static final String ACCOUNTS_BY_ID_NOTIFICATION_SETTINGS = "/accounts/{id}/account-notification-settings";
    private static final String ACCOUNT_NOTIFICATION_SETTINGS = "/account-notification-settings/{settingsId}";
    private static final String ACCOUNT_NOTIFICATION_CATEGORIES = "/account-notification-categories";
    private static final String ACCOUNT_FAVORITE_PRODUCTS = "/accounts/{id}/favorite_products";
    private static final String ACCOUNT_FOLLOWING_COMPANIES = "/accounts/{id}/following_companies";
    private static final String ACCOUNTS_FOLLOWING_MEMBERS = "/accounts/{id}/following_members";
	//private static final String ACCOUNT_FOLLOWING_COMPANIES = "/accounts/{accountID}/following_companies";
	private static final String COMPANY_FOLLOW = "/company/follow";
	private static final String COMPANY_UNFOLLOW_COMPANY_ID = "/company/unfollow/{followed_companyId}";
    
    private static final String USER = "/user";
    private static final String USERS_CREATE = "/users/create";
    
    private static final String FOLLOWING_MEMBERS = "/following_members";

    private String knownUserEPPN = "fforgeadmin";
    private String knownUserID = "102";
    private String notificationSettingID = "1";

    /**
     * Tests for <code> get /accounts/{accountID} </code>
     **/

    @Test
    public void testAccountGet_UnknownUser() {
        String unknownUserID = Integer.toString(Integer.MAX_VALUE);

        given().header("AJP_eppn", knownUserEPPN).expect().statusCode(FORBIDDEN.value()).when().get(ACCOUNTS_BY_ID, unknownUserID);

        // not json is returned on 401. If it is, then the check below should be
        // performed.
        // .then().
        // body(matchesJsonSchemaInClasspath("Schemas/userAccountSchema.json"));
        // check resulting json
    }

    @Test
    public void testAccountGet_KnownUser() {
        given().header("AJP_eppn", knownUserEPPN).expect().statusCode(OK.value()).when().get(ACCOUNTS_BY_ID, knownUserID).then()
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

        given().header("AJP_eppn", knownUserEPPN).expect().statusCode(FORBIDDEN.value()).when().get(ACCOUNTS_BY_ID, unknownUserID);

        // not json is returned on 401. If it is, then the check below should be
        // performed.
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
        UserModel user = getUser(unique);
        JSONObject userAccountJson = getUserAccountJson(unique, user.getAccountId());

        // perform update
        userAccountJson.put("firstName", "NEWGivenName" + unique);
        userAccountJson.put("lastName", "NEWSurname" + unique);
        userAccountJson.put("displayName", "NEWDisplayName" + unique);

        given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", "userEPPN" + unique)
                .body(userAccountJson.toString()).expect().statusCode(UNAUTHORIZED.value()).
        when().patch("/accounts/1" + userAccountJson.getString("id"));

        // check results
    }

    @Test
    public void testAccountUpdateOfNewlyCreatedUser() {
        String unique = TestUserUtil.generateTime();
        UserModel user = getUser(unique);
        JSONObject userAccountJson = getUserAccountJson(unique, user.getAccountId());

        // perform update
        userAccountJson.put("firstName", "NEWGivenName" + unique);
        userAccountJson.put("lastName", "NEWSurname" + unique);
        userAccountJson.put("displayName", "NEWDisplayName" + unique);

        // ServiceLogger.log(logTag, "testAccountUpdateOfNewlyCreatedUser,
        // userAccountJson: " + userAccountJson.toString());

        UserAccount patchedUserAccount = given().header("Content-type", APPLICATION_JSON_VALUE)
                .header("AJP_eppn", "userEPPN" + unique).body(userAccountJson.toString()).expect().statusCode(OK.value())
                .when().patch(ACCOUNTS_BY_ID, userAccountJson.getString("id")).as(UserAccount.class);

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

    private UserModel getUser(String uniqueString) {
        given().header("Content-type", "text/plain").header("AJP_eppn", "userEPPN" + uniqueString)
                .header("AJP_givenName", "userGivenName" + uniqueString).header("AJP_sn", "userSurname" + uniqueString)
                .header("AJP_displayName", "userDisplayName" + uniqueString)
                .header("AJP_mail", "userEmail" + uniqueString).expect().statusCode(OK.value()).when().post(USERS_CREATE);

        UserModel user = given().header("Content-type", "text/plain").header("AJP_eppn", "userEPPN" + uniqueString)
                .header("AJP_givenName", "userGivenName" + uniqueString).header("AJP_sn", "userSurname" + uniqueString)
                .header("AJP_displayName", "userDisplayName" + uniqueString)
                .header("AJP_mail", "userEmail" + uniqueString).expect().statusCode(OK.value()).when().get(USER)
                .as(UserModel.class);

        return user;
    }

    private JSONObject getUserAccountJson(String uniqueString, int userAccountId) {

        String userAccountJsonString = given().header("AJP_eppn", "userEPPN" + uniqueString).expect().statusCode(OK.value())
                .when().get(ACCOUNTS_BY_ID, userAccountId).body().asString();

        JSONObject userAccountJson = new JSONObject(userAccountJsonString);

        return userAccountJson;
    }

    /**
     * Tests for <code> get /accounts/{accountID}/account_servers </code>
     **/
    @Test
    public void testAccountGet_AccountServers() {
        String accountID = "102"; // This is the accountID of alias=baseDOME in
                                  // the servers table (first two entries)

        List<UserAccountServer> receivedAccountServers = Arrays
                .asList(given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN).expect()
                        .statusCode(OK.value()).when().get(ACCOUNT_SERVERS, accountID)
                        .as(UserAccountServer[].class));

        assertNotNull("no server list returned", receivedAccountServers);
        assertTrue("didn't find any servers", receivedAccountServers.size() > 0);
        final UserAccountServer server = receivedAccountServers.get(0);
        assertTrue("testAccountGet_AccountServers: Account server user_id values are not equal",
                (server.getAccountId().equals(accountID)));
        assertTrue("testAccountGet_AccountServers: Account server server_id values are not equal",
                (server.getId().equals("1")));
        assertTrue("testAccountGet_AccountServers: Account server url values are not equal",
                (server.getIp()
                        .equals("http://ec2-52-88-73-23.us-west-2.compute.amazonaws.com:8080/DOMEApiServicesV7/")));
        assertTrue("testAccountGet_AccountServers: Account server user_id values are not equal",
                (server.getName().equals("baseDOME")));
        assertTrue("testAccountGet_AccountServers: Account server status values are not equal",
                (server.getStatus().equals("offline")));

    }

    @Test
    public void testAccountGet_AccountServersWithParameters() {
        String accountID = "102"; // This is the accountID of alias=baseDOME in
                                  // the servers table (first two entries)

        List<UserAccountServer> receivedAccountServers = Arrays
                .asList(given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", userEPPN)
                        .param("_limit", 1).param("_order", "DESC").expect().statusCode(OK.value()).when()
                        .get(ACCOUNT_SERVERS, accountID).as(UserAccountServer[].class));

        assertNotNull("no server list returned", receivedAccountServers);
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
                .get(ACCOUNTS_BY_ID_NOTIFICATION_SETTINGS, knownUserID);
    }

    /**
     * Tests for
     * <code> GET /accounts/{accountID}/account-notification-settings/NotificationSettingID </code>
     **/

    @Test
    public void testAccountNotificationSettingGet_NotificationSettingID() {
        given().header("AJP_eppn", knownUserEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
                .get(ACCOUNT_NOTIFICATION_SETTINGS, notificationSettingID);

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

        given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", knownUserEPPN)
                .body(postedNotificationSetting).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
                .patch(ACCOUNT_NOTIFICATION_SETTINGS, notificationSettingID);

    }

    /**
     * Tests for <code> get /account-notification-categories </code>
     **/

    @Test
    public void testAccount_accountNotificationCategories() {
        given().header("AJP_eppn", knownUserEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
                .get(ACCOUNT_NOTIFICATION_CATEGORIES);
    }

    /**
     * Tests for <code> get /accounts/{accountID}/favorite_products </code>
     **/

    @Test
    public void testAccount_FavoriteProducts() {
        given().header("AJP_eppn", knownUserEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when()
                .get(ACCOUNT_FAVORITE_PRODUCTS, knownUserID);
    }

    /**
     * Tests for <code> get /accounts/{accountID}/following_companies </code>
     **/

    @Test
	public void testAccount_FollowingCompanies() {
		int accountId = 102;
		int companyId = 2;
		int companyId2 = 1;
		given().param("accountId", accountId).param("companyId", companyId).header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.OK.value()).when().post(COMPANY_FOLLOW);
		given().param("accountId", accountId).param("companyId", companyId2).header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.OK.value()).when().post(COMPANY_FOLLOW);

		List<FollowingCompany> res = given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.OK.value()).when()
				.get(ACCOUNT_FOLLOWING_COMPANIES, accountId).as(List.class);
		assertTrue(res.size() == 2);
		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.OK.value()).when().delete(COMPANY_UNFOLLOW_COMPANY_ID, companyId);
		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.OK.value()).when().delete(COMPANY_UNFOLLOW_COMPANY_ID, companyId2);
	}

    /**
     * Tests for <code> get /accounts/{accountID}/following_members </code>
     * Same logic as get /profiles/{profileID}/following_members
     **/

    @Test
    public void testAccount_FollowingMembers() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testAccount_FollowingMembers");

        final String followerUserName = TestUserUtil.createNewUser();
        final int followerId = UserDao.getUserID(followerUserName);
        final String followerIdText = Integer.toString(followerId);
        final String followedUserName = TestUserUtil.createNewUser();
        final int followedId = UserDao.getUserID(followedUserName);
        final String followedIdText = Integer.toString(followedId);

        FollowingMember followRequest = new FollowingMember();
        followRequest.setFollower(followerIdText);
        followRequest.setFollowed(followedIdText);

        final ValidatableResponse postResponse = given().header("Content-type", APPLICATION_JSON_VALUE)
                .header("AJP_eppn", followerUserName).body(followRequest).expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final FollowingMember followingMemberResponse = postResponse.extract().as(FollowingMember.class);

        final FollowingMember expected = new FollowingMember();
        expected.setFollower(followerIdText);
        expected.setFollowed(followedIdText);
        assertEquals("POST response does not match expected following member object", expected,
                followingMemberResponse);

        final ValidatableResponse getResponse = given().header("AJP_eppn", followerUserName).expect()
                .statusCode(OK.value()).when().get(ACCOUNTS_FOLLOWING_MEMBERS, followerIdText).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberListSchema.json"));

        final ArrayList<FollowingMember> list = TestUserUtil.readFollowingMemberResponse(getResponse);
        assertTrue("couldn't find newly added follow entry in GET response list", list.contains(expected));

    }

}