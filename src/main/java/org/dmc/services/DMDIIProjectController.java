package org.dmc.services;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.models.DMDIIProjectModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMDIIProjectController {
	
	private final String logTag = DMDIIProjectController.class.getName();
	
	@Inject
	private DMDIIProjectService dmdiiProjectService;

	@RequestMapping(value = "/dmdiiprojects/{partnerID}", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public List<DMDIIProjectModel> getAllDmdiiProjectsByDMDIIMemberId(@PathVariable("memberID") Integer memberID,
																		@RequestParam("page") Integer page,
																		@RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getAllDmdiiProjectsByDMDIIMemberId as member " + memberID);
		
		return dmdiiProjectService.findDmdiiProjectsByPrimeOrganizationId(memberID, page, pageSize);
	}
	
	@RequestMapping(value = "/dmdiiprojects/{awardedDate}", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public List<DMDIIProjectModel> getAllDMDIIProjectsByAwardedDate(@PathVariable("awardedDate") Date awardedDate,
																	@RequestParam("page") Integer page,
																	@RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getAllDMDIIProjectsByStartDate: " + awardedDate);
		
		return dmdiiProjectService.findDMDIIProjectsByAwardedDate(awardedDate, page, pageSize);
	}
	
	@RequestMapping(value = "/dmdiiprojects/{projectStatusId}", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public List<DMDIIProjectModel> getAllDMDIIProjectsByProjectStatusId(@PathVariable("projectStatusId") Integer projectStatusId,
																		@RequestParam("page") Integer page,
																		@RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getAllDMDIIProjectsByProjectStatusId: " + projectStatusId);
		
		return dmdiiProjectService.findDMDIIProjectsByProjectStatusId(projectStatusId, page, pageSize);
	}
	
	@RequestMapping(value = "/dmdiiprojects", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public List<DMDIIProjectModel> getAllDMDIIProjects(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getAllDMDIIProjects");
		
		return dmdiiProjectService.getAllDMDIIProjects(page, pageSize);
	}
	
	@RequestMapping(value = "/dmdiiprojects/search", params = {"title", "page", "pageSize"}, method = RequestMethod.GET)
	public List<DMDIIProjectModel> getDmdiiProjectsByTitle(@RequestParam("title") String title,
															@RequestParam("page") Integer page,
															@RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getDmdiiProjectsByTitle: " + title);
		
		return dmdiiProjectService.findByTitle(title, page, pageSize);
	}
}
