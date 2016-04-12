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

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/dome-interfaces", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class DomeInterfacesController {
  

  @RequestMapping(value = "/{domeInterfaceId}", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.DELETE)
  public ResponseEntity<Void> domeInterfacesDomeInterfaceIdDelete(
@PathVariable("domeInterfaceId") BigDecimal domeInterfaceId){
      // do some magic!
      return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
  }


  
  @RequestMapping(value = "/{domeInterfaceId}", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<GetDomeInterface> domeInterfacesDomeInterfaceIdGet(
 @PathVariable("domeInterfaceId") BigDecimal domeInterfaceId){
      // do some magic!
      return new ResponseEntity<GetDomeInterface>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  @RequestMapping(value = "/{domeInterfaceId}", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.PATCH)
  public ResponseEntity<GetDomeInterface> domeInterfacesDomeInterfaceIdPatch(
@PathVariable("domeInterfaceId") BigDecimal domeInterfaceId,
  @RequestBody PostUpdateDomeInterface domeInterface){
      // do some magic!
      return new ResponseEntity<GetDomeInterface>(HttpStatus.NOT_IMPLEMENTED);
  }

  
  

  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.POST)
  public ResponseEntity<GetDomeInterface> domeInterfacesPost(
 @RequestBody PostUpdateDomeInterface body,
   @RequestParam(value = "limit", required = false) Integer limit,
   @RequestParam(value = "order", required = false) String order,
    @RequestParam(value = "sort", required = false) String sort){
      // do some magic!
      return new ResponseEntity<GetDomeInterface>(HttpStatus.NOT_IMPLEMENTED);
  }


  
}
