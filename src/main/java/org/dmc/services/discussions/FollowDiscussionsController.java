package org.dmc.services.discussions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.*;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;

@Controller
@RequestMapping(value = "/follow_discussions", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class FollowDiscussionsController {
	
	private final String logTag = FollowDiscussionsController.class.getName();

	@RequestMapping(value = "/{followID}", produces = { "application/json" }, method = RequestMethod.DELETE)
	public ResponseEntity deleteFollowDiscussionsFromFollowID(@PathVariable("followID") String followID) {
		FollowDiscussionsDao followDiscussionsDao = new FollowDiscussionsDao();
		try {
			ServiceLogger.log(logTag, "In deleteFollowDiscussionsFromFollowID");
			followDiscussionsDao.deleteFollowDiscussion(followID);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity postFollowDiscussions(@RequestBody FollowingIndividualDiscussion followingDiscussion) {
		FollowDiscussionsDao followDiscussionsDao = new FollowDiscussionsDao();
		try {
			ServiceLogger.log(logTag, "In postFollowDiscussions");
			return new ResponseEntity<FollowingIndividualDiscussion>(followDiscussionsDao.createFollowDiscussion(followingDiscussion), HttpStatus.CREATED);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

}
