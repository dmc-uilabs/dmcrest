package org.dmc.services;

import javax.inject.Inject;

import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.security.SecurityRoles;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize(SecurityRoles.REQUIRED_ROLE_DMDII_MEMBER)
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
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIDocumentModel postDMDIIPortalImage (@RequestBody DMDIIDocumentModel doc) throws DMCServiceException {
		ServiceLogger.log(logTag, "postDMDIIPortalImage " + doc.getDocumentName());
		return dmdiiDocumentService.save(doc);
	}
}
