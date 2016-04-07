package org.dmc.services.services;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/service_tags", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceTagsController {
  

  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    consumes = { "application/json", "text/xml" },
    method = RequestMethod.GET)
  public ResponseEntity<List<ServiceTag>> serviceTagsGet(
@RequestParam(value = "limit", required = false) Integer limit,
@RequestParam(value = "order", required = false) String order,
@RequestParam(value = "sort", required = false) String sort) {
      // do some magic!
      return new ResponseEntity<List<ServiceTag>>(HttpStatus.NOT_IMPLEMENTED);
  }

  
  
  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    consumes = { "application/json", "text/xml" },
    method = RequestMethod.POST)
  public ResponseEntity<ServiceTag> serviceTagsPost(
		  @RequestBody ServiceTag serviceTag
){
      // do some magic!
      return new ResponseEntity<ServiceTag>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  
  @RequestMapping(value = "/{serviceTagID}", 
    produces = { "application/json", "text/html" }, 
    consumes = { "application/json", "text/xml" },
    method = RequestMethod.DELETE)
  public ResponseEntity<Void> ServiceTagIDDelete1(
@PathVariable("serviceTagID") String serviceTagID){
      // do some magic!
      return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  @RequestMapping(value = "/{serviceTagID}", 
    produces = { "application/json", "text/html" }, 
    consumes = { "application/json", "text/xml" },
    method = RequestMethod.DELETE)
  public ResponseEntity<Void> ServiceTagIDDelete(
@PathVariable("serviceTagID") String serviceTagID){
      // do some magic!
      return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  
}
