package org.dmc.services.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;

import org.dmc.services.ServiceLogger;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;

import static org.springframework.http.MediaType.*;

@Controller
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class DomeAPIController {

	private final String logTag = DomeAPIController.class.getName();
	private DomeAPIDao domeAPIDao = new DomeAPIDao();

	@RequestMapping(value = "/getChildren", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity childrenGet(
			@RequestParam(value = "dateModified", required = false) BigDecimal dateModified,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "domeServer", required = true) String domeServer,
			@RequestParam(value = "modelId", required = false) String modelId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "path", required = false) List<BigDecimal> path,
			@RequestParam(value = "type", required = false) String type,
			//@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "version", required = false) BigDecimal version,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

		ServiceLogger.log(logTag, "In childrenGet: as user " + userEPPN);
		String temp = "";
		
		try {
			DomeEntity domeEntity = new DomeEntity();
			domeEntity.setDateModified(dateModified);
			domeEntity.setDescription(description);
			domeEntity.setDomeServer(domeServer);
			domeEntity.setModelId(modelId);
			domeEntity.setName(name);
			domeEntity.setPath(path);
			domeEntity.setType(type);
			domeEntity.setVersion(version);
			temp = domeAPIDao.getChildren(domeEntity);
			return new ResponseEntity<String>(temp, HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}

	}
	
	@RequestMapping(value = "/getModel", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity modelGet(
			@RequestParam(value = "domeServer", required = true) String domeServer,
			@RequestParam(value = "interfaceId", required = false) String interfaceId,
			@RequestParam(value = "modelId", required = false) String modelId,
			@RequestParam(value = "projectId", required = false) String projectId,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "path", required = true) List<BigDecimal> path,
			@RequestParam(value = "type", required = true) String type,
			//@RequestParam(value = "dateModified", required = false) BigDecimal dateModified,
			//@RequestParam(value = "description", required = false) String description,
			//@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "version", required = true) BigDecimal version,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

		ServiceLogger.log(logTag, "In modelGet: as user " + userEPPN);
		String temp = "";
		
		try {
			DomeModel domeModel = new DomeModel();
			domeModel.setProjectId(projectId);
			domeModel.setInterfaceId(interfaceId);
			domeModel.setDomeServer(domeServer);
			domeModel.setModelId(modelId);
			domeModel.setName(name);
			domeModel.setPath(path);
			domeModel.setType(type);
			domeModel.setVersion(version);
			temp = domeAPIDao.getModel(domeModel);
			return new ResponseEntity<String>(temp, HttpStatus.OK);
		} catch (DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getErrorMessage(), e.getHttpStatusCode());
		}

	}
	
}
