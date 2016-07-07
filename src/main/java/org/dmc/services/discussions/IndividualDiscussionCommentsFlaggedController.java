package org.dmc.services.discussions;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/individual-discussion-comments-flagged", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussionCommentsFlaggedController {
	
	private final String logTag = IndividualDiscussionCommentsFlaggedController.class.getName();

	@RequestMapping(value = "", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity getIndividualDiscussionCommentsFlagged(@RequestParam(value = "commentId", required = true) String commentId,
			@RequestParam(value = "accountId", required = true) String accountId) {
		IndividualDiscussionCommentsFlaggedDao individualDiscussionCommentsFlaggedDao = new IndividualDiscussionCommentsFlaggedDao();
		try {
			ServiceLogger.log(logTag, "In getIndividualDiscussionCommentsFlagged");
			return new ResponseEntity<IndividualDiscussionCommentFlagged>(individualDiscussionCommentsFlaggedDao.getCommentFlagged(commentId, accountId), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity postIndividualDiscussionCommentsFlagged(
			@RequestBody IndividualDiscussionCommentFlagged individualDiscussionCommentFlagged) {
		IndividualDiscussionCommentsFlaggedDao individualDiscussionCommentsFlaggedDao = new IndividualDiscussionCommentsFlaggedDao();
		try {
			ServiceLogger.log(logTag, "In postIndividualDiscussionCommentsFlagged");
			return new ResponseEntity<IndividualDiscussionCommentFlagged>(individualDiscussionCommentsFlaggedDao.createFlagForComment(individualDiscussionCommentFlagged), HttpStatus.CREATED);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

}
