package org.dmc.services.services.specifications;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/array_specifications", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ArraySpecificationsController {
  


  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<List<ArraySpecifications>> arraySpecificationsGet(
@RequestParam(value = "limit", required = false) Integer limit,
 @RequestParam(value = "order", required = false) String order,
@RequestParam(value = "sort", required = false) String sort){
      // do some magic!
      return new ResponseEntity<List<ArraySpecifications>>(HttpStatus.NOT_IMPLEMENTED);
  }

  

 
  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.POST)
  public ResponseEntity<List<ArraySpecifications>> arraySpecificationsPost(
 @RequestBody ArraySpecifications body){
      // do some magic!
      return new ResponseEntity<List<ArraySpecifications>>(HttpStatus.NOT_IMPLEMENTED);
  }

  


  
}
