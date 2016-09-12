package org.dmc.services.products;


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
@RequestMapping(value = "/product_reviews_helpful", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ProductReviewsHelpfulController {
    
    private static final String logTag = ProductReviewsHelpfulController.class.getName();
    
    @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<List<ReviewHelpful>> productReviewsHelpfulGet(@RequestParam(value = "reviewId", required = true) String reviewId,
                                                                        @RequestParam(value = "accountId", required = true) String accountId,
                                                                        @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        // do some magic!
        return new ResponseEntity<List<ReviewHelpful>>(HttpStatus.NOT_IMPLEMENTED);
    }
    
    
    @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE },method = RequestMethod.POST)
    public ResponseEntity<?> productReviewsHelpfulPost(@RequestBody ReviewHelpful serviceReviewHelpful,
                                                       @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN){
        ReviewDao<ProductReview> reviewDao = new ReviewDao<ProductReview>(ReviewType.SERVICE);
        
        try {
            ReviewHelpful retrunedReviewHelpful = reviewDao.createHelpfulReview(serviceReviewHelpful, userEPPN);
            return new ResponseEntity<ReviewHelpful>(retrunedReviewHelpful, HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }
    
    
    
    
    @RequestMapping(value = "/{helpfulID}", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
    public ResponseEntity<ReviewHelpful> productReviewsHelpfulHelpfulIDPatch(@PathVariable("helpfulID") String helpfulID,
                                                                             @RequestBody ReviewHelpful helpful,
                                                                             @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN){
        // do some magic!
        return new ResponseEntity<ReviewHelpful>(HttpStatus.NOT_IMPLEMENTED);
    }
    
    
    
    
    
    
    
}
