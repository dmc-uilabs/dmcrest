package org.dmc.services;

import java.util.Date;
import java.util.List;

import org.dmc.services.data.models.DMDIIProjectModel;
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
	
	@RequestMapping(value = "/dmdiiprojects/{awardedDate}", method = RequestMethod.GET)
	public List<DMDIIProjectModel> getAllDMDIIProjectsByAwardedDate(@PathVariable("awardedDate") Date awardedDate) {
		ServiceLogger.log(logTag, "In getAllDMDIIProjectsByStartDate: " + awardedDate);
		
		return dmdiiProjectService.findDMDIIProjectsByAwardedDate(awardedDate);
	}
	
	@RequestMapping(value = "/dmdiiprojects/{projectStatusId}", method = RequestMethod.GET)
	public List<DMDIIProjectModel> getAllDMDIIProjectsByProjectStatusId(@PathVariable("projectStatusId") Integer projectStatusId) {
		ServiceLogger.log(logTag, "In getAllDMDIIProjectsByProjectStatusId: " + projectStatusId);
		
		return dmdiiProjectService.findDMDIIProjectsByProjectStatusId(projectStatusId);
	}
}
