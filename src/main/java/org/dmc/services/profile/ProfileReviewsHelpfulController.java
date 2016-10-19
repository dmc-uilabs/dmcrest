package org.dmc.services.profile;


import org.dmc.services.reviews.ReviewHelpful;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.reviews.ReviewDao;
import org.dmc.services.reviews.ReviewType;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/profile_reviews_helpful", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ProfileReviewsHelpfulController {
    
    private static final String logTag = ProfileReviewsHelpfulController.class.getName();
    
    @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> profileReviewsHelpfulGet(@RequestParam(value = "reviewId", required = true) String reviewId,
                                                      @RequestParam(value = "accountId", required = true) String accountId,
                                                      @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        ServiceLogger.log(logTag, "profileReviewsHelpfulGet: with user " + userEPPN + " for review id" + reviewId + " with account id " + accountId);
        final ReviewDao<ProfileReview> reviewDao = new ReviewDao<ProfileReview>(ReviewType.PROFILE);
        
        try {
            final List<ReviewHelpful> retrunedReviewHelpful = reviewDao.getHelpfulReview(reviewId, accountId, userEPPN);
            return new ResponseEntity<List<ReviewHelpful>>(retrunedReviewHelpful, HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }
    
    
    @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE },method = RequestMethod.POST)
    public ResponseEntity<?> profileReviewsHelpfulPost(@RequestBody ReviewHelpful serviceReviewHelpful,
                                                       @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN){
        ServiceLogger.log(logTag, "profileReviewsHelpfulPost: with user " + userEPPN +
                                  " for reviewId " + serviceReviewHelpful.getReviewId() +
                                  " with helpful = " + serviceReviewHelpful.getHelpfull());

        final ReviewDao<ProfileReview> reviewDao = new ReviewDao<ProfileReview>(ReviewType.PROFILE);
        
        try {
            final ReviewHelpful retrunedReviewHelpful = reviewDao.createHelpfulReview(serviceReviewHelpful, userEPPN);
            return new ResponseEntity<ReviewHelpful>(retrunedReviewHelpful, HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }
    
    
    @RequestMapping(value = "/{helpfulID}", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
    public ResponseEntity<?> profileReviewsHelpfulHelpfulIDPatch(@PathVariable("helpfulID") String helpfulID,
                                                                 @RequestBody ReviewHelpful helpful,
                                                                 @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN){
        ServiceLogger.log(logTag, "profileReviewsHelpfulHelpfulIDPatch: with user " + userEPPN + " for helpfulID: " + helpfulID);
        final ReviewDao<ProfileReview> reviewDao = new ReviewDao<ProfileReview>(ReviewType.PROFILE);
        
        try {
            final ReviewHelpful retrunedReviewHelpful = reviewDao.patchHelpfulReview(helpfulID, helpful, userEPPN);
            return new ResponseEntity<ReviewHelpful>(retrunedReviewHelpful, HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }
}
