package org.dmc.services;

import static com.jayway.restassured.RestAssured.given;

import org.junit.Test;
import org.springframework.http.HttpStatus;

public class faqIT extends BaseIT {
	private String articleId = "1";
	private String categoryId = "1";
	private String subCategoryId = "1";

	/**
	 * test case for /faq_articles
	 */
	@Test
	public void testfaq_Articles(){
		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when().get("/faq_articles");
	}

	/**
	 * test case for /faq_articles/{id}
	 */
	@Test
	public void testfaqGet_ArticleId(){
		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when().get("/faq_articles/" + articleId);
	}
	
	
	/**
	 * test case for /faq_categories
	 */
	@Test
	public void testfaq_Categories(){
		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when().get("/faq_categories");
	}

	/**
	 * test case for /faq_categories/{id}
	 */
	@Test
	public void testfaqGet_CategoryId(){
		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when().get("/faq_categories/" + categoryId);
	}
	
	
	
	
	/**
	 * test case for /faq_subcategories
	 */
	@Test
	public void testfaq_SubCategories(){
		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when().get("/faq_subcategories");
	}

	/**
	 * test case for /faq_subcategories/{id}
	 */
	@Test
	public void testfaqGet_SubCategoryId(){
		given().header("AJP_eppn", userEPPN).expect().statusCode(HttpStatus.NOT_IMPLEMENTED.value()).when().get("/faq_subcategories/" + subCategoryId);
	}
	
	
	

}
