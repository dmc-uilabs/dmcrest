package org.dmc.services.dmdiimember;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.models.DMDIIMemberModel;
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

	@RequestMapping(value = "/dmdiiMember/type/{typeId}", params = {"page", "pageSize"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<DMDIIMemberModel> getMemberByType(@PathVariable Integer typeId, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
		return dmdiiMemberService.findByTypeId(typeId, page, pageSize);
	}

	@RequestMapping(value = "/dmdiiMember/category/{categoryId}", params = {"page", "pageSize"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<DMDIIMemberModel> getMemberByCategory(@PathVariable Integer categoryId, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
		return dmdiiMemberService.findByCategoryId(categoryId, page, pageSize);
	}

	@RequestMapping(value = "/dmdiiMember/tier/{tier}", params = {"page", "pageSize"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<DMDIIMemberModel> getMemberByTier(@PathVariable Integer tier, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
		return dmdiiMemberService.findByTier(tier, page, pageSize);
	}

	@RequestMapping(value = "/dmdiiMember/create", method = RequestMethod.POST)
	public @ResponseBody DMDIIMember saveDmdiiMember(@RequestBody DMDIIMember member) {
		return dmdiiMemberService.save(member);
	}

}
