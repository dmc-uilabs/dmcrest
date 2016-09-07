package org.dmc.services.products;


import org.dmc.services.reviews.ReviewHelpful;
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
@RequestMapping(value = "/product_reviews_helpful", produces = {APPLICATION_JSON_VALUE})
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class ProductReviewsHelpfulController {
  
  @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
  public ResponseEntity<List<ReviewHelpful>> productReviewsHelpfulGet(@RequestParam(value = "reviewId", required = true) String reviewId,
                                                                      @RequestParam(value = "accountId", required = true) String accountId){
      // do some magic!
      return new ResponseEntity<List<ReviewHelpful>>(HttpStatus.NOT_IMPLEMENTED);
  }


  @RequestMapping(value = "", produces = { APPLICATION_JSON_VALUE },method = RequestMethod.POST)
  public ResponseEntity<ReviewHelpful> productReviewsHelpfulPost(@RequestBody ReviewHelpful serviceReviewHelpful){
      // do some magic!
      return new ResponseEntity<ReviewHelpful>(HttpStatus.NOT_IMPLEMENTED);
  }




  @RequestMapping(value = "/{helpfulID}", produces = { APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
  public ResponseEntity<ReviewHelpful> productReviewsHelpfulHelpfulIDPatch(
                                                                           @PathVariable("helpfulID") String helpfulID,
                                                                           @RequestBody ReviewHelpful helpful){
      // do some magic!
      return new ResponseEntity<ReviewHelpful>(HttpStatus.NOT_IMPLEMENTED);
  }

  
  

  

  
}
