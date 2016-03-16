package org.dmc.services.projects;

import java.util.ArrayList;
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
    	
    	//RoleDao.createRole creates a new Role in the database using the provided POST params
    	//it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
    	//this controller in turn returns this new Role instance to the reques using spring's Jackson which
    	//converts the response to JSON
    	
        return project.createProject(projectname, unixname, projectname, Project.PRIVATE, userEPPN);
    }
    
    @RequestMapping(value = "/projects/oldcreate", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    public Id createProject(@RequestBody String payload,
                            @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) throws Exception {
    	
        ServiceLogger.log(logTag, "In createProject: " + payload + " as user " + userEPPN);
    	
    	//RoleDao.createRole creates a new Role in the database using the provided POST params
    	//it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
    	//this controller in turn returns this new Role instance to the reques using spring's Jackson which
    	//converts the response to JSON
    	
    	return project.createProject(payload, userEPPN);
    }

    @RequestMapping(value = "/projects/create", method = RequestMethod.POST, consumes="application/json", produces="application/json")
    public ResponseEntity<Id> createProject(@RequestBody ProjectCreateRequest payload,
                                @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "In createProject: " + payload + " as user " + userEPPN);

        //RoleDao.createRole creates a new Role in the database using the provided POST params
        //it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
        //this controller in turn returns this new Role instance to the reques using spring's Jackson which
        //converts the response to JSON

        return new ResponseEntity<Id>(project.createProject(payload, userEPPN), HttpStatus.OK);
    }

    @RequestMapping(value = "/projects_members", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<ArrayList<ProjectMember>> getProjectMembers(
    		@RequestParam(value="projectId", required=false) String projectIdString,
    		@RequestParam(value="profileId", required=false) String profileIdString,
    		@RequestParam(value="accept", required=false) boolean accept,
    		@RequestParam(value="_limit", required=false) int _limit,
    		@RequestParam(value="_order", required=false) String _order,
    		@RequestParam(value="_sort", required=false) String _sort,
    		@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "In getProjectMembers: as user " + userEPPN);

        if (null != projectIdString) {
        	return new ResponseEntity<ArrayList<ProjectMember>>(project.getMembersForProject(projectIdString, userEPPN), HttpStatus.OK);
        } else {
        	return new ResponseEntity<ArrayList<ProjectMember>>(project.getProjectMembers(userEPPN), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/projects_members", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity<ProjectMember> addProjectMember(@RequestBody ProjectMember payload,
    		@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "In addProjectMember: as user " + userEPPN);

        return new ResponseEntity<ProjectMember>(project.addProjectMember(payload, userEPPN), HttpStatus.OK);
    }

    @RequestMapping(value = "/projects_members/{memberId}", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<ArrayList<ProjectMember>> getProjectsForMember(@PathVariable("memberId") String memberId, 
    																@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "In getProjectsForMember: for member" + memberId + " as user " + userEPPN);

        return new ResponseEntity<ArrayList<ProjectMember>>(project.getProjectsForMember(memberId, userEPPN), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/projects_members/project/{projectId}", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<ArrayList<ProjectMember>> getMembersForProject(@PathVariable("projectId") String projectId, 
    																@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "In getMembersForProject: for project" + projectId + " as user " + userEPPN);

        return new ResponseEntity<ArrayList<ProjectMember>>(project.getMembersForProject(projectId, userEPPN), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/projects/{projectId}/accept/{memberId}", method = RequestMethod.PATCH, produces="application/json")
    public ResponseEntity<ProjectMember> acceptMemberInProject(@PathVariable("projectId") String projectId,
    																@PathVariable("memberId") String memberId, 
    																@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "In acceptMemberInProject: for member" + memberId + " as user " + userEPPN);

        return new ResponseEntity<ProjectMember>(project.acceptMemberInProject(projectId, memberId, userEPPN), HttpStatus.OK);
    }

    @RequestMapping(value = "/projects/{projectId}/reject/{memberId}", method = RequestMethod.DELETE, produces="application/json")
    public ResponseEntity<ProjectMember> rejectMemberInProject(@PathVariable("projectId") String projectId,
    																@PathVariable("memberId") String memberId, 
    																@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "In rejectMemberInProject: for member" + memberId + " as user " + userEPPN);

        return new ResponseEntity<ProjectMember>(project.rejectMemberInProject(projectId, memberId, userEPPN), HttpStatus.OK);
    }

    
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
