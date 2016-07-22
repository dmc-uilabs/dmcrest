package org.dmc.services.services;

import java.util.ArrayList;

import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.discussions.DiscussionListDao;
import org.dmc.services.projects.Project;
import org.dmc.services.projects.ProjectController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/service_authors", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceAuthorsController {

	private final String logTag = ServiceAuthorsController.class.getName();
	private ServiceAuthorDao serviceAuthorDao = new ServiceAuthorDao();
	
//	@RequestMapping(value = "/{autorhId}", produces = { "application/json",
//			"text/html" }, method = RequestMethod.DELETE)
//	public ResponseEntity<Void> serviceAuthorsAutorhIdDelete(@PathVariable("autorhId") Integer autorhId) {
//		// do some magic!
//		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
//	}

	/**
	 * Create Service Author
	 * @param author
	 * @param userEPPN
	 * @return
	 */
	@RequestMapping(value = "/service_authors", method = RequestMethod.POST, produces = { APPLICATION_JSON_VALUE })
	public ResponseEntity serviceAuthorsPost(@RequestBody ServiceAuthor author, @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
		int httpStatusCode = HttpStatus.OK.value();
		Id createdId = null;
		
		try {
			createdId = serviceAuthorDao.createServiceAuthor(author, userEPPN);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}

		return new ResponseEntity<Id>(createdId, HttpStatus.valueOf(httpStatusCode));
	}

	/**
	 * Get Service Authors
	 * @param author
	 * @param userEPPN
	 * @return
	 */
/*	@RequestMapping(value = "/services/{id}/service_authors", method = RequestMethod.GET, produces = { APPLICATION_JSON_VALUE })
	public ResponseEntity serviceAuthorsGet(@PathVariable("id") int serviceId,  @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
		int httpStatusCode = HttpStatus.OK.value();
		ArrayList<ServiceAuthor> authors = null;
		
		try {
			authors = serviceAuthorDao.getServiceAuthors(serviceId, userEPPN);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}

		return new ResponseEntity<ArrayList<ServiceAuthor>>(authors, HttpStatus.valueOf(httpStatusCode));
	}*/
	
	/**
	 * Delete Service Author
	 * @param author
	 * @param userEPPN
	 * @return
	 */
	@RequestMapping(value = "/service_authors", method = RequestMethod.DELETE, produces = { APPLICATION_JSON_VALUE })
	public ResponseEntity serviceAuthorsDelete(@PathVariable("id") int authorId, @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
		int httpStatusCode = HttpStatus.OK.value();
		Id deletedId = null;
		
		try {
			deletedId = serviceAuthorDao.deleteServiceAuthor(authorId, userEPPN);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}

		return new ResponseEntity<Id>(deletedId, HttpStatus.valueOf(httpStatusCode));
	}
}
