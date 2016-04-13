package org.dmc.services.projects;

import java.util.ArrayList;
import java.util.List;
import java.lang.Exception;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.discussions.Discussion;
import org.dmc.services.discussions.DiscussionListDao;
import org.dmc.services.discussions.IndividualDiscussion;
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
        ServiceLogger.log(logTag, "In createProject: " + payload + " as user " + userEPPN);

        //RoleDao.createRole creates a new Role in the database using the provided POST params
        //it instantiates a new role with these params like i.e new Role(param.name, param.title.....)
        //this controller in turn returns this new Role instance to the reques using spring's Jackson which
        //converts the response to JSON

        return new ResponseEntity<Id>(project.createProject(payload, userEPPN), HttpStatus.OK);
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
    
    @RequestMapping(value = "/projects_join_requests", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<ArrayList<ProjectJoinRequest>> getProjectsJoinRequests(
    		@RequestParam(value="projectId", required=false) ArrayList<String> projects,
    		@RequestParam(value="profileId", required=false) ArrayList<String> profiles,
			@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "In getProjectsJoinRequests: as user " + userEPPN);

        return new ResponseEntity<ArrayList<ProjectJoinRequest>>(project.getProjectJoinRequest(projects, profiles, userEPPN), HttpStatus.OK);
    }

    @RequestMapping(value = "/projects_join_requests", method = RequestMethod.POST, consumes="application/json", produces="application/json")
    public ResponseEntity<ProjectJoinRequest> createProjectJoinRequest(@RequestBody PostProjectJoinRequest payload,
                                @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "In createProjectJoinRequest: " + payload + " as user " + userEPPN);
        return new ResponseEntity<ProjectJoinRequest>(project.createProjectJoinRequest(payload, userEPPN), HttpStatus.OK);
    }

    @RequestMapping(value = "/projects/{projectId}/projects_join_requests", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<ArrayList<ProjectJoinRequest>> getProjectJoinRequests(
    		@PathVariable("projectId") String projectId,
    		@RequestParam(value="profileId", required=false) ArrayList<String> profiles,
			@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "In getProjectsJoinRequests: as user " + userEPPN);

        ArrayList<String> projects = new ArrayList<String>();
        projects.add(projectId);
        return new ResponseEntity<ArrayList<ProjectJoinRequest>>(project.getProjectJoinRequest(projects, profiles, userEPPN), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/profiles/{profileId}/projects_join_requests", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<ArrayList<ProjectJoinRequest>> getProfileJoinRequests(
    		@PathVariable("profileId") String profileId,
    		@RequestParam(value="projectId", required=false) ArrayList<String> projects,
	       	@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "In getProjectsJoinRequests: as user " + userEPPN);

        ArrayList<String> profiles = new ArrayList<String>();
        profiles.add(profileId);
        return new ResponseEntity<ArrayList<ProjectJoinRequest>>(project.getProjectJoinRequest(projects, profiles, userEPPN), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/projects_join_requests/{id}", method = RequestMethod.DELETE, produces="application/json")
    public ResponseEntity<?> deleteProjectJoinRequests(
    		@PathVariable("id") String id,
		@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN)  throws Exception {  	
        ServiceLogger.log(logTag, "In deleteProjectJoinRequests: for id " + id + " as user " + userEPPN);

        try {
            ServiceLogger.log(logTag, "In deleteProjectJoinRequests: for id " + id + " as user " + userEPPN);
            
	    boolean ok = project.deleteProjectRequest(id, userEPPN);
            if (ok) {
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
            } else {
               	return new ResponseEntity<ErrorMessage>(new ErrorMessage("failure to delete project join request"), HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            ServiceLogger.log(logTag, "caught exception: for id " + id + " as user " + userEPPN + " " + e.getMessage());
            
	    if (e.getMessage().equals("you are not allowed to delete this request")) {
               	return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.UNAUTHORIZED);
            } else if (e.getMessage().equals("invalid id")) {
               	return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.FORBIDDEN);        		
            } else {
               	throw e;
            }
        }
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
        } else if (null != profileIdString) {
            	return new ResponseEntity<ArrayList<ProjectMember>>(project.getProjectsForMember(profileIdString, userEPPN), HttpStatus.OK);
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
    
    @RequestMapping(value = "/projects/{projectId}/projects_members", method = RequestMethod.GET, produces="application/json")
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

    
    @RequestMapping(value = "/projects/{projectID}/projects_tags", method = RequestMethod.GET)
    public ArrayList<ProjectTag> getProjectTagList(@PathVariable("projectID") int projectID,
    			  @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
    	ServiceLogger.log(logTag, "In getProjectTagList as user " + userEPPN);
        
    	return project.getProjectTagList(projectID, userEPPN);
    }
    
    
    @RequestMapping(value = "/projects_tags", 
    	    produces = { "application/json", "text/html" }, 
    	    method = RequestMethod.POST)
    	  public ResponseEntity<GetProjectTag> projectsTagsPost(

    	@RequestBody PostProjectTag body
    	){
    	      // do some magic!
    	      return new ResponseEntity<GetProjectTag>(HttpStatus.NOT_IMPLEMENTED);
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
    	ServiceLogger.log(logTag, result.getMessage());
    	return result;
    }
    
    
    /**	
     *
     **/
	@RequestMapping(value = "/projects/{projectID}/project_documents", produces = { "application/json",
	"text/html" }, method = RequestMethod.GET)
public ResponseEntity<List<ProjectDocument>> projectsProjectIDProjectDocumentsGet(
	@PathVariable("projectID") String projectID,
	@RequestParam(value = "projectDocumentId", required = true) String projectDocumentId,
	@RequestParam(value = "limit", required = false) Integer limit,
	@RequestParam(value = "order", required = false) String order,
	@RequestParam(value = "sort", required = false) String sort) {
// do some magic!
return new ResponseEntity<List<ProjectDocument>>(HttpStatus.NOT_IMPLEMENTED);
}
	
	
	 @RequestMapping(value = "/projects/{projectID}/following_discussions", 
			    produces = { "application/json", "text/html" }, 
			    method = RequestMethod.GET)
			  public ResponseEntity<List<IndividualDiscussion>> projectsProjectIDFollowingDiscussionsGet(
			@PathVariable("projectID") String projectID,
			 @RequestParam(value = "limit", required = false) Integer limit	,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "sort", required = false) String sort
			){
			      // do some magic!
			      return new ResponseEntity<List<IndividualDiscussion>>(HttpStatus.NOT_IMPLEMENTED);
			  }
	

	
}
