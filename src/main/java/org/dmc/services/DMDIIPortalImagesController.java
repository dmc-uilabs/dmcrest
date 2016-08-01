package org.dmc.services;

import javax.inject.Inject;

import org.dmc.services.data.models.DMDIIDocumentModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class DMDIIPortalImagesController {

	private final String logTag = DMDIIPortalImagesController.class.getName();
	
	@Inject
	DMDIIDocumentService dmdiiDocumentService;
	
	@RequestMapping(value = "dmdiiportalimage/{id}", method = RequestMethod.GET)
	public DMDIIDocumentModel getDMDIIPortalImageById (@PathVariable("id") Integer id) throws DMCServiceException {
		ServiceLogger.log(logTag, "In getDMDIIPortalImageById: " + id);
		
		return dmdiiDocumentService.findOne(id);
	}
	
	@RequestMapping(value = "/dmdiiportalimage", method = RequestMethod.POST)
	public DMDIIDocumentModel postDMDIIPortalImage (@RequestBody DMDIIDocumentModel doc) throws DMCServiceException {
		ServiceLogger.log(logTag, "postDMDIIPortalImage " + doc.getDocumentName());
		return dmdiiDocumentService.save(doc);
	}
}
