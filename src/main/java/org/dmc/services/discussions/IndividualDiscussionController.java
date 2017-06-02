package org.dmc.services.discussions;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.services.GetDomeInterface;
import org.dmc.services.services.ServiceController;
import org.dmc.services.data.dao.user.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.sql.SQLException;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/individual-discussion", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class IndividualDiscussionController {

	private static final String LOGTAG = IndividualDiscussionController.class.getName();

	@RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity getIndividualDiscussion(@RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "sort", required = false) String sort) {
		final IndividualDiscussionDao individualDiscussionDao = new IndividualDiscussionDao();
		try {
			ServiceLogger.log(LOGTAG, "In getIndividualDiscussion");
			return new ResponseEntity<List<IndividualDiscussion>>(individualDiscussionDao.getListOfCommunityIndividualDiscussions(limit, order, sort), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity postIndividualDiscussion(@RequestBody IndividualDiscussion discussion) {
		final IndividualDiscussionDao individualDiscussionDao = new IndividualDiscussionDao();
		try {
			ServiceLogger.log(LOGTAG, "In postIndividualDiscussion as user " + UserDao.getUserName(discussion.getAccountId().intValue()) + " in projectId: " + discussion.getProjectId());
			return new ResponseEntity<IndividualDiscussion>(individualDiscussionDao.createIndividualDiscussion(discussion), HttpStatus.CREATED);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		} catch (SQLException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{individualDiscussionID}", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity getSingleIndividualDiscussion(@PathVariable("individualDiscussionID") String individualDiscussionID) {
		final IndividualDiscussionDao individualDiscussionDao = new IndividualDiscussionDao();
		try {
			ServiceLogger.log(LOGTAG, "In getSingleIndividualDiscussion");
			return new ResponseEntity<IndividualDiscussion>(individualDiscussionDao.getSingleIndividualDiscussionFromId(individualDiscussionID), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "/{individualDiscussionID}/individual-discussion-comments", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity getIndividualDiscussionCommentsForSingleIndividualDiscussion(@PathVariable("individualDiscussionID") String individualDiscussionID,
			@RequestParam(value = "commentId", required = true) String commentId, @RequestParam(value = "_order", required = false) String order,
			@RequestParam(value = "_sort", required = false) String sort, @RequestParam(value = "_limit", required = false) Integer limit) {
		final IndividualDiscussionDao individualDiscussionDao = new IndividualDiscussionDao();
		try {
			ServiceLogger.log(LOGTAG, "In getIndividualDiscussionCommentsForSingleIndividualDiscussion");
			return new ResponseEntity<List<IndividualDiscussionComment>>(
					individualDiscussionDao.getCommentsForSingleDiscussionId(limit, order, sort, commentId, individualDiscussionID), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "/{individualDiscussionID}/individual-discussion-tags", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity getIndividualDiscussionTagsFromIndividualDiscussionId(@PathVariable("individualDiscussionID") String individualDiscussionID,
			@RequestParam(value = "_limit", required = false) Integer limit, @RequestParam(value = "_order", required = false) String order,
			@RequestParam(value = "_sort", required = false) String sort) {
		final IndividualDiscussionTagsDao individualDiscussionTagsDao = new IndividualDiscussionTagsDao();
		try {
			ServiceLogger.log(LOGTAG, "In getIndividualDiscussionTagsFromIndividualDiscussionId");
			return new ResponseEntity<List<IndividualDiscussionTag>>(individualDiscussionTagsDao.getTagsForSingleDiscussionId(limit, order, sort, individualDiscussionID),
					HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

}
