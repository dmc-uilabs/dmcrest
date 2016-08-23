package org.dmc.services.roleassignment;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.dmc.services.UserService;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.Role;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserRoleAssignment;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.models.UserRoleAssignmentModel;
import org.dmc.services.data.repositories.RoleRepository;
import org.dmc.services.data.repositories.UserRoleAssignmentRepository;
import org.dmc.services.exceptions.ArgumentNotFoundException;
import org.dmc.services.organization.OrganizationService;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.dmc.services.security.SecurityRoles;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
public class UserRoleAssignmentService {

	@Inject
	private MapperFactory mapperFactory;

	@Inject
	private UserRoleAssignmentRepository userRoleAssignmentRepository;

	@Inject
	private RoleRepository roleRepository;

	@Inject
	private UserService userService;

	@Inject
	private OrganizationService organizationService;

	@Transactional
	public void deleteByUserIdAndOrganizationId(Integer userId, Integer organizationId) {
		userRoleAssignmentRepository.deleteByUserIdAndOrganizationId(userId, organizationId);
	}

	public UserRoleAssignmentModel grantRoleToUserForOrg(String role, Integer userId, Integer organizationId) throws ArgumentNotFoundException {
		return grantRoleToUserForOrg(role, userId, organizationId, false);
	}

	@Transactional
	public UserRoleAssignmentModel grantRoleToUserForOrg(String role, Integer userId, Integer organizationId, Boolean selfAdminOverride) throws ArgumentNotFoundException {
		// This method cannot be used to create superadmins
		if (role.equals(SecurityRoles.SUPERADMIN)) {
			throw new AccessDeniedException("403 access denied");
		}

		// Logged in user must be organizational admin OR this is a user self-validating as the first user of a company
		if (!PermissionEvaluationHelper.userHasRole(SecurityRoles.ADMIN, organizationId) &&
				!(selfAdminOverride && role.equals(SecurityRoles.ADMIN))) {
			throw new AccessDeniedException("403 access denied");
		}

		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<Organization, OrganizationModel> orgMapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);
		Mapper<UserRoleAssignment, UserRoleAssignmentModel> userRoleAssignmentMapper = mapperFactory.mapperFor(UserRoleAssignment.class, UserRoleAssignmentModel.class);

		User user = userMapper.mapToEntity(userService.findOne(userId));
		Organization org = orgMapper.mapToEntity(organizationService.findById(organizationId));
		Role userRole = roleRepository.findByRole(role);

		String errPoint = null;
		if (user == null) errPoint = "user";
		if (org == null) errPoint = "organization";
		if (userRole == null) errPoint = "role";
		if (errPoint != null) {
			throw new ArgumentNotFoundException("Could not find " + errPoint);
		}

		UserRoleAssignment newAssignment = new UserRoleAssignment();
		newAssignment.setOrganization(org);
		newAssignment.setUser(user);
		newAssignment.setRole(userRole);

		deleteByUserIdAndOrganizationId(userId, organizationId);
		return userRoleAssignmentMapper.mapToModel(userRoleAssignmentRepository.save(newAssignment));
	}

}
