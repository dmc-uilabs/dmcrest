package org.dmc.services.discussions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/individual-discussion", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussionController {
  

  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<List<IndividualDiscussion>> individualDiscussionGet(
		  @RequestParam(value = "limit", required = false) Integer limit,
@RequestParam(value = "order", required = false) String order,
@RequestParam(value = "sort", required = false) String sort){
      // do some magic!
      return new ResponseEntity<List<IndividualDiscussion>>(HttpStatus.NOT_IMPLEMENTED);
  }

  
  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.POST)
  public ResponseEntity<IndividualDiscussion> individualDiscussionPost(
@RequestBody IndividualDiscussion discussion){
      // do some magic!
      return new ResponseEntity<IndividualDiscussion>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  @RequestMapping(value = "/{individualDiscussionID}/individual-discussion-comments", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<List<IndividualDiscussionComment>> individualDiscussionIndividualDiscussionIDIndividualDiscussionCommentsGet(
@PathVariable("individualDiscussionID") String individualDiscussionID,
@RequestParam(value = "commentId", required = true) String commentId,
@RequestParam(value = "order", required = false) String order,
@RequestParam(value = "sort", required = false) String sort){
      // do some magic!
      return new ResponseEntity<List<IndividualDiscussionComment>>(HttpStatus.NOT_IMPLEMENTED);
  }

  
  

  @RequestMapping(value = "/{individualDiscussionID}/individual-discussion-tags", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<List<IndividualDiscussionTag>> individualDiscussionIndividualDiscussionIDIndividualDiscussionTagsGet(
@PathVariable("individualDiscussionID") String individualDiscussionID,
@RequestParam(value = "limit", required = false) Integer limit,
@RequestParam(value = "order", required = false) String order,
@RequestParam(value = "sort", required = false) String sort){
      // do some magic!
      return new ResponseEntity<List<IndividualDiscussionTag>>(HttpStatus.NOT_IMPLEMENTED);
  }

  
}
