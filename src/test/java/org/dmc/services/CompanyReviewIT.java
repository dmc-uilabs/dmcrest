package org.dmc.services;

/**
 * Created by 200005921 on 6/9/2016.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dmc.services.company.Company;
import org.dmc.services.company.CompanyReview;
import org.dmc.services.utility.CommonUtils;
import org.dmc.services.utility.TestUserUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertTrue;

public class CompanyReviewIT extends BaseIT {

    private static final String logTag = CompanyReviewIT.class.getName();

    private static final String COMPANY_REVIEW_GET_RESOURCE = "/companies/{companyID}/company_reviews";
    private static final String COMPANY_REVIEW_POST_RESOURCE = "/company_reviews";

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

        // int companyId, String name, int accountId, String comment, String userEPPN
        int parentReviewId = 0;
        reviewId = addReview(companyId, memberDisplayName, memberUserId, "My comment", memberEPPN, parentReviewId);

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
    public void testGetReviewsNonMember () {
        given()
                .header("Content-type", "application/json")
                .header("AJP_eppn", nonMemberEPPN)
                .parameters("reviewId", "0")
                .expect()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .when()
                .get(COMPANY_REVIEW_GET_RESOURCE, companyId);
    }

    @Test
    public void testGetReviewsMember () {
        ArrayList<CompanyReview> companyReviewList =
                given()
                        .header("Content-type", "application/json")
                        .header("AJP_eppn", memberEPPN)
                        .parameters("reviewId", "0")
                        .expect()
                        .statusCode(200)
                        .when()
                        .get(COMPANY_REVIEW_GET_RESOURCE, companyId)
                        .as(ArrayList.class);

        assertTrue("Company review list cannot be null", companyReviewList != null);
        assertTrue("Expected 1 company review, got" + companyReviewList.size(), companyReviewList.size() == 1);
    }

    @Test
    public void testAddReviewNonMember () {

        int reviewId = 0;
        String json = createCompanyReviewFixture(companyId, nonMemberDisplayName, nonMemberUserId, "This is a cool comment", reviewId);

        given()
                .header("Content-type", "application/json")
                .header("AJP_eppn", nonMemberEPPN)
                .body(json.toString())
                .expect()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .when()
                .post(COMPANY_REVIEW_POST_RESOURCE);
    }

    public static String createCompanyReviewFixture(int companyId, String name, int accountId, String comment, int reviewId)
    {
//            accountId:1
//            comment:"Cool stuff!"
//            companyId:"1"
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
        int rating = 3;
        int likes = 0;
        int dislikes = 0;
        boolean reply = false;

        return createCompanyReviewFixture(companyId, name, reply, Integer.toString(reviewId), status, date, rating, likes, dislikes, comment, accountId);
    }

    public static String createCompanyReviewFixture(int companyId,
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

        CompanyReview r = new CompanyReview();
        //r.setId(Integer.toString(resultSet.getInt("kd")));
        r.setCompanyId(Integer.toString(companyId));
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

    public int addReview (int companyId, String name, int accountId, String comment, String userEPPN, int reviewId) {

        String json = createCompanyReviewFixture(companyId, name, accountId, comment, reviewId);

        int id =
                given()
                        .header("Content-type", "application/json")
                        .header("AJP_eppn", userEPPN)
                        .body(json.toString())
                        .expect()
                        .statusCode(200)
                        .when()
                        .post(COMPANY_REVIEW_POST_RESOURCE)
                        .then()
                        .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                        .extract()
                        .path("id");
        return id;
    }

    public int addReviewReply (int companyId, String name, int accountId, String comment, int reviewId, String userEPPN, int parentReviewId) {

        String json = createCompanyReviewFixture(companyId, name, accountId, comment, parentReviewId);

        int id =
                given()
                        .header("Content-type", "application/json")
                        .header("AJP_eppn", userEPPN)
                        .body(json.toString())
                        .expect()
                        .statusCode(200)
                        .when()
                        .post(COMPANY_REVIEW_POST_RESOURCE)
                        .then()
                        .body(matchesJsonSchemaInClasspath("Schemas/idSchema.json"))
                        .extract()
                        .path("id");
        return id;
    }

    // TODO
    @Test
    public void testReviewAndReplies() {

        // Add a new review
        int reviewId = addReview(companyId, memberDisplayName, memberUserId, "My awesome review", memberEPPN, 0);
        ArrayList<CompanyReview> companyReviewList =
                given()
                        .header("Content-type", "application/json")
                        .header("AJP_eppn", memberEPPN)
                        .parameters("reviewId", "0")
                        .expect()
                        .statusCode(200)
                        .when()
                        .get(COMPANY_REVIEW_GET_RESOURCE, companyId)
                        .as(ArrayList.class);

        assertTrue("Company review list cannot be null", companyReviewList != null);
        assertTrue("Expected 2 company reviews, got" + companyReviewList.size(), companyReviewList.size() == 2);

        // Add a review reply
        int replyReviewId = addReview(companyId, memberDisplayName, memberUserId, "My awesome review", memberEPPN, reviewId);

        // Add a review reply
        int replyReviewId2 = addReview(companyId, memberDisplayName, memberUserId, "My awesome second review", memberEPPN, reviewId);

        ArrayList<CompanyReview> companyReplyReviewList =
                given()
                        .header("Content-type", "application/json")
                        .header("AJP_eppn", memberEPPN)
                        .parameters("reviewId", Integer.toString(reviewId))
                        .expect()
                        .statusCode(200)
                        .when()
                        .get(COMPANY_REVIEW_GET_RESOURCE, companyId)
                        .as(ArrayList.class);

        assertTrue("Company review list cannot be null", companyReplyReviewList != null);
        assertTrue("Expected 2 company review, got" + companyReplyReviewList.size(), companyReplyReviewList.size() == 2);

    }
}
