package org.dmc.services.web.controller;

import org.apache.commons.collections.CollectionUtils;
import org.dmc.services.DMCServiceException;
import org.dmc.services.DocumentService;
import org.dmc.services.ErrorMessage;
import org.dmc.services.data.models.BaseModel;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.DocumentTagModel;
import org.dmc.services.data.models.PagedResponse;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class DocumentController {

	@Inject
	private DocumentService documentService;

	@RequestMapping(value = "/documents/{id}", method = RequestMethod.GET)
	public DocumentModel getDocument(@PathVariable("id") Integer id) {
		return documentService.findOne(id);
	}

	@RequestMapping(value="/documents", method = RequestMethod.GET)
	public PagedResponse getDocuments (@RequestParam(value = "page", defaultValue = "0") Integer page,
										@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
										@RequestHeader(value = "AJP_eppn") String userEPPN,
										@RequestParam Map<String, String> params) throws DMCServiceException, InvalidFilterParameterException {
		List<? extends BaseModel> results = documentService.filter(params, page, pageSize, userEPPN);

		Long count;
		if(CollectionUtils.isNotEmpty(results)) {
			count = Long.valueOf(results.size());
		} else {
			count = 0L;
		}

		return new PagedResponse(count, results);
	}

	@RequestMapping(value = "/documents", method = RequestMethod.POST)
	public DocumentModel postDocument(@RequestBody @Valid DocumentModel doc) throws DMCServiceException {
		return documentService.save(doc);
	}

	@RequestMapping(value = "/documents/{id}/user/{userId}", method = RequestMethod.POST)
	public ResponseEntity createDocumentForUser(@PathVariable("id") Integer documentId,
	                                            @PathVariable("userId") Integer userId,
	                                            @RequestParam( value = "dmdii", defaultValue = "false") boolean dmdii){
		return this.documentService.shareDocument(documentId, String.valueOf(userId), true, dmdii, false);
	}

	@RequestMapping(value = "/documents/{id}/share", method = RequestMethod.POST)
	public ResponseEntity createDocumentForUser(@PathVariable("id") Integer documentId,
	                                            @RequestParam( value = "dmdii", defaultValue = "false") boolean dmdii,
																							@RequestParam( value = "user", defaultValue = "") String user,
																							@RequestParam( value = "internal", defaultValue = "true") boolean internal,
																							@RequestParam( value = "email", defaultValue = "false") boolean email
																							){
		return this.documentService.shareDocument(documentId, user, internal, dmdii, email);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/documents/{documentId}", method = RequestMethod.DELETE)
	public void deleteDocument(@PathVariable("documentId") Integer documentId) {
		documentService.delete(documentId);
	}

	@RequestMapping(value = "/documents/{documentId}", method = RequestMethod.PATCH)
	public DocumentModel updateDocument(@RequestBody DocumentModel doc, @PathVariable("documentId") Integer documentId) {
		return documentService.update(doc);
	}

	@RequestMapping(value = "/documents/directories/{directoryId}", method = RequestMethod.GET)
	public List<DocumentModel> getDocumentsByDirectory(@PathVariable("directoryId") Integer directoryId) {
		return documentService.findByDirectory(directoryId);
	}

	@RequestMapping(value = "/documents/tags", method = RequestMethod.GET)
	public List<DocumentTagModel> getDocumentTags() {
		return this.documentService.getAllTags();
	}

	@RequestMapping(value = "/documents/versions/{baseDocId}", method = RequestMethod.GET)
	public List<DocumentModel> getVersions (@PathVariable("baseDocId") Integer baseDocId, @RequestHeader(value = "AJP_eppn") String userEPPN) throws IllegalAccessException {
		return this.documentService.getVersions(baseDocId, userEPPN);
	}

	@RequestMapping(value = "/documents", method = RequestMethod.PUT)
	public DocumentModel createNewVersion (@RequestBody @Valid DocumentModel doc, @RequestHeader(value = "AJP_eppn") String userEPPN) throws IllegalAccessException {
		return this.documentService.createNewVersion(doc, userEPPN);
	}

	@RequestMapping(value = "/documents/clone", method = RequestMethod.POST)
	public List<DocumentModel> cloneDocuments (@RequestParam(value = "docIds") List<Integer> docIds,
	                                           @RequestHeader(value = "AJP_eppn") String userEPPN,
	                                           @RequestParam(value = "parentTypeId") Integer parentTypeId) {
		return documentService.cloneDocuments(docIds, parentTypeId, userEPPN);
	}
<<<<<<< HEAD
	
	@ExceptionHandler(IllegalAccessException.class)
	public ResponseEntity exceptionHandler(IllegalAccessException e) {
		ErrorMessage error = new ErrorMessage.ErrorMessageBuilder(e.getMessage()).build();
		return new ResponseEntity<ErrorMessage>(error, HttpStatus.FORBIDDEN);
	}
=======

	@RequestMapping(value = "/documents/{documentId}/accept", method = RequestMethod.PATCH)
	public DocumentModel acceptDocumentIntoProject(@PathVariable("documentId") Integer documentId) throws IllegalAccessException {
		return documentService.acceptDocument(documentId);
	}


>>>>>>> origin/DMC2017-443-toggle-isAccepted-document
}
