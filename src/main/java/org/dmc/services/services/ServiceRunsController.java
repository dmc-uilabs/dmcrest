package org.dmc.services.services;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.discussions.IndividualDiscussionComment;
import org.dmc.services.discussions.IndividualDiscussionDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.springframework.http.MediaType.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/service_runs", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ServiceRunsController {

	private static final String LOGTAG = ServiceRunsController.class.getName();

	@RequestMapping(value = "/{id}", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity<Void> serviceRunsIdDelete(@PathVariable("id") String id) {
		// do some magic!
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/{id}", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity getServiceRunsFromId(@PathVariable("id") String id) {
		final ServiceRunsDao serviceRunsDao = new ServiceRunsDao();
		try {
			ServiceLogger.log(LOGTAG, "In getServiceRunsFromId");
			return new ResponseEntity<GetServiceRun>(serviceRunsDao.getSingleServiceRun(id), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity getServiceRuns(@RequestParam(value = "_limit", required = false) Integer limit, @RequestParam(value = "_order", required = false) String order,
			@RequestParam(value = "_sort", required = false) String sort, @RequestParam(value = "serviceId", required = false) ArrayList<String> serviceIdsList) {
		final ServiceRunsDao serviceRunsDao = new ServiceRunsDao();
		try {
			ServiceLogger.log(LOGTAG, "In getServiceRuns");
			return new ResponseEntity<List<GetServiceRun>>(serviceRunsDao.getListOfServiceRuns(limit, order, sort, serviceIdsList), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "/{id}", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
	public ResponseEntity patchServiceRun(@PathVariable("id") String id, @RequestBody GetServiceRun body) {
		final ServiceRunsDao serviceRunsDao = new ServiceRunsDao();
		try {
			ServiceLogger.log(LOGTAG, "In patchServiceRun");
			return new ResponseEntity<GetServiceRun>(serviceRunsDao.updateServiceRun(id, body), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

	@RequestMapping(value = "/cancel_run/{serviceID}", produces = { APPLICATION_JSON_VALUE, "text/html" }, method = RequestMethod.POST)
	public ResponseEntity<?> cancelServiceRun(@PathVariable("serviceID") String serviceID,
					@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		final ServiceRunsDao serviceRunsDao = new ServiceRunsDao();
			try {
					ServiceLogger.log(LOGTAG, "In cancelServiceRun");
					serviceRunsDao.cancelServiceRun(serviceID);

					return new ResponseEntity<String>("This worked", HttpStatus.OK);
			} catch (DMCServiceException e) {
					ServiceLogger.logException(LOGTAG, e);
					return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
			}
	}

}
