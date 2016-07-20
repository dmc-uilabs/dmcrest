package org.dmc.services.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;

public class PermissionEvaluationHelper {
	
	public static boolean userHasRole(String requiredRole, Integer tenantId) {
		UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<String> userRoles = new ArrayList<String>();
		if (user.hasAuthority(SecurityRoles.SUPERADMIN)) {
			return true;
		}
		String assignedRole = user.getRole(tenantId);
		
		if (assignedRole == null) {
			return false;
		}
		
		userRoles.addAll(getInheritedRolesForRole(requiredRole));
		return userRoles.contains(requiredRole);
	}
	
	public static Set<String> getInheritedRolesForRole(String role) {
		Set<String> completeRoleSetForRole = new HashSet<String>();
		
		switch (role) {
		case SecurityRoles.SUPERADMIN:
			completeRoleSetForRole.add(SecurityRoles.SUPERADMIN);
		case SecurityRoles.ADMIN:
			completeRoleSetForRole.add(SecurityRoles.ADMIN);
		case SecurityRoles.VIP:
			completeRoleSetForRole.add(SecurityRoles.VIP);
		case SecurityRoles.MEMBER:
			completeRoleSetForRole.add(SecurityRoles.MEMBER);
		}
		
		return completeRoleSetForRole;
	}

}
