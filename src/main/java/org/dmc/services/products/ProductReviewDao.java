package org.dmc.services.products;

import java.sql.ResultSet;
import java.util.ArrayList;

public class ProductReviewDao {
	
	private final String logTag = ProductReviewDao.class.getName();

	public ProductReview postProductReviews(ProductReview productReview) {
		return productReview;
	}
	
	public ArrayList<ProductReview> getProductReviews(String serviceId, String reviewId,
													  Integer limit, String order, String sort,
													  Integer rating, Boolean status) {
		ArrayList<ProductReview> productReviewList = new ArrayList<ProductReview>();
		return productReviewList;
	}

	public ProductReview patchProductReviews(ProductReview productReview, String reviewId) {
		return productReview;
	}

//	public Id deleteProductReviews(ProductReview productReview) {
//		//check for site admin, user needs to be site admin to remove review
//		return new Integer(0);
//	}

}