package org.dmc.services;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.DMDIIDocumentTagModel;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	public List<DMDIIDocumentModel> filter(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam Map<String, String> params) throws InvalidFilterParameterException {
		ServiceLogger.log(logTag, "In getAllDMDIIDocuments filter");
		return dmdiiDocumentService.filter(params, page, pageSize);
	}

	@RequestMapping(value = "/dmdiidocuments/undeleted", params = {"page", "pageSize"}, method = RequestMethod.GET, produces = {"application/json"})
	public List<DMDIIDocumentModel> getUndeletedDMDIIDocuments(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getUndeletedDMDIIDocuments");
		return dmdiiDocumentService.getUndeletedDMDIIDocuments(page, pageSize);
	}

	@RequestMapping(value = "/dmdiidocuments/save", method = RequestMethod.POST, consumes = {"application/json"})
	public DMDIIDocumentModel postDMDIIDocument (@RequestBody DMDIIDocumentModel doc) {
		ServiceLogger.log(logTag, "Post DMDIIDocument " + doc.getDocumentName());
		return dmdiiDocumentService.save(doc);
	}

	@RequestMapping(value = "/dmdiidocuments/saveDocumentTag", method = RequestMethod.POST, consumes = {"application/json"})
	public DMDIIDocumentTagModel postDmdiiDocuemntTag (@RequestBody DMDIIDocumentTagModel tag) {
		ServiceLogger.log(logTag, "Post DMDIIDocumentTag " + tag.getTagName());
		return dmdiiDocumentService.saveDocumentTag(tag);
	}
}
