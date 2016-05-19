package org.dmc.services.services;

import java.util.ArrayList;
import java.util.List;
import org.dmc.services.Id;
import org.dmc.services.sharedattributes.Util;

import org.dmc.services.DMCServiceException;
import org.dmc.services.ErrorMessage;
import org.dmc.services.services.Service;
import org.dmc.services.ServiceLogger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.http.HTTPException;


@RestController
public class ServiceImagesController {

	private final String logTag = ServiceImagesController.class.getName();
	private ServiceImagesDao serviceImagesDao = new ServiceImagesDao();

	/*Create a Service Image*/
	@RequestMapping(value = "/service_images", method = RequestMethod.POST)
	public ResponseEntity createServiceImages (@RequestBody ServiceImages payload, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
		ServiceLogger.log(logTag, "Create ServiceImages, userEPPN: " + userEPPN);
		int statusCode = HttpStatus.OK.value();
		Id imageId = null;
      try {
            imageId = serviceImagesDao.createServiceImages(payload, userEPPN);
        } catch(DMCServiceException e) {
            return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
        }
      return new ResponseEntity<Id>(imageId, HttpStatus.valueOf(statusCode));

	}//Create

	/*DELETE /service_images/{imageId}*/
	@RequestMapping(value = "/service_images/{imageId}", produces = { "application/json"}, method = RequestMethod.DELETE)
	public ResponseEntity deleteServiceImages(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN, @PathVariable("imageId") int imageId) {
		ServiceLogger.log(logTag, "DELETE ServiceImage, imageId: " + imageId);
		boolean response;
		try{
			response = serviceImagesDao.deleteServiceImages(imageId, userEPPN);
		    if (response) {
                return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<ErrorMessage>(new ErrorMessage("failure to delete service tag"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
		}

		catch (DMCServiceException e) {
            ServiceLogger.log(logTag, "caught exception: for id " + imageId + " as user " + userEPPN + " " + e.getMessage());
            if (e.getMessage().equals("you are not allowed to delete this service image")) {
                return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
            } else if (e.getMessage().equals("invalid id")) {
                return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
            } else {
                return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
            }
        }//Catch

	}//Delete
}
