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
		if(model == null)
			return null;

		User entity = copyProperties(model, new User());

		Mapper<UserRoleAssignment, UserRoleAssignmentModel> roleMapper = mapperFactory.mapperFor(UserRoleAssignment.class, UserRoleAssignmentModel.class);
		Mapper<UserContactInfo, UserContactInfoModel> contactInfoMapper = mapperFactory.mapperFor(UserContactInfo.class, UserContactInfoModel.class);
		entity.setRoles(roleMapper.mapToEntity(model.getRoles()));
		entity.setUserContactInfo(contactInfoMapper.mapToEntity(model.getUserContactInfo()));

		return entity;
	}

	@Override
	public UserModel mapToModel(User entity) {
		if(entity == null)
			return null;

		UserModel model = copyProperties(entity, new UserModel());

		Mapper<UserContactInfo, UserContactInfoModel> contactInfoMapper = mapperFactory.mapperFor(UserContactInfo.class, UserContactInfoModel.class);
		Mapper<UserRoleAssignment, UserRoleAssignmentModel> roleMapper = mapperFactory.mapperFor(UserRoleAssignment.class, UserRoleAssignmentModel.class);
		model.setUserContactInfo(contactInfoMapper.mapToModel(entity.getUserContactInfo()));
		model.setRoles(roleMapper.mapToModel(entity.getRoles()));

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
