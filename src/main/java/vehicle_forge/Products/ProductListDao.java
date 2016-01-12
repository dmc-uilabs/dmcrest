package vehicle_forge;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductListDao {

	private final String logTag = ServiceListDao.class.getName();
	private ResultSet resultSet;
	
	public ArrayList<vehicle_forge.Product> getProductList(){
		ArrayList<vehicle_forge.Product> list = new ArrayList<vehicle_forge.Product>();
		return list;
		}	

}