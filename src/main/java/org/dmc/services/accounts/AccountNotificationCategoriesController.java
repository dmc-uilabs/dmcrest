package org.dmc.services.accounts;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/account-notification-categories", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-04T18:05:28.094Z")
public class AccountNotificationCategoriesController {
  
  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<List<AccountNotificationCategory>> accountNotificationCategoriesGet(
@RequestParam(value = "limit", required = false) Integer limit,
@RequestParam(value = "order", required = false) String order,
@RequestParam(value = "sort", required = false) String sort
){
      // do some magic!
      return new ResponseEntity<List<AccountNotificationCategory>>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  
  
}
