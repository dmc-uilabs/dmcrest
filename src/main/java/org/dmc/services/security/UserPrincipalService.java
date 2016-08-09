package org.dmc.services.security;

import java.util.Collection;

import javax.inject.Inject;

import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserRoleAssignment;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.dmdiimember.DMDIIMemberDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalService implements UserDetailsService {
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private DMDIIMemberDao dmdiiMemberDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findFirstByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user with username: " + username);
		}
		
		UserPrincipal principal = new UserPrincipal(user.getId(), user.getUsername());
		
		for (UserRoleAssignment roleAssignment : user.getRoles()) {
			String role = roleAssignment.getRole().getRole();
			
			if (role.equals(SecurityRoles.SUPERADMIN)) {
				principal.addAuthorities(PermissionEvaluationHelper.getInheritedRolesForRole(role));
				principal.addRole(0, role);
			} else {
				principal.addRole(roleAssignment.getOrganization().getId(), role);
				
				if (!principal.hasAuthority(role)) {
					principal.addAuthorities(PermissionEvaluationHelper.getInheritedRolesForRole(role));
				}
				
				if (!principal.hasAuthority(SecurityRoles.DMDII_MEMBER) && roleAssignment.getOrganization().getDmdiiMember() != null) {
					principal.addAuthority(SecurityRoles.DMDII_MEMBER);
				}
			}
		}
		
		Collection<Integer> upperTierOrgIds = dmdiiMemberDao.findTier1And2IndustryAndAcademicMemberOrganizationIds();
		Boolean isUpperTier = principal.getAllRoles().keySet().stream().anyMatch(upperTierOrgIds::contains);
		principal.setIsUpperTierMember(isUpperTier);
		
		return principal;
	}

}
