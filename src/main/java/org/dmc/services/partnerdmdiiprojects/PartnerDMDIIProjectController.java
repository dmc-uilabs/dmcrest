package org.dmc.services.partnerdmdiiprojects;

import java.util.List;

import org.dmc.services.ServiceLogger;
import org.dmc.services.models.DMDIIProjectModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PartnerDMDIIProjectController {
	
	private final String logTag = PartnerDMDIIProjectController.class.getName();
	
	DMDIIProjectManager dmdiiProjectManager;

	@RequestMapping(value = "/partnerdmdiiprojects/{partnerID}", method = RequestMethod.GET)
	public List<DMDIIProjectModel> getAllPartnerDmdiiProjects(@PathVariable("partnerID") Integer partnerID) {
		ServiceLogger.log(logTag, "In getAllPartnerDmdiiProjects as partner " + partnerID);
		
		return dmdiiProjectManager.findDmdiiProjectsByPrimeOrganizationId(partnerID);
	}
}
