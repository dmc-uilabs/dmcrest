package org.dmc.services.web.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.DMCServiceException;
import org.dmc.services.DocumentService;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.models.BaseModel;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.PagedResponse;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.web.validator.AWSLinkValidator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentController {
	
	private final String logTag = DocumentController.class.getName();

	@Inject
	private DocumentService documentService;
	
	@Inject
	private AWSLinkValidator awsLinkValidator;
	
	@RequestMapping(value="/documents/{id}", method = RequestMethod.GET)
	public DocumentModel getDocument(@PathVariable("id") Integer id) {
		ServiceLogger.log(logTag, "In getDocumentByDocumentId: " + id);
		
		return documentService.findOne(id);
	}
	
	@RequestMapping(value="/documents", params = {"recent", "page", "pageSize"}, method = RequestMethod.GET)
	public PagedResponse getDocuments (@RequestParam("recent") Integer recent, 
										@RequestParam("page") Integer page, 
										@RequestParam("pageSize") Integer pageSize, 
										@RequestParam Map<String, String> params) throws DMCServiceException, InvalidFilterParameterException {
		ServiceLogger.log(logTag, "In getDocuments filter: ");
		List<? extends BaseModel> results = documentService.filter(params, recent, page, pageSize);
		Long count = documentService.count(params);
		return new PagedResponse(count, results);
	}
	
	@RequestMapping(value="/documents", method = RequestMethod.POST)
	public DocumentModel postDocument (@RequestBody DocumentModel doc, BindingResult result) throws DMCServiceException {
		ServiceLogger.log(logTag, "In postDocument " + doc.getDocumentName());
		validateSaveDocument(doc.getDocumentUrl(), result);
		return documentService.save(doc, result);
	}

	private void validateSaveDocument(String documentUrl, BindingResult result) {
		awsLinkValidator.validate(documentUrl, result);		
	}
	
	@RequestMapping(value="/documents/{documentId}", method = RequestMethod.DELETE)
	public DocumentModel deleteDocument (@PathVariable("documentId") Integer documentId) {
		ServiceLogger.log(logTag, "In deleteDocument id = " + documentId);
		return documentService.delete(documentId);
	}
	
	@RequestMapping(value="/documents/{documentId}", method = RequestMethod.PATCH)
	public DocumentModel updateDocument (@RequestBody DocumentModel doc, @PathVariable("documentId") Integer documentId) {
		ServiceLogger.log(logTag, "In updateDocument: documentId = " + documentId);
		return documentService.update(doc);
	}
}
