package org.dmc.services.projects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/projects_tags", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ProjectsTagsController {
  
  @RequestMapping(value = "/{projectTagid}", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.DELETE)
  public ResponseEntity<Void> projectsTagsProjectTagidDelete(
@PathVariable("projectTagid") String projectTagid){
      // do some magic!
      return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  
}
