package org.dmc.services.projects;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.Exception;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.discussions.Discussion;
import org.dmc.services.discussions.DiscussionListDao;
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
	private DiscussionListDao discussionListDao = new DiscussionListDao();
	
	private ProjectDao project = new ProjectDao(); 
    @RequestMapping(value = "/projects/{projectID}", method = RequestMethod.GET)
    public Project getProject(@PathVariable("projectID") int projectID,
    						  @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {

    	ServiceLogger.log(logTag, "In getProject, projectID: " + projectID + " as user " + userEPPN);
    	return project.getProject(projectID, userEPPN);
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
    		@RequestParam("unixname") String unixname,
            @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws Exception {
    	
        ServiceLogger.log(logTag, "In createProject: " + projectname + ", " + unixname + " as user " + userEPPN);

        long dueDate = 0;
        return project.createProject(projectname, unixname, projectname, Project.PRIVATE, userEPPN, dueDate);
    }
    
    @RequestMapping(value = "/projects/oldcreate", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    public Id createProject(@RequestBody String payload,
                            @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws Exception {
    	
        ServiceLogger.log(logTag, "In createProject: " + payload + " as user " + userEPPN);
    	return project.createProject(payload, userEPPN);
    }

    @RequestMapping(value = "/projects/create", method = RequestMethod.POST, consumes="application/json", produces="application/json")
    public ResponseEntity<Id> createProject(@RequestBody ProjectCreateRequest payload,
                                @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "**********In createProject: " + payload + " as user " + userEPPN);
        return new ResponseEntity<Id>(project.createProject(payload, userEPPN), HttpStatus.OK);
    }

    @RequestMapping(value = "/projects/{projectID}/projects_tags", method = RequestMethod.GET)
    public ArrayList<ProjectTag> getProjectTagList(@PathVariable("projectID") int projectID,
    			  @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
    	ServiceLogger.log(logTag, "In getProjectTagList as user " + userEPPN);
        
    	return project.getProjectTagList(projectID, userEPPN);
    }
    
    /**	
    *Return Project Discussions
    **/
   @RequestMapping(value = "/projects/{projectID}/all-discussions", method = RequestMethod.GET, produces = { "application/json"})
   public ResponseEntity getDiscussions(@PathVariable("projectID") int projectID,
		   								@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN,
		   								@RequestParam(value="_limit", defaultValue="100") int limit,
		   								@RequestParam(value="_order", defaultValue="DESC") String order,
		   								@RequestParam(value="_sort", defaultValue="time_posted") String sort) {
	   
   	ServiceLogger.log(logTag, "getDiscussions, userEPPN: " + userEPPN);
   	int statusCode = HttpStatus.OK.value();
   	ArrayList<Discussion> discussions = null;
   	
   	try {
           discussions = discussionListDao.getDiscussionList(userEPPN, projectID, limit, order, sort);
           return new ResponseEntity<ArrayList<Discussion>>(discussions, HttpStatus.valueOf(statusCode));
   	} catch (HTTPException e) {
   		ServiceLogger.log(logTag, e.getMessage());
   		statusCode = e.getStatusCode();
   		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
   		return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
   	}
   }
    
    @ExceptionHandler(Exception.class)
    public ErrorMessage handleException(Exception ex) {
        // prepare responseEntity
    	ErrorMessage result = new ErrorMessage.ErrorMessageBuilder(ex.getMessage())
		.build();
    	System.out.print(result);
    	return result;
    }
}
