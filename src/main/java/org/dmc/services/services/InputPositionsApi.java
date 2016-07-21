package org.dmc.services.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.dmc.services.DMCServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/input-positions", produces = { APPLICATION_JSON_VALUE })
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-08T14:26:00.636Z")
public class InputPositionsApi {

	@RequestMapping(value = "/{positionInputId}", produces = {
			"application/json"}, method = RequestMethod.DELETE)
	public ResponseEntity<Void> inputPositionsPositionInputIdDelete(
			@PathVariable("positionInputId") BigDecimal positionInputId) {
		// Assume positionInputId is the ID of the service_interface_parameter table id.
		// Delete means to set the position as -999
		InputPositionsDAO ipDao = new InputPositionsDAO();
		try {
		ipDao.deletePosition(positionInputId.intValue());
		}
		catch (DMCServiceException e)
		{
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{positionInputId}", produces = {
			"application/json"}, method = RequestMethod.PATCH)
	public ResponseEntity<ServiceInputsPositions> inputPositionsPositionInputIdPatch(
			@PathVariable("positionInputId") BigDecimal positionInputId,
			@RequestBody List<ServiceInputPosition> positions) {
		// The method definition is confusing -- using positions itself, we can not update anything.
		// Assume positionInputId is interface_id here.
		InputPositionsDAO ipDao = new InputPositionsDAO();
		try {
		ipDao.patchPosition(positionInputId.intValue(),positions);
		} catch (Exception e) {
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ServiceInputsPositions>(
				HttpStatus.OK);
	}

	@RequestMapping(value = "", produces = { "application/json"}, method = RequestMethod.POST)
	public ResponseEntity<ServiceInputsPositions> inputPositionsPost(
			@RequestBody PostServiceInputPosition inputsPositions) {
		InputPositionsDAO ipDao = new InputPositionsDAO();
		ServiceInputsPositions result = new ServiceInputsPositions();
		int serviceId = -999;
		serviceId = new Integer(inputsPositions.getServiceId());
		result.setServiceId(inputsPositions.getServiceId());
		List<ServiceInputPosition> positions = inputsPositions.getPositions();
		try {
			for (int i = 0; i < positions.size(); i++) {
				ipDao.postPositions(inputsPositions);
			}
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ServiceInputsPositions>(result, HttpStatus.OK);
	}
}