package org.dmc.services.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/specifications", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class SpecificationsController {
  


  @RequestMapping(value = "/{specificationId}", 
    produces = { "application/json", "text/html" }, 
    consumes = { "application/json", "text/xml" },
    method = RequestMethod.PATCH)
  public ResponseEntity<ServiceSpecifications> specificationsSpecificationIdPatch(
@PathVariable("specificationId") String specificationId,
     @RequestBody ServiceSpecifications body){
      // do some magic!
      return new ResponseEntity<ServiceSpecifications>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  
}
