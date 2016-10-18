package org.dmc.services.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ServerAccessService;
import org.dmc.services.ServiceLogger;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/account_servers", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-04T18:05:28.094Z")
public class AccountServersController {
	
	private final String logTag = AccountServersController.class.getName();
	private AccountServersDao accountServersDao = new AccountServersDao();
	
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ServerAccessService accessService;

	@RequestMapping(value = "", produces = { "application/json", "text/html" }, method = RequestMethod.POST)
	public ResponseEntity<?> accountServersServerIDPost(@RequestBody String body,
														@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(logTag, "accountServersServerIDPost, userEPPN: " + userEPPN);
		
		int httpStatusCode = HttpStatus.CREATED.value();
		UserAccountServer userAccountServer = null;
		
		try {
			ObjectNode json = mapper.readValue(body, ObjectNode.class);
			UserAccountServer server = mapper.convertValue(json.get("server"), UserAccountServer.class);
			Boolean isPub = json.get("isPub").asBoolean();

			userAccountServer = accountServersDao.postUserAccountServer(server, userEPPN);
			if(isPub){
				accessService.addServerToGlobalById(Integer.valueOf(userAccountServer.getId()), userEPPN);
			}
		} catch (DMCServiceException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		} catch (Exception e){
			ServiceLogger.log(logTag, e.getMessage());
			return new ResponseEntity<String>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
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
			ServiceLogger.log(logTag, e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
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
			ServiceLogger.log(logTag, e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}
		
		return new ResponseEntity<UserAccountServer>(userAccountServer, HttpStatus.valueOf(httpStatusCode));
	}

	
	@RequestMapping(value = "/{serverID}", produces = { "application/json", "text/html" },method = RequestMethod.PATCH)
	public ResponseEntity<?> accountServersServerIDPatch(@PathVariable("serverID") String serverID,
																		 @RequestBody String body,
																		 @RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		
		ServiceLogger.log(logTag, "accountServersServerIDPatch, userEPPN: " + userEPPN);
		
		int httpStatusCode = HttpStatus.OK.value();
		UserAccountServer userAccountServer = null;
		
		try {
			UserAccountServer original = accountServersDao.getUserAccountServer(Integer.parseInt(serverID), userEPPN);
			ObjectNode json = mapper.readValue(body, ObjectNode.class);
			UserAccountServer server = mapper.convertValue(json.get("server"), UserAccountServer.class);
			server.setId(original.getId());
			server.setAccountId(original.getAccountId());
			Boolean changePub = json.get("changePub").asBoolean();

			userAccountServer = accountServersDao.patchUserAccountServer(serverID, server, userEPPN);

			if(changePub){
				accessService.changeServerPublicAccess(Integer.valueOf(serverID), userEPPN);
			}
		} catch (DMCServiceException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		} catch (Exception e){
			ServiceLogger.log(logTag, e.getMessage());
			return new ResponseEntity<String>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<UserAccountServer>(userAccountServer, HttpStatus.valueOf(httpStatusCode));
	}

}
