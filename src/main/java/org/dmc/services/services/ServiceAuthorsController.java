package org.dmc.services.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/service_authors", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-07T17:42:57.404Z")
public class ServiceAuthorsController {

	@RequestMapping(value = "/{autorhId}", produces = { "application/json",
			"text/html" }, method = RequestMethod.DELETE)
	public ResponseEntity<Void> serviceAuthorsAutorhIdDelete(@PathVariable("autorhId") Integer autorhId) {
		// do some magic!
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "", produces = { "application/json", "text/html" }, method = RequestMethod.POST)
	public ResponseEntity<ServiceAuthor> serviceAuthorsPost(@RequestBody ServiceAuthor author) {
		// do some magic!
		return new ResponseEntity<ServiceAuthor>(HttpStatus.NOT_IMPLEMENTED);
	}

}
