package org.dmc.services.services;

import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;

import org.dmc.services.ServiceLogger;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;

import static org.springframework.http.MediaType.*;

@Controller
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class DomeAPIController {

	private final String logTag = DomeAPIController.class.getName();
	private DomeInterfacesDao domeInterfacesDao = new DomeInterfacesDao();

	@RequestMapping(value = "/get-children", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity childrenGet(@RequestBody PostUpdateDomeInterface postUpdateDomeInterface,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

		ServiceLogger.log(logTag, "In childrenGet: as user " + userEPPN);

		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
		
		/*try {
			return new ResponseEntity<GetDomeInterface>(
					domeInterfacesDao.createDomeInterface(postUpdateDomeInterface, userEPPN), HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}*/

	}
}
