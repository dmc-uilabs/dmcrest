package org.dmc.services.projects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.*;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;

@Controller
@RequestMapping(value = "/projects_tags", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ProjectsTagsController {
	
    private final String logTag = ProjectsTagsController.class.getName();
    private ProjectsTagsDao projectsTagsDao = new ProjectsTagsDao();
  
  @RequestMapping(value = "/{projectTagid}", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.DELETE)
  public ResponseEntity<Void> projectsTagsProjectTagidDelete(
@PathVariable("projectTagid") String projectTagid){
      // do some magic!
      return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
  }
  
  /**
   * Post Project Tag
   * @param tag
   * @param userEPPN
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/projects_tags", method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity createProjectTag(@RequestBody ProjectTag tag, @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) throws Exception {
      ServiceLogger.log(logTag, "In create ProjectTag: as user " + userEPPN);

      ProjectTag createdTag = null;
      
      try {
          createdTag = projectsTagsDao.createProjectsTags(tag, userEPPN);
          return new ResponseEntity<ProjectTag>(createdTag, HttpStatus.valueOf(HttpStatus.OK.value()));
      } catch (DMCServiceException e) {
          ServiceLogger.logException(logTag, e);
          return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
      } 
  }

  

  
}
