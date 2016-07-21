package org.dmc.services.products;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/product_reviews", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ProductReviewsController {

	@RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<List<ProductReview>> productReviewsGet(
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "sort", required = false) String sort) {
		ProductReviewDao productReviewDao = new ProductReviewDao();
		return new ResponseEntity<List<ProductReview>>(productReviewDao.getProductReviews(), HttpStatus.OK);
	}

	@RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<ProductReview> productReviewsPost(@RequestBody ProductReview review) {
		ProductReviewDao productReviewDao = new ProductReviewDao();
		return new ResponseEntity<ProductReview>(productReviewDao.postProductReviews(review), HttpStatus.OK);
	}

	@RequestMapping(value = "/{reviewId}", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
	public ResponseEntity<ProductReview> productReviewsReviewIdPatch(@PathVariable("reviewId") String reviewId,
			@RequestBody ProductReview review) {
		ProductReviewDao productReviewDao = new ProductReviewDao();
		return new ResponseEntity<ProductReview>(productReviewDao.patchProductReviews(review), HttpStatus.OK);
	}

}
