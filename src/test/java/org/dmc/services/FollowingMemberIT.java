package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;

import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.member.PostFollowingMember;
import org.dmc.services.member.FollowingMember;
import org.dmc.services.projects.ProjectJoinRequest;
import org.dmc.services.utility.TestUserUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.jayway.restassured.response.ValidatableResponse;

public class FollowingMemberIT extends BaseIT {

    private final static String LOGTAG = FollowingMemberIT.class.getName();

    private static final String FOLLOWING_MEMBERS = "/following_members";

    @Test
    public void testPostAndGetFollowingList() throws Exception {
        ServiceLogger.log(LOGTAG, "starting testGetFollowingList");

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
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final ValidatableResponse getResponse = given().header("AJP_eppn", followerUserName)
            .expect().statusCode(OK.value()).when()
            .get(FOLLOWING_MEMBERS).then()
            .body(matchesJsonSchemaInClasspath("Schemas/followingMemberListSchema.json"));

        final ArrayList<FollowingMember> list = readFollowingMemberResponse(getResponse);

        final FollowingMember expected = new FollowingMember();
        expected.setAccountId(followerIdText);
        expected.setProfileId(followedIdText);
        assertTrue("couldn't find newly added follow entry", list.contains(expected));

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

        PostFollowingMember follow1Request = new PostFollowingMember();
        follow1Request.setAccountId(follower1IdText);
        follow1Request.setProfileId(followedIdText);

        final ValidatableResponse post1Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", follower1UserName).body(follow1Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final String follower2UserName = TestUserUtil.createNewUser();
        final int follower2Id = UserDao.getUserID(follower2UserName);
        final String follower2IdText = Integer.toString(follower2Id);

        PostFollowingMember follow2Request = new PostFollowingMember();
        follow2Request.setAccountId(follower2IdText);
        follow2Request.setProfileId(followedIdText);

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
        expected1.setAccountId(follower1IdText);
        expected1.setProfileId(followedIdText);
        assertTrue("couldn't find newly added follow entry: follower1 " + follower1IdText + " - followed " + followedIdText, list1.contains(expected1));

        final FollowingMember expected2 = new FollowingMember();
        expected2.setAccountId(follower2IdText);
        expected2.setProfileId(followedIdText);
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

        PostFollowingMember follow1Request = new PostFollowingMember();
        follow1Request.setAccountId(follower1IdText);
        follow1Request.setProfileId(followedIdText);

        final ValidatableResponse post1Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", follower1UserName).body(follow1Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final String follower2UserName = TestUserUtil.createNewUser();
        final int follower2Id = UserDao.getUserID(follower2UserName);
        final String follower2IdText = Integer.toString(follower2Id);

        PostFollowingMember follow2Request = new PostFollowingMember();
        follow2Request.setAccountId(follower2IdText);
        follow2Request.setProfileId(followedIdText);

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
        notexpected1.setAccountId(follower1IdText);
        notexpected1.setProfileId(followedIdText);
        assertFalse("should not find follower1 when only looking for follower 2", list1.contains(notexpected1));

        final FollowingMember expected2 = new FollowingMember();
        expected2.setAccountId(follower2IdText);
        expected2.setProfileId(followedIdText);
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

        PostFollowingMember follow1Request = new PostFollowingMember();
        follow1Request.setAccountId(follower1IdText);
        follow1Request.setProfileId(followedIdText);

        final ValidatableResponse post1Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", follower1UserName).body(follow1Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final String follower2UserName = TestUserUtil.createNewUser();
        final int follower2Id = UserDao.getUserID(follower2UserName);
        final String follower2IdText = Integer.toString(follower2Id);

        PostFollowingMember follow2Request = new PostFollowingMember();
        follow2Request.setAccountId(follower2IdText);
        follow2Request.setProfileId(followedIdText);

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
        expected1.setAccountId(follower1IdText);
        expected1.setProfileId(followedIdText);
        assertTrue("should find follower1 when only looking for followed", list1.contains(expected1));

        final FollowingMember expected2 = new FollowingMember();
        expected2.setAccountId(follower2IdText);
        expected2.setProfileId(followedIdText);
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

        PostFollowingMember follow1Request = new PostFollowingMember();
        follow1Request.setAccountId(follower1IdText);
        follow1Request.setProfileId(followedIdText);

        final ValidatableResponse post1Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", follower1UserName).body(follow1Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final String follower2UserName = TestUserUtil.createNewUser();
        final int follower2Id = UserDao.getUserID(follower2UserName);
        final String follower2IdText = Integer.toString(follower2Id);

        PostFollowingMember follow2Request = new PostFollowingMember();
        follow2Request.setAccountId(follower2IdText);
        follow2Request.setProfileId(followedIdText);

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
        expected2.setAccountId(follower2IdText);
        expected2.setProfileId(followedIdText);
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

        PostFollowingMember follow1Request = new PostFollowingMember();
        follow1Request.setAccountId(followerIdText);
        follow1Request.setProfileId(followed1IdText);

        final ValidatableResponse post1Response = given().header("Content-type", APPLICATION_JSON_VALUE).header("AJP_eppn", followerUserName).body(follow1Request)
                .expect().statusCode(OK.value()).when()
                .post(FOLLOWING_MEMBERS).then()
                .body(matchesJsonSchemaInClasspath("Schemas/followingMemberSchema.json"));

        final String followed2UserName = TestUserUtil.createNewUser();
        final int followed2Id = UserDao.getUserID(followed2UserName);
        final String followed2IdText = Integer.toString(followed2Id);

        PostFollowingMember follow2Request = new PostFollowingMember();
        follow2Request.setAccountId(followerIdText);
        follow2Request.setProfileId(followed2IdText);

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
        expected1.setAccountId(followerIdText);
        expected1.setProfileId(followed1IdText);
        assertTrue("failed to find followed 1 and follower", list1.contains(expected1));

        final FollowingMember expected2 = new FollowingMember();
        expected2.setAccountId(followerIdText);
        expected2.setProfileId(followed2IdText);
        assertTrue("failed to find followed 2 and follower", list1.contains(expected2));

    }

    private ArrayList<FollowingMember> readFollowingMemberResponse(ValidatableResponse response) {
        final ArrayList<FollowingMember> list = new ArrayList<FollowingMember>();
        final JSONArray jsonArray = new JSONArray(response.extract().asString());
        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject json = jsonArray.getJSONObject(i);
            final FollowingMember item = new FollowingMember();
            item.setAccountId(json.getString("accountId"));
            item.setProfileId(json.getString("profileId"));
            list.add(item);
        }
        return list;
    }
}
