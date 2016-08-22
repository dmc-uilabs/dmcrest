package org.dmc.services.roleassignment;

import javax.inject.Inject;

import org.dmc.services.data.models.UserRoleAssignmentModel;
import org.dmc.services.exceptions.ArgumentNotFoundException;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.dmc.services.security.SecurityRoles;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRoleAssignmentController {

	@Inject
	private UserRoleAssignmentService userRoleAssignmentService;
	
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_ADMIN)
	@RequestMapping(value = "/userRole", method = RequestMethod.PUT)
	public UserRoleAssignmentModel updateRole(@RequestBody UserRoleAssignmentModel roleAssignment) throws ArgumentNotFoundException {
		if (roleAssignment.getRole().equals(SecurityRoles.SUPERADMIN) || !PermissionEvaluationHelper.userHasRole(SecurityRoles.ADMIN, roleAssignment.getOrganizationId())) {
			throw new AccessDeniedException("403 access denied");
		}
		
		return userRoleAssignmentService.grantRoleToUserForOrg(roleAssignment.getRole(), roleAssignment.getUserId(), roleAssignment.getOrganizationId());
	}
	
	@PreAuthorize(SecurityRoles.REQUIRED_ROLE_ADMIN)
	@RequestMapping(value = "/userRole", method = RequestMethod.DELETE)
	public void deleteRole(@RequestBody UserRoleAssignmentModel roleAssignment) {
		if (roleAssignment.getRole().equals(SecurityRoles.SUPERADMIN) || !PermissionEvaluationHelper.userHasRole(SecurityRoles.ADMIN, roleAssignment.getOrganizationId())) {
			throw new AccessDeniedException("403 access denied");
		}
		
		userRoleAssignmentService.deleteByUserIdAndOrganizationId(roleAssignment.getUserId(), roleAssignment.getOrganizationId());
	}
}
