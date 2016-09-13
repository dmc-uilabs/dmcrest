package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import java.sql.SQLException;

import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONObject;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Ignore
public class UserIT extends BaseIT {

    private static final String LOGTAG = UserIT.class.getName();

    private static final String USER_RESOURCE = "/user";
    private static final String USER_CREATE_RESOURCE = "/users/create";
    private static final String USER_INFO_RESOURCE = "/user-basic-information";

    @Test
    public void testNewUserWithCompany() {
        final String newUserEPPN = TestUserUtil.createNewUser();

        // create user; will not have company
        final UserModel newUser =
            given().
                header("Content-type", APPLICATION_JSON_VALUE).
                header("AJP_eppn", newUserEPPN).
                header("AJP_givenName",newUserEPPN).
                header("AJP_sn",newUserEPPN).
                header("AJP_displayName",newUserEPPN).
                header("AJP_givenName",newUserEPPN).
            expect().
                statusCode(OK.value()).
            when().
                get(USER_RESOURCE).as(UserModel.class);

        // check; does not have have company
        assertTrue("New user is assigned company " + newUser.getCompanyId() + " when the comapnay should be -1", newUser.getCompanyId() == -1);

        // add basis information
        final JSONObject json = new JSONObject();
        json.put("email", "test basic info email");
        json.put("firstName", "test basic info first name");
        json.put("lastName", "test basic info last name");
        json.put("company", "1");

        given()
            .header("Content-type", APPLICATION_JSON_VALUE)
            .header("AJP_eppn", newUserEPPN)
            .body(json.toString())
        .expect()
            .statusCode(OK.value())
        .when()
        .post(USER_INFO_RESOURCE);//.as(Integer.class);

        final UserModel newUserGet =
            given().
                header("Content-type", APPLICATION_JSON_VALUE).
                header("AJP_eppn", newUserEPPN).
                header("AJP_givenName",newUserEPPN).
                header("AJP_sn",newUserEPPN).
                header("AJP_displayName",newUserEPPN).
                header("AJP_givenName",newUserEPPN).
            expect().
                statusCode(OK.value()).
            when().
                get(USER_RESOURCE).as(UserModel.class);

        // check user; should know have company

    }

    @Test
    public void testUserIncorrectInvocation(){
        ServiceLogger.log(LOGTAG, "starting testUserIncorrectInvocation");
        final JSONObject json = new JSONObject();
        final String unique = TestUserUtil.generateTime();

        // callin enpoint with content is payload, which is not used when received.
        json.put("user_name", "username " + unique);
        json.put("email", "testemail" + unique + "@ge.com");
        json.put("password", "pwd " + unique);
        json.put("name", "usertester " + unique);


        final Integer id = given().body(json.toString()).expect().statusCode(OK.value()).when().post(USER_CREATE_RESOURCE).then()
        .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).extract().path("id");

        assertTrue("Valid user ID return.  Value is " + id + ", but should be -99999.", id == -99999);
    }

    @Test
    public void testUserCreate(){
        ServiceLogger.log(LOGTAG, "starting testUserCreate");
        final String unique = TestUserUtil.generateTime();

        final Integer id =
            given().
                header("Content-type", TEXT_PLAIN_VALUE).
                header("AJP_eppn", "userEPPN" + unique).
                header("AJP_givenName", "userGivenName" + unique).
                header("AJP_sn", "userSurname" + unique).
                header("AJP_displayName", "userDisplayName" + unique).
                header("AJP_mail", "userEmail" + unique).
                expect().
                statusCode(OK.value()).
                when().
                post(USER_CREATE_RESOURCE).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).
                extract().path("id");

        // check return value > 0
        assertTrue("Added user: " + "userEPPN" + unique + " Valid user ID must be greater then zero.  Value is " + id + ".", id > 0);
        // could also check email syntax

    }

    public static Integer createUserAndReturnID(String unique){
        ServiceLogger.log(LOGTAG, "starting testUserCreate: in helper");

        final Integer id =
            given().
                header("Content-type", TEXT_PLAIN_VALUE).
                header("AJP_eppn", "userEPPN" + unique).
                header("AJP_givenName", "userGivenName" + unique).
                header("AJP_sn", "userSurname" + unique).
                header("AJP_displayName", "display" + unique).
                header("AJP_mail", "userEmail" + unique).
                expect().
                statusCode(OK.value()).
                when().
                post(USER_CREATE_RESOURCE).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).
                extract().path("id");

        return id;
    }

    @Test
    public void testGetUserNameAndGetUserID(){

        ServiceLogger.log(LOGTAG, "starting testUserCreate");
        final String unique = TestUserUtil.generateTime();

        final Integer id = createUserAndReturnID(unique);
        // check return value > 0
        assertTrue("Added user: " + "userEPPN" + unique + " Valid user ID must be greater then zero.  Value is " + id + ".", id > 0);
        // could also check email syntax

        String newUserName = null;
        try {
            newUserName = UserDao.getUserName(id);
        } catch (SQLException e) {
            assertTrue("java.sql.SQLException thrown",false);
        }
        assertTrue("User names are not equal", newUserName.equals("userEPPN" + unique));

        int newUserId = -1;
        try {
            newUserId = UserDao.getUserID("userEPPN" + unique);
        } catch(SQLException e) {
            assertTrue("java.sql.SQLException thrown",false);
        }
        assertTrue("User IDs are not equal", id.equals(newUserId));
    }


    @Test
    public void testUserGet_UnknownUser(){
        ServiceLogger.log(LOGTAG, "starting testUserGet_UnknownUser");
        final String unknownUser = TestUserUtil.uniqueUserEPPN();

        given().
            header("AJP_eppn", unknownUser).
            expect().
            statusCode(OK.value()).
            when().
            get(USER_RESOURCE).
            then().
            body(matchesJsonSchemaInClasspath("Schemas/userSchema.json"));
    }

    @Test
    public void testUserGet_KnownUser(){
        ServiceLogger.log(LOGTAG, "starting testUserGet_KnownUser");
        final String knownUser = "fforgeadmin";

        given().
            header("AJP_eppn", knownUser).
            expect().
            statusCode(OK.value()).
            when().
            get(USER_RESOURCE).
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
        final String knownUserEPPN = createNewUser();

        final UserModel knownUser = getUser(knownUserEPPN);
        final JSONObject knownUserJSON = getUserJson(knownUserEPPN);

        // update user's display name
        final String unique = TestUserUtil.generateTime();
        knownUserJSON.put("displayName", "NEWDisplayName" + unique);

        final UserModel patchedKnownUser =
            given().
                header("Content-type", APPLICATION_JSON_VALUE).
                header("AJP_eppn", knownUserEPPN).
                body(knownUserJSON.toString()).
            expect().
                statusCode(OK.value()).
            when().
            patch(USER_RESOURCE).as(UserModel.class);

        ServiceLogger.log(LOGTAG, "Body of patchedKnownUser is " + patchedKnownUser + ".");

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
        final String knownUserEPPN = createNewUser();
        final UserModel knownUser = getUser(knownUserEPPN);
        final ObjectMapper mapper = new ObjectMapper();

        final boolean profile = knownUser.getOnboarding().getProfile();
        final boolean account = knownUser.getOnboarding().getAccount();
        final boolean company = knownUser.getOnboarding().getCompany();
        final boolean storefront = knownUser.getOnboarding().getStorefront();

        knownUser.getOnboarding().setProfile(!profile);
        knownUser.getOnboarding().setAccount(!account);
        knownUser.getOnboarding().setCompany(!company);
        knownUser.getOnboarding().setStorefront(!storefront);

        String patchedKnownUserJSONinString = null;
        try {
            patchedKnownUserJSONinString = mapper.writeValueAsString(knownUser);
        } catch (JsonProcessingException e) {

        }

        final UserModel patchedKnownUser =
            given().
                header("Content-type", APPLICATION_JSON_VALUE).
                header("AJP_eppn", knownUserEPPN).
                body(patchedKnownUserJSONinString).
            expect().
                statusCode(OK.value()).
            when().
                patch(USER_RESOURCE).as(UserModel.class);

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
        final String knownUserEPPN = createNewUser();
        final JSONObject knownUserJSON = getUserJson(knownUserEPPN);
        final ObjectMapper mapper = new ObjectMapper();

        // why are we testing that a new user's default values are correct in this test as opposed to
        // a user-creation specific test?
//        UserModel knownUser = null;
//        try{
//            knownUser = mapper.readValue(knownUserJSON.toString(), UserModel.class);
//        } catch (java.io.IOException e) {
//            assertTrue("Cannot map User from knownUserJSON: "+ knownUserJSON.toString(),false);
//        }
//
//
//        // check User POJOs
//        assertTrue("User account id is > 0", knownUser.getAccountId() > 0);
//        assertTrue("User profile id is > 0", knownUser.getProfileId() > 0);
//        assertTrue("User company id is set unexpectedly", knownUser.getCompanyId() == -1);
//        assertTrue("User role is set unexpectedaly", knownUser.getRole() == -1);
//        assertFalse("User termsConditions have not been expected (false)", knownUser.getTermsConditions());
//
//        // check User subattribute classes
//        final UserOnboarding defaultUserOnboarding = new UserOnboarding();
//        assertTrue("New user's onboarding status is not equal to default onboarding status",
//                   knownUser.getOnboarding().equals(defaultUserOnboarding));
//
//        final UserNotifications defaultUserNotifications = new UserNotifications();
//        assertTrue("New user's notifications status is not equal to default notifications status",
//                   knownUser.getNotifications().equals(defaultUserNotifications));
//
//        final UserRunningServices defaultUserRunningServices = new UserRunningServices();
//        assertTrue("New user's Running Services status is not equal to default Running Services status",
//                   knownUser.getRunningServices().equals(defaultUserRunningServices));
//
//        final UserMessages defaultUserMessages = new UserMessages();
//        assertTrue("New user's Messages status is not equal to default Running Messages status",
//                   knownUser.getMessages().equals(defaultUserMessages));
//
//        String patchedKnownUserJSONinString = null;
//        try {
//            patchedKnownUserJSONinString = mapper.writeValueAsString(knownUser);
//        } catch (JsonProcessingException e) {
//
//        }

        UserModel patchedKnownUser =
            given().
                header("Content-type", APPLICATION_JSON_VALUE).
                header("AJP_eppn", knownUserEPPN).
                body(knownUserJSON.toString()).
                expect().
                statusCode(OK.value()).
                when().
                patch(USER_RESOURCE).as(UserModel.class);

        // check results of PATCH
        assertTrue("knownUser and patchedKnownUser are not equal", patchedKnownUser.equals(knownUser));
    }

    private String createNewUser() {
        final String unique = TestUserUtil.generateTime();

        final Integer id =
            given().
                header("Content-type", TEXT_PLAIN_VALUE).
                header("AJP_eppn", "userEPPN" + unique).
                header("AJP_givenName", "userGivenName" + unique).
                header("AJP_sn", "userSurname" + unique).
                header("AJP_displayName", "userDisplayName" + unique).
                header("AJP_mail", "userEmail" + unique).
                expect().
                statusCode(OK.value()).
                when().
                post(USER_CREATE_RESOURCE).
                then().
                body(matchesJsonSchemaInClasspath("Schemas/idSchema.json")).
                extract().path("id");

        return new String("userEPPN" + unique);
    }

    private JSONObject getUserJson(String userEPPN) {
        final String userJsonString =
            given().
                header("AJP_eppn", userEPPN).
            expect().
                statusCode(OK.value()).
            when().
                get(USER_RESOURCE).
                body().asString();

        final JSONObject userJson = new JSONObject(userJsonString);

        return userJson;
    }

    private UserModel getUser(String userEPPN) {
        final UserModel user =
            given().
                header("AJP_eppn", userEPPN).
            expect().
                statusCode(OK.value()).
            when().
                get(USER_RESOURCE).as(UserModel.class);

        return user;
    }
}