package org.dmc.services.discussions;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/individual-discussion-comments-flagged", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussionCommentsFlaggedController {

	private static final String LOGTAG = IndividualDiscussionCommentsFlaggedController.class.getName();

	@RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity getIndividualDiscussionCommentsFlagged(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN,
			@RequestParam(value = "commentId", required = true) String commentId, @RequestParam(value = "accountId", required = true) String accountId) {
		final IndividualDiscussionCommentsFlaggedDao individualDiscussionCommentsFlaggedDao = new IndividualDiscussionCommentsFlaggedDao();
		try {
			ServiceLogger.log(LOGTAG, "In getIndividualDiscussionCommentsFlagged");
			return new ResponseEntity<IndividualDiscussionCommentFlagged>(individualDiscussionCommentsFlaggedDao.getCommentFlagged(commentId, accountId, userEPPN), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, consumes = { APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity postIndividualDiscussionCommentsFlagged(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN,
			@RequestBody IndividualDiscussionCommentFlagged individualDiscussionCommentFlagged) {
		final IndividualDiscussionCommentsFlaggedDao individualDiscussionCommentsFlaggedDao = new IndividualDiscussionCommentsFlaggedDao();
		try {
			ServiceLogger.log(LOGTAG, "In postIndividualDiscussionCommentsFlagged");
			return new ResponseEntity<IndividualDiscussionCommentFlagged>(individualDiscussionCommentsFlaggedDao.createFlagForComment(individualDiscussionCommentFlagged, userEPPN),
					HttpStatus.CREATED);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

}
