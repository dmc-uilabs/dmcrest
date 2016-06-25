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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectDocumentController {

	private final String logTag = ProjectDocumentController.class.getName();
	private ProjectDocumentDao projectDocumentDao = new ProjectDocumentDao(); 
	
	/*
	 * POST PROJECT DOCUMENT, returns a the uploaded ProjectDocument POJO
	 */
	@RequestMapping(value = "/projects/{projectID}/project_documents",method = RequestMethod.POST, consumes = "application/json", produces = {"application/json"} )
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
	
	/* 
	 * POST PROJECT DOCUMENT, returns an array of ProjectDocuments POJO
	 */
	@RequestMapping(value = "/projects/{projectID}/project_documents",method = RequestMethod.GET,  produces = {"application/json"} )
	public ResponseEntity getProjectDocumentsId(@PathVariable("projectID") int projectID,
			@RequestParam(value = "documentGroupId", required = true) int documentGroupId, 
			@RequestParam(value = "limit", defaultValue = "100", required=false) Integer limit,
	        @RequestParam(value = "order", defaultValue = "ASC", required=false) String order,
	        @RequestParam(value = "sort", defaultValue = "file_id", required=false) String sort) {

		ServiceLogger.log(logTag, " GET ProjectDocuments by Project " + projectID);
		int statusCode = HttpStatus.OK.value();
		ArrayList<ProjectDocument> documentList = null;
		
		try {
			documentList = projectDocumentDao.getProjectDocuments(projectID, documentGroupId, limit, order, sort);
			return new ResponseEntity<ArrayList<ProjectDocument>>(documentList, HttpStatus.valueOf(statusCode));

		} catch(DMCServiceException e) {
			ServiceLogger.logException(logTag, e);
			return new ResponseEntity<String>(e.getMessage(), e.getHttpStatusCode());
		}		
	}
}