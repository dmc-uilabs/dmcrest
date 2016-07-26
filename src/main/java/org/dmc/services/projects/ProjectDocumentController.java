package org.dmc.services.projects;

import java.util.ArrayList;
import org.dmc.services.Id;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectDocumentController {

	private final String logTag = ProjectDocumentController.class.getName();
	private ProjectDocumentDao projectDocumentDao = new ProjectDocumentDao(); 
	
	/*
	 * POST PROJECT DOCUMENT, returns a the uploaded ProjectDocument POJO
	 */
	@RequestMapping(value = "/project_documents",method = RequestMethod.POST, consumes = "application/json", produces = {"application/json"} )
	public ResponseEntity postProjectDocument(@RequestBody ProjectDocument payload){
		ServiceLogger.log(logTag, " POST ProjectDocuments by Project " + payload.getProjectId());
		int statusCode = HttpStatus.OK.value();
		Id id = null;
		
		try {
			id = projectDocumentDao.postProjectDocuments(payload);
			return new ResponseEntity<Id>(id, HttpStatus.valueOf(statusCode));

		} catch(DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}		
	}
	

	
	@RequestMapping(value = "/project_documents/{id}", method = RequestMethod.DELETE, produces = "application/json")
		public ResponseEntity deleteProjectDoc(@PathVariable("id") int id,
				@RequestHeader(value = "AJP_eppn", required = true) String userEPPN) {
			ServiceLogger.log(logTag, "In delete ProjectTag: as user " + userEPPN);

			Id deleted = null;

			try {
				deleted = projectDocumentDao.deleteProjectDoc(id, userEPPN);
				return new ResponseEntity<Id>(deleted, HttpStatus.valueOf(HttpStatus.OK.value()));
			} catch (DMCServiceException e) {
				ServiceLogger.logException(logTag, e);
				return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
			}
		}

}