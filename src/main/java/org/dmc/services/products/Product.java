package org.dmc.services.products;

public class Product {

	private Product(ProductBuilder builder){
	}
	
	public static class ProductBuilder {
    	
    	public Product build() {
    		return new Product(this);
    	}
		
    }

}