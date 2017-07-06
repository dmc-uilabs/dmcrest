package org.dmc.services.web.controller;

import org.dmc.services.DMDIIProjectEventsService;
import org.dmc.services.DMDIIProjectNewsService;
import org.dmc.services.DMDIIProjectService;
import org.dmc.services.DMDIIProjectUpdateService;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.models.BaseModel;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIProjectEventModel;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.DMDIIProjectNewsModel;
import org.dmc.services.data.models.DMDIIProjectUpdateModel;
import org.dmc.services.data.models.PagedResponse;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.security.UserPrincipal;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize(SecurityRoles.REQUIRED_ROLE_DMDII_MEMBER)
public class DMDIIProjectController {

	private final String logTag = DMDIIProjectController.class.getName();

	@Inject
	private DMDIIProjectService dmdiiProjectService;

	@Inject
	private DMDIIProjectEventsService dmdiiProjectEventsService;

	@Inject
	private DMDIIProjectNewsService dmdiiProjectNewsService;

	@Inject
	private DMDIIProjectUpdateService dmdiiProjectUpdateService;

	@RequestMapping(value = "/dmdiiprojects", params = {"page", "pageSize"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public PagedResponse filter(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam Map<String, String> params) throws InvalidFilterParameterException {
		ServiceLogger.log(logTag, "In filter");

		List<? extends BaseModel> results = dmdiiProjectService.filter(params, page, pageSize);
		Long count = dmdiiProjectService.count(params);
		return new PagedResponse(count, results);
	}

	@RequestMapping(value = "/dmdiiprojects/member", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public PagedResponse getDmdiiProjectsByDMDIIMemberId(@RequestParam("dmdiiMemberId") Integer dmdiiMemberId,
																		@RequestParam("page") Integer page,
																		@RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getDmdiiProjectsByDMDIIMemberId as member " + dmdiiMemberId);

		List<DMDIIProjectModel> results = dmdiiProjectService.findDmdiiProjectsByPrimeOrganizationId(dmdiiMemberId, page, pageSize);
		Long count = dmdiiProjectService.countDmdiiProjectsByPrimeOrganizationId(dmdiiMemberId);
		return new PagedResponse(count, results);
	}

	@RequestMapping(value = "/dmdiiprojects/member/active", method = RequestMethod.GET)
	public PagedResponse getActiveDMDIIProjectsByDMDIIMemberId(@RequestParam("dmdiiMemberId") Integer dmdiiMemberId,
																		@RequestParam("page") Integer page,
																		@RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getActiveDMDIIProjectsByDMDIIMemberId as member " + dmdiiMemberId);

		List<DMDIIProjectModel> results = dmdiiProjectService.findDMDIIProjectsByPrimeOrganizationIdAndIsActive(dmdiiMemberId, page, pageSize);
		List<DMDIIProjectModel> ccResults = dmdiiProjectService.findActiveDMDIIProjectsByContributingCompany(dmdiiMemberId);
		List<DMDIIProjectModel> totalResults = new ArrayList<DMDIIProjectModel>(results);
		totalResults.addAll(ccResults);
		Long count = new Long(totalResults.size());
		return new PagedResponse(count, totalResults);
	}

	@RequestMapping(value = "dmdiiProject/{id}", method = RequestMethod.GET)
	public DMDIIProjectModel getDMDIIProject(@PathVariable Integer id) {
		return dmdiiProjectService.findOne(id);
	}

	@RequestMapping(value = "/dmdiiprojects/awardDate", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public PagedResponse getDMDIIProjectsByAwardedDate(@RequestParam("awardedDate") Date awardedDate,
																	@RequestParam("page") Integer page,
																	@RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getDMDIIProjectsByStartDate: " + awardedDate);

		List<? extends BaseModel> results = dmdiiProjectService.findDMDIIProjectsByAwardedDate(awardedDate, page, pageSize);
		Long count = dmdiiProjectService.countDMDIIProjectsByAwardedDate(awardedDate);
		return new PagedResponse(count, results);
	}

	@RequestMapping(value = "/dmdiiprojects/search", params = {"title", "page", "pageSize"}, method = RequestMethod.GET)
	public PagedResponse getDmdiiProjectsByTitle(@RequestParam("title") String title,
															@RequestParam("page") Integer page,
															@RequestParam("pageSize") Integer pageSize) {
		ServiceLogger.log(logTag, "In getDmdiiProjectsByTitle: " + title + " as user " + ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());

		List<? extends BaseModel> results = dmdiiProjectService.findByTitle(title, page, pageSize);
		Long count = dmdiiProjectService.countByTitle(title);
		return new PagedResponse(count, results);
	}

	@RequestMapping(value = "/dmdiiproject/contributingcompanies", params = {"projectId"}, method = RequestMethod.GET)
	public List<DMDIIMemberModel> getDMDIIProjectContributingCompaniesByDMDIIProjectId(@RequestParam("projectId") Integer projectId) {
		ServiceLogger.log(logTag, "In getDMDIIProjectContributingCompaniesByDMDIIProjectId: " + projectId);

		return dmdiiProjectService.findContributingCompanyByProjectId(projectId);
	}

	@RequestMapping(value = "/dmdiiProject/save", method = RequestMethod.POST)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIProjectModel saveDMDIIProject (@RequestBody DMDIIProjectModel project) {
		if (project.getId() == null) {
			return dmdiiProjectService.save(project);
		} else {
			return dmdiiProjectService.update(project);
		}
	}

	@RequestMapping(value = "/dmdiiProjects/{id}", method = RequestMethod.PATCH)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIProjectModel update (@RequestBody DMDIIProjectModel project) {
		return dmdiiProjectService.update(project);
	}

	@RequestMapping(value = "/dmdiiProjects/{id}", method = RequestMethod.DELETE)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public void delete(@PathVariable("id") Integer id) {
		dmdiiProjectService.delete(id);
	}

	@RequestMapping(value = "/dmdiiProject/news", params = "limit", method = RequestMethod.GET)
	public List<DMDIIProjectNewsModel> getDmdiiProjectNews(@RequestParam("limit") Integer limit) {
		return dmdiiProjectService.getDmdiiProjectNews(limit);
	}

	@RequestMapping(value = "/dmdiiProject/events", params = "limit", method = RequestMethod.GET)
	public List<DMDIIProjectEventModel> getDmdiiProjectEvents(@RequestParam("limit") Integer limit) {
		ServiceLogger.log(logTag, "In getDmdiiProjectEvents as user " + ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
		return dmdiiProjectService.getDmdiiProjectEvents(limit);
	}

	@RequestMapping(value = "/dmdiiProject/events", method = RequestMethod.POST)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIProjectEventModel saveDMDIIProjectEvent (@RequestBody DMDIIProjectEventModel projectEvent) {
		return dmdiiProjectEventsService.save(projectEvent);
	}

	@RequestMapping(value = "/dmdiiProject/news", method = RequestMethod.POST)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIProjectNewsModel saveDMDIIProjectNews (@RequestBody DMDIIProjectNewsModel projectNews) {
		return dmdiiProjectNewsService.save(projectNews);
	}

	@RequestMapping(value = "/dmdiiProject/events/{id}", method = RequestMethod.DELETE)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public void deleteEvent(@PathVariable("id") Integer id) {
		dmdiiProjectEventsService.delete(id);
	}

	@RequestMapping(value = "/dmdiiProject/news/{id}", method = RequestMethod.DELETE)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public void deleteNews(@PathVariable("id") Integer id) {
		dmdiiProjectNewsService.delete(id);
	}

	@RequestMapping(value = "/dmdiiProjectUpdate", params = {"limit", "projectId"}, method = RequestMethod.GET)
	public List<DMDIIProjectUpdateModel> getDMDIIProjectUpdates (@RequestParam("limit") Integer limit, @RequestParam("projectId") Integer projectId) {
		ServiceLogger.log(logTag, "In getDMDIIProjectUpdates");
		return dmdiiProjectUpdateService.getDMDIIProjectUpdatesByProjectId(limit, projectId);
	}

	@RequestMapping(value = "/dmdiiProjectUpdate", method = RequestMethod.POST)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIProjectUpdateModel saveDMDIIProjectUpdate (@RequestBody DMDIIProjectUpdateModel projectUpdate) {
		ServiceLogger.log(logTag, "In saveDMDIIProjectUpdate");
		return dmdiiProjectUpdateService.save(projectUpdate);
	}

	@RequestMapping(value = "/dmdiiProjectUpdate/{id}", method = RequestMethod.DELETE)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public void deleteUpdate(@PathVariable("id") Integer id) {
		dmdiiProjectUpdateService.delete(id);
	}

	@RequestMapping(value = "/contributingCompanies", params = "dmdiiMemberId", method = RequestMethod.GET)
	public List<DMDIIProjectModel> getDMDIIProjectsByContributingCompany (@RequestParam ("dmdiiMemberId") Integer dmdiiMemberId) {
		ServiceLogger.log(logTag, "In getDMDIIProjectsByContributingCompany: " + dmdiiMemberId);
		return dmdiiProjectService.findDMDIIProjectsByContributingCompany(dmdiiMemberId);
	}
 }
