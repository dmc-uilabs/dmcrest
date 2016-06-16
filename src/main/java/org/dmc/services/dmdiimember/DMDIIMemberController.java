package org.dmc.services.dmdiimember;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.exceptions.MissingParameterException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMDIIMemberController {

	@Inject
	private DMDIIMemberService dmdiiMemberService;

	@RequestMapping(value = "/dmdiiMember", params = {"page", "pageSize"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<DMDIIMemberModel> getPage(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
		return dmdiiMemberService.findPage(page, pageSize);
	}

	@RequestMapping(value = "/dmdiiMember/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody DMDIIMemberModel getMember(@PathVariable Integer id) {
		return dmdiiMemberService.findOne(id);
	}

	@RequestMapping(value = "/dmdiiMember/type", method = RequestMethod.GET, params = {"page", "pageSize"})
	public @ResponseBody List<DMDIIMemberModel> getMembersByType(@RequestParam(value = "categoryId", required = false) Integer categoryId,
																@RequestParam(value = "tier", required = false) Integer tier,
																@RequestParam("page") Integer page,
																@RequestParam("pageSize") Integer pageSize) throws MissingParameterException {
		if (categoryId == null && tier == null) {
			throw new MissingParameterException("No request parameter found for either categoryId or tier");
		}
		
		return dmdiiMemberService.findByType(categoryId, tier, page, pageSize);
	}

	@RequestMapping(value = "/dmdiiMember/save", method = RequestMethod.POST)
	public @ResponseBody DMDIIMember saveDmdiiMember(@RequestBody DMDIIMember member) {
		return dmdiiMemberService.save(member);
	}

	@RequestMapping(value = "/dmdiiMember/search", method = RequestMethod.GET,params = {"page", "pageSize", "name"})
	public @ResponseBody List<DMDIIMemberModel> findMembersByName(@RequestParam("page") Integer page,
																@RequestParam("pageSize") Integer pageSize,
																@RequestParam("name") String name) {
		return dmdiiMemberService.findByName(name, page, pageSize);
	}

}
