package org.dmc.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.DMDIIProjectNewsModel;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMDIIProjectController {
	
	private final String logTag = DMDIIProjectController.class.getName();
	
	@Inject
	private DMDIIProjectService dmdiiProjectService;
	
	@RequestMapping(value = "/dmdiiprojects", params = {"page", "pageSize"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DMDIIProjectModel> filter(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam Map<String, String> params) throws InvalidFilterParameterException {
		ServiceLogger.log(logTag, "In filter");
		
		return dmdiiProjectService.filter(params, page, pageSize);
	}

	@RequestMapping(value = "/dmdiiprojects/member", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public List<DMDIIProjectModel> getDmdiiProjectsByDMDIIMemberId(@RequestParam("dmdiiMemberId") Integer dmdiiMemberId,
																		@RequestParam("page") Integer page,
																		@RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getDmdiiProjectsByDMDIIMemberId as member " + dmdiiMemberId);
		
		return dmdiiProjectService.findDmdiiProjectsByPrimeOrganizationId(dmdiiMemberId, page, pageSize);
	}
	
	@RequestMapping(value = "dmdiiProject/{id}", method = RequestMethod.GET)
	public @ResponseBody DMDIIProjectModel getDMDIIProject(@PathVariable Integer id) {
		return dmdiiProjectService.findOne(id);
	}
	
	@RequestMapping(value = "/dmdiiprojects/awardDate", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public List<DMDIIProjectModel> getDMDIIProjectsByAwardedDate(@RequestParam("awardedDate") Date awardedDate,
																	@RequestParam("page") Integer page,
																	@RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getDMDIIProjectsByStartDate: " + awardedDate);
		
		return dmdiiProjectService.findDMDIIProjectsByAwardedDate(awardedDate, page, pageSize);
	}
	
	@RequestMapping(value = "/dmdiiprojects/search", params = {"title", "page", "pageSize"}, method = RequestMethod.GET)
	public List<DMDIIProjectModel> getDmdiiProjectsByTitle(@RequestParam("title") String title,
															@RequestParam("page") Integer page,
															@RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getDmdiiProjectsByTitle: " + title);
		
		return dmdiiProjectService.findByTitle(title, page, pageSize);
	}
	
	@RequestMapping(value = "/dmdiiproject/contributingcompanies", params = {"projectId"}, method = RequestMethod.GET)
	public List<DMDIIMemberModel> getDMDIIProjectContributingCompaniesByDMDIIProjectId(@RequestParam("projectId") Integer projectId) {
		ServiceLogger.log(logTag, "In getDMDIIProjectContributingCompaniesByDMDIIProjectId: " + projectId);
		
		return dmdiiProjectService.findContributingCompanyByProjectId(projectId);
	}
	
	@RequestMapping(value = "/dmdiiProject/save", method = RequestMethod.POST)
	public DMDIIProjectModel saveDMDIIProject (@RequestBody DMDIIProjectModel project) {
		return dmdiiProjectService.save(project);
	}
	
	@RequestMapping(value = "/dmdiiProject/news", params = "limit", method = RequestMethod.GET)
	public List<DMDIIProjectNewsModel> getDmdiiProjectNews(@RequestParam("limit") Integer limit) {
		return dmdiiProjectService.getDmdiiProjectNews(limit);
	}
}
