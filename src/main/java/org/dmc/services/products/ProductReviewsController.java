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

import org.dmc.services.DMCServiceException;
import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;


import org.dmc.services.reviews.ReviewDao;
import org.dmc.services.reviews.ReviewType;


import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/product_reviews", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ProductReviewsController {

    private ReviewDao<ProductReview> reviewDao = new ReviewDao<ProductReview>(ReviewType.SERVICE);
    private final String logTag = ProductReviewsController.class.getName();

	@RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<List<ProductReview>> productReviewsGet(
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "sort", required = false) String sort) {
		ProductReviewDao productReviewDao = new ProductReviewDao();
		
		String x = new String();
		Integer y = new Integer(0);
		Boolean z = new Boolean(false);
		
		List<ProductReview> productReviews = productReviewDao.getProductReviews(x, x, limit, order, sort, y, z);
		return new ResponseEntity<List<ProductReview>>(productReviews, HttpStatus.OK);
	}

	@RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
    public ResponseEntity<?> productReviewsPost(@RequestBody ProductReview productReview,
                                                @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        
        int statusCode = HttpStatus.OK.value();
        
        try {
            Id id = reviewDao.createReview(productReview, userEPPN);
            return new ResponseEntity<Id>(id, HttpStatus.valueOf(statusCode));
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
	}

	@RequestMapping(value = "/{reviewId}", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
	public ResponseEntity<ProductReview> productReviewsReviewIdPatch(@PathVariable("reviewId") String reviewId,
			@RequestBody ProductReview review) {
		ProductReviewDao productReviewDao = new ProductReviewDao();
		return new ResponseEntity<ProductReview>(productReviewDao.patchProductReviews(review, reviewId), HttpStatus.OK);
	}

}
