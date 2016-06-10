package org.dmc.services.projects;

import java.util.ArrayList;
import java.lang.Exception;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.profile.Profile;
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
public class ProjectMemberController {

    private final String logTag = ProjectMemberController.class.getName();
    private ProjectMemberDao projectMemberDao = new ProjectMemberDao();

    /**
     * GET member
     * @param userEPPN
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/members", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getProjectMembers(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws Exception {
        
        ServiceLogger.log(logTag, "In getProjectMembers: as user " + userEPPN);

        try {
        	return new ResponseEntity<ArrayList<Profile>>(projectMemberDao.getMembers(userEPPN), HttpStatus.OK);
            
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
        } 
    }
    
    @RequestMapping(value = "/projects_members", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getProjectMembers(
            @RequestParam(value = "projectId", required = false) String projectIdString,
            @RequestParam(value = "profileId", required = false) String profileIdString, 
            @RequestParam(value = "accept", required = false) Boolean accept,
            @RequestParam(value = "_limit", required = false) Integer _limit,
            @RequestParam(value = "_order", required = false) String _order,
            @RequestParam(value = "_sort", required = false) String _sort,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) throws Exception {
        
        ServiceLogger.log(logTag, "In getProjectMembers: as user " + userEPPN);

        try {
            
            if (null != projectIdString) {
                return new ResponseEntity<ArrayList<ProjectMember>>(projectMemberDao.getMembersForProject(projectIdString, userEPPN), HttpStatus.OK);
            } else if (null != profileIdString) {
                return new ResponseEntity<ArrayList<ProjectMember>>(projectMemberDao.getProjectsForMember(profileIdString, userEPPN), HttpStatus.OK);
            } else {
                return new ResponseEntity<ArrayList<ProjectMember>>(projectMemberDao.getProjectMembers(userEPPN), HttpStatus.OK);
            }
            
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
        } 
    }
    
    /**
     * Create Project Member
     * @param member
     * @param userEPPN
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/projects_members", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity addProjectMember(@RequestBody ProjectMember member, @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) throws Exception {
        ServiceLogger.log(logTag, "In addProjectMember: as user " + userEPPN);

        ProjectMember createdMember = null;
        
        try {
            createdMember = projectMemberDao.addProjectMember(member, userEPPN);
            return new ResponseEntity<ProjectMember>(createdMember, HttpStatus.valueOf(HttpStatus.OK.value()));
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
        } 
    }

    /**
     * Update Project Member
     * @param member
     * @param userEPPN
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/projects_members/{memberId}", method = RequestMethod.PATCH, produces = "application/json")
    public ResponseEntity updateProjectMember(@PathVariable("memberId") String memberId, @RequestBody ProjectMember member, @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) throws Exception {
        ServiceLogger.log(logTag, "In addProjectMember: as user " + userEPPN);

        ProjectMember createdMember = null;
        
        try {
            createdMember = projectMemberDao.updateProjectMember(memberId, member, userEPPN);
            return new ResponseEntity<ProjectMember>(createdMember, HttpStatus.valueOf(HttpStatus.OK.value()));
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
        } 
    }
    
    /** 
     * Get Project Members
     * @param memberId
     * @param userEPPN
     * @return
     * @throws Exception
     */
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
