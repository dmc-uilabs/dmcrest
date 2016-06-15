package org.dmc.services.discussions;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.services.GetDomeInterface;
import org.dmc.services.services.ServiceController;
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
@RequestMapping(value = "/individual-discussion", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussionController {

	private final String logTag = IndividualDiscussionController.class.getName();

	@RequestMapping(value = "", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity getIndividualDiscussion(
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "sort", required = false) String sort) {

		IndividualDiscussionDao individualDiscussionDao = new IndividualDiscussionDao();
		try {
			ServiceLogger.log(logTag, "In getIndividualDiscussion");
			return new ResponseEntity<List<IndividualDiscussion>>(individualDiscussionDao.getListOfCommunityIndividualDiscussions(limit, order, sort), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity postIndividualDiscussion(@RequestBody IndividualDiscussion discussion) {
		IndividualDiscussionDao individualDiscussionDao = new IndividualDiscussionDao();
		try {
			ServiceLogger.log(logTag, "In postIndividualDiscussion");
			return new ResponseEntity<IndividualDiscussion>(individualDiscussionDao.createIndividualDiscussion(discussion), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "/{individualDiscussionID}/individual-discussion-comments", produces = { "application/json",
			"text/html" }, method = RequestMethod.GET)
	public ResponseEntity<List<IndividualDiscussionComment>> individualDiscussionIndividualDiscussionIDIndividualDiscussionCommentsGet(
			@PathVariable("individualDiscussionID") String individualDiscussionID,
			@RequestParam(value = "commentId", required = true) String commentId, @RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "sort", required = false) String sort) {
		// do some magic!
		return new ResponseEntity<List<IndividualDiscussionComment>>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/{individualDiscussionID}/individual-discussion-tags", produces = { "application/json",
			"text/html" }, method = RequestMethod.GET)
	public ResponseEntity<List<IndividualDiscussionTag>> individualDiscussionIndividualDiscussionIDIndividualDiscussionTagsGet(
			@PathVariable("individualDiscussionID") String individualDiscussionID, @RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "order", required = false) String order, @RequestParam(value = "sort", required = false) String sort) {
		// do some magic!
		return new ResponseEntity<List<IndividualDiscussionTag>>(HttpStatus.NOT_IMPLEMENTED);
	}

}
