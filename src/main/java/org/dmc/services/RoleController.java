package org.dmc.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RoleController {

	private final String logTag = RoleController.class.getName();
	
	private RoleDao roleDao = new RoleDao(); 
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public Role getRole(@PathVariable("id") int id) {
    	ServiceLogger.log(logTag, "In getRole");
    	ServiceLogger.log(logTag, "In getRole, id: " + id);
    	return roleDao.getRole(id);
    }
    
    /*
    @RequestMapping(value = "/role/create", method = RequestMethod.POST)
    @ResponseBody
    public String createRole(@RequestParam(value="name", defaultValue="anonymous") String name) {
    	System.out.println("In createRole role: " + name);
    	
    	
    	//RoleDao.createRole creates a new Role in the database using the provided POST params
    	//it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
    	//this controller in turn returns this new Role instance to the reques using spring's Jackson which
    	//converts the response to JSON
    	
    	return RoleDao.createRole(params);
    	
    	//Create role and update db through JDBC then return role using new role's id
    	
    	if (true) {
    		return "Could not create role";
    		//return new ResponseEntity<Role>(HttpStatus.BAD_REQUEST);
    	}
    	//return new ResponseEntity<String>(json,HttpStatus.OK);
    	return "Role created successfully";
    	
    }
*/
    
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