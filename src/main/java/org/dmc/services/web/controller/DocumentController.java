package org.dmc.services.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.DMCServiceException;
import org.dmc.services.DocumentService;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.web.validator.AWSLinkValidator;
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
	
	@RequestMapping(value="/document/{documentId}", method = RequestMethod.GET)
	public DocumentModel getDocumentByDocumentId(@PathVariable("documentId") Integer documentId) {
		ServiceLogger.log(logTag, "In getDocumentByDocumentId: " + documentId);
		
		return documentService.findOne(documentId);
	}
	
	@RequestMapping(value="/documents/organization", params = {"organizationId", "fileTypeId", "limit"}, method = RequestMethod.GET)
	public List<DocumentModel> getDocumentsByOrganizationIdAndFileTypeId (@RequestParam("organizationId") Integer organizationId, 
			@RequestParam("fileTypeId") Integer fileTypeId, @RequestParam("limit") Integer limit) {
		ServiceLogger.log(logTag, "In getDocumentsByOrganizationIdAndFileTypeId: organizationId = " + organizationId + " and fileTypeId = " + fileTypeId);
		return documentService.findDocumentsByOrganizationIdAndFileTypeId(organizationId, fileTypeId, limit);
	}
	
	@RequestMapping(value="/document/organization", params = {"organizationId", "fileTypeId"}, method = RequestMethod.GET)
	public DocumentModel getMostRecentDocumentByFileTypeIdAndOrganizationId (@RequestParam("fileTypeId") Integer fileTypeId, @RequestParam("organizationId") Integer organizationId) {
		ServiceLogger.log(logTag, "in getMostRecentDocumentByFileTypeIdAndOrganizationId: fileTypeId = " + fileTypeId + " and organizationId = " + organizationId);
		return documentService.findMostRecentDocumentByFileTypeIdAndOrganizationId(fileTypeId, organizationId);
	}
	
	@RequestMapping(value="/document", method = RequestMethod.POST)
	public DocumentModel postDocument (@RequestBody DocumentModel doc, BindingResult result) throws DMCServiceException {
		ServiceLogger.log(logTag, "In postDocument " + doc.getDocumentName());
		validateSaveDocument(doc.getDocumentUrl(), result);
		return documentService.save(doc, result);
	}

	private void validateSaveDocument(String documentUrl, BindingResult result) {
		awsLinkValidator.validate(documentUrl, result);		
	}
	
	@RequestMapping(value="/document/{documentId}", method = RequestMethod.DELETE)
	public DocumentModel deleteDocument (@PathVariable("documentId") Integer documentId) {
		ServiceLogger.log(logTag, "In deleteDocument id = " + documentId);
		return documentService.delete(documentId);
	}
	
	@RequestMapping(value="/document/organization/count", params = {"organizationId", "fileTypeId"}, method = RequestMethod.GET)
	public Long countDocumentsByOrganizationIdAndFileTypeId (@RequestParam("organizationId") Integer organizationId, @RequestParam("fileTypeId") Integer fileTypeId) {
		return documentService.countDocumentsByOrganizationIdAndFileType(organizationId, fileTypeId);
	}
}
