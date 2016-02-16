package org.dmc.services.users;

import org.dmc.services.Id;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private final String logTag = UserController.class.getName();
	
	private UserDao user = new UserDao(); 
   
   
    
    
    @RequestMapping(value = "/users/create", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    
    public Id createUser(@RequestBody String payload, 
			 @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN,
			 @RequestHeader(value="AJP_givenName", defaultValue="testUserFirstName") String userFirstName,
			 @RequestHeader(value="AJP_sn", defaultValue="testUserSurname") String userSurname,
			 @RequestHeader(value="AJP_displayName", defaultValue="testUserFullName") String userFull,
			 @RequestHeader(value="AJP_mail", defaultValue="testUserEmail") String userEmail)
    {
    	//System.out.println("In createRole role: " + name);
    	
    	
    	//RoleDao.createRole creates a new Role in the database using the provided POST params
    	//it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
    	//this controller in turn returns this new Role instance to the reques using spring's Jackson which
    	//converts the response to JSON
    	
        return user.createUser(payload, userEPPN, userFirstName, userSurname, userFull, userEmail);
    	
    	//Create role and update db through JDBC then return role using new role's id
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET, headers = {"Content-type=text/plain"})
    
    public User user(@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)
    {
    	System.out.println("In user: " + userEPPN);
    	   
        return user.getUser(userEPPN);
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