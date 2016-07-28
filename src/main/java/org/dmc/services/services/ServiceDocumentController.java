package org.dmc.services.services;

import org.dmc.services.Id;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ErrorMessage;
import org.dmc.services.ServiceLogger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


/*
 * Controller class to implement REST endpoints for service image uploads to AWS S3
 */
@RestController
public class ServiceDocumentController {

	private final String logTag = ServiceImagesController.class.getName();
	private ServiceDocumentDao serviceDocumentDao = new ServiceDocumentDao();

	/*Create a Service Image*/
	@RequestMapping(value = "/service_documents", method = RequestMethod.POST)
	public ResponseEntity createServiceDocument(@RequestBody ServiceDocument payload, @RequestHeader(value="AJP_eppn", defaultValue="testUser") String userEPPN) {
		ServiceLogger.log(logTag, "Create ServiceImages, userEPPN: " + userEPPN);
		int statusCode = HttpStatus.OK.value();
		Id docId = null;
  	try {
  		docId = serviceDocumentDao.createServiceDocument(payload, userEPPN);
    } catch(DMCServiceException e) {
      	return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
    	}
    return new ResponseEntity<Id>(docId, HttpStatus.valueOf(statusCode));
	}//Create

	/*DELETE /service_images/{imageId}*/
	@RequestMapping(value = "/service_documents/{docId}", produces = { "application/json"}, method = RequestMethod.DELETE)
	public ResponseEntity deleteServiceDocument(@RequestHeader(value = "AJP_eppn", defaultValue = "testUser") String userEPPN, @PathVariable("docId") int id) {
		ServiceLogger.log(logTag, "DELETE ServiceDocument, docId: " + id);
		boolean response;
		try{
			response = serviceDocumentDao.deleteServiceDocument(id, userEPPN);
		    if (response) {
                return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<ErrorMessage>(new ErrorMessage("failure to delete service tag"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
		} catch (DMCServiceException e) {
            ServiceLogger.log(logTag, "caught exception: for id " + id + " as user " + userEPPN + " " + e.getMessage());
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
