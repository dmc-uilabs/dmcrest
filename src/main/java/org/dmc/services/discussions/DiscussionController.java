package org.dmc.services.discussions;

import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscussionController {

	private final String logTag = DiscussionController.class.getName();
	private DiscussionDao discussion = new DiscussionDao(); 
	private DiscussionListDao discussionList = new DiscussionListDao();

	@RequestMapping(value = "/discussions/{discussionID}", method = RequestMethod.GET, produces = { "application/json"})
    public Discussion getDiscussion(@PathVariable("discussionID") int discussionID) {
    	ServiceLogger.log(logTag, "In getDiscussion");
    	ServiceLogger.log(logTag, "In getDiscussion, discussionID: " + discussionID);
    	return discussion.getDiscussion(discussionID);
    }
    
    /**
    Return a list of Discussions
    **/
   @RequestMapping(value = "/all-discussions", method = RequestMethod.GET, produces = { "application/json"})
   public ResponseEntity getDiscussions(@RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
	   
   	ServiceLogger.log(logTag, "getDiscussions, userEPPN: " + userEPPN);
   	int statusCode = HttpStatus.OK.value();
   	ArrayList<Discussion> discussions = null;
   	
   	try {
           discussions = discussionList.getDiscussionList(userEPPN);
           return new ResponseEntity<ArrayList<Discussion>>(discussions, HttpStatus.valueOf(statusCode));
   	} catch (HTTPException e) {
   		ServiceLogger.log(logTag, e.getMessage());
   		statusCode = e.getStatusCode();
   		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
   		return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
   	}
   }
    
    @RequestMapping(value = "/discussions/create", method = RequestMethod.POST, headers = {"Content-type=text/plain"})
    public Id createDiscussion(@RequestBody String payload) {
    	return discussion.createDiscussion(payload);
    }
}