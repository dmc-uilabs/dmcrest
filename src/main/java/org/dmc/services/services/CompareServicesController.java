package org.dmc.services.services;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

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

	@RequestMapping(value = "/{id}", produces = { APPLICATION_JSON_VALUE, TEXT_HTML_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity<?> compareServicesIdDelete(@PathVariable("id") String id,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(LOGTAG, "In deleteCompareService: for id" + id + "as user " + userEPPN);
		try {
			compareServiceDao.deleteCompareService(id, userEPPN);
		} catch (DMCServiceException e) {
			ServiceLogger.log(LOGTAG, e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE, TEXT_HTML_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<?> compareServicesPost(@RequestBody PostCompareService body,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		try {
			GetCompareService compareService = new GetCompareService();
			compareService = compareServiceDao.createCompareService(body, userEPPN);
			return new ResponseEntity<GetCompareService>(compareService, HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(LOGTAG, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
	}

}
