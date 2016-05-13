package org.dmc.services.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.dmc.services.Id;

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

	@RequestMapping(value = "/service_images", method = RequestMethod.POST)
	public ResponseEntity createServiceImages (@RequestBody ServiceImages payload, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
		ServiceLogger.log(logTag, "Create ServiceImages, userEPPN: " + userEPPN);
		int statusCode = HttpStatus.OK.value(); 
		ServiceImages image = new ServiceImages();
		
      try {
            image = serviceImagesDao.createServiceImages(payload, userEPPN);
            return new ResponseEntity<ServiceImages>(image, HttpStatus.valueOf(statusCode));
        } catch(HTTPException e) {
    		statusCode = e.getStatusCode();
    		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
    		return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
        }  
	}


	@RequestMapping(value = "/service_images", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity getServiceImages(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN) {
		ServiceLogger.log(logTag, "In GET ServiceImage as user " + userEPPN); 
			int statusCode = HttpStatus.OK.value(); 
			ArrayList<ServiceTag> imageList = null; 
			try{ 
				imageList = serviceImagesDao.getServiceImages(); 
				return new ResponseEntity<ArrayList<ServiceTag>>(imageList, HttpStatus.valueOf(statusCode));
			}
			
			catch(HTTPException e) {
	    		statusCode = e.getStatusCode();
	    		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
	    		return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
	        }  				
	}

	
	/*
	DELETE /service_images/{imageId}
	 */
	@RequestMapping(value = "service_images/{imageId}", produces = { "application/json"}, method = RequestMethod.DELETE)
	public ResponseEntity deleteServiceImages(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN, @PathVariable("imageId") String imageId) {

		ServiceLogger.log(logTag, "DELETE ServiceImage, imageId: " + imageId);
		int statusCode = HttpStatus.OK.value(); 
		Id id = null; 
		try{ 
			id = serviceImagesDao.deleteServiceImages(imageId, userEPPN); 
			return new ResponseEntity<Id>(id, HttpStatus.valueOf(statusCode));
		}
		
		catch(HTTPException e) {
    		statusCode = e.getStatusCode();
    		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
    		return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
        }	
	}
}
