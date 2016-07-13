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
	
}
