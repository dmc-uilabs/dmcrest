package org.dmc.services;

import java.util.Date;
import java.util.List;

import org.dmc.services.models.DMDIIProjectModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMDIIProjectController {
	
	private final String logTag = DMDIIProjectController.class.getName();
	
	DMDIIProjectService dmdiiProjectService;

	@RequestMapping(value = "/dmdiiprojects/{partnerID}", method = RequestMethod.GET)
	public List<DMDIIProjectModel> getAllDmdiiProjectsByDMDIIMemberId(@PathVariable("memberID") Integer memberID) {
		ServiceLogger.log(logTag, "In getAllDmdiiProjectsByDMDIIMemberId as member " + memberID);
		
		return dmdiiProjectService.findDmdiiProjectsByPrimeOrganizationId(memberID);
	}
	
	@RequestMapping(value = "/dmdiiprojects/{startDate}", method = RequestMethod.GET)
	public List<DMDIIProjectModel> getAllDMDIIProjectsByStartDate(@PathVariable("startDate") Date startDate) {
		ServiceLogger.log(logTag, "In getAllDMDIIProjectsByStartDate: " + startDate);
		
		return dmdiiProjectService.findDMDIIProjectsByStartDate(startDate);
	}
}
