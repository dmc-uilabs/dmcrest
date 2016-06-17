package org.dmc.services;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.models.DMDIIDocumentModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMDIIDocumentController {
	
	private final String logTag = DMDIIDocumentController.class.getName();
	
	@Inject
	DMDIIDocumentService dmdiiDocumentService;
	
	@RequestMapping(value = "/dmdiidocuments/dmdiiProjectId", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public List<DMDIIDocumentModel> getDMDIIDocumentsByDMDIIProjectId(@RequestParam ("dmdiiProjectId") Integer dmdiiProjectId,
																		@RequestParam("page") Integer page,
																		@RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getAllDMDIIDocumentsByDMDIIProjectId: " + dmdiiProjectId);
		
		return dmdiiDocumentService.getDMDIIDocumentsByDMDIIProject(dmdiiProjectId, page, pageSize);
	}
	
	@RequestMapping(value = "/dmdiidocument/{dmdiiDocumentId}", method = RequestMethod.GET)
	public DMDIIDocumentModel getDMDIIDocumentByDMDIIDocumentId(@PathVariable("dmdiiDocumentId") Integer dmdiiDocumentId) {
		ServiceLogger.log(logTag, "In getDMDIIDocumentByDMDIIDocumentId: " + dmdiiDocumentId);
		
		return dmdiiDocumentService.getDMDIIDocumentByDMDIIDocumentId(dmdiiDocumentId);
	}
	
	@RequestMapping(value = "/dmdiidocuments", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public List<DMDIIDocumentModel> getAllDMDIIProjectDocuments(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getAllDMDIIProjectDocuments");
		return dmdiiDocumentService.findPage(page, pageSize);
	}
}
