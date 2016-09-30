package org.dmc.services.web.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.dmc.services.DMCServiceException;
import org.dmc.services.DocumentService;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.models.BaseModel;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.PagedResponse;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentController {
	
	private final String logTag = DocumentController.class.getName();

	@Inject
	private DocumentService documentService;
	
	@RequestMapping(value="/documents/{id}", method = RequestMethod.GET)
	public DocumentModel getDocument(@PathVariable("id") Integer id) {
		ServiceLogger.log(logTag, "In getDocumentByDocumentId: " + id);
		
		return documentService.findOne(id);
	}
	
	@RequestMapping(value="/documents", params = {"recent"}, method = RequestMethod.GET)
	public PagedResponse getDocuments (@RequestParam("recent") Integer recent, 
										@RequestParam(value = "page", defaultValue = "0") Integer page, 
										@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, 
										@RequestParam Map<String, String> params) throws DMCServiceException, InvalidFilterParameterException {
		ServiceLogger.log(logTag, "In getDocuments filter: ");
		List<? extends BaseModel> results = documentService.filter(params, recent, page, pageSize);
		Long count = documentService.count(params);
		return new PagedResponse(count, results);
	}
	
	@RequestMapping(value="/documents", method = RequestMethod.POST)
	public DocumentModel postDocument (@RequestBody @Valid DocumentModel doc) throws DMCServiceException {
		ServiceLogger.log(logTag, "In postDocument " + doc.getDocumentName());
		return documentService.save(doc);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value="/documents/{documentId}", method = RequestMethod.DELETE)
	public void deleteDocument (@PathVariable("documentId") Integer documentId) {
		ServiceLogger.log(logTag, "In deleteDocument id = " + documentId);
		documentService.delete(documentId);
	}
	
	@RequestMapping(value="/documents/{documentId}", method = RequestMethod.PATCH)
	public DocumentModel updateDocument (@RequestBody DocumentModel doc, @PathVariable("documentId") Integer documentId) {
		ServiceLogger.log(logTag, "In updateDocument: documentId = " + documentId);
		return documentService.update(doc);
	}
}
