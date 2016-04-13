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
@RequestMapping(value = "/individual-discussion-comments-helpful", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussionCommentsHelpfulController {
  

  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<List<IndividualDiscussionCommentHelpful>> individualDiscussionCommentsHelpfulGet(
		  @RequestParam(value = "commentId", required = true) String commentId,
@RequestParam(value = "accountId", required = true) String accountId,
@RequestParam(value = "limit", required = false) Integer limit,
@RequestParam(value = "order", required = false) String order,
@RequestParam(value = "sort", required = false) String sort){
      // do some magic!
      return new ResponseEntity<List<IndividualDiscussionCommentHelpful>>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.POST)
  public ResponseEntity<IndividualDiscussionCommentHelpful> individualDiscussionCommentsHelpfulPost(
@RequestBody IndividualDiscussionCommentHelpful individualDiscussionCommentHelpful){
      // do some magic!
      return new ResponseEntity<IndividualDiscussionCommentHelpful>(HttpStatus.NOT_IMPLEMENTED);
  }

  
  

  @RequestMapping(value = "/{helpfulID}", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.PATCH)
  public ResponseEntity<IndividualDiscussionCommentHelpful> individualDiscussionCommentsHelpfulHelpfulIDPatch(
@PathVariable("helpfulID") String helpfulID,
   @RequestBody IndividualDiscussionCommentHelpful helpful){
      // do some magic!
      return new ResponseEntity<IndividualDiscussionCommentHelpful>(HttpStatus.NOT_IMPLEMENTED);
  }

  

}
