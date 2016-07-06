package org.dmc.services.security;

import javax.inject.Inject;

import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserRoleAssignment;
import org.dmc.services.data.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalService implements UserDetailsService {
	
	@Inject
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (!username.equals("test3")) {
			throw new UsernameNotFoundException("test error");
		}
		
		UserPrincipal principal = new UserPrincipal(1003, "test3");
		principal.addRole(1, "VIP");
		
		return principal;
	}

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		User user = userRepository.findFirstByUsername(username);
//		if (user == null) {
//			throw new UsernameNotFoundException("Could not find user with username: " + username);
//		}
//		
//		UserPrincipal principal = new UserPrincipal(user.getId(), user.getUsername());
//		
//		for (UserRoleAssignment roleAssignment : user.getRoles()) {
//			principal.addRole(roleAssignment.getOrganizationId(), roleAssignment.getRole().getRole());
//		}
//		
//		return principal;
//	}

}
