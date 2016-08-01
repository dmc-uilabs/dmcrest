package org.dmc.services.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dmc.services.data.entities.DMDIIProjectItemAccessLevel;
import org.springframework.security.core.context.SecurityContextHolder;

public class PermissionEvaluationHelper {
	
	public static boolean userMeetsProjectAccessRequirement(DMDIIProjectItemAccessLevel accessLevel, Integer projectOrganization) {
		List<Integer> wrapper = new ArrayList<Integer>();
		wrapper.add(projectOrganization);
		return userMeetsProjectAccessRequirement(accessLevel, wrapper);
	}
	
	public static boolean userMeetsProjectAccessRequirement(DMDIIProjectItemAccessLevel accessLevel, List<Integer> projectOrganizations) {
		UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.hasAuthority(SecurityRoles.SUPERADMIN)) {
			return true;
		}
		
		Boolean meetsRequirement = false;
		
		switch (accessLevel) {
		case ALL_MEMBERS:
			meetsRequirement = user.hasAuthority(SecurityRoles.MEMBER);
			break;
		case PROJECT_PARTICIPANTS:
			meetsRequirement = isProjectParticipantForRole(user, projectOrganizations, SecurityRoles.MEMBER);
			break;
		case PROJECT_PARTICIPANTS_AND_UPPER_TIER_MEMBERS:
			meetsRequirement = user.getIsUpperTierMember() || isProjectParticipantForRole(user, projectOrganizations, SecurityRoles.MEMBER);
			break;
		case PROJECT_PARTICIPANT_VIPS:
			meetsRequirement = isProjectParticipantForRole(user, projectOrganizations, SecurityRoles.VIP);
			break;
		}
		
		return meetsRequirement;
	}
	
	private static boolean isProjectParticipantForRole(UserPrincipal user, List<Integer> projectParticipantIds, String role) {
		return projectParticipantIds.stream()
				.filter(user.getAllRoles().keySet()::contains)
				.anyMatch((n) -> userHasRole(role, n));
	}
	
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
			completeRoleSetForRole.add(SecurityRoles.DMDII_MEMBER);
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
