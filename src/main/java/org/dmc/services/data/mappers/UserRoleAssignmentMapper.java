package org.dmc.services.data.mappers;

import javax.inject.Inject;

import org.dmc.services.data.entities.UserRoleAssignment;
import org.dmc.services.data.models.UserRoleAssignmentModel;
import org.dmc.services.data.repositories.OrganizationDao;
import org.dmc.services.data.repositories.RoleRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserRoleAssignmentMapper extends AbstractMapper<UserRoleAssignment, UserRoleAssignmentModel> {
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private RoleRepository roleRepository;
	
	@Inject
	private OrganizationDao organizationDao;

	@Override
	public UserRoleAssignment mapToEntity(UserRoleAssignmentModel model) {
		if (model == null) return null;
		
		UserRoleAssignment entity = copyProperties(model, new UserRoleAssignment());
		entity.setOrganization(organizationDao.findOne(model.getOrganizationId()));
		entity.setUser(userRepository.findOne(model.getUserId()));
		entity.setRole(roleRepository.findByRole(model.getRole()));
		return entity;
	}

	@Override
	public UserRoleAssignmentModel mapToModel(UserRoleAssignment entity) {
		if (entity == null) return null;
		
		UserRoleAssignmentModel model = copyProperties(entity, new UserRoleAssignmentModel());
		model.setRole(entity.getRole().getRole());
		model.setOrganizationId(entity.getOrganization().getId());
		model.setUserId(entity.getUser().getId());
		return model;
	}

	@Override
	public Class<UserRoleAssignment> supportsEntity() {
		return UserRoleAssignment.class;
	}

	@Override
	public Class<UserRoleAssignmentModel> supportsModel() {
		return UserRoleAssignmentModel.class;
	}

}
