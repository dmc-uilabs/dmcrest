package org.dmc.services.company;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/company_featured", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class CompanyFeaturedController {
  

  @RequestMapping(value = "/add", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.POST)
  public ResponseEntity<InlineResponse2011> companyFeaturedAddPost(
	@RequestParam(value = "companyId", required = true) Integer companyId,
    @RequestParam(value = "position", required = true) Integer position,
     @RequestParam(value = "serviceId", required = true) Integer serviceId){
      // do some magic!
      return new ResponseEntity<InlineResponse2011>(HttpStatus.NOT_IMPLEMENTED);
  }


  @RequestMapping(value = "/{company_featuredId}", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.DELETE)
  public ResponseEntity<Void> companyFeaturedCompanyFeaturedIdDelete(
@PathVariable("companyFeaturedId") Integer companyFeaturedId){
      // do some magic!
      return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  @RequestMapping(value = "/{company_featuredId}/position", 
    produces = { "application/json", "text/html" }, 
    method = RequestMethod.PATCH)
  public ResponseEntity<InlineResponse2011> companyFeaturedCompanyFeaturedIdPositionPatch(
@PathVariable("companyFeaturedId") Integer companyFeaturedId,
@RequestParam(value = "position", required = true) Integer position){
      // do some magic!
      return new ResponseEntity<InlineResponse2011>(HttpStatus.NOT_IMPLEMENTED);
  }

  

  
}
