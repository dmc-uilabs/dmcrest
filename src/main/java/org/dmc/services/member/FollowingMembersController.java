package org.dmc.services.member;


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
@RequestMapping(value = "/following_members", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class FollowingMembersController {
  


  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<List<FollowingMemeber>> followingMembersGet(
		  @RequestParam(value = "accountId", required = false) String accountId,
@RequestParam(value = "id", required = false) String id,
@RequestParam(value = "profileId", required = false) String profileId,
@RequestParam(value = "limit", required = false) Integer limit,
@RequestParam(value = "order", required = false) String order,
 @RequestParam(value = "sort", required = false) String sort){
      // do some magic!
      return new ResponseEntity<List<FollowingMemeber>>(HttpStatus.NOT_IMPLEMENTED);
  }

  


  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.POST)
  public ResponseEntity<FollowingMemeber> followingMembersPost(
@RequestBody PostFollowingMemeber body){
      // do some magic!
      return new ResponseEntity<FollowingMemeber>(HttpStatus.NOT_IMPLEMENTED);
  }

  

 
  @RequestMapping(value = "/{id}", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.DELETE)
  public ResponseEntity<Void> followingMembersIdDelete(
@PathVariable("id") String id){
      // do some magic!
      return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
  }

 
  
  @RequestMapping(value = "/{id}", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<FollowingMemeber> followingMembersIdGet(
@PathVariable("id") String id){
      // do some magic!
      return new ResponseEntity<FollowingMemeber>(HttpStatus.NOT_IMPLEMENTED);
  }

  
}
