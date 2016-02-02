package org.dmc.services.components;

import org.dmc.services.ServiceLogger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ComponentController {

	private final String logTag = ComponentController.class.getName();
	
	private ComponentDao component = new ComponentDao(); 
    @RequestMapping(value = "/components/{componentID}", method = RequestMethod.GET)
    public Component getComponent(@PathVariable("componentID") int componentID) {
    	ServiceLogger.log(logTag, "In getComponent");
    	ServiceLogger.log(logTag, "In getComponent, componentID: " + componentID);
    	return component.getComponent(componentID);
    }
    
	private ComponentListDao componentList = new ComponentListDao(); 
    @RequestMapping(value = "/components", method = RequestMethod.GET)
    public ArrayList<Component> getComponentList() {
    	ServiceLogger.log(logTag, "In getcomponentList");/*
    	ServiceLogger.log(logTag, "In getcomponentList, authorization: " + authorization);*/
    	return componentList.getComponentList();
    	
    }
   
    @RequestMapping(value = "/projects/{projectId}/components", method = RequestMethod.GET)
    public ArrayList<Component> getComponentList(@PathVariable("projectId") int projectId){
    	ServiceLogger.log(logTag, "Getting components of project " + projectId);
    	return componentList.getComponentList(projectId);
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