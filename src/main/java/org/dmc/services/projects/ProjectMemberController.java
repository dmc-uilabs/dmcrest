package org.dmc.services.projects;

import java.util.ArrayList;
import java.util.List;
import java.lang.Exception;

import org.dmc.services.DMCServiceException;
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
public class ProjectMemberController {

	private final String logTag = ProjectMemberController.class.getName();
	private ProjectMemberDao projectMemberDao = new ProjectMemberDao();

	@RequestMapping(value = "/projects_members", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ArrayList<ProjectMember>> getProjectMembers(@RequestParam(value = "projectId", required = false) String projectIdString,
			@RequestParam(value = "profileId", required = false) String profileIdString, @RequestParam(value = "accept", required = false) boolean accept,
			@RequestParam(value = "_limit", required = false) int _limit, @RequestParam(value = "_order", required = false) String _order,
			@RequestParam(value = "_sort", required = false) String _sort, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws Exception {
		ServiceLogger.log(logTag, "In getProjectMembers: as user " + userEPPN);

		if (null != projectIdString) {
			return new ResponseEntity<ArrayList<ProjectMember>>(projectMemberDao.getMembersForProject(projectIdString, userEPPN), HttpStatus.OK);
		} else if (null != profileIdString) {
			return new ResponseEntity<ArrayList<ProjectMember>>(projectMemberDao.getProjectsForMember(profileIdString, userEPPN), HttpStatus.OK);
		} else {
			return new ResponseEntity<ArrayList<ProjectMember>>(projectMemberDao.getProjectMembers(userEPPN), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/projects_members", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<ProjectMember> addProjectMember(@RequestBody ProjectMember payload, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws Exception {
		ServiceLogger.log(logTag, "In addProjectMember: as user " + userEPPN);

		return new ResponseEntity<ProjectMember>(projectMemberDao.addProjectMember(payload, userEPPN), HttpStatus.OK);
	}

	@RequestMapping(value = "/projects_members/{memberId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ArrayList<ProjectMember>> getProjectsForMember(@PathVariable("memberId") String memberId, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)
			throws Exception {
		ServiceLogger.log(logTag, "In getProjectsForMember: for member" + memberId + " as user " + userEPPN);

		return new ResponseEntity<ArrayList<ProjectMember>>(projectMemberDao.getProjectsForMember(memberId, userEPPN), HttpStatus.OK);
	}

	@RequestMapping(value = "/projects/{projectId}/projects_members", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ArrayList<ProjectMember>> getMembersForProject(@PathVariable("projectId") String projectId, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)
			throws Exception {
		ServiceLogger.log(logTag, "In getMembersForProject: for project" + projectId + " as user " + userEPPN);

		return new ResponseEntity<ArrayList<ProjectMember>>(projectMemberDao.getMembersForProject(projectId, userEPPN), HttpStatus.OK);
	}

	/**
	 * Accept Accept
	 * @param projectId
	 * @param memberId
	 * @param userEPPN
	 */
	@RequestMapping(value = "/projects/{projectId}/accept/{memberId}", method = RequestMethod.PATCH, produces = "application/json")
	public ResponseEntity acceptMemberInProject(@PathVariable("projectId") String projectId, @PathVariable("memberId") String memberId,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		
		ServiceLogger.log(logTag, "In acceptMemberInProject: for member " + memberId + " as user " + userEPPN);
		
		try {
			return new ResponseEntity<ProjectMember>(projectMemberDao.acceptMemberInProject(projectId, memberId, userEPPN), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}
	}

	/**
	 * Reject Member
	 * @param projectId
	 * @param memberId
	 * @param userEPPN
	 */
	@RequestMapping(value = "/projects/{projectId}/reject/{memberId}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity rejectMemberInProject(@PathVariable("projectId") String projectId, @PathVariable("memberId") String memberId,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)  {
		
		ServiceLogger.log(logTag, "In rejectMemberInProject: for member " + memberId + " as user " + userEPPN);
		
		try {
			return new ResponseEntity<ProjectMember>(projectMemberDao.rejectMemberInProject(projectId, memberId, userEPPN), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}
	}

}
