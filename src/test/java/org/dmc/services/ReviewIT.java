package org.dmc.services;

import org.dmc.services.reviews.ReviewHelpful;

import static com.jayway.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ReviewIT extends BaseIT {
    
    private static final String logTag = ReviewIT.class.getName();

    static public ReviewHelpful addReviewHelpful(int reviewId, int userId, String userEPPN, boolean helpful, String REVIEW_HELPFULL_POST_RESOURCE) {
        ReviewHelpful reviewHelpful = new ReviewHelpful();
        reviewHelpful.setReviewId(Integer.toString(reviewId)); // id of review
        reviewHelpful.setAccountId(Integer.toString(userId)); // id of user
        reviewHelpful.setHelpfull(helpful);
        
        ServiceLogger.log(logTag, "Added helpful review for reviewId " + reviewId + " from user " + userEPPN + " (id:"+userId+") with helpful = "+helpful+" to endpoint "+REVIEW_HELPFULL_POST_RESOURCE);
        
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
}
