package org.dmc.services.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.projects.ProjectJoinRequest;
import org.dmc.services.services.specifications.Specification;
import org.dmc.services.services.specifications.SpecificationDao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

	private final String logTag = ServiceController.class.getName();

	private ServiceDao serviceDao = new ServiceDao();

	@RequestMapping(value = "/services/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getService(@PathVariable("id") int id,
	        @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(logTag, "getService, id: " + id);
		try {
		    return new ResponseEntity<Service>(serviceDao.getService(id, userEPPN), HttpStatus.OK);
		} catch (DMCServiceException e) {
		    ServiceLogger.logException(logTag, e);
		    return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}
	}

	private SpecificationDao specSearch = new SpecificationDao();

	@RequestMapping(value = "/services/{serviceID}/specifications", method = RequestMethod.GET)
	public Specification getSpecification(@PathVariable("serviceID") int serviceID) {
		ServiceLogger.log(logTag, "In getSpecification");
		ServiceLogger.log(logTag, "In getSpecification, serviceID: " + serviceID);
		return specSearch.getSpecification(serviceID);
	}

	private ServiceListDao serviceListDao = new ServiceListDao();

	@RequestMapping(value = "/services", method = RequestMethod.GET)
	public ArrayList<Service> getServiceList() {
		ServiceLogger.log(logTag, "getServiceList ");
		return serviceListDao.getServiceList();
	}

	@RequestMapping(value = "/projects/{projectId}/services", method = RequestMethod.GET)
	public ArrayList<Service> getServiceList(@PathVariable("projectId") int projectId) {
		ServiceLogger.log(logTag, "In getServiceList, projectId = " + projectId);
		return serviceListDao.getServiceList(projectId);
	}

	@RequestMapping(value = "/components/{componentId}/services", method = RequestMethod.GET)
	public ArrayList<Service> getServiceByComponentList(@PathVariable("componentId") int componentId) {
		ServiceLogger.log(logTag, "In getServiceByComponentList, componentId = " + componentId);
		return serviceListDao.getServiceByComponentList(componentId);
	}

	@RequestMapping(value = "/services", produces = { "application/json", "text/html" }, method = RequestMethod.POST)
	public ResponseEntity<?> postService(@RequestBody Service body,
	        @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        try {
            ServiceLogger.log(logTag, "In createService");
            return new ResponseEntity<Service>(serviceDao.createService(body, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
        }
	}

	@RequestMapping(value = "/services/{serviceID}", produces = { "application/json",
			"text/html" }, method = RequestMethod.PATCH)
	public ResponseEntity<?> servicesServiceIDPatch(@PathVariable("serviceID") String serviceID,
			@RequestBody Service service,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        try {
            ServiceLogger.log(logTag, "In patchService, serviceID = " + serviceID);
            return new ResponseEntity<Service>(serviceDao.patchService(serviceID, service, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(logTag, e);
            return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
        }
	}

	@RequestMapping(value = "/services/{serviceID}/service_authors", produces = { "application/json",
			"text/html" }, method = RequestMethod.GET)
	public ResponseEntity<List<ServiceAuthor>> servicesServiceIDServiceAuthorsGet(
			@PathVariable("serviceID") String serviceID) {
		// do some magic!
		return new ResponseEntity<List<ServiceAuthor>>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/services/{serviceID}/service_documents", produces = { "application/json",
			"text/html" }, method = RequestMethod.GET)
	public ResponseEntity<List<ServiceDocument>> servicesServiceIDServiceDocumentsGet(
			@PathVariable("serviceID") String serviceID,
			@RequestParam(value = "serviceDocumentId", required = false) String serviceDocumentId,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "sort", required = false) String sort) {
		// do some magic!
		return new ResponseEntity<List<ServiceDocument>>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/services/{serviceID}/service_history", produces = { "application/json",
			"text/html" }, method = RequestMethod.GET)
	public ResponseEntity<List<ServiceHistory>> servicesServiceIDServiceHistoryGet(
			@PathVariable("serviceID") String serviceID,
			@RequestParam(value = "period", required = false) String period,
			@RequestParam(value = "section", required = false) String section) {
		// do some magic!
		return new ResponseEntity<List<ServiceHistory>>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/services/{serviceID}/service_images", produces = { "application/json",
			"text/html" }, method = RequestMethod.GET)
	public ResponseEntity<List<ServiceImages>> servicesServiceIDServiceImagesGet(
			@PathVariable("serviceID") String serviceID) {
		// do some magic!
		return new ResponseEntity<List<ServiceImages>>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/services/{serviceID}/service_tags", produces = { "application/json",
			"text/html" }, method = RequestMethod.GET)
	public ResponseEntity<List<ServiceTag>> servicesServiceIDServiceTagsGet(@PathVariable("serviceID") String serviceID,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "sort", required = false) String sort) {
		// do some magic!
		return new ResponseEntity<List<ServiceTag>>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/services/{serviceID}/services_statistic", produces = { "application/json",
			"text/html" }, method = RequestMethod.GET)
	public ResponseEntity<List<ServiceStats>> servicesServiceIDServicesStatisticGet(
			@PathVariable("serviceID") String serviceID) {
		// do some magic!
		return new ResponseEntity<List<ServiceStats>>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/services/{serviceId}/dome-interfaces", produces = { "application/json",
			"text/html" }, method = RequestMethod.GET)
	public ResponseEntity<GetDomeInterface> servicesServiceIdDomeInterfacesGet(
			@PathVariable("serviceId") BigDecimal serviceId,
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "sort", required = false) String sort) {
		// do some magic!
		return new ResponseEntity<GetDomeInterface>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/services/{serviceId}/input-positions", produces = { "application/json",
			"text/html" }, method = RequestMethod.GET)
	public ResponseEntity<List<ServiceInputsPositions>> servicesServiceIdInputPositionsGet(
			@PathVariable("serviceId") BigDecimal serviceId) {
		// do some magic!
		return new ResponseEntity<List<ServiceInputsPositions>>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/service/{serviceID}/specifications", produces = { "application/json",
			"text/html" }, method = RequestMethod.POST)
	public ResponseEntity<Void> postServiceSpecification(@PathVariable("serviceID") String serviceID,
			@RequestBody ServiceSpecifications body) {
		// do some magic!
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

}