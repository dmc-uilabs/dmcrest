package org.dmc.services.market;


import org.dmc.services.components.Component;
import org.dmc.services.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/market", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class MarketController {
  

  @RequestMapping(value = "/components", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<List<Component>> marketComponentsGet(
@RequestParam(value = "limit", required = false) Integer limit,
@RequestParam(value = "order", required = false) String order,
@RequestParam(value = "start", required = false) Integer start,
@RequestParam(value = "sort", required = false) String sort,
@RequestParam(value = "titleLike", required = false) String titleLike,
@RequestParam(value = "serviceType", required = false) String serviceType,
@RequestParam(value = "authors", required = false) List<Integer> authors,
@RequestParam(value = "ratings", required = false) List<String> ratings,
@RequestParam(value = "favorites", required = false) String favorites,
@RequestParam(value = "dates", required = false) List<String> dates){
      // do some magic!
      return new ResponseEntity<List<Component>>(HttpStatus.NOT_IMPLEMENTED);
  }

  
  

  @RequestMapping(value = "/new_services", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<List<Service>> marketNewServicesGet(
		  @RequestParam(value = "limit", required = false) Integer limit,
@RequestParam(value = "order", required = false) String order,
@RequestParam(value = "start", required = false) Integer start,
@RequestParam(value = "sort", required = false) String sort){
      // do some magic!
      return new ResponseEntity<List<Service>>(HttpStatus.NOT_IMPLEMENTED);
  }


  
  @RequestMapping(value = "/popular_services", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<List<Service>> marketPopularServicesGet(
		  @RequestParam(value = "limit", required = false) Integer limit,
@RequestParam(value = "order", required = false) String order,
@RequestParam(value = "start", required = false) Integer start, 
    @RequestParam(value = "sort", required = false) String sort){
      // do some magic!
      return new ResponseEntity<List<Service>>(HttpStatus.NOT_IMPLEMENTED);
  }

  


  @RequestMapping(value = "/services", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<List<Service>> marketServicesGet(
		  @RequestParam(value = "limit", required = false) Integer limit,
@RequestParam(value = "order", required = false) String order,
@RequestParam(value = "start", required = false) Integer start,
@RequestParam(value = "sort", required = false) String sort,
@RequestParam(value = "titleLike", required = false) String titleLike,
@RequestParam(value = "serviceType", required = false) String serviceType,
@RequestParam(value = "authors", required = false) List<Integer> authors,
@RequestParam(value = "ratings", required = false) List<String> ratings,
@RequestParam(value = "favorites", required = false) String favorites,
@RequestParam(value = "dates", required = false) List<String> dates){
      // do some magic!
      return new ResponseEntity<List<Service>>(HttpStatus.NOT_IMPLEMENTED);
  }

  


  
}
