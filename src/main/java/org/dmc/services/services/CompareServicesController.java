package org.dmc.services.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/compare_services", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-08-24T16:43:27.555-04:00")
public class CompareServicesController {

	@RequestMapping(value = "/{id}", produces = { "application/json", "text/html" }, method = RequestMethod.DELETE)
	public ResponseEntity<Void> compareServicesIdDelete(
			@PathVariable("id") String id

	){
		// do some magic!
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "", produces = { "application/json", "text/html" }, consumes = { "application/json",
			"text/xml" }, method = RequestMethod.POST)
	public ResponseEntity<GetCompareService> compareServicesPost(

			@RequestBody PostCompareService body){
		// do some magic!
		return new ResponseEntity<GetCompareService>(HttpStatus.NOT_IMPLEMENTED);
	}

}
