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
@RequestMapping(value = "/dome-interfaces", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class DomeInterfacesController {
	
	private final String logTag = DomeInterfacesController.class.getName();
	private DomeInterfacesDao domeInterfacesDao = new DomeInterfacesDao();

	
	@RequestMapping(value = "", produces = { "application/json" }, method = RequestMethod.POST)
	public ResponseEntity domeInterfacesPost(@RequestBody PostUpdateDomeInterface postUpdateDomeInterface,
															   @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN){
		
//		ServiceLogger.log(logTag, "In domeInterfacesPost: as user " + userEPPN);
//		
//		try {
//			return new ResponseEntity<GetDomeInterface>(domeInterfacesDao.createDomeInterface(postUpdateDomeInterface, userEPPN), HttpStatus.OK);
//		} catch (DMCServiceException e) {
//			ServiceLogger.logException(logTag, e);
//			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
//		}
		// do some magic!
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	
	@RequestMapping(value = "/{domeInterfaceId}", produces = { "application/json" }, method = RequestMethod.DELETE)
	public ResponseEntity<Void> domeInterfacesDomeInterfaceIdDelete(@PathVariable("domeInterfaceId") BigDecimal domeInterfaceId){
		// do some magic!
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	
	
	@RequestMapping(value = "/{domeInterfaceId}", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<GetDomeInterface> domeInterfacesDomeInterfaceIdGet(@PathVariable("domeInterfaceId") BigDecimal domeInterfaceId){
		// do some magic!
		return new ResponseEntity<GetDomeInterface>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	
	
	@RequestMapping(value = "/{domeInterfaceId}", produces = { "application/json" }, method = RequestMethod.PATCH)
	public ResponseEntity<GetDomeInterface> domeInterfacesDomeInterfaceIdPatch(@PathVariable("domeInterfaceId") BigDecimal domeInterfaceId,
																			   @RequestBody PostUpdateDomeInterface domeInterface){
		// do some magic!
		return new ResponseEntity<GetDomeInterface>(HttpStatus.NOT_IMPLEMENTED);
	}
}
