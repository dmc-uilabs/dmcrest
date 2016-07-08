package org.dmc.services.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;

public class PermissionEvaluationHelper {
	
	public static boolean userHasRole(String requiredRole, Integer tenantId) {
		UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<String> userRoles = new ArrayList<String>();
		if (user.getIsSuperAdmin()) {
			return true;
		}
		String assignedRole = user.getRole(tenantId);
		
		if (assignedRole == null) {
			return false;
		}
		
		switch (assignedRole) {
		case SecurityRoles.ADMIN:
			userRoles.add(SecurityRoles.ADMIN);
		case SecurityRoles.VIP:
			userRoles.add(SecurityRoles.VIP);
		case SecurityRoles.MEMBER:
			userRoles.add(SecurityRoles.MEMBER);
		}
		return userRoles.contains(requiredRole);
	}

}
