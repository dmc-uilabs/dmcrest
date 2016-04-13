package org.dmc.services.products;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/favorite_products", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class FavoriteProductsController {
  

  @RequestMapping(value = "/{favorite_productId}", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.DELETE)
  public ResponseEntity<Void> favoriteProductsFavoriteProductIdDelete(
 @PathVariable("favoriteProductId") Integer favoriteProductId){
      // do some magic!
      return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
  }

  


  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.POST)
  public ResponseEntity<InlineResponse2012> favoriteProductsPost(
 @RequestParam(value = "accountId", required = true) Integer accountId,
@RequestParam(value = "serviceId", required = true) Integer serviceId){
      // do some magic!
      return new ResponseEntity<InlineResponse2012>(HttpStatus.NOT_IMPLEMENTED);
  }

  

 
  

  
}
