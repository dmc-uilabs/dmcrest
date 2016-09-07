package org.dmc.services;

/**
 * Created by 200005921 on 6/9/2016.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dmc.services.company.Company;
import org.dmc.services.services.Service;
import org.dmc.services.products.ProductReview;
import org.dmc.services.utility.CommonUtils;
import org.dmc.services.utility.TestUserUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;

public class ProductReviewIT extends BaseIT {

    private static final String logTag = ProductReviewIT.class.getName();

    private static final String PRODUCT_REVIEW_GET_RESOURCE = "/product/{productID}/product_reviews";
    private static final String PRODUCT_REVIEW_POST_RESOURCE = "/product_reviews";

    private int ownerUserId = -1;
    private String ownerEPPN;

    private int memberUserId = -1;
    private String memberEPPN;
    private String memberDisplayName;

    private int nonMemberUserId = -1;
    private String nonMemberEPPN;
    private String nonMemberDisplayName;

    private int companyId = -1;
    private int serviceId = -1;
    private int reviewId = -1;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static final int DEFAULT_RATING = 3;
    public static final int DEFAULT_LIKES = 0;
    public static final int DEFAULT_DISLIKES = 0;
    public static final String DEFAULT_COMMENT = "This is my awesome product comment";
    public static final String DEFAULT_REPLY_COMMENT = DEFAULT_COMMENT + " reply";

    @Before
    public void setupData () {

        ServiceLogger.log(logTag, "starting setupData");
        String unique = TestUserUtil.generateTime();

        // set up user as owner of company
        ownerEPPN = "userEPPN" + unique;
        String ownerGivenName = "userGivenName" + unique;
        String ownerSurName = "userSurName" + unique;
        String ownerDisplayName = "userDisplayName" + unique;
        String ownerEmail = "userEmail" + unique;

        ownerUserId = CommonUtils.createUser(ownerEPPN, ownerGivenName, ownerSurName, ownerDisplayName, ownerEmail);

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


        Service service = CommonUtils.createNewServiceObjectToPost("junit service test", "junit service test",
                                                                   ownerEPPN, "service type",
                                                                   "service specifications", "1");
        
        Service serviceResponse = CommonUtils.createService(userEPPN, service);
        serviceId = serviceResponse.getId();
        //        int companyId, String name, int accountId, String comment, String userEPPN;
        int parentReviewId = 0;
        reviewId = addReview(serviceResponse.getId(), memberDisplayName, memberUserId, DEFAULT_COMMENT, memberEPPN, parentReviewId);

        
        // Create a user that is not a member of the company
        String unique4 = TestUserUtil.generateTime();
        nonMemberEPPN = "userEPPN" + unique4;
        String nonMember3GivenName = "userGivenName" + unique4;
        String nonMember3SurName= "userSurName" + unique4;
        String nonMember3DisplayName = "userDisplayName" + unique4;
        String nonMember3Email = "userEmail" + unique4;
        nonMemberDisplayName = nonMember3DisplayName;

        nonMemberUserId = CommonUtils.createUser(nonMemberEPPN,nonMember3GivenName, nonMember3SurName, nonMember3DisplayName, nonMember3Email);
    }

    @Test
    public void testGetReviewsMember () {
//        ArrayList<Map<String,?>> jsonAsArrayList  =
//                given()
//                        .header("Content-type", "application/json")
//                        .header("AJP_eppn", memberEPPN)
//                        .parameters("reviewId", "0")
//                        .expect()
//                        .statusCode(200)
//                        .when()
//                        .get(COMPANY_REVIEW_GET_RESOURCE, companyId)
//                        .as(ArrayList.class);

        ProductReview[] productReviews  =
                given()
                        .header("Content-type", "application/json")
                        .header("AJP_eppn", memberEPPN)
                        .parameters("reviewId", "0")
                        .expect()
                        .statusCode(200)
                        .when()
                        .get(PRODUCT_REVIEW_GET_RESOURCE, serviceId)
                        .as(ProductReview[].class);

        assertTrue("Product review list cannot be null", productReviews != null);
        assertTrue("Expected 1 product review, got" + productReviews.length, productReviews.length == 1);

        ProductReview review = productReviews[0];
        assertTrue(review.getId() != null && review.getId().equals(Integer.toString(reviewId)));
        assertTrue(review.getReviewId() != null && review.getReviewId().equals(Integer.toString(0)));
        assertTrue(review.getAccountId() != null && review.getAccountId().equals(Integer.toString(memberUserId)));
        assertTrue(review.getName() != null && review.getName().equals(memberDisplayName));
        assertTrue(review.getReply() != null && review.getReply().equals(Boolean.FALSE));

        // Note: that only the top-level reviews have a stars rating; replies do not have stars.
        assertTrue(review.getRating() != null && review.getRating().intValue() == DEFAULT_RATING);

        // Note: Until the endpoints for helpful reviews are implemented, likes/dislikes = 0
        assertTrue(review.getLike() != null && review.getLike().intValue() == DEFAULT_LIKES);
        assertTrue(review.getDislike() != null && review.getDislike().intValue() == DEFAULT_DISLIKES);

        assertTrue(review.getStatus() != null && review.getStatus().booleanValue() == true);
        assertTrue(review.getComment() != null && review.getComment().equals(DEFAULT_COMMENT));



    }

    @Test
    public void testAddReviewNonMember () {

        int reviewId = 0;
        String json = createProductReviewFixture(serviceId, nonMemberDisplayName, nonMemberUserId, "This is a cool comment", reviewId);

        given()
                .header("Content-type", "application/json")
                .header("AJP_eppn", nonMemberEPPN)
                .body(json.toString())
                .expect()
                .statusCode(HttpStatus.OK.value())
                .when()
                .post(PRODUCT_REVIEW_POST_RESOURCE);
    }

    @Test
    public void testReviewAndReplies() {

        // Add a new review
        int reviewId = addReview(serviceId, memberDisplayName, memberUserId, "My awesome review", memberEPPN, 0);
        ProductReview[] productReviews  =
                given()
                        .header("Content-type", "application/json")
                        .header("AJP_eppn", memberEPPN)
                        .parameters("reviewId", "0")
                        .expect()
                        .statusCode(200)
                        .when()
                        .get(PRODUCT_REVIEW_GET_RESOURCE, serviceId)
                        .as(ProductReview[].class);

        assertTrue("Company review list cannot be null", productReviews != null);
        assertTrue("Expected 2 company reviews, got" + productReviews.length, productReviews.length == 2);

        // Add a review reply
        int replyReviewId = addReview(serviceId, memberDisplayName, memberUserId, DEFAULT_REPLY_COMMENT + " 1", memberEPPN, reviewId);

        // Add a review reply
        int replyReviewId2 = addReview(serviceId, memberDisplayName, memberUserId, DEFAULT_REPLY_COMMENT + " 2", memberEPPN, reviewId);

        ProductReview[] productReviewReplies  =
                given()
                        .header("Content-type", "application/json")
                        .header("AJP_eppn", memberEPPN)
                        .parameters("reviewId", Integer.toString(reviewId))
                        .expect()
                        .statusCode(200)
                        .when()
                        .get(PRODUCT_REVIEW_GET_RESOURCE, serviceId)
                        .as(ProductReview[].class);

        assertTrue("Company review list cannot be null", productReviewReplies != null);
        assertTrue("Expected 2 company review, got" + productReviewReplies.length, productReviewReplies.length == 2);

        for (int i=0; i < productReviewReplies.length; i++) {
            ProductReview review = productReviewReplies[i];

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

    public static String createProductReviewFixture(int serviceId, String name, int accountId, String comment, int reviewId)
    {
//            accountId:1
//            comment:"Cool stuff!"
//            serviceId:"1"
//            date:"1468095204202"
//            dislike:0
//            id:12
//            like:0
//            name:"Thomas Smith"
//            rating:3
//            reply:false
//            reviewId:0
//            status:true

        //String reviewId = Integer.toString(reviewIdCount++);
        boolean status = true;
        BigDecimal date = BigDecimal.valueOf(Calendar.getInstance().getTime().getTime());
        int rating = DEFAULT_RATING;
        int likes = DEFAULT_LIKES;
        int dislikes = DEFAULT_DISLIKES;
        boolean reply = false;

        return createProductReviewFixture(serviceId, name, reply, Integer.toString(reviewId), status, date, rating, likes, dislikes, comment, accountId);
    }

    public static String createProductReviewFixture(int serviceId,
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

        ProductReview r = new ProductReview();
        //r.setId(Integer.toString(resultSet.getInt("kd")));
        r.setProductId(Integer.toString(serviceId));
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
        String companyJSONString = null;
        try {
            companyJSONString = mapper.writeValueAsString(r);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return companyJSONString;

    }

    public int addReview (int serviceId, String name, int accountId, String comment, String userEPPN, int reviewId) {
        String json = createProductReviewFixture(serviceId, name, accountId, comment, reviewId);

        int id =
                given()
                        .header("Content-type", "application/json")
                        .header("AJP_eppn", userEPPN)
                        .body(json.toString())
                        .expect()
                        .statusCode(200)
                        .when()
                        .post(PRODUCT_REVIEW_POST_RESOURCE)
                        .then()
                        .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                        .extract()
                        .path("id");
        
        ServiceLogger.log(logTag, "Added product review for service " + serviceId + ", returned review id " + id);
        return id;
    }

    public int addReviewReply (int serviceId, String name, int accountId, String comment, int reviewId, String userEPPN, int parentReviewId) {

        String json = createProductReviewFixture(serviceId, name, accountId, comment, parentReviewId);

        int id =
                given()
                        .header("Content-type", "application/json")
                        .header("AJP_eppn", userEPPN)
                        .body(json.toString())
                        .expect()
                        .statusCode(200)
                        .when()
                        .post(PRODUCT_REVIEW_POST_RESOURCE)
                        .then()
                        .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                        .extract()
                        .path("id");
        return id;
    }

}
