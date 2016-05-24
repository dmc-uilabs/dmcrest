package org.dmc.services.products;

import java.sql.ResultSet;
import java.util.ArrayList;

public class ProductListDao {

	private final String logTag = ProductListDao.class.getName();
	private ResultSet resultSet;
	
	public ArrayList<Product> getProductList(){
		ArrayList<Product> list = new ArrayList<Product>();
		return list;
		}	

}