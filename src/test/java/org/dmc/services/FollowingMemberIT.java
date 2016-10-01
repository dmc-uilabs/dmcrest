package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;

import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.member.FollowingMember;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.restassured.response.ValidatableResponse;

public class FollowingMemberIT extends BaseIT {

    private final static String LOGTAG = FollowingMemberIT.class.getName();

    private static final String FOLLOWING_MEMBERS = "/following_members";
    private static final String FOLLOWING_MEMBERS_BY_ID = "/following_members/{id}";

    @Test
    public void testPostAndGetFollowingList() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testGetFollowingList");

        final String followerUserName = TestUserUtil.createNewUser();
        final int followerId = UserDao.getUserID(followerUserName);
        final String followerIdText = Integer.toString(followerId);
        final String followedUserName = TestUserUtil.createNewUser();
        final int followedId = UserDao.getUserID(followedUserName);
        final String followedIdText = Integer.toString(followedId);

        FollowingMember followRequest = new FollowingMember();
        followRequest.setFollower(followerIdText);
        followRequest.setFollowed(followedIdText);

        final ValidatableResponse postResponse = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", followerUserName).body(followRequest)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final FollowingMember followingMemberResponse = postResponse.extract().as(FollowingMember.class);

        final FollowingMember expected = new FollowingMember();
        expected.setFollower(followerIdText);
        expected.setFollowed(followedIdText);
        assertEquals("POST response does not match expected following member object", expected, followingMemberResponse);

        final ValidatableResponse getResponse = given().header("AJP_eppn", followerUserName)
            .expect().statusCode(OK.value()).when()
            .get(FOLLOWING_MEMBERS).then()
            .body(matchesJsonSchemaInClasspath("Schemas/followingMemberListSchema.json"));

        final ArrayList<FollowingMember> list = readFollowingMemberResponse(getResponse);
        assertTrue("couldn't find newly added follow entry in GET response list", list.contains(expected));

    }

    @Test
    public void testPostAndGetFollowingById() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testPostAndGetFollowingById");

        final String followerUserName = TestUserUtil.createNewUser();
        final int followerId = UserDao.getUserID(followerUserName);
        final String followerIdText = Integer.toString(followerId);
        final String followedUserName = TestUserUtil.createNewUser();
        final int followedId = UserDao.getUserID(followedUserName);
        final String followedIdText = Integer.toString(followedId);

        FollowingMember followRequest = new FollowingMember();
        followRequest.setFollower(followerIdText);
        followRequest.setFollowed(followedIdText);

        final ValidatableResponse postResponse = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", followerUserName).body(followRequest)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final FollowingMember followingMemberResponse = postResponse.extract().as(FollowingMember.class);

        final FollowingMember expected = new FollowingMember();
        expected.setFollower(followerIdText);
        expected.setFollowed(followedIdText);
        assertEquals("POST response does not match expected following member object", expected, followingMemberResponse);

        final ValidatableResponse getResponse = given().header("AJP_eppn", followerUserName)
            .expect().statusCode(OK.value()).when()
            .get(FOLLOWING_MEMBERS_BY_ID, followingMemberResponse.getId()).then()
            .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));
        final FollowingMember followingMemberGetResponse = getResponse.extract().as(FollowingMember.class);

        assertEquals("expected post and get objects to matech", followingMemberResponse, followingMemberGetResponse);

    }

    @Test
    public void testDeleteFollowingMember() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testDeleteFollowingMember");

        final String followerUserName = TestUserUtil.createNewUser();
        final int followerId = UserDao.getUserID(followerUserName);
        final String followerIdText = Integer.toString(followerId);
        final String followedUserName = TestUserUtil.createNewUser();
        final int followedId = UserDao.getUserID(followedUserName);
        final String followedIdText = Integer.toString(followedId);

        FollowingMember followRequest = new FollowingMember();
        followRequest.setFollower(followerIdText);
        followRequest.setFollowed(followedIdText);

        final ValidatableResponse postResponse = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", followerUserName).body(followRequest)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final FollowingMember followingMemberResponse = postResponse.extract().as(FollowingMember.class);

        given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", followerUserName)
                .expect().statusCode(NO_CONTENT.value()).when()
                .delete(FOLLOWING_MEMBERS_BY_ID, followingMemberResponse.getId());

        final ValidatableResponse getResponse = given().header("AJP_eppn", followerUserName)
                .expect().statusCode(OK.value()).when()
                .get(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberListSchema.json"));

        final ArrayList<FollowingMember> list = readFollowingMemberResponse(getResponse);

        assertFalse("entry should have been deleted", list.contains(followingMemberResponse));
    }

    @Test
    public void testPostMultipleFollowersAndGetFollowingList() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testPostMultipleFollowersAndGetFollowingList");

        final String follower1UserName = TestUserUtil.createNewUser();
        final int follower1Id = UserDao.getUserID(follower1UserName);
        final String follower1IdText = Integer.toString(follower1Id);
        final String followedUserName = TestUserUtil.createNewUser();
        final int followedId = UserDao.getUserID(followedUserName);
        final String followedIdText = Integer.toString(followedId);

        FollowingMember follow1Request = new FollowingMember();
        follow1Request.setFollower(follower1IdText);
        follow1Request.setFollowed(followedIdText);

        final ValidatableResponse post1Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", follower1UserName).body(follow1Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final String follower2UserName = TestUserUtil.createNewUser();
        final int follower2Id = UserDao.getUserID(follower2UserName);
        final String follower2IdText = Integer.toString(follower2Id);

        FollowingMember follow2Request = new FollowingMember();
        follow2Request.setFollower(follower2IdText);
        follow2Request.setFollowed(followedIdText);

        final ValidatableResponse post2Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", follower2UserName).body(follow2Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final ValidatableResponse get1Response = given().header("AJP_eppn", follower1UserName)
            .expect().statusCode(OK.value()).when()
            .get(FOLLOWING_MEMBERS).then()
            .body(matchesJsonSchemaInClasspath("Schemas/followingMemberListSchema.json"));

        final ArrayList<FollowingMember> list1 = readFollowingMemberResponse(get1Response);

        final FollowingMember expected1 = new FollowingMember();
        expected1.setFollower(follower1IdText);
        expected1.setFollowed(followedIdText);
        assertTrue("couldn't find newly added follow entry: follower1 " + follower1IdText + " - followed " + followedIdText, list1.contains(expected1));

        final FollowingMember expected2 = new FollowingMember();
        expected2.setFollower(follower2IdText);
        expected2.setFollowed(followedIdText);
        assertTrue("couldn't find newly added follow entry: follower2 " + follower2IdText + " - followed " + followedIdText, list1.contains(expected2));

        final ValidatableResponse get2Response = given().header("AJP_eppn", follower2UserName)
                .expect().statusCode(OK.value()).when()
                .get(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberListSchema.json"));

        final ArrayList<FollowingMember> list2 = readFollowingMemberResponse(get2Response);
        assertEquals("both users should find same results", list1, list2);
    }

    @Test
    public void testGetOnlyMatchingFollowerList() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testGetOnlyMatchingFollowerList");

        final String follower1UserName = TestUserUtil.createNewUser();
        final int follower1Id = UserDao.getUserID(follower1UserName);
        final String follower1IdText = Integer.toString(follower1Id);
        final String followedUserName = TestUserUtil.createNewUser();
        final int followedId = UserDao.getUserID(followedUserName);
        final String followedIdText = Integer.toString(followedId);

        FollowingMember follow1Request = new FollowingMember();
        follow1Request.setFollower(follower1IdText);
        follow1Request.setFollowed(followedIdText);

        final ValidatableResponse post1Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", follower1UserName).body(follow1Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final String follower2UserName = TestUserUtil.createNewUser();
        final int follower2Id = UserDao.getUserID(follower2UserName);
        final String follower2IdText = Integer.toString(follower2Id);

        FollowingMember follow2Request = new FollowingMember();
        follow2Request.setFollower(follower2IdText);
        follow2Request.setFollowed(followedIdText);

        final ValidatableResponse post2Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", follower2UserName).body(follow2Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final ValidatableResponse get1Response = given().header("AJP_eppn", follower2UserName).param("accountId", follower2IdText)
            .expect().statusCode(OK.value()).when()
            .get(FOLLOWING_MEMBERS).then()
            .body(matchesJsonSchemaInClasspath("Schemas/followingMemberListSchema.json"));

        final ArrayList<FollowingMember> list1 = readFollowingMemberResponse(get1Response);
        assertEquals("should only find one following entry when querying by follower", 1, list1.size());

        final FollowingMember notexpected1 = new FollowingMember();
        notexpected1.setFollower(follower1IdText);
        notexpected1.setFollowed(followedIdText);
        assertFalse("should not find follower1 when only looking for follower 2", list1.contains(notexpected1));

        final FollowingMember expected2 = new FollowingMember();
        expected2.setFollower(follower2IdText);
        expected2.setFollowed(followedIdText);
        assertTrue("should find follower2 when only looking for follower 2", list1.contains(expected2));

    }

    @Test
    public void testGetOnlyMatchingFollowedList() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testGetOnlyMatchingFollowedList");

        final String follower1UserName = TestUserUtil.createNewUser();
        final int follower1Id = UserDao.getUserID(follower1UserName);
        final String follower1IdText = Integer.toString(follower1Id);
        final String followedUserName = TestUserUtil.createNewUser();
        final int followedId = UserDao.getUserID(followedUserName);
        final String followedIdText = Integer.toString(followedId);

        FollowingMember follow1Request = new FollowingMember();
        follow1Request.setFollower(follower1IdText);
        follow1Request.setFollowed(followedIdText);

        final ValidatableResponse post1Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", follower1UserName).body(follow1Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final String follower2UserName = TestUserUtil.createNewUser();
        final int follower2Id = UserDao.getUserID(follower2UserName);
        final String follower2IdText = Integer.toString(follower2Id);

        FollowingMember follow2Request = new FollowingMember();
        follow2Request.setFollower(follower2IdText);
        follow2Request.setFollowed(followedIdText);

        final ValidatableResponse post2Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", follower2UserName).body(follow2Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final ValidatableResponse get1Response = given().header("AJP_eppn", follower2UserName).param("profileId", followedIdText)
            .expect().statusCode(OK.value()).when()
            .get(FOLLOWING_MEMBERS).then()
            .body(matchesJsonSchemaInClasspath("Schemas/followingMemberListSchema.json"));

        final ArrayList<FollowingMember> list1 = readFollowingMemberResponse(get1Response);
        assertEquals("should find two following entry when querying by followed", 2, list1.size());

        final FollowingMember expected1 = new FollowingMember();
        expected1.setFollower(follower1IdText);
        expected1.setFollowed(followedIdText);
        assertTrue("should find follower1 when only looking for followed", list1.contains(expected1));

        final FollowingMember expected2 = new FollowingMember();
        expected2.setFollower(follower2IdText);
        expected2.setFollowed(followedIdText);
        assertTrue("should find follower2 when only looking for followed", list1.contains(expected2));

    }

    @Test
    public void testGetOnlyMatchingFollowerAndFollowedList() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testGetOnlyMatchingFollowerAndFollowedList");

        final String follower1UserName = TestUserUtil.createNewUser();
        final int follower1Id = UserDao.getUserID(follower1UserName);
        final String follower1IdText = Integer.toString(follower1Id);
        final String followedUserName = TestUserUtil.createNewUser();
        final int followedId = UserDao.getUserID(followedUserName);
        final String followedIdText = Integer.toString(followedId);

        FollowingMember follow1Request = new FollowingMember();
        follow1Request.setFollower(follower1IdText);
        follow1Request.setFollowed(followedIdText);

        final ValidatableResponse post1Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", follower1UserName).body(follow1Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final String follower2UserName = TestUserUtil.createNewUser();
        final int follower2Id = UserDao.getUserID(follower2UserName);
        final String follower2IdText = Integer.toString(follower2Id);

        FollowingMember follow2Request = new FollowingMember();
        follow2Request.setFollower(follower2IdText);
        follow2Request.setFollowed(followedIdText);

        final ValidatableResponse post2Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", follower2UserName).body(follow2Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final ValidatableResponse get1Response = given().header("AJP_eppn", follower2UserName).param("accountId",  follower2IdText).param("profileId", followedIdText)
            .expect().statusCode(OK.value()).when()
            .get(FOLLOWING_MEMBERS).then()
            .body(matchesJsonSchemaInClasspath("Schemas/followingMemberListSchema.json"));

        final ArrayList<FollowingMember> list1 = readFollowingMemberResponse(get1Response);
        assertEquals("should find only one following entry when querying by followed and follower", 1, list1.size());

        final FollowingMember expected2 = new FollowingMember();
        expected2.setFollower(follower2IdText);
        expected2.setFollowed(followedIdText);
        assertTrue("should find follower2 when only looking for followed and follower 2", list1.contains(expected2));

    }

    @Test
    public void testGetOnlyMatchingMultileFollowed() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testGetOnlyMatchingFollowerAndFollowedList");

        final String followerUserName = TestUserUtil.createNewUser();
        final int followerId = UserDao.getUserID(followerUserName);
        final String followerIdText = Integer.toString(followerId);
        final String followed1UserName = TestUserUtil.createNewUser();
        final int followed1Id = UserDao.getUserID(followed1UserName);
        final String followed1IdText = Integer.toString(followed1Id);

        FollowingMember follow1Request = new FollowingMember();
        follow1Request.setFollower(followerIdText);
        follow1Request.setFollowed(followed1IdText);

        final ValidatableResponse post1Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", followerUserName).body(follow1Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final String followed2UserName = TestUserUtil.createNewUser();
        final int followed2Id = UserDao.getUserID(followed2UserName);
        final String followed2IdText = Integer.toString(followed2Id);

        FollowingMember follow2Request = new FollowingMember();
        follow2Request.setFollower(followerIdText);
        follow2Request.setFollowed(followed2IdText);

        final ValidatableResponse post2Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", followerUserName).body(follow2Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final ValidatableResponse get1Response = given().header("AJP_eppn", followerUserName).param("accountId",  followerIdText).param("profileId", followed1IdText).param("profileId", followed2IdText)
            .expect().statusCode(OK.value()).when()
            .get(FOLLOWING_MEMBERS).then()
            .body(matchesJsonSchemaInClasspath("Schemas/followingMemberListSchema.json"));

        final ArrayList<FollowingMember> list1 = readFollowingMemberResponse(get1Response);
        assertEquals("should find only two following entry when querying by both followeds and follower", 2, list1.size());

        final FollowingMember expected1 = new FollowingMember();
        expected1.setFollower(followerIdText);
        expected1.setFollowed(followed1IdText);
        assertTrue("failed to find followed 1 and follower", list1.contains(expected1));

        final FollowingMember expected2 = new FollowingMember();
        expected2.setFollower(followerIdText);
        expected2.setFollowed(followed2IdText);
        assertTrue("failed to find followed 2 and follower", list1.contains(expected2));

    }

    private ArrayList<FollowingMember> readFollowingMemberResponse(ValidatableResponse response) {
        final ArrayList<FollowingMember> list = new ArrayList<FollowingMember>();
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject json = jsonArray.getJSONObject(i);
            final FollowingMember item = new FollowingMember();
            item.setFollower(json.getString("accountId"));
            item.setFollowed(json.getString("profileId"));
            list.add(item);
        }
        return list;
    }
}
