package org.dmc.services;

import org.dmc.services.config.JpaTestConfig;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.organization.OrganizationService;
import org.dmc.services.roleassignment.UserRoleAssignmentService;
import org.dmc.services.security.UserPrincipalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Created by kskronek on 9/1/2016.
 */
@Import(JpaTestConfig.class)
//@ComponentScan("org.dmc.services")
public class UserServiceTestConfig {

	@Bean
	public MapperFactory getMapperFactory() {
		return new MapperFactory();
	}

	@Bean
	public OrganizationUserService getOrganizationUserService() {
		return new OrganizationUserService();
	}

	@Bean
	public UserRoleAssignmentService getUserRoleAssignmentService() {
		return new UserRoleAssignmentService();
	}

	@Bean
	public OrganizationService getOrganizationService() {
		return new OrganizationService();
	}

	@Bean
	public UserPrincipalService getUserPrincipalService() {
		return new UserPrincipalService();
	}

	@Bean
	public UserService getUserService() {
		return new UserService();
	}
}
