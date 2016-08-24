package org.dmc.services.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.DMCServiceException;
import org.dmc.services.DMDIIQuickLinkService;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.models.DMDIIQuickLinkModel;
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
public class DMDIIQuickLinkController {
	
	private final String logTag = DMDIIQuickLinkController.class.getName();
	
	@Inject
	DMDIIQuickLinkService dmdiiQuickLinkService;
	
	@Inject
	private AWSLinkValidator awsLinkValidator;
	
	@RequestMapping(value = "/dmdiiquicklink/{id}", method = RequestMethod.GET)
	public DMDIIQuickLinkModel getDMDIIQuickLinkById (@PathVariable("id") Integer id) throws DMCServiceException {
		ServiceLogger.log(logTag, "In getDMDIIQuickLinkById: " + id);
		
		return dmdiiQuickLinkService.findOne(id);
	}
	
	@RequestMapping(value = "/dmdiiquicklink", method = RequestMethod.POST)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIQuickLinkModel postDMDIIQuickLink (@RequestBody DMDIIQuickLinkModel link, BindingResult result) throws DMCServiceException {
		ServiceLogger.log(logTag, "postDMDIIQuickLink");
		
		if(link.getDoc() != null) {
			validateSaveDocument(link.getDoc().getDocumentUrl(), result);
		}
		return dmdiiQuickLinkService.save(link, result);
	}

	private void validateSaveDocument(String documentUrl, BindingResult result) {
		awsLinkValidator.validate(documentUrl, result);		
	}
	
	@RequestMapping(value = "/dmdiiquicklink", params = "limit", method = RequestMethod.GET)
	public List<DMDIIQuickLinkModel> getDMDIIQuickLinks (@RequestParam("limit") Integer limit) throws DMCServiceException {
		return dmdiiQuickLinkService.getDMDIIQuickLinks(limit);
	}

}
