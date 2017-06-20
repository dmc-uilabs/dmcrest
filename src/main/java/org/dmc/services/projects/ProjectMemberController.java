package org.dmc.services.projects;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.profile.Profile;
import org.dmc.services.notification.NotificationService;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.data.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ProjectMemberController {

	@Inject
	private NotificationService notificationService;

	@Inject
	private UserRepository userRepository;

	@Inject
    private ProjectMemberDao projectMemberDao;

    private static final String LOGTAG = ProjectMemberController.class.getName();

    /**
     * GET member
     * @param userEPPN
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/members", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProjectMembers(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws Exception {

        ServiceLogger.log(LOGTAG, "In getProjectMembers: as user " + userEPPN);

        try {
            return new ResponseEntity<ArrayList<Profile>>(projectMemberDao.getMembers(userEPPN), HttpStatus.OK);

        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/projects_members", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProjectMembers(
            @RequestParam(value = "projectId", required = false) String projectList,
            @RequestParam(value = "profileId", required = false) String profileList,
            @RequestParam(value = "accept", required = false) Boolean accept,
            @RequestParam(value = "_limit", required = false) Integer _limit,
            @RequestParam(value = "_order", required = false) String _order,
            @RequestParam(value = "_sort", required = false) String _sort,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws Exception {

        ServiceLogger.log(LOGTAG, "In getProjectMembers: as user " + userEPPN);

        try {
            return new ResponseEntity<ArrayList<ProjectMember>>(projectMemberDao.getProjectMembers(projectList, profileList, accept, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    /**
     * Create Project Member
     * @param member
     * @param userEPPN
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/projects_members", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProjectMember(@RequestBody ProjectMember member, @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) throws Exception {
        ServiceLogger.log(LOGTAG, "In addProjectMember: as user " + userEPPN);
        try {
            final ProjectMember createdMember = projectMemberDao.createProjectMemberRequest(member, userEPPN);
						if (member.getAccept() == false){
							User sender = userRepository.findOne(Integer.parseInt(member.getFromProfileId()));
							User recipient = userRepository.findOne(Integer.parseInt(member.getProfileId()));
							notificationService.notifyInviteToWorkspace(sender, recipient, member.getProjectId());
						}
            return new ResponseEntity<ProjectMember>(createdMember, HttpStatus.valueOf(HttpStatus.OK.value()));

        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    /**
     * Update Project Member
     * @param member
     * @param userEPPN
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveprojects_members/{memberId}", method = RequestMethod.PATCH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProjectMember(@PathVariable("memberId") String memberId, @RequestBody ProjectMember member, @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) throws Exception {
        ServiceLogger.log(LOGTAG, "In updateProjectMember: for request " + memberId + " as user " + userEPPN);

        try {
            // TODO: confirm that id in ProjectMember object matches the {memberId}, otherwise return BadRequest
            return new ResponseEntity<ProjectMember>(projectMemberDao.acceptMemberInProject(memberId, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    /**
     * Update Project Member
     * @param member
     * @param userEPPN
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/projects_members/{memberId}", method = RequestMethod.PATCH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProjectMembers(@PathVariable("memberId") String memberId, @RequestBody List<ProjectMember> memberList, @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) throws Exception {
        ServiceLogger.log(LOGTAG, "In updateProjectMember: for request " + memberId + " as user " + userEPPN);

        try {
            // TODO: confirm that id in ProjectMember object matches the {memberId}, otherwise return BadRequest
            return new ResponseEntity<ProjectMember>(projectMemberDao.acceptMemberInProject(memberId, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    /**
     * Get Project Members
     * @param memberId
     * @param userEPPN
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/projects_members/{memberId}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProjectsForMember(@PathVariable("memberId") String memberId, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)
            throws Exception {
        ServiceLogger.log(LOGTAG, "In getProjectsForMember: for member" + memberId + " as user " + userEPPN);

        return new ResponseEntity<ArrayList<ProjectMember>>(projectMemberDao.getProjectMembersByInvitation(memberId, userEPPN), HttpStatus.OK);
    }

    @RequestMapping(value = "/projects/{projectId}/projects_members", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMembersForProject(@PathVariable("projectId") String projectId, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)
            throws Exception {
        ServiceLogger.log(LOGTAG, "In getMembersForProject: for project" + projectId + " as user " + userEPPN);

        return new ResponseEntity<ArrayList<ProjectMember>>(projectMemberDao.getProjectMembers(projectId, null, new Boolean(true), userEPPN), HttpStatus.OK);
    }

	/**
	 *
	 * @param requestId
	 * @param userEPPN
	 * @return
	 */
    @RequestMapping(value = "/new_members/{requestId}", method = RequestMethod.PATCH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> acceptMemberInProject(@PathVariable("requestId") String requestId,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

        ServiceLogger.log(LOGTAG, "In acceptMemberInProject: for request " + requestId + " as user " + userEPPN);

        try {
            return new ResponseEntity<ProjectMember>(projectMemberDao.acceptMemberInProject(requestId, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

	/**
	 *
	 * @param requestId
	 * @param userEPPN
	 * @return
	 */
    @RequestMapping(value = "/projects_members/{requestId}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> rejectMemberInProject(@PathVariable("requestId") String requestId,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)  {

        ServiceLogger.log(LOGTAG, "In rejectMemberInProject: for requestId " + requestId + " as user " + userEPPN);

        try {
            return new ResponseEntity<ProjectMember>(projectMemberDao.rejectMemberInProject(requestId, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

}
