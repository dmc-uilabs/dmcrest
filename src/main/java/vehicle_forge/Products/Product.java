package vehicle_forge;

public class Product {

	private Product(ProductBuilder builder){
	}
	
	public static class ProductBuilder {
    	
    	public Product build() {
    		return new Product(this);
    	}
		
    }

}