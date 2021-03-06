package org.dmc.services.web.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.DMCServiceException;
import org.dmc.services.DMDIIDocumentService;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.DMDIIDocumentTagModel;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.security.SecurityRoles;
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
@PreAuthorize(SecurityRoles.REQUIRED_ROLE_DMDII_MEMBER)
public class DMDIIDocumentController {

	private final String logTag = DMDIIDocumentController.class.getName();

	@Inject
	DMDIIDocumentService dmdiiDocumentService;

	@Inject
	private AWSLinkValidator awsLinkValidator;

	@RequestMapping(value = "/dmdiidocuments/dmdiiProjectId", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public List<DMDIIDocumentModel> getDMDIIDocumentsByDMDIIProjectId(@RequestParam ("dmdiiProjectId") Integer dmdiiProjectId,
																		@RequestParam("page") Integer page,
																		@RequestParam("pageSize") Integer pageSize) throws DMCServiceException {
		ServiceLogger.log(logTag, "In getAllDMDIIDocumentsByDMDIIProjectId: >>>>>>>> " + dmdiiProjectId);

		return dmdiiDocumentService.getDMDIIDocumentsByDMDIIProject(dmdiiProjectId, page, pageSize);
	}

	@RequestMapping(value = "/dmdiidocument/{dmdiiDocumentId}", method = RequestMethod.GET)
	public DMDIIDocumentModel getDMDIIDocumentByDMDIIDocumentId(@PathVariable("dmdiiDocumentId") Integer dmdiiDocumentId) throws DMCServiceException {
		ServiceLogger.log(logTag, "In getDMDIIDocumentByDMDIIDocumentId: " + dmdiiDocumentId);

		return dmdiiDocumentService.findOne(dmdiiDocumentId);
	}

	@RequestMapping(value = "/dmdiidocuments", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public List<DMDIIDocumentModel> filter(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam Map<String, String> params) throws InvalidFilterParameterException, DMCServiceException {
		ServiceLogger.log(logTag, "In getAllDMDIIDocuments filter");
		return dmdiiDocumentService.filter(params, page, pageSize);
	}

	@RequestMapping(value = "/dmdiidocument", method = RequestMethod.POST, consumes = {"application/json"})
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIDocumentModel postDMDIIDocument (@RequestBody DMDIIDocumentModel doc, BindingResult result) throws DMCServiceException {
		ServiceLogger.log(logTag, "Post DMDIIDocument " + doc.getDocumentName());

		// validateSaveDocument(doc.getDocumentUrl(), result);
		return dmdiiDocumentService.save(doc, result);
	}

	@RequestMapping(value = "/dmdiidocuments/{documentId}", method = RequestMethod.PATCH)
	public DMDIIDocumentModel updateDMDIIDocument (@RequestBody DMDIIDocumentModel doc, @PathVariable("documentId") Integer documentId) {
		return dmdiiDocumentService.update(doc);
	}

	private void validateSaveDocument(String documentUrl, BindingResult result) {
		awsLinkValidator.validate(documentUrl, result);
	}

	@RequestMapping(value = "/dmdiidocuments/getAllTags", method = RequestMethod.GET)
	public List<DMDIIDocumentTagModel> getAllTags() {
		return dmdiiDocumentService.getAllTags();
	}

	@RequestMapping(value = "/dmdiidocuments/saveDocumentTag", method = RequestMethod.POST, consumes = {"application/json"})
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIDocumentTagModel postDmdiiDocuemntTag (@RequestBody DMDIIDocumentTagModel tag) {
		ServiceLogger.log(logTag, "Post DMDIIDocumentTag " + tag.getTagName());
		return dmdiiDocumentService.saveDocumentTag(tag);
	}

	@RequestMapping(value = "/staticdocument/{fileTypeId}", method = RequestMethod.GET)
	public DMDIIDocumentModel getMostRecentStaticDocumentByFileTypeId (@PathVariable("fileTypeId") Integer fileTypeId) throws DMCServiceException {
		ServiceLogger.log(logTag, "In getMostRecentStaticDocumentByFileTypeId: " + fileTypeId);
		return dmdiiDocumentService.findMostRecentStaticFileByFileTypeId(fileTypeId);
	}

	@RequestMapping(value = "/dmdiidocument/filetype", params = {"fileTypeId", "dmdiiProjectId"}, method = RequestMethod.GET)
	public DMDIIDocumentModel getMostRecentDocumentByFileTypeIdAndDMDIIProjectId (@RequestParam("fileTypeId") Integer fileTypeId, @RequestParam("dmdiiProjectId") Integer dmdiiProjectId) {
		ServiceLogger.log(logTag, "In getMostRecentDocumentByFileTypeIdAndDMDIIProjectId: fileType = " + fileTypeId + " dmdiiProject = " + dmdiiProjectId);
		return dmdiiDocumentService.findMostRecentDocumentByFileTypeIdAndDMDIIProjectId(fileTypeId, dmdiiProjectId);
	}

	@RequestMapping(value="/dmdiidocument/{dmdiiDocumentId}", method = RequestMethod.DELETE)
	public DMDIIDocumentModel deleteDMDIIDocument (@PathVariable("dmdiiDocumentId") Integer dmdiiDocumentId) {
		ServiceLogger.log(logTag, "In deleteDMDIIDocument id = " + dmdiiDocumentId);
		return dmdiiDocumentService.delete(dmdiiDocumentId);
	}


}
