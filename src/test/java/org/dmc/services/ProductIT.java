package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.dmc.services.products.ProductReview;
import org.dmc.services.utility.TestUserUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductIT extends BaseIT {
	
	private String serviceId = "1";
	private String reviewId = "1";
	private String helpfulId = "1";
	private Integer favoriteProductId = 1;
	private String knownEPPN;
	
	@Before
	public void before() {
		if (knownEPPN == null) {
			knownEPPN = TestUserUtil.createNewUser();
		}
	}

	/*
	 * test case for GET /product/{service_Id}/product_reviews
	 */
	@Test
	public void testProductGet_ProductServiceReviews() {
		given().
		param("reviewId", reviewId).
		header("AJP_eppn", knownEPPN).
		header("Content-type", APPLICATION_JSON_VALUE).
		expect().
		statusCode(HttpStatus.OK.value()). // need figure out where the malformed syntax
		when().
		get("/product/" + serviceId + "/product_reviews");
	}

	
	
	/*
	 * test case for GET /product_reviews
	 */
	@Test
	public void testProductGet_ProductReview() {
		given().
		header("AJP_eppn", knownEPPN).
		header("Content-type", APPLICATION_JSON_VALUE).
		expect().
		statusCode(HttpStatus.OK.value()).
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
		header("Content-type", APPLICATION_JSON_VALUE).
		header("AJP_eppn", knownEPPN).
		body(postedProductReviewJSONString).
		expect().
		statusCode(HttpStatus.OK.value()).
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
		header("AJP_eppn", knownEPPN).
		body(patchedProductReviewJSONString).
		expect().
		statusCode(HttpStatus.OK.value()).
		when().
		patch("/product_reviews/" + reviewId);
	}

	
	/*
	 * test case for DELETE /favorite_products/{favorite_productId}
	 */
	@Test
	public void testProductDelete_FavoriteProductbyId() {
		given().
		header("AJP_eppn", knownEPPN).
		expect().
		statusCode(400). // need figure out where the malformed syntax
		when().
		delete("/favorite_products/" + favoriteProductId);
	}
	
}
