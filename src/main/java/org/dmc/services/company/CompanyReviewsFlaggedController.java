package org.dmc.services.company;


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
@RequestMapping(value = "/company_reviews_flagged", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class CompanyReviewsFlaggedController {
  

  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.GET)
  public ResponseEntity<List<CompanyReviewFlagged>> companyReviewsFlaggedGet(
	 @RequestParam(value = "reviewId", required = true) String reviewId,
   @RequestParam(value = "accountId", required = true) String accountId){
      // do some magic!
      return new ResponseEntity<List<CompanyReviewFlagged>>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  @RequestMapping(value = "", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.POST)
  public ResponseEntity<CompanyReviewFlagged> companyReviewsFlaggedPost(
		  @RequestBody CompanyReviewFlagged companyReviewFlagged){
      // do some magic!
      return new ResponseEntity<CompanyReviewFlagged>(HttpStatus.NOT_IMPLEMENTED);
  }


  
}
