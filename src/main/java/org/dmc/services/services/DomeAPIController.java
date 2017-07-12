package org.dmc.services.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.apache.commons.lang3.StringUtils;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.DomeServerService;

import static org.springframework.http.MediaType.*;

@Controller
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class DomeAPIController {

	private final String logTag = DomeAPIController.class.getName();
	private DomeAPIDao domeAPIDao = new DomeAPIDao();
	
	@Autowired
    DomeServerService serverService;

	@RequestMapping(value = "/getChildren", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity getChildrenFromDome(
			@RequestParam(value = "dateModified", required = false) BigDecimal dateModified,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "domeServer", required = true) String domeServer,
			@RequestParam(value = "modelId", required = false) String modelId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "path", required = false) List<BigDecimal> path,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "version", required = false) BigDecimal version,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

		ServiceLogger.log(logTag, "In childrenGet: as user " + userEPPN);
		String children = "";
		
		try {
			DomeEntity domeEntity = new DomeEntity();
			domeEntity.setDateModified(dateModified);
			domeEntity.setDescription(description);
			//If it's the ID instead of the URL
			if(StringUtils.isNumeric(domeServer)) {
				domeEntity.setDomeServer(serverService.getServerURLById(Integer.valueOf(domeServer)));
			} else {
				domeEntity.setDomeServer(domeServer);
			}
			domeEntity.setModelId(modelId);
			domeEntity.setName(name);
			domeEntity.setPath(path);
			domeEntity.setType(type);
			domeEntity.setVersion(version);
			children = domeAPIDao.getChildren(domeEntity);
			return new ResponseEntity<String>(children, HttpStatus.OK);
		} catch (DMCServiceException e) {
			if (!e.getError().equals(DMCError.IncorrectType)) {
				ServiceLogger.logException(logTag, e);
			}
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}

	}
	
	@RequestMapping(value = "/getModel", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity getModelFromDome(
			@RequestParam(value = "domeServer", required = true) String domeServer,
			@RequestParam(value = "interfaceId", required = true) String interfaceId,
			@RequestParam(value = "modelId", required = false) String modelId,
			@RequestParam(value = "projectId", required = false) String projectId,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "path", required = true) List<BigDecimal> path,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "version", required = true) BigDecimal version,
			@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {

		ServiceLogger.log(logTag, "In modelGet: as user " + userEPPN);
		String model = "";
		
		try {
			DomeModel domeModel = new DomeModel();
			domeModel.setProjectId(projectId);
			domeModel.setInterfaceId(interfaceId);
			//If it's the ID instead of the URL
			if(StringUtils.isNumeric(domeServer)) {
				domeModel.setDomeServer(serverService.getServerURLById(Integer.valueOf(domeServer)));
			} else {
				domeModel.setDomeServer(domeServer);
			}
			domeModel.setModelId(modelId);
			domeModel.setName(name);
			domeModel.setPath(path);
			domeModel.setType("interface"); //All getModel calls to API have type of interface (frontend may specify as 'project' but API needs type equal to 'interface')
			domeModel.setVersion(version);
			model = domeAPIDao.getModel(domeModel);
			return new ResponseEntity<String>(model, HttpStatus.OK);
		} catch (DMCServiceException e) {
			if (!e.getError().equals(DMCError.IncorrectType)) {
				ServiceLogger.logException(logTag, e);
			}
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}

	}
	
}
