package org.dmc.services;

import static com.jayway.restassured.RestAssured.*;

import org.dmc.services.products.ProductReview;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class ProductIT extends BaseIT {
	
	private String serviceId = "1";
	private String reviewId = "1";
	private String helpfulId = "1";
	private Integer favoriteProductId = 1;

	/*
	 * test case for GET /product/{service_Id}/product_reviews
	 */
	@Test
	public void testProductGet_ProductServiceReviews() {
		given().
		param("reviewId", reviewId).
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(400). // need figure out where the malformed syntax
		when().
		get("/product/" + serviceId + "/product_reviews");
	}
	
	
	
	/*
	 * test case for GET /product_reviews
	 */
	@Test
	public void testProductGet_ProductReview() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/product_reviews");
	}
	
	
	/*
	 * test case for POST /product_reviews
	 */
	@Test
	public void testProductPost_ProductReview() {
		ProductReview obj = new ProductReview();
		ObjectMapper mapper = new ObjectMapper();
		String postedProductReviewJSONString = null;
		try {
			postedProductReviewJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", "user_EPPN").
		body(postedProductReviewJSONString).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		post("/product_reviews");
	}
	
	
	/*
	 * test case for PATCH /product_reviews/{reviewId}
	 */
	@Test
	public void testProductPatch_ProductReviewById() {
		ProductReview obj = new ProductReview();
		ObjectMapper mapper = new ObjectMapper();
		String patchedProductReviewJSONString = null;
		try {
			patchedProductReviewJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", "user_EPPN").
		body(patchedProductReviewJSONString).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		patch("/product_reviews/" + reviewId);
	}
	
	
	
	/*
	 * test case for GET /product_reviews_helpful
	 */
	@Test
	public void testProductGet_ReviewHelpful() {
		given().
		param("reviewId", "1").
		param("accountId", "1").
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/product_reviews_helpful");
	}
	
	
	/*
	 * test case for POST /product_reviews_helpful
	 */
	@Test
	public void testProductPost_ReviewHelpful() {
		ProductReview obj = new ProductReview();
		ObjectMapper mapper = new ObjectMapper();
		String postedHelpfulProductReviewJSONString = null;
		try {
			postedHelpfulProductReviewJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", "user_EPPN").
		body(postedHelpfulProductReviewJSONString).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		post("/product_reviews_helpful");
	}
	
	
	
	/*
	 * test case for PATCH /product_reviews_helpful/{helpfulID}
	 */
	@Test
	public void testProductPatch_ReviewHelpfulbyId() {
		ProductReview obj = new ProductReview();
		ObjectMapper mapper = new ObjectMapper();
		String patchedProductReviewJSONString = null;
		try {
			patchedProductReviewJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", "user_EPPN").
		body(patchedProductReviewJSONString).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		patch("/product_reviews_helpful/" + helpfulId);
	}
	
	
	/*
	 * test case for GET /product_reviews_flagged
	 */
	@Test
	public void testProductGet_Reviewflagged() {
		given().
		param("reviewId", "1").
		param("accountId", "1").
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		get("/product_reviews_flagged");
	}
	
	
	/*
	 * test case for POST /product_reviews_flagged
	 */
	@Test
	public void testProductPost_Reviewflagged() {
		ProductReview obj = new ProductReview();
		ObjectMapper mapper = new ObjectMapper();
		String postedflaggedProductReviewJSONString = null;
		try {
			postedflaggedProductReviewJSONString = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		given().
		header("Content-type", "application/json").
		header("AJP_eppn", "user_EPPN").
		body(postedflaggedProductReviewJSONString).
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		post("/product_reviews_flagged");
	}

	
	/*
	 * test case for DELETE /favorite_products/{favorite_productId}
	 */
	@Test
	public void testProductDelete_FavoriteProductbyId() {
		given().
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(400). // need figure out where the malformed syntax
		when().
		delete("/favorite_products/" + favoriteProductId);
	}
	
	
	/*
	 * test case for POST /favorite_products
	 */
	@Test
	public void testProductPost_FavoriteProduct() {		
		given().
		param("accountId", "1").
		param("serviceId", "1").
		header("AJP_eppn", "user_EPPN").
		expect().
		statusCode(HttpStatus.NOT_IMPLEMENTED.value()).
		when().
		post("/favorite_products");
	}
	
}
