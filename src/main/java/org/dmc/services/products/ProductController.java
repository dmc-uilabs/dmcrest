package org.dmc.services.products;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ErrorMessage;
import org.dmc.services.ServiceLogger;

import org.dmc.services.reviews.ReviewDao;
import org.dmc.services.reviews.ReviewType;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/product", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ProductController {
    
    private ReviewDao reviewDao = new ReviewDao(ReviewType.SERVICE);
    private final String logTag = ProductController.class.getName();

    @RequestMapping(value = "/{serviceId}/product_reviews", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> productServiceIdProductReviewsGet(@PathVariable("serviceId") String serviceId,
                                                                                 @RequestParam(value = "reviewId", required = true) String reviewId,
                                                                                 @RequestParam(value = "limit", required = false) Integer limit,
                                                                                 @RequestParam(value = "order", required = false) String order,
                                                                                 @RequestParam(value = "sort", required = false) String sort,
                                                                                 @RequestParam(value = "rating", required = false) Integer rating,
                                                                                 @RequestParam(value = "status", required = false) Boolean status,
                                                                                 @RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
        ServiceLogger.log(logTag, "Query for product " + serviceId + " review id " + reviewId);
        List<ProductReview> reviews = null;
        int statusCode = HttpStatus.OK.value();
        
        int reviewIdInt = 0;
        try {
            reviewIdInt = Integer.parseInt(reviewId);
        } catch (NumberFormatException nfe) {
            
        }
        
        try {
            int productIdInt = Integer.parseInt(serviceId);
            
            if (reviewIdInt == 0) {
                reviews = reviewDao.getReviews(productIdInt, reviewId, limit, order, sort, rating, status, userEPPN, ProductReview.class);
                
            } else if (reviewIdInt > 0) {
                reviews = reviewDao.getReviewReplies(productIdInt, reviewId, limit, order, sort, rating, status, userEPPN, ProductReview.class);
            }
            
            return new ResponseEntity<List<ProductReview>>(reviews, HttpStatus.valueOf(statusCode));
        } catch (NumberFormatException nfe) {
            ServiceLogger.log(logTag, "Invalid companyId: " + serviceId + ": " + nfe.getMessage());
            return new ResponseEntity<String>("Invalid companyId: " + serviceId, HttpStatus.BAD_REQUEST);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }
}
