package org.dmc.services;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.dmc.services.users.UserController;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;

import org.dmc.services.users.User;
import org.dmc.services.users.UserOnboarding;
import org.dmc.services.users.UserNotifications;
import org.dmc.services.users.UserRunningServices;
import org.dmc.services.users.UserMessages;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

//@Ignore
public class UserIT extends BaseIT {

    private static final String logTag = UserIT.class.getName();

	@Test
	public void testUserIncorrectInvocation(){
		ServiceLogger.log(logTag, "starting testUserIncorrectInvocation");
		JSONObject json = new JSONObject();
		String unique = TestUserUtil.generateTime();
		
        // callin enpoint with content is payload, which is not used when received.
		json.put("user_name", "username " + unique);
		json.put("email", "testemail" + unique + "@ge.com");
		json.put("password", "pwd " + unique);
		json.put("name", "usertester " + unique);
		
		
		Integer id = given().body(json.toString()).expect().statusCode(200).when().post("/users/create").then()
		.body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).extract().path("id");
        
        assertTrue("Valid user ID return.  Value is " + id + ", but should be -99999.", id == -99999);
	}
    
    @Test
	public void testUserCreate(){
		ServiceLogger.log(logTag, "starting testUserCreate");
		String unique = TestUserUtil.generateTime();
        
        Integer id =
        given().
        header("Content-type", "text/plain").
        header("AJP_eppn", "userEPPN" + unique).
        header("AJP_givenName", "userGivenName" + unique).
        header("AJP_sn", "userSurname" + unique).
        header("AJP_displayName", "userDisplayName" + unique).
        header("AJP_mail", "userEmail" + unique).
        expect().
        statusCode(200).
		when().
        post("/users/create").
		then().
        body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).
        extract().path("id");

        // check return value > 0
        assertTrue("Added user: " + "userEPPN" + unique + " Valid user ID must be greater then zero.  Value is " + id + ".", id > 0);
        // could also check email syntax
        
	}

	
    @Test
	public void testUserGet_UnknownUser(){
		ServiceLogger.log(logTag, "starting testUserGet_UnknownUser");
        String unknownUser = "unknown";
        
		given().
        header("AJP_eppn", unknownUser).
		expect().
        statusCode(200).
		when().
        get("/user").
		then().
        body(matchesJsonSchemaInClasspath("Schemas/userSchema.json"));
	}

    @Test
	public void testUserGet_KnownUser(){
		ServiceLogger.log(logTag, "starting testUserGet_KnownUser");
        String knownUser = "fforgeadmin";
        
		given().
        header("AJP_eppn", knownUser).
		expect().
        statusCode(200).
		when().
        get("/user").
		then().
        body(matchesJsonSchemaInClasspath("Schemas/userSchema.json"));
	}
    
    /**
     Create a new user, patch that user with a different display name
     Ckeck - New and patched user's display names are not equal
     Then modify patched user object's display name, back to orginal name
     Check - Users are equal is true
     **/
    @Test
	public void testUserPatch_KnownUsers_DisplayName(){
        String knownUserEPPN = createNewUser();
        
        User knownUser = getUser(knownUserEPPN);
        JSONObject knownUserJSON = getUserJson(knownUserEPPN);
        
        // update user's display name
        String unique = TestUserUtil.generateTime();
        knownUserJSON.put("displayName", "NEWDisplayName" + unique);
        
        User patchedKnownUser =
        given().
            header("Content-type", "application/json").
            header("AJP_eppn", knownUserEPPN).
            body(knownUserJSON.toString()).
		expect().
            statusCode(200).
		when().
        patch("/user").as(User.class);
        
        ServiceLogger.log(logTag, "Body of patchedKnownUser is " + patchedKnownUser + ".");

        // check results
        assertFalse("New and patched user's display names are equal ",
                    knownUser.getDisplayName().equals(patchedKnownUser.getDisplayName()));
        
        patchedKnownUser.setDisplayName(knownUser.getDisplayName());

        assertTrue("User are not equal", patchedKnownUser.equals(knownUser));
	}

    
    /**
     Create a new user, 
     Check that its UserOnboarding status is equal to default UserOnboarding status
     Modify new user's UserOnboarding status so that all status fields are opposite their orginal value
     Patch the user with the newly modified UserOnboarding status
     Check that user returned from PATCH is equal to user set to the the patch method
     **/
    @Test
	public void testUserPatch_KnownUsers_OnboardingStatus() {
        String knownUserEPPN = createNewUser();
        JSONObject knownUserJSON = getUserJson(knownUserEPPN);
        ObjectMapper mapper = new ObjectMapper();
        
        User knownUser = null;
        try{
        knownUser = mapper.readValue(knownUserJSON.toString(), User.class);
        } catch (java.io.IOException e) {
            assertTrue("Cannot map User from knownUserJSON: "+ knownUserJSON.toString(),false);
        }
        
        UserOnboarding defaultUserOnboarding = new UserOnboarding();
        assertTrue("New user's onboarding status is not equal to default onboarding status",
                   knownUser.getOnboarding().equals(defaultUserOnboarding));
        
        boolean profile = knownUser.getOnboarding().getProfile();
        boolean account = knownUser.getOnboarding().getAccount();
        boolean company = knownUser.getOnboarding().getCompany();
        boolean storefront = knownUser.getOnboarding().getStorefront();

        assertTrue("Default OnboardingStatus is set to false",
                   !profile && !account &&
                   !company && !storefront);
        
        knownUser.getOnboarding().setProfile(!profile);
        knownUser.getOnboarding().setAccount(!account);
        knownUser.getOnboarding().setCompany(!company);
        knownUser.getOnboarding().setStorefront(!storefront);
        
        String patchedKnownUserJSONinString = null;
        try {
            patchedKnownUserJSONinString = mapper.writeValueAsString(knownUser);
        } catch (JsonProcessingException e) {
            
        }
        
        User patchedKnownUser =
        given().
            header("Content-type", "application/json").
            header("AJP_eppn", knownUserEPPN).
            body(patchedKnownUserJSONinString).
		expect().
            statusCode(200).
		when().
            patch("/user").as(User.class);
        
        // check results of PATCH
        assertTrue("knownUser and patchedKnownUser are not equal", patchedKnownUser.equals(knownUser));
	}
    
    
    /**
     Create new user
     Check that new user has all of its class attributes set to defaults
     PATCH the new user, unmodified
     Check that new user and patched user are equal
     **/
    @Test
	public void testUserPatch_KnownUsers_NoModification() {
        String knownUserEPPN = createNewUser();
        JSONObject knownUserJSON = getUserJson(knownUserEPPN);
        ObjectMapper mapper = new ObjectMapper();
        
        User knownUser = null;
        try{
            knownUser = mapper.readValue(knownUserJSON.toString(), User.class);
        } catch (java.io.IOException e) {
            assertTrue("Cannot map User from knownUserJSON: "+ knownUserJSON.toString(),false);
        }
        
        // check User POJOs
        assertTrue("User account id is > 0", knownUser.getAccountId() > 0);
        assertTrue("User profile id is > 0", knownUser.getProfileId() > 0);
        assertTrue("User company id is = -1", knownUser.getCompanyId() == -1);
        assertTrue("User role is = -1", knownUser.getRole() == -1);
        assertFalse("User termsConditions is false", knownUser.getTermsConditions());

        // check User subattribute classes
        UserOnboarding defaultUserOnboarding = new UserOnboarding();
        assertTrue("New user's onboarding status is not equal to default onboarding status",
                   knownUser.getOnboarding().equals(defaultUserOnboarding));
        
        UserNotifications defaultUserNotifications = new UserNotifications();
        assertTrue("New user's notifications status is not equal to default notifications status",
                   knownUser.getNotifications().equals(defaultUserNotifications));

        UserRunningServices defaultUserRunningServices = new UserRunningServices();
        assertTrue("New user's Running Services status is not equal to default Running Services status",
                   knownUser.getRunningServices().equals(defaultUserRunningServices));

        UserMessages defaultUserMessages = new UserMessages();
        assertTrue("New user's Messages status is not equal to default Running Messages status",
                   knownUser.getMessages().equals(defaultUserMessages));

        String patchedKnownUserJSONinString = null;
        try {
            patchedKnownUserJSONinString = mapper.writeValueAsString(knownUser);
        } catch (JsonProcessingException e) {
            
        }
        
        User patchedKnownUser =
        given().
        header("Content-type", "application/json").
        header("AJP_eppn", knownUserEPPN).
        body(patchedKnownUserJSONinString).
		expect().
        statusCode(200).
		when().
        patch("/user").as(User.class);
        
        // check results of PATCH
        assertTrue("knownUser and patchedKnownUser are not equal", patchedKnownUser.equals(knownUser));
	}
    
    private String createNewUser() {
        String unique = TestUserUtil.generateTime();
        
        Integer id =
        given().
        header("Content-type", "text/plain").
        header("AJP_eppn", "userEPPN" + unique).
        header("AJP_givenName", "userGivenName" + unique).
        header("AJP_sn", "userSurname" + unique).
        header("AJP_displayName", "userDisplayName" + unique).
        header("AJP_mail", "userEmail" + unique).
        expect().
        statusCode(200).
		when().
        post("/users/create").
		then().
        body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).
        extract().path("id");
        
        return new String("userEPPN" + unique);
    }
    
    private JSONObject getUserJson(String userEPPN) {
        String userJsonString =
        given().
            header("AJP_eppn", userEPPN).
        expect().
            statusCode(200).
        when().
            get("/user").
            body().asString();
        
        JSONObject userJson = new JSONObject(userJsonString);
        
        return userJson;
    }
    
    private User getUser(String userEPPN) {
        User user =
		given().
            header("AJP_eppn", userEPPN).
		expect().
            statusCode(200).
		when().
            get("/user").as(User.class);
        
        return user;
    }
}