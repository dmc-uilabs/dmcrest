package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserContactInfo;
import org.dmc.services.data.models.UserContactInfoModel;
import org.dmc.services.data.models.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserModel> {

	@Override
	public User mapToEntity(UserModel model) {
		User entity = copyProperties(model, new User());

		Mapper<UserContactInfo, UserContactInfoModel> contactInfoMapper = mapperFactory.mapperFor(UserContactInfo.class, UserContactInfoModel.class);
		entity.setUserContactInfo(contactInfoMapper.mapToEntity(model.getUserContactInfo()));

		return entity;
	}

	@Override
	public UserModel mapToModel(User entity) {
		UserModel model = copyProperties(entity, new UserModel());

		Mapper<UserContactInfo, UserContactInfoModel> contactInfoMapper = mapperFactory.mapperFor(UserContactInfo.class, UserContactInfoModel.class);
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
