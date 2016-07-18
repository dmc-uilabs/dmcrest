package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;

import org.dmc.services.utility.TestUserUtil;
import org.junit.Before;
import org.junit.Test;



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
		header("AJP_eppn", knownEPPN).
		expect().
		statusCode(400). // need figure out where the malformed syntax
		when().
		delete("/favorite_products/" + favoriteProductId);
	}
	
}
