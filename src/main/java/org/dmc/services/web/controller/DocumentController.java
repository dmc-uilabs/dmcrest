package org.dmc.services.web.controller;

import org.dmc.services.DMCServiceException;
import org.dmc.services.DocumentService;
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
	
	@RequestMapping(value="/documents", params = {"recent"}, method = RequestMethod.GET)
	public PagedResponse getDocuments (@RequestParam("recent") Integer recent, 
										@RequestParam(value = "page", defaultValue = "0") Integer page, 
										@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
										@RequestHeader(value = "AJP_eppn") String userEPPN,
										@RequestParam Map<String, String> params) throws DMCServiceException, InvalidFilterParameterException {
		List<? extends BaseModel> results = documentService.filter(params, recent, page, pageSize, userEPPN);
		Long count = documentService.count(params, userEPPN);
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
		return this.documentService.shareDocument(documentId, userId, dmdii);
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

	@RequestMapping(value = "/documents/tags", method = RequestMethod.GET)
	public List<DocumentTagModel> getDocumentTags() {
		return this.documentService.getAllTags();
	}
}
