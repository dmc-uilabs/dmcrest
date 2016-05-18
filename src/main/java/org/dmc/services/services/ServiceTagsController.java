package org.dmc.services.services;

import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.projects.ProjectController;
import org.dmc.services.projects.ProjectCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by 200005921 on 5/12/2016.
 */

@RestController
public class ServiceTagsController {

    private final String logTag = ServiceTagsController.class.getName();

    // Implemented by ServiceController
    //    GET /services/{serviceID}/service_tags

    // These immplemented below
    //    GET /service_tags
    //    POST /service_tags
    //    DELETE /service_tags/{serviceTagID}

    private ServiceTagsDao serviceTagsDao = new ServiceTagsDao();

    @RequestMapping(value = "/service_tags", method = RequestMethod.GET, produces = { APPLICATION_JSON_VALUE })
    public ResponseEntity getServiceTags (@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        List<ServiceTag> tags = null;
        int statusCode = HttpStatus.OK.value();

        try {
            tags = serviceTagsDao.getServiceList(userEPPN);
        } catch (Exception ex) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(ex.getMessage()).build();
            return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
        }
        return new ResponseEntity<List<ServiceTag>>(tags, HttpStatus.valueOf(statusCode));
    }
/*
    @RequestMapping(value = "/service_tags", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseBody
    public ResponseEntity insertServiceTag (@RequestBody ServiceTag payload, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
        ServiceLogger.log(logTag, "In insertServiceTag: " + payload + " as user " + userEPPN);
        int statusCode = HttpStatus.OK.value();
        Id id = null;
        try {
            id = serviceTagsDao.insertServiceTag(payload, userEPPN);
        }
        catch (Exception ex) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(ex.getMessage()).build();
            return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));

        }
        return new ResponseEntity<Id>(id, HttpStatus.OK);
    }*/

    @RequestMapping(value = "/service_tags/{serviceTagID}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteServiceTag(@PathVariable("serviceTagID") int serviceTagID, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN)  {
        ServiceLogger.log(logTag, "In deleteServiceTag: for id " + serviceTagID + " as user " + userEPPN);

        try {

            boolean ok = serviceTagsDao.deleteServiceTag(serviceTagID, userEPPN);
            if (ok) {
                return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<ErrorMessage>(new ErrorMessage("failure to delete service tag"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            ServiceLogger.log(logTag, "caught exception: for id " + serviceTagID + " as user " + userEPPN + " " + e.getMessage());

            if (e.getMessage().equals("you are not allowed to delete this service tag")) {
                return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.UNAUTHORIZED);
            } else if (e.getMessage().equals("invalid id")) {
                return new ResponseEntity<ErrorMessage>(new ErrorMessage(e.getMessage()), HttpStatus.FORBIDDEN);
            } else {
                int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
                ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
                return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
            }
        }

    }

}
