package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.Role;
import org.dmc.services.data.entities.UserRoleAssignment;
import org.dmc.services.data.models.RoleModel;
import org.dmc.services.data.models.UserRoleAssignmentModel;
import org.springframework.stereotype.Component;

@Component
public class UserRoleAssignmentMapper extends AbstractMapper<UserRoleAssignment, UserRoleAssignmentModel> {

	@Override
	public UserRoleAssignment mapToEntity(UserRoleAssignmentModel model) {
		if (model == null) return null;
		
		Mapper<Role, RoleModel> roleMapper = mapperFactory.mapperFor(Role.class, RoleModel.class);
		UserRoleAssignment entity = copyProperties(model, new UserRoleAssignment());
		entity.setRole(roleMapper.mapToEntity(model.getRole()));
		return entity;
	}

	@Override
	public UserRoleAssignmentModel mapToModel(UserRoleAssignment entity) {
		if (entity == null) return null;
		
		Mapper<Role, RoleModel> roleMapper = mapperFactory.mapperFor(Role.class, RoleModel.class);
		UserRoleAssignmentModel model = copyProperties(entity, new UserRoleAssignmentModel());
		model.setRole(roleMapper.mapToModel(entity.getRole()));
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
