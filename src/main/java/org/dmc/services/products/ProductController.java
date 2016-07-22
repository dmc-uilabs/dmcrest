package org.dmc.services.products;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/product", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ProductController {

  @RequestMapping(value = "/{serviceId}/product_reviews", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
  public ResponseEntity<List<ProductReview>> productServiceIdProductReviewsGet(@PathVariable("serviceId") String serviceId,
																			   @RequestParam(value = "reviewId", required = true) String reviewId,
																			   @RequestParam(value = "limit", required = false) Integer limit,
																			   @RequestParam(value = "order", required = false) String order,
																			   @RequestParam(value = "sort", required = false) String sort,
																			   @RequestParam(value = "rating", required = false) Integer rating,
																			   @RequestParam(value = "status", required = false) Boolean status){
	  ProductReviewDao productReviewDao = new ProductReviewDao();
	  List<ProductReview> productReviews = productReviewDao.getProductReviews(serviceId, reviewId, limit, order, sort, rating, status);
      return new ResponseEntity<List<ProductReview>>(productReviews, HttpStatus.OK);
  }
}
