package org.dmc.services;

/**
 * Created by 200005921 on 6/9/2016.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.dmc.services.company.Company;
import org.dmc.services.profile.ProfileReview;
import org.dmc.services.reviews.ReviewFlagged;
import org.dmc.services.utility.CommonUtils;
import org.dmc.services.utility.TestUserUtil;
import org.dmc.services.utility.TestReviewUtil;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;

public class ProfileReviewIT extends BaseIT {

    private static final String logTag = ProfileReviewIT.class.getName();

    private static final String PROFILE_REVIEW_GET_RESOURCE = "/profiles/{productID}/profile_reviews";
    private static final String PROFILE_REVIEW_POST_RESOURCE = "/profile_reviews";
    private static final String PROFILE_REVIEW_HELPFULL_POST_RESOURCE = "/profile_reviews_helpful";
    private static final String PROFILE_REVIEW_FLAGGED_POST_RESOURCE = "/profile_reviews_flagged";
    private static final String PROFILE_REVIEW_GET_BY_ID = "/profile_reviews/{reviewId}";
    private static final String PROFILE_REVIEW_PATCH_BY_ID = "/profile_reviews/{reviewId}";
    private static final String PROFILE_REVIEW_REVIEW_REPLIES_BY_REVIEW_ID = "/review_replies/{reviewId}";

    private int ownerUserId = -1;
    private String ownerEPPN;

    private int memberUserId = -1;
    private String memberEPPN;
    private String memberDisplayName;

    private int nonMemberUserId = -1;
    private String nonMemberEPPN;
    private String nonMemberDisplayName;

    private int companyId = -1;
    private int reviewId = -1;
    private int profileId = -1;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static final int DEFAULT_RATING = 3;
    public static final int DEFAULT_LIKES = 0;
    public static final int DEFAULT_DISLIKES = 0;
    public static final String DEFAULT_COMMENT = "This is my awesome profile comment";
    public static final String DEFAULT_REPLY_COMMENT = DEFAULT_COMMENT + " reply";

    @Before
    public void setupData () {

        ServiceLogger.log(logTag, "starting setupData");
        String unique = TestUserUtil.generateTime();

        // set up user as owner of profile
        ownerEPPN = "userEPPN" + unique;
        String ownerGivenName = "userGivenName" + unique;
        String ownerSurName = "userSurName" + unique;
        String ownerDisplayName = "userDisplayName" + unique;
        String ownerEmail = "userEmail" + unique;

        ownerUserId = CommonUtils.createUser(ownerEPPN, ownerGivenName, ownerSurName, ownerDisplayName, ownerEmail);
        profileId = ownerUserId;
        
        // add company
        companyId = CommonUtils.createCompany(ownerEPPN);

        // Create a user that is a member of the company
        String unique3 = TestUserUtil.generateTime();
        memberEPPN = "userEPPN" + unique3;
        String member3GivenName = "userGivenName" + unique3;
        String member3SurName= "userSurName" + unique3;
        String member3DisplayName = "userDisplayName" + unique3;
        memberDisplayName = member3DisplayName;
        String member3Email = "userEmail" + unique3;

        memberUserId = CommonUtils.createUser(memberEPPN,member3GivenName, member3SurName, member3DisplayName, member3Email);
        CommonUtils.addMemberToCompany(memberUserId, companyId, ownerEPPN);

        // Create a user that is not a member of the company
        String unique4 = TestUserUtil.generateTime();
        nonMemberEPPN = "userEPPN" + unique4;
        String nonMember3GivenName = "userGivenName" + unique4;
        String nonMember3SurName= "userSurName" + unique4;
        String nonMember3DisplayName = "userDisplayName" + unique4;
        String nonMember3Email = "userEmail" + unique4;
        nonMemberDisplayName = nonMember3DisplayName;

        nonMemberUserId = CommonUtils.createUser(nonMemberEPPN,nonMember3GivenName, nonMember3SurName, nonMember3DisplayName, nonMember3Email);

        // int profileId, String name, int accountId, String comment, String userEPPN
        int parentReviewId = 0;
        reviewId = addReview(profileId, memberDisplayName, memberUserId, DEFAULT_COMMENT, memberEPPN, parentReviewId);
    }

    @Test
    public void testPatchProfileByProfileReviewId(){
    	List<ProfileReview> list =Arrays.asList(given()
    	         .header("AJP_eppn", memberEPPN)
    	         .expect()
    	         .statusCode(HttpStatus.OK.value())
    	         .when()
    	         .get(PROFILE_REVIEW_GET_BY_ID, reviewId)
    	         .as(ProfileReview[].class));
    	assertTrue(list != null);
    	ProfileReview res = list.get(0);
    	String name = res.getName();
    	int accountId = Integer.parseInt(res.getAccountId());
    	String comment = res.getComment() + " update comments by test user";
    	String reviewId = res.getReviewId();
    	int id = Integer.parseInt(res.getId());
    	int profileId = Integer.parseInt(res.getProfileId());
    	
    	 boolean status = true;
         BigDecimal date = BigDecimal.valueOf(Calendar.getInstance().getTime().getTime());
         int rating = DEFAULT_RATING + 1;
         int likes = DEFAULT_LIKES;
         int dislikes = DEFAULT_DISLIKES;
         boolean reply = false;
    	
    	String json = createPatchProfileReviewFixture(id, profileId, name, reply, reviewId, status, date, rating, likes, dislikes, comment, accountId);
    	ProfileReview patch_res = given()
    			.header("Content-type", APPLICATION_JSON_VALUE)  
   	         .header("AJP_eppn", memberEPPN)
   	         .body(json.toString())
   	         .expect()
   	         .statusCode(HttpStatus.OK.value())
   	         .when()
   	         .patch(PROFILE_REVIEW_PATCH_BY_ID, reviewId)
   	         .as(ProfileReview.class);
    	assertTrue(patch_res != null);
    	
    	List<ProfileReview> getPatchedResult =Arrays.asList(given()
    			
   	         .header("AJP_eppn", memberEPPN)
   	         .expect()
   	         .statusCode(HttpStatus.OK.value())
   	         .when()
   	         .get(PROFILE_REVIEW_GET_BY_ID, reviewId)
   	         .as(ProfileReview[].class));
    	assertTrue(getPatchedResult != null);
    	ProfileReview review = getPatchedResult.get(0);
    	assertTrue(review.getComment().equals(comment));
    	assertTrue(review.getRating() == rating);

    }
    
    @Test
    public void testGetReviewsMember () {
        ProfileReview[] profileReviews  =
                given()
                        .header("Content-type", APPLICATION_JSON_VALUE)
                        .header("AJP_eppn", memberEPPN)
                        .parameters("reviewId", "0")
                        .expect()
                        .statusCode(200)
                        .when()
                        .get(PROFILE_REVIEW_GET_RESOURCE, profileId)
                        .as(ProfileReview[].class);

        assertTrue("Profile review list cannot be null", profileReviews != null);
        assertTrue("Expected 1 profile review, got" + profileReviews.length, profileReviews.length == 1);

        ProfileReview review = profileReviews[0];
        ServiceLogger.log(logTag, "starting review "+review.toString());

        assertTrue(review.getId() != null && review.getId().equals(Integer.toString(reviewId)));
        assertTrue(review.getReviewId() != null && review.getReviewId().equals(Integer.toString(0)));
        assertTrue(review.getAccountId() != null && review.getAccountId().equals(Integer.toString(memberUserId)));
        assertTrue(review.getName() != null && review.getName().equals(memberDisplayName));
        assertTrue(review.getReply() != null && review.getReply().equals(Boolean.FALSE));

        // Note: that only the top-level reviews have a stars rating; replies do not have stars.
        assertTrue(review.getRating() != null && review.getRating().intValue() == DEFAULT_RATING);

        // Note: Until the endpoints for helpful reviews are implemented, likes/dislikes = 0
        assertTrue("Expected more then zero review likes", review.getLike() != null && review.getLike().intValue() > 0);
        assertTrue("Expected more then zero review dislikes", review.getDislike() != null && review.getDislike().intValue() > 0);
        assertTrue(review.getStatus() != null && review.getStatus().booleanValue() == true);
        assertTrue(review.getComment() != null && review.getComment().equals(DEFAULT_COMMENT));
    }

    @Test
    public void testGetProfileReviewByReviewId(){
    	List<ProfileReview> res = given()
         .header("AJP_eppn", memberEPPN)
         .expect()
         .statusCode(HttpStatus.OK.value())
         .when()
         .get(PROFILE_REVIEW_GET_BY_ID, reviewId)
         .as(List.class);
    	assertTrue(res.size() == 1);
    }
    
    @Test
    public void testGetReviewRepliesByReviewId(){
    	List<ProfileReview> res = given()
         .header("AJP_eppn", memberEPPN)
         .expect()
         .statusCode(HttpStatus.OK.value())
         .when()
         .get(PROFILE_REVIEW_REVIEW_REPLIES_BY_REVIEW_ID, reviewId)
         .as(List.class);
    }
    
   @Test
    public void testAddReviewNonMember () {

        int reviewId = 0;
        String json = createProfileReviewFixture(profileId, nonMemberDisplayName, nonMemberUserId, "This is a cool comment", reviewId);

        given()
                .header("Content-type", APPLICATION_JSON_VALUE)
                .header("AJP_eppn", nonMemberEPPN)
                .body(json.toString())
                .expect()
                .statusCode(HttpStatus.OK.value())
                .when()
                .post(PROFILE_REVIEW_POST_RESOURCE);
    }

    @Test
    public void testReviewAndReplies() {

        // Add a new review
        int reviewId = addReview(profileId, memberDisplayName, memberUserId, "My awesome review", memberEPPN, 0);

        ReviewFlagged createdReviewFlagged = TestReviewUtil.addFlaggedReview(reviewId, memberUserId, "Reason", "Comment", memberEPPN, PROFILE_REVIEW_FLAGGED_POST_RESOURCE);
        ReviewFlagged[] retrievedReviewFlagged = TestReviewUtil.getFlaggedReview(reviewId, memberUserId, memberEPPN, PROFILE_REVIEW_FLAGGED_POST_RESOURCE);
        assertTrue("not equal", createdReviewFlagged.equals(retrievedReviewFlagged[0]));

        ProfileReview[] profileReviews  =
                given()
                        .header("Content-type", APPLICATION_JSON_VALUE)
                        .header("AJP_eppn", memberEPPN)
                        .parameters("reviewId", "0")
                        .expect()
                        .statusCode(200)
                        .when()
                        .get(PROFILE_REVIEW_GET_RESOURCE, profileId)
                        .as(ProfileReview[].class);

        assertTrue("Company review list cannot be null", profileReviews != null);
        assertTrue("Expected 2 profile reviews, got" + profileReviews.length, profileReviews.length == 2);

        // Add a review reply
        int replyReviewId = addReview(profileId, memberDisplayName, memberUserId, DEFAULT_REPLY_COMMENT + " 1", memberEPPN, reviewId);

        // Add a review reply
        int replyReviewId2 = addReview(profileId, memberDisplayName, memberUserId, DEFAULT_REPLY_COMMENT + " 2", memberEPPN, reviewId);

        ProfileReview[] profileReviewReplies  =
                given()
                        .header("Content-type", APPLICATION_JSON_VALUE)
                        .header("AJP_eppn", memberEPPN)
                        .parameters("reviewId", Integer.toString(reviewId))
                        .expect()
                        .statusCode(200)
                        .when()
                        .get(PROFILE_REVIEW_GET_RESOURCE, profileId)
                        .as(ProfileReview[].class);

        assertTrue("Company review list cannot be null", profileReviewReplies != null);
        assertTrue("Expected 2 profile review, got" + profileReviewReplies.length, profileReviewReplies.length == 2);

        for (int i=0; i < profileReviewReplies.length; i++) {
            ProfileReview review = profileReviewReplies[i];

            int expectedReplyId = 0;
            String expectedComment = null;
            assertTrue(review.getId() != null);
            if (Integer.parseInt(review.getId()) == replyReviewId) {
                expectedReplyId = replyReviewId;
                expectedComment = DEFAULT_REPLY_COMMENT + " 1";
            } else if (Integer.parseInt(review.getId()) == replyReviewId2) {
                expectedReplyId = replyReviewId2;
                expectedComment = DEFAULT_REPLY_COMMENT + " 2";
            }

            assertTrue(review.getId() != null && review.getId().equals(Integer.toString(expectedReplyId)));
            assertTrue(review.getReviewId() != null && review.getReviewId().equals(Integer.toString(reviewId)));
            assertTrue(review.getAccountId() != null && review.getAccountId().equals(Integer.toString(memberUserId)));
            assertTrue(review.getName() != null && review.getName().equals(memberDisplayName));
            assertTrue(review.getReply() != null && review.getReply().equals(Boolean.FALSE));

            // Note: that only the top-level reviews have a stars rating; replies do not have stars.
            assertTrue(review.getRating() != null && review.getRating().intValue() == 0);

            // Note: Until the endpoints for helpful reviews are implemented, likes/dislikes = 0
            assertTrue(review.getLike() != null && review.getLike().intValue() == DEFAULT_LIKES);
            assertTrue(review.getDislike() != null && review.getDislike().intValue() == DEFAULT_DISLIKES);
            assertTrue(review.getStatus() != null && review.getStatus().booleanValue() == true);
            assertTrue(review.getComment() != null && review.getComment().equals(expectedComment));
        }

    }

    public static String createProfileReviewFixture(int profileId, String name, int accountId, String comment, int reviewId)
    {
        //String reviewId = Integer.toString(reviewIdCount++);
        boolean status = true;
        BigDecimal date = BigDecimal.valueOf(Calendar.getInstance().getTime().getTime());
        int rating = DEFAULT_RATING;
        int likes = DEFAULT_LIKES;
        int dislikes = DEFAULT_DISLIKES;
        boolean reply = false;

        return createProfileReviewFixture(profileId, name, reply, Integer.toString(reviewId), status, date, rating, likes, dislikes, comment, accountId);
    }

    public static String createProfileReviewFixture(int profileId,
                                                    String name,
                                                    boolean reply,
                                                    String reviewId,
                                                    boolean status,
                                                    BigDecimal date,
                                                    int rating,
                                                    int likes,
                                                    int dislikes,
                                                    String comment,
                                                    int accountId)
    {

        ProfileReview r = new ProfileReview();
        //r.setId(Integer.toString(resultSet.getInt("kd")));
        r.setProfileId(Integer.toString(profileId));
        r.setName(name);
        r.setReply(reply);
        r.setReviewId(reviewId);
        r.setStatus(status);
        r.setDate(date);
        r.setRating(rating);
        r.setLike(likes);
        r.setDislike(dislikes);
        r.setComment(comment);
        r.setAccountId(Integer.toString(accountId));

        ObjectMapper mapper = new ObjectMapper();
        String jSONString = null;
        try {
            jSONString = mapper.writeValueAsString(r);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jSONString;

    }

	public static String createPatchProfileReviewFixture(int id, int profileId, String name, boolean reply, String reviewId,
			boolean status, BigDecimal date, int rating, int likes, int dislikes, String comment, int accountId) {

		ProfileReview r = new ProfileReview();
		r.setId(Integer.toString(id));
		r.setProfileId(Integer.toString(profileId));
		r.setName(name);
		r.setReply(reply);
		r.setReviewId(reviewId);
		r.setStatus(status);
		r.setDate(date);
		r.setRating(rating);
		r.setLike(likes);
		r.setDislike(dislikes);
		r.setComment(comment);
		r.setAccountId(Integer.toString(accountId));

		ObjectMapper mapper = new ObjectMapper();
		String jSONString = null;
		try {
			jSONString = mapper.writeValueAsString(r);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jSONString;

	}

    public int addReview (int profileId, String name, int accountId, String comment, String userEPPN, int reviewId) {

        String json = createProfileReviewFixture(profileId, name, accountId, comment, reviewId);

        int id =
                given()
                        .header("Content-type", APPLICATION_JSON_VALUE)
                        .header("AJP_eppn", userEPPN)
                        .body(json.toString())
                        .expect()
                        .statusCode(200)
                        .when()
                        .post(PROFILE_REVIEW_POST_RESOURCE)
                        .then()
                        .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                        .extract()
                        .path("id");
        
        ServiceLogger.log(logTag, "Added profile review for userid " + profileId + ", returned review id " + id);
        TestReviewUtil.addReviewHelpful(id, memberUserId, memberEPPN, true, PROFILE_REVIEW_HELPFULL_POST_RESOURCE);
        TestReviewUtil.addReviewHelpful(id, nonMemberUserId, nonMemberEPPN, false, PROFILE_REVIEW_HELPFULL_POST_RESOURCE);

        return id;
    }

    public int addReviewReply (int profileId, String name, int accountId, String comment, int reviewId, String userEPPN, int parentReviewId) {

        String json = createProfileReviewFixture(profileId, name, accountId, comment, parentReviewId);

        int id =
                given()
                        .header("Content-type", APPLICATION_JSON_VALUE)
                        .header("AJP_eppn", userEPPN)
                        .body(json.toString())
                        .expect()
                        .statusCode(200)
                        .when()
                        .post(PROFILE_REVIEW_POST_RESOURCE)
                        .then()
                        .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                        .extract()
                        .path("id");
        
        //        TestReviewUtil.addReviewHelpful(id, memberUserId, memberEPPN, true, PROFILE_REVIEW_HELPFULL_POST_RESOURCE);
        //        TestReviewUtil.addReviewHelpful(id, nonMemberUserId, nonMemberEPPN, false, PROFILE_REVIEW_HELPFULL_POST_RESOURCE);

        return id;
    }
}
