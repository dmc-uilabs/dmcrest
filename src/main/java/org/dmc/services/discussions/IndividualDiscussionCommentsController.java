package org.dmc.services.discussions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity getIndividualDiscussionComments(@RequestParam(value = "_limit", required = false) Integer limit,
			@RequestParam(value = "_order", required = false) String order, @RequestParam(value = "_sort", required = false) String sort,
			@RequestParam(value = "commentId", required = true) String commentId,
			@RequestParam(value = "individual-discussionId", required = false) ArrayList<String> individualDiscussionIdList) {
		IndividualDiscussionDao individualDiscussionDao = new IndividualDiscussionDao();
		try {
			ServiceLogger.log(logTag, "In getIndividualDiscussionComments");
			return new ResponseEntity<List<IndividualDiscussionComment>>(
					individualDiscussionDao.getListOfIndividualDiscussionComments(limit, order, sort, commentId, individualDiscussionIdList), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "/{commentID}", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity individualDiscussionCommentsCommentIDDelete(@PathVariable("commentID") String commentID) {
		IndividualDiscussionDao individualDiscussionDao = new IndividualDiscussionDao();
		try {
			ServiceLogger.log(logTag, "In getIndividualDiscussionComments");
			return new ResponseEntity<IndividualDiscussionComment>(individualDiscussionDao.getCommentFromId(commentID), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "/{commentID}", produces = { "application/json" }, method = RequestMethod.PATCH)
	public ResponseEntity individualDiscussionCommentsCommentIDPatch(@PathVariable("commentID") BigDecimal commentID, @RequestBody IndividualDiscussionComment comment) {
		IndividualDiscussionDao individualDiscussionDao = new IndividualDiscussionDao();
		try {
			ServiceLogger.log(logTag, "In getIndividualDiscussionComments");
			return new ResponseEntity<IndividualDiscussionComment>(individualDiscussionDao.updateIndividualDiscussionComment(commentID, comment), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

}
