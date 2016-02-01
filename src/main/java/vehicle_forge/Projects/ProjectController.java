package vehicle_forge;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.Exception;

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
    @RequestMapping(value = "/projects/{projectID}", method = RequestMethod.GET)
    public Project getProject(@PathVariable("projectID") int projectID) {
    	ServiceLogger.log(logTag, "In getProject");
    	ServiceLogger.log(logTag, "In getProject, projectID: " + projectID);
    	return project.getProject(projectID);
    }
    
    private ProjectListDao projectList = new ProjectListDao(); 
    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public ArrayList<Project> getProjectList(@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
    	ServiceLogger.log(logTag, "In getProjectList as user " + userEPPN);
    	return projectList.getProjectList(userEPPN);
    }
    

	// leaving this as an example of how to work with parameters to URL
	// instead of json, but json is probably preferable
    @RequestMapping(value = "/projects/createWithParameter", method = RequestMethod.POST)
    @ResponseBody
    public Id createProject(
    		@RequestParam("projectname") String projectname,
    		@RequestParam("unixname") String unixname) throws Exception {
    	System.out.println("In createProject: " + projectname + "," + unixname);
    	
    	//RoleDao.createRole creates a new Role in the database using the provided POST params
    	//it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
    	//this controller in turn returns this new Role instance to the reques using spring's Jackson which
    	//converts the response to JSON
    	
    	return project.createProject(projectname, unixname);    	
    }
    
    @RequestMapping(value = "/projects/create", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    public Id createProject(@RequestBody String payload) throws Exception {
    	System.out.println("In createProject: " + payload);
    	
    	//RoleDao.createRole creates a new Role in the database using the provided POST params
    	//it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
    	//this controller in turn returns this new Role instance to the reques using spring's Jackson which
    	//converts the response to JSON
    	
    	return project.createProject(payload);    	
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
