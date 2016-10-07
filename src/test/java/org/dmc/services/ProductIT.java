package org.dmc.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.products.ProductReview;
import org.dmc.services.products.FavoriteProduct;
import org.dmc.services.utility.TestUserUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static com.jayway.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ProductIT extends BaseIT {
	private static final String LOGTAG = ProductIT.class.getName(); 
	private String productId = "1";
    private int accountId = 1;
	private String reviewId = "1";
	private String helpfulId = "1";
	private Integer favoriteProductId = 1;
	private String knownEPPN;
	
    public static final int DEFAULT_RATING = 3;
    public static final int DEFAULT_LIKES = 0;
    public static final int DEFAULT_DISLIKES = 0;
    public static final String DEFAULT_COMMENT = "This is my awesome comment";
    public static final String DEFAULT_REPLY_COMMENT = "This is my awesome comment";

	@Before
	public void before() {
		if (knownEPPN == null) {
			knownEPPN = TestUserUtil.createNewUser();
            try {
                accountId = UserDao.getUserID(knownEPPN);
            } catch(SQLException e) {
                
            }
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
		get("/product/" + productId + "/product_reviews");
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
	    ServiceLogger.log(LOGTAG, "starting testProductPost_ProductReview");
        ProductReview obj = createProductReviewFixture(Integer.parseInt(productId), "product review name",
                                                       accountId, "product comment", Integer.parseInt("-1"));
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
        ServiceLogger.log(LOGTAG, "In favoriteProductsFavoriteProductIdDelete: for favoriteProductId " + favoriteProductId + " as user " + knownEPPN);
        
        
        FavoriteProduct favoriteProduct =
        given().
        header("Content-type", APPLICATION_JSON_VALUE).
        header("AJP_eppn", knownEPPN).
        queryParam("accountId", accountId).
        queryParam("serviceId", productId).
        expect().
        statusCode(HttpStatus.CREATED.value()). // need figure out where the malformed syntax
        when().
        post("/favorite_products").as(FavoriteProduct.class);
        
        
        FavoriteProduct[] favoriteProductByAccount =
        given().
        header("Content-type", APPLICATION_JSON_VALUE).
        header("AJP_eppn", knownEPPN).
        queryParam("accountId", accountId).
        expect().
        statusCode(HttpStatus.OK.value()). // need figure out where the malformed syntax
        when().
        get("/favorite_products").as(FavoriteProduct[].class);
        
        assertTrue("User's created favorite product is not retrieved", favoriteProductByAccount[0].equals(favoriteProduct));
        
        
        FavoriteProduct[] favoriteProductByService =
        given().
        header("Content-type", APPLICATION_JSON_VALUE).
        header("AJP_eppn", knownEPPN).
        queryParam("serviceId", productId).
        expect().
        statusCode(HttpStatus.OK.value()). // need figure out where the malformed syntax
        when().
        get("/favorite_products").as(FavoriteProduct[].class);
        
        assertTrue("No favorite products returned when seaching for service " + productId, favoriteProductByAccount.length > 0);
        
		given().
        header("Content-type", "application/json").
		header("AJP_eppn", knownEPPN).
		expect().
		statusCode(HttpStatus.OK.value()). // need figure out where the malformed syntax
		when().
		delete("/favorite_products/" + favoriteProduct.getId());

        ServiceLogger.log(LOGTAG, "In favoriteProductsFavoriteProductIdDelete: for favoriteProductId " + favoriteProductId + " as user " + knownEPPN);

    }
	
    public static ProductReview createProductReviewFixture(int productId, String name, int accountId, String comment, int reviewId)
    {
        //String reviewId = Integer.toString(reviewIdCount++);
        boolean status = true;
        BigDecimal date = BigDecimal.valueOf(Calendar.getInstance().getTime().getTime());
        int rating = DEFAULT_RATING;
        int likes = DEFAULT_LIKES;
        int dislikes = DEFAULT_DISLIKES;
        boolean reply = false;
        
        return createProductReviewFixture(productId, name, reply, Integer.toString(reviewId), status, date, rating, likes, dislikes, comment, accountId);
    }
    
    private static ProductReview createProductReviewFixture(int productId, String name, boolean reply,
                                                     String reviewId, boolean status, BigDecimal date,
                                                     int rating, int likes, int dislikes, String comment,
                                                     int accountId) {
        ProductReview r = new ProductReview();
        //r.setId(Integer.toString(resultSet.getInt("kd")));
        r.setProductId(Integer.toString(productId));
        r.setName(name);
        r.setReply(reply);
        r.setReviewId(reviewId);
        r.setStatus(status);
        r.setDate(date);
        r.setRating(rating);
        r.setLike(likes);
        r.setDislike(dislikes);
        r.setComment(comment);
        r.setAccountId(Integer.toString(accountId));
        return r;
    }
}
