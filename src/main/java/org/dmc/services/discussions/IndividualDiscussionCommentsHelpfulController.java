package org.dmc.services.discussions;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/individual-discussion-comments-helpful", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussionCommentsHelpfulController {

	private final String logTag = IndividualDiscussionCommentsHelpfulController.class.getName();
	private IndividualDiscussionCommentsHelpfulDao commentsHelpfulDao = new IndividualDiscussionCommentsHelpfulDao();

	@RequestMapping(value = "", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity getIndividualDiscussionCommentsHelpful(@RequestParam(value = "commentId", required = true) String commentId,
			@RequestParam(value = "accountId", required = true) String accountId) {
		try {
			ServiceLogger.log(logTag, "In getIndividualDiscussionCommentsHelpful");
			return new ResponseEntity<IndividualDiscussionCommentHelpful>(commentsHelpfulDao.getIndividualDiscussionCommentHelpful(commentId, accountId), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity postIndividualDiscussionCommentsHelpful(@RequestBody IndividualDiscussionCommentHelpful individualDiscussionCommentHelpful) {
		try {
			ServiceLogger.log(logTag, "In postIndividualDiscussionCommentsHelpful");
			return new ResponseEntity<IndividualDiscussionCommentHelpful>(commentsHelpfulDao.createIndividualDiscussionCommentHelpful(individualDiscussionCommentHelpful),
					HttpStatus.CREATED);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "/{helpfulID}", produces = { "application/json", "text/html" }, method = RequestMethod.PATCH)
	public ResponseEntity patchIndividualDiscussionCommentsHelpfulHelpfulID(@PathVariable("helpfulID") String helpfulID, @RequestBody IndividualDiscussionCommentHelpful helpful) {
		try {
			ServiceLogger.log(logTag, "In patchIndividualDiscussionCommentsHelpfulHelpfulID");
			return new ResponseEntity<IndividualDiscussionCommentHelpful>(commentsHelpfulDao.updateIndividualDiscussionCommentHelpful(helpfulID, helpful), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

}
