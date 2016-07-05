package org.dmc.services.users;

import java.util.ArrayList;
import java.lang.Exception;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.projects.ProjectMember;
import org.dmc.services.projects.ProjectMemberDao;
import org.dmc.services.profile.Profile;
import org.dmc.services.profile.ProfileDao;

import java.util.Iterator;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignUserController {
	
	private final String logTag = AssignUserController.class.getName();
	private ProjectMemberDao projectMemberDao = new ProjectMemberDao();
	
	/**
	 * GET assign_users
	 * @param userEPPN
	 * @param project number
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/assign_users", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> getAssignUsers(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN,
											@RequestHeader(value = "projectId", defaultValue = "-1") Integer projectId) throws Exception {
		
		ServiceLogger.log(logTag, "In getAssignUsers: as user " + userEPPN);
		
		try {
			ArrayList<Profile> members = projectMemberDao.getMembers(userEPPN); //ToDo: User projectNumber to get correct set of users
			
			ArrayList<AssignUser> assignUser = new ArrayList<AssignUser>();
			
			Iterator<Profile> iter = members.iterator();
			while(iter.hasNext()) {
				Profile userProfile = iter.next();
				AssignUser user = new AssignUser(userProfile.getId(), userProfile.getDisplayName());
				assignUser.add(user);
			}
			
			return new ResponseEntity<ArrayList<AssignUser>>(assignUser, HttpStatus.OK);
			
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}
	
	
	
	
	
	/**
	 * GET assign_users
	 * @param userEPPN
	 * @param project number
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/assign_users/{projectId}", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> getAssignUsersForProject(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN,
													  @PathVariable(value = "projectId") Integer projectId) throws Exception {
		
		ServiceLogger.log(logTag, "In getAssignUsers: as user " + userEPPN);
		
		try {
			ArrayList<ProjectMember> members =  projectMemberDao.getMembersForProject(projectId.toString(), userEPPN);
			ArrayList<AssignUser> assignUser = new ArrayList<AssignUser>();
			ProfileDao profileDao = new ProfileDao();
			
			Iterator<ProjectMember> iter = members.iterator();
			while(iter.hasNext()) {
				ProjectMember projectMember = iter.next();
				Profile userProfile = profileDao.getProfile(Integer.parseInt(projectMember.getProfileId()));
				AssignUser user = new AssignUser(userProfile.getId(), userProfile.getDisplayName());
				assignUser.add(user);
			}
			
			return new ResponseEntity<ArrayList<AssignUser>>(assignUser, HttpStatus.OK);
			
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	
}
