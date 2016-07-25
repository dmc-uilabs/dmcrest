package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserContactInfo;
import org.dmc.services.data.entities.UserRoleAssignment;
import org.dmc.services.data.models.UserContactInfoModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.models.UserRoleAssignmentModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserModel> {

	@Override
	public User mapToEntity(UserModel model) {
		if (model == null) return null;
		Mapper<UserRoleAssignment, UserRoleAssignmentModel> roleMapper = mapperFactory.mapperFor(UserRoleAssignment.class, UserRoleAssignmentModel.class);
		Mapper<UserContactInfo, UserContactInfoModel> contactInfoMapper = mapperFactory.mapperFor(UserContactInfo.class, UserContactInfoModel.class);
		
		User entity = copyProperties(model, new User());
		entity.setRoles(roleMapper.mapToEntity(model.getRoles()));
		entity.setUserContactInfo(contactInfoMapper.mapToEntity(model.getUserContactInfo()));

		return entity;
	}

	@Override
	public UserModel mapToModel(User entity) {
		if (entity == null) return null;
		Mapper<UserRoleAssignment, UserRoleAssignmentModel> roleMapper = mapperFactory.mapperFor(UserRoleAssignment.class, UserRoleAssignmentModel.class);
		Mapper<UserContactInfo, UserContactInfoModel> contactInfoMapper = mapperFactory.mapperFor(UserContactInfo.class, UserContactInfoModel.class);
		
		UserModel model = copyProperties(entity, new UserModel());
		model.setRoles(roleMapper.mapToModel(entity.getRoles()));
		model.setUserContactInfo(contactInfoMapper.mapToModel(entity.getUserContactInfo()));

		return model;
	}

	@Override
	public Class<User> supportsEntity() {
		return User.class;
	}

	@Override
	public Class<UserModel> supportsModel() {
		return UserModel.class;
	}

}
