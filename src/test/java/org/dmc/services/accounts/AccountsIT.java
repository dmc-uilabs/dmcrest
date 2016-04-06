package org.dmc.services.accounts;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static com.jayway.restassured.RestAssured.*;
import static org.junit.Assert.*;

import org.json.JSONObject;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.dmc.services.BaseIT;
import org.dmc.services.utility.TestUserUtil;
import org.dmc.services.accounts.UserAccount;
import org.dmc.services.users.User;


public class AccountsIT extends BaseIT {
    private final String logTag = AccountsIT.class.getName();
    private String knownUserEPPN = "fforgeadmin";
    private String knownUserID = "102";
    
    /**
     Tests for <code> get /accounts/{accountID} </code>
     **/
    
    @Test
	public void testAccountGet_UnknownUser(){
        String unknownUserEPPN = TestUserUtil.uniqueUserEPPN();
        String unknownUserID = Integer.toString(Integer.MAX_VALUE);
        
		given().
        header("AJP_eppn", unknownUserEPPN).
		expect().
        statusCode(401).
		when().
        get("/accounts/" + unknownUserID);

        // not json is returned on 401.  If it is, then the check below should be perfromed.
        //		.then().
        //        body(matchesJsonSchemaInClasspath("Schemas/userAccountSchema.json"));
        // check resulting json
	}
    
    @Test
	public void testAccountGet_KnownUser() {
		given().
        header("AJP_eppn", knownUserEPPN).
		expect().
        statusCode(200).
		when().
        get("/accounts/" + knownUserID).
		then().
        body(matchesJsonSchemaInClasspath("Schemas/userAccountSchema.json"));
        
        // check results
//        assertTrue(false);
	}
    
    
    /**
    Tests for <code> get /accounts/{accountID}/account_servers </code>
    **/
   
   @Test
	public void testAccountGet_AccountServers(){
		given().
       header("AJP_eppn", knownUserEPPN).
		expect().
       statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
       get("/accounts/" + knownUserID +"/account_servers");

	}
    

    /**
     <code> testAccountGet_MismatchedUserEPPNAndUserId </code> Tests for mismatched user EPPN (from header) and user ID (from path)
     **/
    @Test
	public void testAccountGet_MismatchedUserEPPNAndUserId(){
        String unknownUserID = Integer.toString(Integer.MAX_VALUE);

        given().
        header("AJP_eppn", knownUserEPPN).
		expect().
        statusCode(401).
		when().
        get("/accounts/" + unknownUserID);
        
        // not json is returned on 401.  If it is, then the check below should be perfromed.
        //		.then().
        //        body(matchesJsonSchemaInClasspath("Schemas/userAccountSchema.json"));
        // check resulting json
    }
    
    /**
     Tests for <code> patch /accounts/{accountID} </code>
     **/
    
    @Test
    public void testAccountUpdate_MismatchedUserId(){
        String unique = TestUserUtil.generateTime();
        User user = getUser(unique);
        JSONObject userAccountJson = getUserAccountJson(unique, user.getAccountId());
        
        // perfrom update
        userAccountJson.put("firstName", "NEWGivenName" + unique);
        userAccountJson.put("lastName", "NEWSurname" + unique);
        userAccountJson.put("displayName", "NEWDisplayName" + unique);
        
        given().
            header("Content-type", "application/json").
            header("AJP_eppn", "userEPPN" + unique).
            body(userAccountJson.toString()).
		expect().
            statusCode(401).  // Unauthorized access
		when().
            patch("/accounts/1" + userAccountJson.getString("id"));
        
        // check results
    }

    @Test
	public void testAccountUpdateOfNewlyCreatedUser(){
        String unique = TestUserUtil.generateTime();
        User user = getUser(unique);
        JSONObject userAccountJson = getUserAccountJson(unique, user.getAccountId());
        
        // perfrom update
        userAccountJson.put("firstName", "NEWGivenName" + unique);
        userAccountJson.put("lastName", "NEWSurname" + unique);
        userAccountJson.put("displayName", "NEWDisplayName" + unique);
        
//        ServiceLogger.log(logTag, "testAccountUpdateOfNewlyCreatedUser, userAccountJson: " + userAccountJson.toString());
        
        UserAccount patchedUserAccount =
        given().
            header("Content-type", "application/json").
            header("AJP_eppn", "userEPPN" + unique).
            body(userAccountJson.toString()).
		expect().
            statusCode(200).
		when().
        patch("/accounts/" + userAccountJson.getString("id")).
            as(UserAccount.class);
        
        
        // check results
        assertTrue("New and patched user's id are equal "+userAccountJson.getString("id") +" "+ patchedUserAccount.getId(),
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
        User user = given().
        header("Content-type", "text/plain").
        header("AJP_eppn", "userEPPN" + uniqueString).
        header("AJP_givenName", "userGivenName" + uniqueString).
        header("AJP_sn", "userSurname" + uniqueString).
        header("AJP_displayName", "userDisplayName" + uniqueString).
        header("AJP_mail", "userEmail" + uniqueString).
        expect().
        statusCode(200).
		when().
        get("/user").as(User.class);

        return user;
    }
    
    private JSONObject getUserAccountJson(String uniqueString, int userAccountId) {
        
        String userAccountJsonString =
        given().
            header("AJP_eppn", "userEPPN" + uniqueString).
        expect().
            statusCode(200).
        when().
            get("/accounts/" + userAccountId).
            body().asString();
        
        JSONObject userAccountJson = new JSONObject(userAccountJsonString);
        
        return userAccountJson;
    }
    
    private UserAccount getUserAccount(String uniqueString, int userAccountId) {
        UserAccount userAccount =
            given().
                header("AJP_eppn", "userEPPN" + uniqueString).
                expect().
                statusCode(200).
            when().
                get("/accounts/" + userAccountId).as(UserAccount.class);
        
        return userAccount;
    }
}