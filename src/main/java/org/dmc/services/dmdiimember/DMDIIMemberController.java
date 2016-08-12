package org.dmc.services.dmdiimember;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.DMDIIMemberEventService;
import org.dmc.services.DMDIIMemberNewsService;
import org.dmc.services.data.models.BaseModel;
import org.dmc.services.data.models.DMDIIMemberAutocompleteModel;
import org.dmc.services.data.models.DMDIIMemberEventModel;
import org.dmc.services.data.models.DMDIIMemberMapEntryModel;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIMemberNewsModel;
import org.dmc.services.data.models.PagedResponse;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.security.SecurityRoles;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize(SecurityRoles.REQUIRED_ROLE_DMDII_MEMBER)
public class DMDIIMemberController {

	@Inject
	private DMDIIMemberService dmdiiMemberService;

	@Inject
	private DMDIIMemberEventService dmdiiMemberEventService;

	@Inject
	private DMDIIMemberNewsService dmdiiMemberNewsService;

	@RequestMapping(value = "/dmdiiMember", params = {"page", "pageSize"}, method = RequestMethod.GET)
	public PagedResponse filter(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam Map<String, String> params) throws InvalidFilterParameterException {
		List<? extends BaseModel> results = dmdiiMemberService.filter(params, page, pageSize);
		Long count = dmdiiMemberService.count(params);
		return new PagedResponse(count, results);
	}

	@RequestMapping(value = "/dmdiiMember/mapEntry", method = RequestMethod.GET)
	public List<DMDIIMemberMapEntryModel> getMapEntries() {
		return dmdiiMemberService.getMapEntries();
	}

	@RequestMapping(value = "/dmdiiMember/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DMDIIMemberModel getMember(@PathVariable Integer id) {
		return dmdiiMemberService.findOne(id);
	}

	@RequestMapping(value = "/dmdiiMember/save", method = RequestMethod.POST)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_SUPERADMIN)
	public DMDIIMemberModel saveDmdiiMember(@RequestBody DMDIIMemberModel member) {
		return dmdiiMemberService.save(member);
	}

	@RequestMapping(value = "/dmdiiMember/search", method = RequestMethod.GET,params = {"page", "pageSize", "name"})
	public PagedResponse findMembersByName(@RequestParam("page") Integer page,
																@RequestParam("pageSize") Integer pageSize,
																@RequestParam("name") String name) {
		List<? extends BaseModel> results = dmdiiMemberService.findByName(name, page, pageSize);
		Long count = dmdiiMemberService.countByName(name);
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

	@RequestMapping(value = "/dmdiiMember/all", method = RequestMethod.GET)
	public List<DMDIIMemberAutocompleteModel> getAllMembers() {
		return dmdiiMemberService.getAllMembers();
	}

}
