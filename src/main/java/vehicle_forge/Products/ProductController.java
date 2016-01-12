package vehicle_forge;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

	private final String logTag = ProductController.class.getName();
	
    
    private ProductListDao ProductList = new ProductListDao(); 
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ArrayList<Product> getProductList() {
    	ServiceLogger.log(logTag, "In getProductList");
    	//ServiceLogger.log(logTag, "In getProductList, authorization: " + authorization);
    	return ProductList.getProductList();
    }
	
	/*
	@RequestMapping(value = "/projects/{pid}/Products", method = RequestMethod.GET)
	public ArrayList<Product> getProductList(@PathVariable("pid") int pid){
		ServiceLogger.log(logTag, "In getProductList with projectId: "+ pid);
    	//ServiceLogger.log(logTag, "In getProductList, authorization: " + authorization);
    	return ProductList.getProductList(pid);
	}
    
    
    @RequestMapping(value = "/Products/create", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    
    public Id createProduct(@RequestBody String payload) {
    	//System.out.println("In createRole role: " + name);
    	
    	
    	//RoleDao.createRole creates a new Role in the database using the provided POST params
    	//it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
    	//this controller in turn returns this new Role instance to the reques using spring's Jackson which
    	//converts the response to JSON
    	
    	return Product.createProduct(payload);
    	
    	//Create role and update db through JDBC then return role using new role's id
    	
    	
    	
    }

    
    /*
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateRole(@RequestParam(value="id", defaultValue="-1") int id) {
    	System.out.println("In createRole role: " + id);
    	
    	
    	//RoleDao.createRole updates the Role in the database identified by id using the provided POST params
    	//it creates an instance of this role i.e new Role(param.id, param.name, param.title.....)
    	//this controller in turn returns this updated Role instance to the reques using spring's Jackson which
    	//converts the response to JSON
    	
    	return RoleDao.updateRole(params);
    }
    */
}