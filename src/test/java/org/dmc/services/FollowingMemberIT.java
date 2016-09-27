package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.member.PostFollowingMember;
import org.dmc.services.utility.TestUserUtil;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.jayway.restassured.response.ValidatableResponse;

public class FollowingMemberIT extends BaseIT {

    private final static String LOGTAG = FollowingMemberIT.class.getName();

    private static final String FOLLOWING_MEMBERS = "/following_members";

    @Test
    public void testGetFollowingList() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testGetFollowingList");
//        @RequestParam(value = "accountId", required = false) String accountId,
//        @RequestParam(value = "id", required = false) String id,
//        @RequestParam(value = "profileId", required = false) String profileId,
//        @RequestParam(value = "limit", required = false) Integer limit,
//        @RequestParam(value = "start", required = false) Integer start,
//        @RequestParam(value = "order", required = false) String order,
//        @RequestParam(value = "sort", required = false) String sort, 
//        @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

        ServiceLogger.log(LOGTAG, "userEPPN = " + userEPPN);

        final String followerUserName = TestUserUtil.createNewUser();
        final int followerId = UserDao.getUserID(followerUserName);
        final String followerIdText = Integer.toString(followerId);
        final String followedUserName = TestUserUtil.createNewUser();
        final int followedId = UserDao.getUserID(followedUserName);
        final String followedIdText = Integer.toString(followedId);

        PostFollowingMember followRequest = new PostFollowingMember();
        followRequest.setAccountId(followerIdText);
        followRequest.setProfileId(followedIdText);
        
        final ValidatableResponse postResponse = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", followerUserName).body(followRequest)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .log().all().body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final ValidatableResponse getResponse = given().header("AJP_eppn", followerUserName)
            .expect().statusCode(OK.value()).when()
            .get(FOLLOWING_MEMBERS).then()
            .log().all().body(matchesJsonSchemaInClasspath("Schemas/followingMemberListSchema.json"));

    }

}
