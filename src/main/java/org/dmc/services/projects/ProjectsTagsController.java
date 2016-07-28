package org.dmc.services.projects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;

import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProjectsTagsController {

    private final static String LOGTAG = ProjectsTagsController.class.getName();
    private ProjectsTagsDao projectsTagsDao = new ProjectsTagsDao();

    /**
     * Post Project Tag
     * 
     * @param tag
     * @param userEPPN
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/projects_tags", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProjectTag(@RequestBody ProjectTag tag,
            @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
        ServiceLogger.log(LOGTAG, "In create ProjectTag: as user " + userEPPN);

        try {
            final ProjectTag createdTag = projectsTagsDao.createProjectTags(tag, userEPPN);
            return new ResponseEntity<ProjectTag>(createdTag, HttpStatus.valueOf(HttpStatus.OK.value()));
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    /**
     * Get Project Tags
     * 
     * @param projectId
     * @param userEPPN
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/projects_tags", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllProjectTags(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)

    {
        ServiceLogger.log(LOGTAG, "In getAllProjectTags: as user " + userEPPN);

        try {
            return new ResponseEntity<ArrayList<ProjectTag>>(projectsTagsDao.getProjectTags(null, userEPPN),
                    HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    /**
     * Get Project Tags
     * 
     * @param projectId
     * @param userEPPN
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/projects/{projectId}/projects_tags", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProjectTags(@PathVariable("projectId") int projectId,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        ServiceLogger.log(LOGTAG, "In getProjectTags: for project" + projectId + " as user " + userEPPN);

        try {
            return new ResponseEntity<ArrayList<ProjectTag>>(projectsTagsDao.getProjectTags(projectId, userEPPN),
                    HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    /**
     * Delete project Tag
     * 
     * @param tagId
     * @param userEPPN
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/projects_tags/{projectTagid}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProjectTag(@PathVariable("projectTagid") int tagId,
            @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
        ServiceLogger.log(LOGTAG, "In delete ProjectTag: as user " + userEPPN);

        try {
            final Id deletedTag = projectsTagsDao.deleteProjectTag(tagId, userEPPN);
            return new ResponseEntity<Id>(deletedTag, HttpStatus.valueOf(HttpStatus.OK.value()));
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

}
