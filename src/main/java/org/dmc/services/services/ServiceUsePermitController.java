package org.dmc.services.services;

import java.util.List;

import org.dmc.services.ServiceLogger;
import org.dmc.services.ServiceUsePermitService;
import org.dmc.services.data.models.ServiceUsePermitModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/service_permits")
public class ServiceUsePermitController {
	
	private final String logTag = ServiceUsePermitController.class.getName();
	
	@Autowired
	private ServiceUsePermitService supService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getServicePermitById(@PathVariable("id") Integer id) {
		ServiceLogger.log(logTag, "Retrieving service use permit by id: " + id);
		return new ResponseEntity<ServiceUsePermitModel>(supService.getServiceUsePermit(id), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/service/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getServicePermitByServiceId(@PathVariable("id") Integer id) {
		ServiceLogger.log(logTag, "Retrieving service use permits by service: " + id);
		return new ResponseEntity<List<ServiceUsePermitModel>>(supService.getServiceUsePermitByServiceId(id), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/organization/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getServicePermitByOrgId(@PathVariable("id") Integer id) {
		ServiceLogger.log(logTag, "Retrieving service use permits by organization: " + id);
		return new ResponseEntity<List<ServiceUsePermitModel>>(supService.getServiceUsePermitByOrgId(id), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/organization/service/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getServicePermitByOrgIdAndServiceId(@PathVariable("id") Integer id) {
		ServiceLogger.log(logTag, "Retrieving service use permit for current organization with service id: " + id);
		return new ResponseEntity<ServiceUsePermitModel>(supService.getServiceUsePermitByOrganizationIdAndServiceId(id), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/check/service/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> checkServicePermit(@PathVariable("id") Integer id,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(logTag, "Checking service use permit with current org for service: " + id + " and user: " + userEPPN);
		return new ResponseEntity<Boolean>(supService.checkOrgServicePermit(id), HttpStatus.OK);
	}

}
