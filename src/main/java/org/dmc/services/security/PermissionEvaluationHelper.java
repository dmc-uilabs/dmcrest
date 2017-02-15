package org.dmc.services.security;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dmc.services.data.entities.User;

import org.dmc.services.data.entities.DMDIIProjectItemAccessLevel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.dmc.services.ServiceLogger;

public class PermissionEvaluationHelper {

	public static boolean userMeetsProjectAccessRequirement(String accessLevel, Integer projectOrganization) {
		List<Integer> wrapper = new ArrayList<Integer>();
		wrapper.add(projectOrganization);
		return userMeetsProjectAccessRequirement(accessLevel, wrapper);
	}

	public static boolean userMeetsProjectAccessRequirement(String accessLevel, List<Integer> projectOrganizations) {
		UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.hasAuthority(SecurityRoles.SUPERADMIN)) {
			return true;
		}

		Boolean meetsRequirement = false;

		ServiceLogger.log("My org  info ", Integer.toString(user.getmyOrg()));

		ServiceLogger.log("Doc access   info ", accessLevel);

   System.out.println("AHORA------------------ the org  ------  ");

// TODO split accessLevel into the various words and compare them with the org id

		// if (accessLevel.indexOf(Integer.toString(user.getmyOrg())) >= 0){
		// 	meetsRequirement = true;
		// }

    String[] accessSplit = accessLevel.split("\\s");



		if (accessSplit[0].equals("ORG")){
								System.out.println("Inside the ORG structure " );
								System.out.println("My org is "+ Integer.toString(user.getmyOrg()) );

			for (int x=1; x<accessSplit.length; x++) {
					if (accessSplit[x].equals(Integer.toString(user.getmyOrg()).trim())){
						System.out.println("My org IS on the list " + Integer.toString(user.getmyOrg()));
						meetsRequirement = true;
					}else{
            	System.out.println("My org is not in the list testing the org value "+ Integer.toString(user.getmyOrg()) );
					}
			}

		}












     ServiceLogger.log("before all  ...  ", accessLevel);
		if(accessLevel.equals("ALL_MEMBERS")){
			ServiceLogger.log("into all the members ...  ", accessLevel);
			meetsRequirement = user.hasAuthority(SecurityRoles.MEMBER);
		}

		if(accessLevel.equals("PROJECT_PARTICIPANTS")){
			meetsRequirement = isProjectParticipantForRole(user, projectOrganizations, SecurityRoles.MEMBER);
		}
		if(accessLevel.equals("PROJECT_PARTICIPANTS_AND_UPPER_TIER_MEMBERS")){
			meetsRequirement = user.getIsUpperTierMember() || isProjectParticipantForRole(user, projectOrganizations, SecurityRoles.MEMBER);
		}
		if(accessLevel.equals("PROJECT_PARTICIPANT_VIPS")){
			meetsRequirement = isProjectParticipantForRole(user, projectOrganizations, SecurityRoles.VIP);
		}

    	ServiceLogger.log("in the very access level ", Boolean.toString(meetsRequirement));

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

		userRoles.addAll(getInheritedRolesForRole(assignedRole));
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
