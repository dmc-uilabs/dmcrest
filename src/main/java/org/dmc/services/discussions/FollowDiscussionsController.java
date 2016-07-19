package org.dmc.services.discussions;

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
@RequestMapping(value = "/follow_discussions", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class FollowDiscussionsController {

	private static final String LOGTAG = FollowDiscussionsController.class.getName();

	@RequestMapping(value = "/{followID}", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity deleteFollowDiscussionsFromFollowID(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN,
			@PathVariable("followID") String followID) {
		final FollowDiscussionsDao followDiscussionsDao = new FollowDiscussionsDao();
		try {
			ServiceLogger.log(LOGTAG, "In deleteFollowDiscussionsFromFollowID");
			followDiscussionsDao.deleteFollowDiscussion(followID, userEPPN);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity postFollowDiscussions(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN,
			@RequestBody FollowingIndividualDiscussion followingDiscussion) {
		final FollowDiscussionsDao followDiscussionsDao = new FollowDiscussionsDao();
		try {
			ServiceLogger.log(LOGTAG, "In postFollowDiscussions");
			return new ResponseEntity<FollowingIndividualDiscussion>(followDiscussionsDao.createFollowDiscussion(followingDiscussion, userEPPN), HttpStatus.CREATED);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

}
