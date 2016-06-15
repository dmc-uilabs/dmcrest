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
@RequestMapping(value = "/individual-discussion-comments", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussionCommentsController {

	private final String logTag = IndividualDiscussionCommentsController.class.getName();
	
	@RequestMapping(value = "", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity postIndividualDiscussionComments(@RequestBody IndividualDiscussionComment discussionComment) {
		IndividualDiscussionDao individualDiscussionDao = new IndividualDiscussionDao();
		try {
			ServiceLogger.log(logTag, "In postIndividualDiscussionComments");
			return new ResponseEntity<IndividualDiscussionComment>(individualDiscussionDao.createIndividualDiscussionComment(discussionComment), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}
	}
	
	@RequestMapping(value = "", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<IndividualDiscussionComment> getIndividualDiscussionComments(@RequestBody IndividualDiscussionComment discussionComment) {
		// do some magic!
		return new ResponseEntity<IndividualDiscussionComment>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/{commentID}", produces = { "application/json", "text/html" }, method = RequestMethod.DELETE)
	public ResponseEntity<Void> individualDiscussionCommentsCommentIDDelete(@PathVariable("commentID") String commentID) {
		// do some magic!
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/{commentID}", produces = { "application/json", "text/html" }, method = RequestMethod.PATCH)
	public ResponseEntity<IndividualDiscussionComment> individualDiscussionCommentsCommentIDPatch(@PathVariable("commentID") String commentID,
			@RequestBody IndividualDiscussionComment comment) {
		// do some magic!
		return new ResponseEntity<IndividualDiscussionComment>(HttpStatus.NOT_IMPLEMENTED);
	}

}
