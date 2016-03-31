package org.dmc.services.discussions;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscussionController {

	private final String logTag = DiscussionController.class.getName();
	private DiscussionDao discussionDao = new DiscussionDao(); 
	private DiscussionListDao discussionListDao = new DiscussionListDao();

	@RequestMapping(value = "/discussions/{discussionID}", method = RequestMethod.GET, produces = { "application/json"})
    public Discussion getDiscussion(@PathVariable("discussionID") int discussionID) {
    	ServiceLogger.log(logTag, "In getDiscussion");
    	ServiceLogger.log(logTag, "In getDiscussion, discussionID: " + discussionID);
    	return discussionDao.getDiscussion(discussionID);
    }
    
    
    /**	
    *Return Discussions
    **/
   @RequestMapping(value = "/all-discussions", method = RequestMethod.GET, produces = { "application/json"})
   public ResponseEntity getDiscussions(@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN,
		   								@RequestParam(value="_limit", defaultValue="100") int limit,
		   								@RequestParam(value="_order", defaultValue="DESC") String order,
		   								@RequestParam(value="_sort", defaultValue="time_posted") String sort) {
	   
   	ServiceLogger.log(logTag, "getDiscussions, userEPPN: " + userEPPN);
   	int statusCode = HttpStatus.OK.value();
   	ArrayList<Discussion> discussions = null;
   	
   	try {
           discussions = discussionListDao.getDiscussionList(userEPPN, limit, order, sort);
           return new ResponseEntity<ArrayList<Discussion>>(discussions, HttpStatus.valueOf(statusCode));
   	} catch (HTTPException e) {
   		ServiceLogger.log(logTag, e.getMessage());
   		statusCode = e.getStatusCode();
   		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
   		return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
   	}
   }

   /**
   Create a Discussion
   **/
  @RequestMapping(value = "/discussions/create", produces = { "application/json"}, method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity createDiscussion(@RequestBody Discussion discussion, 
		  								 @RequestHeader(value="AJP_eppn", required=true) String userEPPN) {
	   
  	ServiceLogger.log(logTag, "Create Discussion, userEPPN: " + userEPPN);
  	int statusCode = HttpStatus.OK.value();
  	Id createdId = null;
  	
  	try {
          createdId = discussionDao.createDiscussion(discussion, userEPPN);
          return new ResponseEntity<Id>(createdId, HttpStatus.valueOf(statusCode));
  	} catch (HTTPException e) {
  		ServiceLogger.log(logTag, e.getMessage());
  		statusCode = e.getStatusCode();
  		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
  		return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
  	}
  }

}