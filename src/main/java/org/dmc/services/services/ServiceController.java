package org.dmc.services.services;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.DocumentService;
import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.data.entities.PaymentPlan;
import org.dmc.services.payments.PaymentPlanService;
import org.dmc.services.projects.ProjectController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    private final static String LOGTAG = ServiceController.class.getName();

    private ServiceDao serviceDao = new ServiceDao();
    private ServiceTagsDao serviceTagsDao = new ServiceTagsDao();
    private ServiceSpecificationDao specificationDao = new ServiceSpecificationDao();

    @Inject
    private DocumentService documentService;

    @Inject
    private PaymentPlanService paymentPlanService;

    @Inject
    private ProjectController projectController;

    @RequestMapping(value = "/services/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getService(@PathVariable("id") int id,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        ServiceLogger.log(LOGTAG, "getService, id: " + id);
        try {
            return new ResponseEntity<Service>(serviceDao.getService(id, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServiceList(@RequestParam(value = "id", required = false) List<Integer> serviceIds) {
        try {
            ServiceLogger.log(LOGTAG, "In getServiceList");

            if(serviceIds != null) {
            	return new ResponseEntity<ArrayList<Service>>(serviceDao.getServiceListByIds(serviceIds), HttpStatus.OK);
            } else {
            	return new ResponseEntity<ArrayList<Service>>(serviceDao.getServiceList(), HttpStatus.OK);
            }
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/services/pay_plan")
    public ResponseEntity<?> getServicePaymentPlansForIds(@RequestParam("id") List<Integer> serviceIds,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
    	ServiceLogger.log(LOGTAG, "getServicePaymentPlansForIds");
    	try {
    		return new ResponseEntity<List<PaymentPlan>>(paymentPlanService.getPlans(serviceIds), HttpStatus.OK);
    	} catch (DMCServiceException e) {
    		ServiceLogger.logException(LOGTAG, e);
    		return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
    	}
    }

    @RequestMapping(value = "/services/{id}/pay_plan")
    public ResponseEntity<?> getServicePaymentPlans(@PathVariable("id") int id,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
    	ServiceLogger.log(LOGTAG, "getServicePaymentPlans, id: " + id);
    	try {
    		return new ResponseEntity<List<PaymentPlan>>(paymentPlanService.getPlans(id), HttpStatus.OK);
    	} catch (DMCServiceException e) {
    		ServiceLogger.logException(LOGTAG, e);
    		return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
    	}
    }

    @RequestMapping(value = "/projects/{projectId}/services", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServiceList(@PathVariable("projectId") int projectId) {
        try {
            ServiceLogger.log(LOGTAG, "In getServiceList, projectId = " + projectId);
            return new ResponseEntity<ArrayList<Service>>(serviceDao.getServiceList(projectId), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/defaultServices", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDefaultServiceList(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

        ServiceLogger.log(LOGTAG, "In getDefaultServiceList as user: " + userEPPN);

        try {
          int defaultProjectId = projectController.findOrCreateDefaultProject(userEPPN);
          return new ResponseEntity<ArrayList<Service>>(serviceDao.getServiceList(defaultProjectId), HttpStatus.OK);
        }
        catch (DMCServiceException e){
          ServiceLogger.logException(LOGTAG, e);
          return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/defaultServices/{parentId}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDefaultService(@PathVariable("parentId") int parentId,
                                               @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

        ServiceLogger.log(LOGTAG, "In getDefaultService as user: " + userEPPN);

        try {
          int defaultProjectId = projectController.findOrCreateDefaultProject(userEPPN);
          Integer existingServiceId = checkForExistingServiceInDefaultSpace(Integer.toString(defaultProjectId), Integer.toString(parentId));
          return new ResponseEntity<Service>(serviceDao.getService(existingServiceId, userEPPN), HttpStatus.OK);
        }
        catch (DMCServiceException e){
          ServiceLogger.logException(LOGTAG, e);
          return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/components/{componentId}/services", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getServiceByComponentList(@PathVariable("componentId") int componentId) {
        try {
            ServiceLogger.log(LOGTAG, "In getServiceByComponentList, componentId = " + componentId);
            return new ResponseEntity<ArrayList<Service>>(serviceDao.getServiceByComponentList(componentId),
                    HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/services", produces = { APPLICATION_JSON_VALUE, "text/html" }, method = RequestMethod.POST)
    public ResponseEntity<?> postService(@RequestBody Service body,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

        ServiceLogger.log(LOGTAG, "In createService");

        if (body.getProjectId() == null) {
          ServiceLogger.log(LOGTAG, "no projectId included for service create, userEPPN: " + userEPPN);
          try {
              String defaultProjectId = Integer.toString(projectController.findOrCreateDefaultProject(userEPPN));
              body.setProjectId(defaultProjectId);
          } catch (DMCServiceException e) {
              ServiceLogger.logException(LOGTAG, e);
              return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
          }

          Integer existingServiceId = checkForExistingServiceInDefaultSpace(body.getProjectId(), body.getParent());
          if (!existingServiceId.equals(0)) {
            ServiceLogger.log(LOGTAG, "service already exists in default space, returning existing service, userEPPN: " + userEPPN);
            return new ResponseEntity<Service>(serviceDao.getService(existingServiceId, userEPPN), HttpStatus.OK);
          }
        }


        try {
            return new ResponseEntity<Service>(serviceDao.createService(body, userEPPN), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/services/{serviceID}", produces = { APPLICATION_JSON_VALUE,
            TEXT_HTML_VALUE }, method = RequestMethod.PATCH)
    public ResponseEntity<?> servicesServiceIDPatch(@PathVariable("serviceID") String serviceID,
            @RequestBody Service service,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        try {
            ServiceLogger.log(LOGTAG, "In patchService, serviceID = " + serviceID);

            if (!serviceDao.userIsAuthorizedToUpdate(serviceID, userEPPN, service)) {
              throw new DMCServiceException(DMCError.NotAuthorizedToChange, "User: " + userEPPN + " is not allowed to update service: " + serviceID);
            } else {
              if (service.getPublished()) {
                documentService.makeDocsPublic(serviceID);
              }
              return new ResponseEntity<Service>(serviceDao.patchService(serviceID, service, userEPPN), HttpStatus.OK);
            }
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
            return new ResponseEntity<ErrorMessage>(error, e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/services/{serviceId}/service_authors", produces = {
            APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> servicesServiceIDServiceAuthorsGet(@PathVariable("serviceId") int serviceId,
            @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
        ServiceAuthorDao serviceAuthorDao = new ServiceAuthorDao();
        int httpStatusCode = HttpStatus.OK.value();
        ArrayList<ServiceAuthor> authors = null;

        try {
            authors = serviceAuthorDao.getServiceAuthors(serviceId, userEPPN);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }

        return new ResponseEntity<ArrayList<ServiceAuthor>>(authors, HttpStatus.valueOf(httpStatusCode));
    }

    @RequestMapping(value = "/services/{serviceID}/services_history", produces = { APPLICATION_JSON_VALUE,
            TEXT_HTML_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> servicesServiceIDServiceHistoryGet(@PathVariable("serviceID") String serviceID,
            @RequestParam(value = "period", required = false) String period,
            @RequestParam(value = "section", required = false) String section,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        // do some magic!
        ServiceLogger.log(LOGTAG, "getServiceHistory, id: " + serviceID);
        try {
            return new ResponseEntity<List<ServiceHistory>>(serviceDao.getHistory(serviceID, period, section, userEPPN),
                    HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    @RequestMapping(value = "/services/{serviceID}/service_tags", produces = { APPLICATION_JSON_VALUE,
            TEXT_HTML_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> servicesServiceIDServiceTagsGet(@PathVariable("serviceID") String serviceID,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        int statusCode = HttpStatus.OK.value();
        try {
            final List<ServiceTag> tags = serviceTagsDao.getServiceListByServiceId(Integer.parseInt(serviceID), userEPPN);
            return new ResponseEntity<List<ServiceTag>>(tags, HttpStatus.valueOf(statusCode));
        } catch (Exception ex) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(ex.getMessage()).build();
            return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
        }
    }

    @RequestMapping(value = "/services/{serviceID}/services_statistic", produces = { APPLICATION_JSON_VALUE,
            TEXT_HTML_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> servicesServiceIDServicesStatisticGet(
            @PathVariable("serviceID") String serviceID,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        final ServiceStatsDao ssd = new ServiceStatsDao();
        final List<ServiceStats> ssList = ssd.getServiceStats(serviceID);
        return new ResponseEntity<List<ServiceStats>>(ssList, HttpStatus.OK);
    }

    @RequestMapping(value = "/services/{serviceId}/dome-interfaces", produces = { APPLICATION_JSON_VALUE,
            TEXT_HTML_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> servicesServiceIdDomeInterfacesGet(@PathVariable("serviceId") BigDecimal serviceId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

        final DomeInterfacesDao domeInterfacesDao = new DomeInterfacesDao();

        try {
            ServiceLogger.log(LOGTAG, "In getServiceIdDomeInterfaces, serviceId = " + serviceId);
            return new ResponseEntity<List<GetDomeInterface>>(
                    domeInterfacesDao.getDomeInterfacesFromServiceId(serviceId, limit, order, sort), HttpStatus.OK);
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }

    }

    @RequestMapping(value = "/services/{serviceId}/input-positions", produces = {
            APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
    public ResponseEntity<?> servicesServiceIdInputPositionsGet(
            @PathVariable("serviceId") BigDecimal serviceId,
            @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        final InputPositionsDAO ipDao = new InputPositionsDAO();
        try {
            final List<ServiceInputsPositions> result = ipDao.getPositions(serviceId.intValue());
            return new ResponseEntity<List<ServiceInputsPositions>>(result, HttpStatus.OK);
        } catch (DMCServiceException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    /**
     * Create Service Specification
     *
     * @param video
     * @param userEPPN
     * @return
     */
    @RequestMapping(value = "/service/{serviceID}/specifications", method = RequestMethod.POST, produces = {
            APPLICATION_JSON_VALUE, TEXT_HTML_VALUE })
    public ResponseEntity<?> createServiceSpecification(@PathVariable("serviceID") String serviceIdText,
            @RequestBody ServiceSpecifications spec,
            @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {

        ServiceLogger.log(LOGTAG, "createServiceSpecification");
        final ArrayList<ServiceSpecifications> specs = new ArrayList<ServiceSpecifications>();
        specs.add(spec);

        try {
            int serviceId = -1;
            try {
                serviceId = Integer.parseInt(serviceIdText);
            } catch (NumberFormatException e) {
                throw new DMCServiceException(DMCError.ServiceInterfaceNotMatch,
                        "unable to parse " + serviceIdText + " as an integer");
            }
            final ArrayList<Integer> ids = specificationDao.createServiceSpecifications(serviceId, specs, userEPPN);
            return new ResponseEntity<ArrayList<Integer>>(ids, HttpStatus.valueOf(HttpStatus.OK.value()));
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    /**
     * Create Service Specifications
     *
     * @param video
     * @param userEPPN
     * @return
     */
    @RequestMapping(value = "/array_specifications", method = RequestMethod.POST, produces = { APPLICATION_JSON_VALUE,
            TEXT_HTML_VALUE })
    public ResponseEntity<?> createServiceSpecifications(@RequestBody ArrayList<ServiceSpecifications> specs,
            @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {

        ServiceLogger.log(LOGTAG, "createServiceSpecifications, Specs:" + specs);

        try {
            final ArrayList<Integer> ids = specificationDao.createServiceSpecifications(-1, specs, userEPPN);
            return new ResponseEntity<ArrayList<Integer>>(ids, HttpStatus.valueOf(HttpStatus.OK.value()));
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);

            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    /**
     * Update Service Specification
     *
     * @param video
     * @param userEPPN
     * @return
     */
    @RequestMapping(value = "/specifications/{specificationId}", method = RequestMethod.PATCH, produces = {
            APPLICATION_JSON_VALUE, TEXT_HTML_VALUE })
    @ResponseBody
    public ResponseEntity<?> updateServiceSpecification(@PathVariable("specificationId") int specId,
            @RequestBody ServiceSpecifications spec,
            @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {

        ServiceLogger.log(LOGTAG, "updateServiceSpecification");
        try {
            final Id id = specificationDao.updateServiceSpecification(specId, spec, userEPPN);
            return new ResponseEntity<Id>(id, HttpStatus.valueOf(HttpStatus.OK.value()));
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    /**
     * Retrieve service Specifications for A Services
     *
     * @param id
     * @param userEPPN
     * @return
     */
    @RequestMapping(value = "/services/{serviceId}/specifications", method = RequestMethod.GET, produces = {
            APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getServiceSpecifications(@RequestParam(value = "_limit", required = false) Integer limit,
            @RequestParam(value = "_order", required = false) String order,
            @RequestParam(value = "_sort", required = false) String sort, @PathVariable("serviceId") int id,
            @RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {

        ServiceLogger.log(LOGTAG, "getServiceSpecifications, userEPPN: " + userEPPN);
        ArrayList<ServiceSpecifications> specs = null;

        try {
            specs = specificationDao.getServiceSpecifications(id, limit, order, sort, userEPPN);
            return new ResponseEntity<ArrayList<ServiceSpecifications>>(specs,
                    HttpStatus.valueOf(HttpStatus.OK.value()));
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    /**
     * Retrieve service Specifications
     *
     * @param id
     * @param userEPPN
     * @return
     */
    @RequestMapping(value = "/array_specifications", method = RequestMethod.GET, produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getServiceSpecifications(
            @RequestHeader(value = "AJP_eppn", required = true) String userEPPN,
            @RequestParam(value = "_limit", defaultValue = "1000", required = false) Integer limit,
            @RequestParam(value = "_order", defaultValue = "DESC", required = false) String order,
            @RequestParam(value = "_sort", defaultValue = "id", required = false) String sort) {

        ServiceLogger.log(LOGTAG, "getServiceSpecifications, userEPPN: " + userEPPN);
        try {
            final ArrayList<ServiceSpecifications> specs = specificationDao.getServiceSpecifications(-1, limit, order, sort, userEPPN);
            return new ResponseEntity<ArrayList<ServiceSpecifications>>(specs,
                    HttpStatus.valueOf(HttpStatus.OK.value()));
        } catch (DMCServiceException e) {
            ServiceLogger.logException(LOGTAG, e);
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
    }

    private Integer checkForExistingServiceInDefaultSpace(String defaultWorkspaceId, String parentServiceId) {

      Integer workspaceId = Integer.parseInt(defaultWorkspaceId);
      ArrayList<Service> servicesInWorkspace = serviceDao.getServiceList(workspaceId);

      int count = 0;
      while (servicesInWorkspace.size() > count) {
         Service currentService = servicesInWorkspace.get(count);
         if (currentService.getParent().equals(parentServiceId)) {
           return currentService.getId();
         }
         count++;
      }

      return 0;
    }

}
