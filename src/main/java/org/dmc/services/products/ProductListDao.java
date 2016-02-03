package org.dmc.services.products;

import org.dmc.services.services.ServiceListDao;

import java.sql.ResultSet;
import java.util.ArrayList;

public class ProductListDao {

	private final String logTag = ServiceListDao.class.getName();
	private ResultSet resultSet;
	
	public ArrayList<Product> getProductList(){
		ArrayList<Product> list = new ArrayList<Product>();
		return list;
		}	

}