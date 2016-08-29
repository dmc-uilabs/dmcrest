package org.dmc.services.services;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping(value = "/compare_services", produces = { "application/json", "text/html" })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-08-24T16:43:27.555-04:00")
public class CompareServicesController {
	private static final String LOGTAG = CompareServicesController.class.getName();
	private CompareServicesDao compareServiceDao = new CompareServicesDao();

	@RequestMapping(value = "/{id}", produces = { "application/json", "text/html" }, method = RequestMethod.DELETE)
	public ResponseEntity<Void> compareServicesIdDelete(
			@PathVariable("id") String id

	){
		// do some magic!
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "", produces = { "application/json", "text/html" }, method = RequestMethod.POST)
	public ResponseEntity<?> compareServicesPost(
			@RequestBody PostCompareService body,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN){
		int httpStatusCode = HttpStatus.OK.value();
		GetCompareService compareService = new GetCompareService();
		try {
			compareService = compareServiceDao.createCompareService(body, userEPPN);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}

		return new ResponseEntity<GetCompareService>(compareService, HttpStatus.valueOf(httpStatusCode));
	}

}
