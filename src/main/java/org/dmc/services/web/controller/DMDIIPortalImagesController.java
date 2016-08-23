package org.dmc.services.web.controller;

import javax.inject.Inject;

import org.dmc.services.DMCServiceException;
import org.dmc.services.DMDIIDocumentService;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.web.validator.AWSLinkValidator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
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
	
	@Inject
	private AWSLinkValidator awsLinkValidator;
	
	@RequestMapping(value = "dmdiiportalimage/{id}", method = RequestMethod.GET)
	public DMDIIDocumentModel getDMDIIPortalImageById (@PathVariable("id") Integer id) throws DMCServiceException {
		ServiceLogger.log(logTag, "In getDMDIIPortalImageById: " + id);
		
		return dmdiiDocumentService.findOne(id);
	}
	
	@RequestMapping(value = "/dmdiiportalimage", method = RequestMethod.POST)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIDocumentModel postDMDIIPortalImage (@RequestBody DMDIIDocumentModel doc, BindingResult result) throws DMCServiceException {
		ServiceLogger.log(logTag, "postDMDIIPortalImage " + doc.getDocumentName());
		
		validateSaveDocument(doc.getDocumentUrl(), result);
		return dmdiiDocumentService.save(doc, result);
	}

	private void validateSaveDocument(String documentUrl, BindingResult result) {
		awsLinkValidator.validate(documentUrl, result);		
	}
}
