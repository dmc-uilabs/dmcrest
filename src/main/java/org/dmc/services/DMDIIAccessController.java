package org.dmc.services;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.models.OrganizationUserModel;
import org.dmc.services.data.models.UserRoleModel;
import org.springframework.web.bind.annotation.RequestBody;
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
	private OrganizationUserService organizationUserService;
	
	@RequestMapping(value = "/dmdiiaccess/role", method = RequestMethod.GET)
	public UserRoleModel getRoleByDMDIIMemberId (@RequestParam("dmdiiMemberId") Integer dmdiiMemberId) {
		
		ServiceLogger.log(logTag, "In getRoleByDMDIIMemberId: " + dmdiiMemberId);
		
		return userRoleService.findByUserId(dmdiiMemberId);
	}
	
	@RequestMapping(value = "/dmdiiaccess/role", method = RequestMethod.POST)
	public UserRoleModel postUserRole (@RequestBody UserRoleModel userRole) {
		ServiceLogger.log(logTag, "Post UserRoleModel for user " + userRole.getRoleId());
		return userRoleService.save(userRole);
	}
	
	@RequestMapping(value = "/user/verify", method = RequestMethod.GET)
	public List<OrganizationUserModel> getUsersByOrganizationId(@RequestParam("organizationId") Integer organizationId) {
		ServiceLogger.log(logTag, "In getUsersByOrganizationId: " + organizationId);
		return organizationUserService.getUsersByOrganizationId(organizationId);
	}
}
