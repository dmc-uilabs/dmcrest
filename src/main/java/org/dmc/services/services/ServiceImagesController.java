package org.dmc.services.services;

import java.math.BigDecimal;
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
            //If the Given Service Id does not exist, return a -1 
            if (imageId == null){
            	imageId = new Id.IdBuilder(-1).build();
            	statusCode = HttpStatus.BAD_REQUEST.value(); 
            }
            return new ResponseEntity<Id>(imageId, HttpStatus.valueOf(statusCode));
        } catch(HTTPException e) {
    		statusCode = e.getStatusCode();
    		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
    		return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
        }  
	}

	
	/*
	 * Get all images for a part
	 */
	@RequestMapping(value = "/services/{serviceId}/service_images", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity getServiceImages(@PathVariable("serviceId") int serviceId,  @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
		ServiceLogger.log(logTag, "In GET ServiceImage by User " + userEPPN); 
			int statusCode = HttpStatus.OK.value(); 
			ArrayList<ServiceImages> imageList = null; 
			try{ 
				imageList = serviceImagesDao.getServiceImages(serviceId); 
				return new ResponseEntity<ArrayList<ServiceImages>>(imageList, HttpStatus.valueOf(statusCode));
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
	public ResponseEntity deleteServiceImages(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN, @PathVariable("imageId") int imageId) {
		ServiceLogger.log(logTag, "DELETE ServiceImage, imageId: " + imageId);
		int statusCode = HttpStatus.OK.value(); 
		Id id = null; 
		try{ 
			id = serviceImagesDao.deleteServiceImages(imageId, userEPPN); 
			
			//If nothing was deleted, return -1
			 if (id == null){
	            	id = new Id.IdBuilder(-1).build();
	            	statusCode = HttpStatus.BAD_REQUEST.value(); 
	            }
			return new ResponseEntity<Id>(id, HttpStatus.valueOf(statusCode));
		}
		
		catch(HTTPException e) {
    		statusCode = e.getStatusCode();
    		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
    		return new ResponseEntity<ErrorMessage>(error, HttpStatus.valueOf(statusCode));
        }	
	}
}
