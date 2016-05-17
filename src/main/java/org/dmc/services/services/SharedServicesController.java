package org.dmc.services.services;


import org.dmc.services.ErrorMessage;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/shared-services", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class SharedServicesController {

    private final String logTag = SharedServicesController.class.getName();

    private SharedServicesDao sharedServicesDao = new SharedServicesDao();

    @RequestMapping(value = "", produces = { "application/json", "text/html" },  method = RequestMethod.GET)
    public ResponseEntity<?> sharedServiceGetAll(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN){
        // do some magic!
        ServiceLogger.log(logTag, "In sharedServiceGetAll: " + " as user " + userEPPN);

        List<SharedService> services = null;
        int statusCode = HttpStatus.OK.value();
        try {
            services = sharedServicesDao.getSharedServices(userEPPN);
        } catch (Exception ex) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(ex.getMessage()).build();
            return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
        }
        return new ResponseEntity<List<SharedService>>(services, HttpStatus.valueOf(statusCode));
    }

  @RequestMapping(value = "/{id}", produces = { "application/json", "text/html" },  method = RequestMethod.GET)
  public ResponseEntity<?> sharedServiceGetById(@PathVariable("id") int id, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN){
      // do some magic!
      ServiceLogger.log(logTag, "In sharedServiceGetById: " + "id=" + id + " as user " + userEPPN);

      List<SharedService> services = null;
      int statusCode = HttpStatus.OK.value();
      try {
          services = sharedServicesDao.getSharedServicesById(id, userEPPN);
      } catch (Exception ex) {
          statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
          ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(ex.getMessage()).build();
          return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
      }
      return new ResponseEntity<List<SharedService>>(services, HttpStatus.valueOf(statusCode));
  }

  @RequestMapping(value = "",  produces = { "application/json", "text/html" }, method = RequestMethod.POST)
  public ResponseEntity<?> sharedServicesPost( @RequestBody PostSharedService payload, @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN){
      // do some magic!
      ServiceLogger.log(logTag, "In sharedServicesPost: " + payload + " as user " + userEPPN);

      int statusCode = HttpStatus.OK.value();
      Id id = null;
      try {
          id = sharedServicesDao.insertSharedService(payload, userEPPN);
      }
      catch (Exception ex) {
          statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
          ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(ex.getMessage()).build();
          return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));

      }
      return new ResponseEntity<Id>(id, HttpStatus.OK);
  }

 
  
}
