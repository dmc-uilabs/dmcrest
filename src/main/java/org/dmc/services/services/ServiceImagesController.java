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
@RequestMapping(value = "/service_images", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceImagesController {

	@RequestMapping(value = "", produces = { "application/json", "text/html" }, method = RequestMethod.GET)
	public ResponseEntity<List<ServiceTag>> serviceImagesGet(
			@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "sort", required = false) String sort) {
		// do some magic!
		return new ResponseEntity<List<ServiceTag>>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/{imageId}", produces = { "application/json", "text/html" }, method = RequestMethod.DELETE)
	public ResponseEntity<Void> serviceImagesImageIdDelete(@PathVariable("imageId") String imageId) {
		// do some magic!
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "", produces = { "application/json", "text/html" }, method = RequestMethod.POST)
	public ResponseEntity<ServiceImages> serviceImagesPost(@RequestBody ServiceImages serviceImage) {
		// do some magic!
		return new ResponseEntity<ServiceImages>(HttpStatus.NOT_IMPLEMENTED);
	}

}
