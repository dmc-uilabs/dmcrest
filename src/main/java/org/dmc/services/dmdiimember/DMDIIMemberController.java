package org.dmc.services.dmdiimember;

import org.dmc.services.DMDIIMemberEventService;
import org.dmc.services.DMDIIMemberNewsService;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.models.BaseModel;
import org.dmc.services.data.models.DMDIIMemberAutocompleteModel;
import org.dmc.services.data.models.DMDIIMemberEventModel;
import org.dmc.services.data.models.DMDIIMemberMapEntryModel;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIMemberNewsModel;
import org.dmc.services.data.models.PagedResponse;
import org.dmc.services.dmdiimember.DMDIIMemberService.DuplicateDMDIIMemberException;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.security.UserPrincipal;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.collections.CollectionUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize(SecurityRoles.REQUIRED_ROLE_DMDII_MEMBER)
public class DMDIIMemberController {

	private final String logTag = DMDIIMemberController.class.getName();

	@Inject
	private DMDIIMemberService dmdiiMemberService;

	@Inject
	private DMDIIMemberEventService dmdiiMemberEventService;

	@Inject
	private DMDIIMemberNewsService dmdiiMemberNewsService;

	@RequestMapping(value = "/dmdiiMember", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public PagedResponse filter(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam(value="tier", required=false) String[] tiers, @RequestParam(value="type", required=false) String[] types, @RequestParam(value="activeProjects", required=false) String[] activeProjects) throws InvalidFilterParameterException {
		List<? extends BaseModel> results = dmdiiMemberService.filter(page, pageSize, tiers, types, activeProjects);

		Long count = dmdiiMemberService.count(tiers, types, activeProjects, null);

		return new PagedResponse(count, results);
	}

	@RequestMapping(value = "/dmdiiMember/mapEntry", method = RequestMethod.GET)
	public List<DMDIIMemberMapEntryModel> getMapEntries() {
		ServiceLogger.log(logTag, "In getMapEntries as user " + ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
		return dmdiiMemberService.getMapEntries();
	}

	@RequestMapping(value = "/dmdiiMember/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DMDIIMemberModel getMember(@PathVariable Integer id) {
		return dmdiiMemberService.findOne(id);
	}

	@RequestMapping(value = "/dmdiiMember/save", method = RequestMethod.POST)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_ADMIN)
	public DMDIIMemberModel saveDmdiiMember(@RequestBody DMDIIMemberModel member) throws DuplicateDMDIIMemberException {
		if(!PermissionEvaluationHelper.userHasRole(SecurityRoles.SUPERADMIN, 0)) {
			if (!PermissionEvaluationHelper.userHasRole(SecurityRoles.ADMIN, member.getOrganization().getId())) {
				throw new AccessDeniedException("403 Access denied");
			}
		}

		// Only a superadmin may create a new dmdiiMember
		if (member.getId() == null && !PermissionEvaluationHelper.userHasRole(SecurityRoles.SUPERADMIN, 0)) {
			throw new AccessDeniedException("403 Access denied");
		}

		// If user is not a superadmin, only certain fields may be updated
		if (!PermissionEvaluationHelper.userHasRole(SecurityRoles.SUPERADMIN, 0)) {
			DMDIIMemberModel existingMember = dmdiiMemberService.findOne(member.getId());
			existingMember.getOrganization().setAreasOfExpertise(member.getOrganization().getAreasOfExpertise());
			existingMember.getOrganization().setDesiredAreasOfExpertise(member.getOrganization().getDesiredAreasOfExpertise());
			existingMember.setContacts(member.getContacts());
			member = existingMember;
		}

		return dmdiiMemberService.save(member);
	}

	@RequestMapping(value = "/dmdiiMember/search", method = RequestMethod.GET,params = {"page", "pageSize", "name"})
	public PagedResponse findMembersByName(@RequestParam("page") Integer page,
																@RequestParam("pageSize") Integer pageSize,
																@RequestParam("name") String name,
																@RequestParam(value="tier", required=false) String[] tiers,
																@RequestParam(value="type", required=false) String[] types,
																@RequestParam(value="activeProjects", required=false) String[] activeProjects) throws InvalidFilterParameterException {
		if(!"".equals(name)){
			ServiceLogger.log(logTag, "In findMembersByName: " + name + " as user " + ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
		}
		List<? extends BaseModel> results = dmdiiMemberService.findByNameOrTags(name, page, pageSize, tiers, types, activeProjects);

		Long count = dmdiiMemberService.count(tiers, types, activeProjects, name);

		return new PagedResponse(count, results);
	}

	@RequestMapping(value = "/dmdiiMember/news", params = "limit", method = RequestMethod.GET)
	public List<DMDIIMemberNewsModel> getDmdiiMemberNews(@RequestParam("limit") Integer limit) {
		return dmdiiMemberService.getDmdiiMemberNews(limit);
	}

	@RequestMapping(value = "/dmdiiMember/events", params = "limit", method = RequestMethod.GET)
	public List<DMDIIMemberEventModel> getDmdiiMemberEvents(@RequestParam("limit") Integer limit) {
		return dmdiiMemberService.getDmdiiMemberEvents(limit);
	}

	@RequestMapping(value = "/dmdiiMember/events", method = RequestMethod.POST)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIMemberEventModel saveDMDIIMemberEvent (@RequestBody DMDIIMemberEventModel memberEvent) {
		return dmdiiMemberEventService.save(memberEvent);
	}

	@RequestMapping(value = "/dmdiiMember/news", method = RequestMethod.POST)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIMemberNewsModel saveDMDIIMemberNews (@RequestBody DMDIIMemberNewsModel memberNews) {
		return dmdiiMemberNewsService.save(memberNews);
	}

	@RequestMapping(value = "/dmdiiMember/events/{eventId}", method = RequestMethod.DELETE)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public void deleteEvent(@PathVariable("eventId") Integer eventId) {
		dmdiiMemberEventService.delete(eventId);
	}

	@RequestMapping(value = "/dmdiiMember/news/{newsId}", method = RequestMethod.DELETE)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public void deleteNews(@PathVariable("newsId") Integer newsId) {
		dmdiiMemberNewsService.delete(newsId);
	}

	@RequestMapping(value = "/dmdiiMember/all", method = RequestMethod.GET)
	public List<DMDIIMemberAutocompleteModel> getAllMembers() {
		return dmdiiMemberService.getAllMembers();
	}

}
