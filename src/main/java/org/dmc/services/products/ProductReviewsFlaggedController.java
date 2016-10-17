package org.dmc.services.products;

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
import org.dmc.services.reviews.ReviewFlagged;
import org.dmc.services.reviews.ReviewType;
import org.dmc.services.products.ProductReview;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/product_reviews_flagged", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ProductReviewsFlaggedController {
    
    private static final String logTag = ProductReviewsFlaggedController.class.getName();
    
    @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> productReviewsFlaggedGet(@RequestParam(value = "reviewId", required = true) String reviewId,
                                                                    @RequestParam(value = "accountId", required = true) String accountId,
                                                                    @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN){
        ServiceLogger.log(logTag, "productReviewsFlaggedGet: with user " + userEPPN + " for review id" + reviewId + " with account id " + accountId);
        final ReviewDao<ProductReview> reviewDao = new ReviewDao<ProductReview>(ReviewType.SERVICE);
        
        try {
            final List<ReviewFlagged> retrunedReviewFlagged = reviewDao.getFlaggedReview(reviewId, accountId, userEPPN);
            return new ResponseEntity<List<ReviewFlagged>>(retrunedReviewFlagged, HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }
    
    @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
    public ResponseEntity<?> productReviewsFlaggedPost(@RequestBody ReviewFlagged reviewFlagged,
                                                       @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN){
        ServiceLogger.log(logTag, "productReviewsFlaggedPost: with user " + userEPPN);
        final ReviewDao<ProductReview> reviewDao = new ReviewDao<ProductReview>(ReviewType.SERVICE);
        
        try {
            final ReviewFlagged retrunedReviewFlagged = reviewDao.createFlaggedReview(reviewFlagged, userEPPN);
            return new ResponseEntity<ReviewFlagged>(retrunedReviewFlagged, HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }
}
