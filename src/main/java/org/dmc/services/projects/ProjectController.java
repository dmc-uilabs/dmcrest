package org.dmc.services.projects;

import org.assertj.core.util.Lists;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.models.ProjectJoinApprovalRequestModel;
import org.dmc.services.discussions.Discussion;
import org.dmc.services.discussions.DiscussionListDao;
import org.dmc.services.discussions.IndividualDiscussion;
import org.dmc.services.discussions.IndividualDiscussionDao;
import org.dmc.services.exceptions.ArgumentNotFoundException;
import org.dmc.services.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.xml.ws.http.HTTPException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ProjectController {

	@Inject
	private ProjectDao projectDao;

	@Inject
	private ProjectJoinApprovalRequestService projectJoinApprovalRequestSevice;

	private final String logTag = ProjectController.class.getName();
	private DiscussionListDao discussionListDao = new DiscussionListDao();

	@RequestMapping(value = "/projects/{projectID}", method = RequestMethod.GET)
	public Project getProject(@PathVariable("projectID") int projectID, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

		ServiceLogger.log(logTag, "In getProject, projectID: " + projectID + " as user " + userEPPN);
		return projectDao.getProject(projectID, userEPPN);
	}

	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public List<Project> getProjectList(
			@RequestParam(value="_start", required=false) Integer start,
			@RequestParam(value="_limit", required=false) Integer limit,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(logTag, "In getProjectList as user " + userEPPN);
		return getAllPublicAndPrivateProjects(userEPPN);
	}

	@RequestMapping(value = "projects/all", method = RequestMethod.GET)
	public List<Project> getProjectList(
	        @RequestParam(value="_order", required=false) String order,
	        @RequestParam(value="_sort", required=false) String sort,
            @RequestParam(value="_start", required=false) Integer start,
            @RequestParam(value="_limit", required=false) Integer limit,
	        @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        ServiceLogger.log(logTag, "In getProjectList as user " + userEPPN);
        // @TODO: expand to handle arguments and change return type to a ResponseEntity to match new approach for error handling
        return getAllPublicAndPrivateProjects(userEPPN);
	}

	@RequestMapping(value = "projects/allPublic", method = RequestMethod.GET)
	public List<Project> getPublicProjectList(
			@RequestParam(value="_order", required=false) String order,
			@RequestParam(value="_sort", required=false) String sort,
			@RequestParam(value="_start", required=false, defaultValue = "0") Integer start,
			@RequestParam(value="_limit", required=false, defaultValue = "10") Integer limit,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(logTag, "In getProjectList as user " + userEPPN);
		return projectDao.getPublicProjects(limit, start);
	}

	@RequestMapping(value = "projects/myProjects", method = RequestMethod.GET)
	public List<Project> getMyProjectList(
			@RequestParam(value="_order", required=false) String order,
			@RequestParam(value="_sort", required=false) String sort,
			@RequestParam(value="_start", required=false, defaultValue = "0") Integer start,
			@RequestParam(value="_limit", required=false, defaultValue = "10") Integer limit,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(logTag, "In getProjectList as user " + userEPPN);
		return projectDao.getProjectList(userEPPN, limit, start);
	}

	// Hack to add support for public projects, being rewritten to use JPA soon
	private List<Project> getAllPublicAndPrivateProjects(String userEPPN) {
		List<Project> privateProjects = projectDao.getProjectList(userEPPN, null, null);
		List<Project> publicProjects = projectDao.getPublicProjects(null, null);

		Set<Project> projects = new TreeSet<Project>(new Comparator<Project>() {
			@Override
			public int compare(Project o1, Project o2) {
				return Integer.compare(o1.getId(), o2.getId());
			}
		});

		projects.addAll(privateProjects);
		projects.addAll(publicProjects);

		return Lists.newArrayList(projects);
	}

	// leaving this as an example of how to work with parameters to URL
	// instead of json, but json is probably preferable
	@RequestMapping(value = "/projects/createWithParameter", method = RequestMethod.POST)
	@ResponseBody
	public Id createProject(@RequestParam("projectname") String projectname, @RequestParam("unixname") String unixname, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)
			throws Exception {

		ServiceLogger.log(logTag, "In createProject: " + projectname + ", " + unixname + " as user " + userEPPN);

		long dueDate = 0;
		int createdOn = 0;
		return projectDao.createProject(projectname, unixname, projectname, Project.PRIVATE, "admin", userEPPN, dueDate, createdOn);
	}

	@RequestMapping(value = "/projects/create", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Id> createProject(@RequestBody ProjectCreateRequest payload, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws Exception {
		ServiceLogger.log(logTag, "In createProject: " + payload + " as user " + userEPPN);

		return new ResponseEntity<Id>(projectDao.createProject(payload, userEPPN), HttpStatus.OK);
	}

	/*
	 * @RequestMapping(value = "/role/update", method = RequestMethod.POST)
	 *
	 * @ResponseBody public String updateRole(@RequestParam(value="id",
	 * defaultValue="-1") int id) { System.out.println("In createRole role: " +
	 * id);
	 *
	 *
	 * //RoleDao.createRole updates the Role in the database identified by id
	 * using the provided POST params //it creates an instance of this role i.e
	 * new Role(param.id, param.name, param.title.....) //this controller in
	 * turn returns this updated Role instance to the reques using spring's
	 * Jackson which //converts the response to JSON
	 *
	 * return RoleDao.updateRole(params); }
	 */

	@RequestMapping(value = "/projects_join_requests", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ArrayList<ProjectJoinRequest>> getProjectsJoinRequests(@RequestParam(value = "projectId", required = false) ArrayList<String> projects,
			@RequestParam(value = "profileId", required = false) ArrayList<String> profiles, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws Exception {
		ServiceLogger.log(logTag, "In getProjectsJoinRequests: as user " + userEPPN);

		return new ResponseEntity<ArrayList<ProjectJoinRequest>>(projectDao.getProjectJoinRequest(projects, profiles, userEPPN), HttpStatus.OK);
	}

	@RequestMapping(value = "/projects_join_requests", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ProjectJoinRequest> createProjectJoinRequest(@RequestBody PostProjectJoinRequest payload, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)
			throws Exception {
		ServiceLogger.log(logTag, "In createProjectJoinRequest: " + payload + " as user " + userEPPN);
		return new ResponseEntity<ProjectJoinRequest>(projectDao.createProjectJoinRequest(payload, userEPPN), HttpStatus.OK);
	}

	@RequestMapping(value = "/projects/{projectId}/projects_join_requests", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ArrayList<ProjectJoinRequest>> getProjectJoinRequests(@PathVariable("projectId") String projectId,
			@RequestParam(value = "profileId", required = false) ArrayList<String> profiles, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws Exception {
		ServiceLogger.log(logTag, "In getProjectsJoinRequests: as user " + userEPPN);

		ArrayList<String> projects = new ArrayList<String>();
		projects.add(projectId);
		return new ResponseEntity<ArrayList<ProjectJoinRequest>>(projectDao.getProjectJoinRequest(projects, profiles, userEPPN), HttpStatus.OK);
	}

	@RequestMapping(value = "/profiles/{profileId}/projects_join_requests", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ArrayList<ProjectJoinRequest>> getProfileJoinRequests(@PathVariable("profileId") String profileId,
			@RequestParam(value = "projectId", required = false) ArrayList<String> projects, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws Exception {
		ServiceLogger.log(logTag, "In getProjectsJoinRequests: as user " + userEPPN);

		ArrayList<String> profiles = new ArrayList<String>();
		profiles.add(profileId);
		return new ResponseEntity<ArrayList<ProjectJoinRequest>>(projectDao.getProjectJoinRequest(projects, profiles, userEPPN), HttpStatus.OK);
	}

	@RequestMapping(value = "/projects_join_requests/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<?> deleteProjectJoinRequests(@PathVariable("id") String id, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws Exception {
		ServiceLogger.log(logTag, "In deleteProjectJoinRequests: for id " + id + " as user " + userEPPN);

		try {
			ServiceLogger.log(logTag, "In deleteProjectJoinRequests: for id " + id + " as user " + userEPPN);

			boolean ok = projectDao.deleteProjectRequest(id, userEPPN);
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

	/**
	 * Return Project Discussions
	 **/
    // this is same as below - yaml may be wrong as to what should be defined
	@RequestMapping(value = "/projects/{projectID}/all-discussions", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity getAllDiscussions(@PathVariable("projectID") int projectID,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN,
            @RequestParam(value = "_limit", defaultValue = "100") Integer limit,
            @RequestParam(value = "_order", defaultValue = "DESC") String order,
            @RequestParam(value = "_sort", defaultValue = "time_posted") String sort) {

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

	@RequestMapping(value = "/projects/{projectID}/individual-discussion", method = RequestMethod.GET, produces = {
			"application/json" })
	public ResponseEntity getIndividualDiscussionsFromProjectId(@PathVariable("projectID") Integer projectID,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN,
			@RequestParam(value = "_limit", required = false) Integer limit,
			@RequestParam(value = "_order", required = false) String order,
			@RequestParam(value = "_sort", required = false) String sort) {
		IndividualDiscussionDao individualDiscussionDao = new IndividualDiscussionDao();
		ServiceLogger.log(logTag, "getIndividualDiscussionsFromProjectId, userEPPN: " + userEPPN);

		try {
			return new ResponseEntity<List<IndividualDiscussion>>(
					individualDiscussionDao.getIndividualDiscussionsFromProjectId(projectID, limit, order, sort),
					HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@ExceptionHandler(Exception.class)
	public ErrorMessage handleException(Exception ex) {
		// prepare responseEntity
		ErrorMessage result = new ErrorMessage.ErrorMessageBuilder(ex.getMessage()).build();
		ServiceLogger.log(logTag, result.getMessage());
		return result;
	}

	@RequestMapping(value = "/projects/{projectID}/following_discussions", produces = {
			"application/json" }, method = RequestMethod.GET)
	public ResponseEntity<?> getFollowingDiscussionsFromProjectId(@PathVariable("projectID") Integer projectID,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		IndividualDiscussionDao individualDiscussionDao = new IndividualDiscussionDao();
		ServiceLogger.log(logTag, "getFollowingDiscussionFromProjectId, userEPPN:" + userEPPN);
		List<IndividualDiscussion> result;
		try {
			result = individualDiscussionDao.getFollowingDiscussionFromProjectId(projectID, limit, order, sort,
					userEPPN);
			return new ResponseEntity<List<IndividualDiscussion>>(result, HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	/**
	 * Update Project
	 * @param id
	 * @param project
	 * @param userEPPN
	 * @return
	 */
	@RequestMapping(value = "/projects/{id}", method = RequestMethod.PATCH, produces = { APPLICATION_JSON_VALUE })
	public ResponseEntity updateProject(@PathVariable("id") int id, @RequestBody Project project, @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
		ServiceLogger.log(logTag, "updateProject, for id = " + id + " (body) project: " + project.toString());

		int httpStatusCode = HttpStatus.OK.value();
		Id updatedId = null;

		try {
			updatedId = projectDao.updateProject(id, project, userEPPN);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}

		return new ResponseEntity<Id>(updatedId, HttpStatus.valueOf(httpStatusCode));
	}

	@RequestMapping(value = "/projects/{id}/joinApprovalRequests", method = RequestMethod.GET)
	public List<ProjectJoinApprovalRequestModel> getProjectJoinApprovalRequests(@PathVariable("id") Integer projectId) {
		return projectJoinApprovalRequestSevice.getProjectJoinApprovalRequests(projectId);
	}

	@RequestMapping(value = "/projects/{id}/joinApprovalRequests", method = RequestMethod.POST)
	public ProjectJoinApprovalRequestModel createProjectJoinApprovalRequest(@PathVariable("id") Integer projectId) {
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return projectJoinApprovalRequestSevice.createProjectJoinApprovalRequest(projectId, userPrincipal.getUsername());
	}

	@RequestMapping(value = "/projectJoinApprovalRequests/{id}", method = RequestMethod.PUT, params = "action=approve")
	public ProjectJoinApprovalRequestModel approveProjectJoinApprovalRequest(@PathVariable("id") Integer id) throws Exception {
		UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return projectJoinApprovalRequestSevice.approveRequest(id, user.getId());
	}

	@RequestMapping(value = "/projectJoinApprovalRequests/{id}", method = RequestMethod.PUT, params = "action=decline")
	public ProjectJoinApprovalRequestModel declineProjectJoinApprovalRequest(@PathVariable("id") Integer id) throws ArgumentNotFoundException {
		UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return projectJoinApprovalRequestSevice.declineRequest(id, user.getId());
	}

}
