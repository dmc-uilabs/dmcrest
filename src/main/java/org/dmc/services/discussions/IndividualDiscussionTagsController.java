package org.dmc.services.discussions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/individual-discussion-tags", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussionTagsController {
  

  @RequestMapping(value = "/{disscusionTagID}", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.DELETE)
  public ResponseEntity<Void> individualDiscussionTagsDisscusionTagIDDelete(
@PathVariable("disscusionTagID") String disscusionTagID){
      // do some magic!
      return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
  }




  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.POST)
  public ResponseEntity<IndividualDiscussionTag> individualDiscussionTagsPost(
 @RequestBody IndividualDiscussionTag discussionTag){
      // do some magic!
      return new ResponseEntity<IndividualDiscussionTag>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  
}
