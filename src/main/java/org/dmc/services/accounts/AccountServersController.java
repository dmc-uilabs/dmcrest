package org.dmc.services.accounts;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;

import javax.xml.ws.http.HTTPException;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/account_servers", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-04T18:05:28.094Z")
public class AccountServersController {
	
	private final String logTag = AccountServersController.class.getName();
	private AccountServersDao accountServersDao = new AccountServersDao();
	
	@RequestMapping(value = "", produces = { "application/json", "text/html" }, method = RequestMethod.POST)
	public ResponseEntity<?> accountServersServerIDPost(@RequestBody UserAccountServer accountServer,
																		@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

		ServiceLogger.log(logTag, "accountServersServerIDPost, userEPPN: " + userEPPN);
		
		int httpStatusCode = HttpStatus.CREATED.value();
		UserAccountServer userAccountServer = null;
		
		try {
			userAccountServer = accountServersDao.postUserAccountServer(accountServer, userEPPN);
		} catch (DMCServiceException e) {
			ServiceLogger.log(logTag, e.getErrorMessage());
			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}
		
		return new ResponseEntity<UserAccountServer>(userAccountServer, HttpStatus.valueOf(httpStatusCode));
	}
	
	
	@RequestMapping(value = "/{serverID}", produces = { "application/json", "text/html" }, method = RequestMethod.DELETE)
	public ResponseEntity<?> accountServersServerIDDelete(@PathVariable("serverID") String serverID,
															 @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		
		ServiceLogger.log(logTag, "accountServersServerIDDelete, userEPPN: " + userEPPN + " and server id " + serverID);
		
		int httpStatusCode = HttpStatus.OK.value();
		
		try {
			accountServersDao.deleteUserAccountServer(Integer.parseInt(serverID), userEPPN);
		} catch (DMCServiceException e) {
			ServiceLogger.log(logTag, e.getErrorMessage());
			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}

		return new ResponseEntity<Void>(HttpStatus.valueOf(httpStatusCode));
	}

	
	@RequestMapping(value = "/{serverID}", produces = { "application/json", "text/html" }, method = RequestMethod.GET)
	public ResponseEntity<?> accountServersServerIDGet(@PathVariable("serverID") String serverID,
																	   @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(logTag, "accountServersServerIDGet, userEPPN: " + userEPPN + " and server id " + serverID);
		
		int httpStatusCode = HttpStatus.OK.value();
		UserAccountServer userAccountServer = null;
		
		try {
			userAccountServer = accountServersDao.getUserAccountServer(Integer.parseInt(serverID), userEPPN);
		} catch (DMCServiceException e) {
			ServiceLogger.log(logTag, e.getErrorMessage());
			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}
		
		return new ResponseEntity<UserAccountServer>(userAccountServer, HttpStatus.valueOf(httpStatusCode));
	}

	
	@RequestMapping(value = "/{serverID}", produces = { "application/json", "text/html" },method = RequestMethod.PATCH)
	public ResponseEntity<?> accountServersServerIDPatch(@PathVariable("serverID") String serverID,
																		 @RequestBody UserAccountServer server,
																		 @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		
		ServiceLogger.log(logTag, "accountServersServerIDPatch, userEPPN: " + userEPPN);
		
		int httpStatusCode = HttpStatus.OK.value();
		UserAccountServer userAccountServer = null;
		
		try {
			userAccountServer = accountServersDao.patchUserAccountServer(serverID, server, userEPPN);
		} catch (DMCServiceException e) {
			ServiceLogger.log(logTag, e.getErrorMessage());
			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}
		
		return new ResponseEntity<UserAccountServer>(userAccountServer, HttpStatus.valueOf(httpStatusCode));
	}

}
