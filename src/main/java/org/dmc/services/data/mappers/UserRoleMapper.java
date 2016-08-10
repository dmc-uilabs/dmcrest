package org.dmc.services.data.mappers;

import javax.inject.Inject;

import org.dmc.services.RoleService;
import org.dmc.services.UserService;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.Role;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserRole;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.models.RoleModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.models.UserRoleModel;
import org.dmc.services.dmdiimember.OrganizationService;
import org.springframework.stereotype.Component;

@Component
public class UserRoleMapper extends AbstractMapper <UserRole, UserRoleModel> {
	
	@Inject
	private UserService userService;
	
	@Inject
	private OrganizationService organizationService;
	
	@Inject
	private RoleService roleService;

	@Override
	public UserRole mapToEntity(UserRoleModel model) {
		UserRole entity = copyProperties(model, new UserRole());
		
		Mapper<Role, RoleModel> roleMapper = mapperFactory.mapperFor(Role.class, RoleModel.class);
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<Organization, OrganizationModel> orgMapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);
		
		entity.setRole(roleMapper.mapToEntity(roleService.findOne(model.getRoleId())));
		entity.setUser(userMapper.mapToEntity(userService.findOne(model.getUserId())));
		if (model.getOrganizationId() != null) {
			entity.setOrganization(orgMapper.mapToEntity(organizationService.findOne(model.getOrganizationId())));
		}
		
		return entity;
	}

	@Override
	public UserRoleModel mapToModel(UserRole entity) {
		UserRoleModel model = copyProperties(entity, new UserRoleModel());
		
		Mapper<Role, RoleModel> roleMapper = mapperFactory.mapperFor(Role.class, RoleModel.class);
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<Organization, OrganizationModel> orgMapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);
		
		model.setRoleId(entity.getRole().getId());
		model.setUserId(entity.getUser().getId());
		if (entity.getOrganization() != null) {
			model.setOrganizationId(entity.getOrganization().getId());
		}
		
		return model;
	}

	@Override
	public Class<UserRole> supportsEntity() {
		return UserRole.class;
	}

	@Override
	public Class<UserRoleModel> supportsModel() {
		return UserRoleModel.class;
	}

}
