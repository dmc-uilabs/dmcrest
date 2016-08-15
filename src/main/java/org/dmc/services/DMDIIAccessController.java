package org.dmc.services;

import javax.inject.Inject;

import org.dmc.services.data.models.UserRoleModel;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.dmc.services.security.SecurityRoles;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize(SecurityRoles.REQUIRED_ROLE_DMDII_MEMBER)
public class DMDIIAccessController {

	private final String logTag = DMDIIAccessController.class.getName();

	@Inject
	private UserRoleService userRoleService;

	@RequestMapping(value = "/dmdiiaccess/role", method = RequestMethod.GET)
	public UserRoleModel getRoleByDMDIIMemberId (@RequestParam("dmdiiMemberId") Integer dmdiiMemberId) {

		ServiceLogger.log(logTag, "In getRoleByDMDIIMemberId: " + dmdiiMemberId);

		return userRoleService.findByUserId(dmdiiMemberId);
	}

	@RequestMapping(value = "/dmdiiaccess/role", method = RequestMethod.POST)
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_ADMIN)
	public UserRoleModel postUserRole (@RequestBody UserRoleModel userRole) {
		boolean authorized = false;
		
		if (userRole.getRoleId() == 1) {
			authorized = PermissionEvaluationHelper.userHasRole(SecurityRoles.SUPERADMIN, 0);
		} else {
			authorized = PermissionEvaluationHelper.userHasRole(SecurityRoles.ADMIN, userRole.getOrganizationId());
		}
		
		if (!authorized) {
			throw new AccessDeniedException("403 Access Denied");
		}
		
		ServiceLogger.log(logTag, "Post UserRoleModel for user " + userRole.getRoleId());
		return userRoleService.save(userRole);
	}

}
