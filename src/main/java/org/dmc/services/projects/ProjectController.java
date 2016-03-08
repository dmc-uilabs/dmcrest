package org.dmc.services.projects;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.Exception;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;

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
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestController
public class ProjectController {

	private final String logTag = ProjectController.class.getName();
	
	private ProjectDao project = new ProjectDao(); 
    @RequestMapping(value = "/projects/{projectID}", produces = { "application/json" }, method = RequestMethod.GET)
    public Project getProject(@PathVariable("projectID") int projectID,
    						  @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {

    	ServiceLogger.log(logTag, "In getProject, projectID: " + projectID + " as user " + userEPPN);
    	return project.getProject(projectID, userEPPN);
    }
    
    private ProjectListDao projectList = new ProjectListDao(); 
    @RequestMapping(value = "/projects", produces = { "application/json" }, method = RequestMethod.GET)
    public ArrayList<Project> getProjectList(@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
    	ServiceLogger.log(logTag, "In getProjectList as user " + userEPPN);
    	return projectList.getProjectList(userEPPN);
    }
    

	// leaving this as an example of how to work with parameters to URL
	// instead of json, but json is probably preferable
    @RequestMapping(value = "/projects/createWithParameter", produces = { "application/json" }, method = RequestMethod.POST)
    @ResponseBody
    public Id createProject(
    		@RequestParam("projectname") String projectname,
    		@RequestParam("unixname") String unixname,
            @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws Exception {
    	
        ServiceLogger.log(logTag, "In createProject: " + projectname + ", " + unixname + " as user " + userEPPN);
    	
    	//RoleDao.createRole creates a new Role in the database using the provided POST params
    	//it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
    	//this controller in turn returns this new Role instance to the reques using spring's Jackson which
    	//converts the response to JSON
    	
    	return project.createProject(projectname, unixname, userEPPN);
    }
    
    @RequestMapping(value = "/projects/create", method = RequestMethod.POST, produces = { "application/json" }, headers = {"Content-type=application/json"})
    public Id createProject(@RequestBody String payload,
                            @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws Exception {
    	
        ServiceLogger.log(logTag, "In createProject: " + payload + " as user " + userEPPN);
    	
    	//RoleDao.createRole creates a new Role in the database using the provided POST params
    	//it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
    	//this controller in turn returns this new Role instance to the reques using spring's Jackson which
    	//converts the response to JSON
    	
    	return project.createProject(payload, userEPPN);
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
    
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception ex) {
//        // prepare responseEntity
//		return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(Exception.class)
    public ErrorMessage handleException(Exception ex) {
        // prepare responseEntity
    	ErrorMessage result = new ErrorMessage.ErrorMessageBuilder(ex.getMessage())
		.build();
    	System.out.print(result);
    	return result;
    }
}
