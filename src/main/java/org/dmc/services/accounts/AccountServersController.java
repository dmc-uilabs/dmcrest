package org.dmc.services.accounts;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/account_servers", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-04T18:05:28.094Z")
public class AccountServersController {

	@RequestMapping(value = "/{serverID}", produces = { "application/json", "text/html" }, method = RequestMethod.DELETE)
	public ResponseEntity<Void> accountServersServerIDDelete(@PathVariable("serverID") String serverID) {
		// do some magic!
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/{serverID}", produces = { "application/json", "text/html" }, method = RequestMethod.GET)
	public ResponseEntity<UserAccountServer> accountServersServerIDGet(@PathVariable("serverID") String serverID

	) {
		// do some magic!
		return new ResponseEntity<UserAccountServer>(HttpStatus.NOT_IMPLEMENTED);
	}

	@RequestMapping(value = "/{serverID}", produces = { "application/json", "text/html" },method = RequestMethod.PATCH)
	public ResponseEntity<UserAccountServer> accountServersServerIDPatch(@PathVariable("serverID") String serverID,
			@RequestBody UserAccountServer server) {
		// do some magic!
		return new ResponseEntity<UserAccountServer>(HttpStatus.NOT_IMPLEMENTED);
	}

}
