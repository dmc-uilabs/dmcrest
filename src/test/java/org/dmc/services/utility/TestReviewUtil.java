package org.dmc.services.utility;

import org.dmc.services.reviews.ReviewFlagged;
import org.dmc.services.reviews.ReviewHelpful;

import static com.jayway.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class TestReviewUtil {
    
    private static final String logTag = TestReviewUtil.class.getName();

    static public ReviewHelpful addReviewHelpful(int reviewId, int userId, String userEPPN, boolean helpful, String REVIEW_HELPFULL_POST_RESOURCE) {
        ReviewHelpful reviewHelpful = new ReviewHelpful();
        reviewHelpful.setReviewId(Integer.toString(reviewId)); // id of review
        reviewHelpful.setAccountId(Integer.toString(userId)); // id of user
        reviewHelpful.setHelpful(helpful);
        
//        ServiceLogger.log(logTag, "Added helpful review for reviewId " + reviewId + " from user " + userEPPN + " (id:"+userId+") with helpful = "+helpful+" to endpoint "+REVIEW_HELPFULL_POST_RESOURCE);
        
        ReviewHelpful returnedReviewHelpful =
        given().
            header("Content-type", APPLICATION_JSON_VALUE).
            header("AJP_eppn", userEPPN).body(reviewHelpful).
        expect().
            statusCode(200).
        when().
            post(REVIEW_HELPFULL_POST_RESOURCE).
        then().
            extract().as(ReviewHelpful.class);
        
        return returnedReviewHelpful;
    }
    
    
    static public ReviewFlagged addFlaggedReview(int review_id, int user_id, String reason, String comment, String userEPPN, String REVIEW_FLAGGED_POST_RESOURCE) {
        ReviewFlagged reviewFlagged = new ReviewFlagged();
        reviewFlagged.setReviewId(Integer.toString(review_id));
        reviewFlagged.setAccountId(Integer.toString(user_id));
        //ToDo: add String reason, String comment
        
        ReviewFlagged retrievedReviewFlagged =
        given().
        header("Content-type", APPLICATION_JSON_VALUE).
        header("AJP_eppn", userEPPN).
        body(reviewFlagged).
        expect().
        statusCode(200).
        when().
        post(REVIEW_FLAGGED_POST_RESOURCE).
        as(ReviewFlagged.class);
        return retrievedReviewFlagged;
    }
    
    static public ReviewFlagged[] getFlaggedReview(int review_id, int user_id, String userEPPN, String REVIEW_FLAGGED_POST_RESOURCE) {
        ReviewFlagged[] retrievedReviewFlagged =
        given().
        header("Content-type", APPLICATION_JSON_VALUE).
        header("AJP_eppn", userEPPN).
        param("reviewId", review_id).
        param("accountId", user_id).
        expect().
        statusCode(200).
        when().
        get(REVIEW_FLAGGED_POST_RESOURCE).
        as(ReviewFlagged[].class);
        return retrievedReviewFlagged;
    }
}
