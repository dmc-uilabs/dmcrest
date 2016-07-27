package org.dmc.services;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DMDIIAccessController {

	private final String logTag = DMDIIAccessController.class.getName();
	
	@Inject
	private UserRoleService userRoleService;
	
	@Inject
	private DMDIIDocumentService dmdiiDocumentService;
	
	@RequestMapping(value = "/dmdiiaccess/role", method = RequestMethod.GET)
	public String getRoleByDMDIIMemberId (@RequestParam("dmdiiMemberId") Integer dmdiiMemberid) {
		
		ServiceLogger.log(logTag, "In getRoleByDMDIIMemberId: " + dmdiiMemberid);
		
		return userService.getAccessLevel(dmdiiMemberid);
	}
}
