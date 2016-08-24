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
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class AssignUserController {

    private final static String LOGTAG = AssignUserController.class.getName();
    private ProjectMemberDao projectMemberDao = new ProjectMemberDao();

	/**
	 * GET assign_users
	 * @param userEPPN
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/assign_users", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAssignUsers(
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws Exception {

        ServiceLogger.log(LOGTAG, "In getAssignUsers: as user " + userEPPN);

        try {
            final ArrayList<Profile> members = projectMemberDao.getMembers(userEPPN);
            final ArrayList<AssignUser> assignUser = new ArrayList<AssignUser>();

            final Iterator<Profile> iter = members.iterator();
            while (iter.hasNext()) {
                final Profile userProfile = iter.next();
                final AssignUser user = new AssignUser(userProfile.getId(), userProfile.getDisplayName());
                assignUser.add(user);
            }

            return new ResponseEntity<ArrayList<AssignUser>>(assignUser, HttpStatus.OK);

        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

	/**
	 * GET assign_users
	 * @param userEPPN
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/assign_users/{projectId}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAssignUsersForProject(
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN,
            @PathVariable(value = "projectId") Integer projectId) throws Exception {

        ServiceLogger.log(LOGTAG, "In getAssignUsers: as user " + userEPPN);

        try {
            final ArrayList<ProjectMember> members = projectMemberDao.getMembersForProject(projectId.toString(), null,
                    userEPPN);
            final ArrayList<AssignUser> assignUser = new ArrayList<AssignUser>();
            final ProfileDao profileDao = new ProfileDao();

            final Iterator<ProjectMember> iter = members.iterator();
            while (iter.hasNext()) {
                final ProjectMember projectMember = iter.next();
                final Profile userProfile = profileDao.getProfile(Integer.parseInt(projectMember.getProfileId()));
                final AssignUser user = new AssignUser(userProfile.getId(), userProfile.getDisplayName());
                assignUser.add(user);
            }

            return new ResponseEntity<ArrayList<AssignUser>>(assignUser, HttpStatus.OK);

        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }
}
