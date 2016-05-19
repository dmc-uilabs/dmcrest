package org.dmc.services.dmdiimember;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMDIIMemberController {
	
	@Inject
	private DMDIIMemberService dmdiiMemberService;
	
	@RequestMapping(value = "/dmdiiMember", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<DMDIIMember> getPage() {
		return dmdiiMemberService.findPage(0, 50);
	}

}
