package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.UserContactInfo;
import org.dmc.services.data.entities.UserMemberPortalContactInfo;
import org.dmc.services.data.entities.UserPrivateContactInfo;
import org.dmc.services.data.entities.UserPublicContactInfo;
import org.dmc.services.data.models.UserContactInfoModel;
import org.dmc.services.data.models.UserMemberPortalContactInfoModel;
import org.dmc.services.data.models.UserPrivateContactInfoModel;
import org.dmc.services.data.models.UserPublicContactInfoModel;
import org.springframework.stereotype.Component;

@Component
public class UserContactInfoMapper extends AbstractMapper<UserContactInfo, UserContactInfoModel> {

	@Override
	public UserContactInfo mapToEntity(UserContactInfoModel model) {
		UserContactInfo entity = copyProperties(model, new UserContactInfo());

		Mapper<UserPrivateContactInfo, UserPrivateContactInfoModel> privateMapper = mapperFactory.mapperFor(UserPrivateContactInfo.class, UserPrivateContactInfoModel.class);
		Mapper<UserPublicContactInfo, UserPublicContactInfoModel> publicMapper = mapperFactory.mapperFor(UserPublicContactInfo.class, UserPublicContactInfoModel.class);
		Mapper<UserMemberPortalContactInfo, UserMemberPortalContactInfoModel> memberPortalMapper = mapperFactory.mapperFor(UserMemberPortalContactInfo.class, UserMemberPortalContactInfoModel.class);

		entity.setUserPrivateContactInfo(privateMapper.mapToEntity(model.getUserPrivateContactInfo()));
		entity.setUserPublicContactInfo(publicMapper.mapToEntity(model.getUserPublicContactInfo()));
		entity.setUserMemberPortalContactInfo(memberPortalMapper.mapToEntity(model.getUserMemberPortalContactInfo()));

		return entity;
	}

	@Override
	public UserContactInfoModel mapToModel(UserContactInfo entity) {
		UserContactInfoModel model = copyProperties(entity, new UserContactInfoModel());

		Mapper<UserPrivateContactInfo, UserPrivateContactInfoModel> privateMapper = mapperFactory.mapperFor(UserPrivateContactInfo.class, UserPrivateContactInfoModel.class);
		Mapper<UserPublicContactInfo, UserPublicContactInfoModel> publicMapper = mapperFactory.mapperFor(UserPublicContactInfo.class, UserPublicContactInfoModel.class);
		Mapper<UserMemberPortalContactInfo, UserMemberPortalContactInfoModel> memberPortalMapper = mapperFactory.mapperFor(UserMemberPortalContactInfo.class, UserMemberPortalContactInfoModel.class);

		model.setUserPrivateContactInfo(privateMapper.mapToModel(entity.getUserPrivateContactInfo()));
		model.setUserPublicContactInfo(publicMapper.mapToModel(entity.getUserPublicContactInfo()));
		model.setUserMemberPortalContactInfo(memberPortalMapper.mapToModel(entity.getUserMemberPortalContactInfo()));

		return model;
	}

	@Override
	public Class<UserContactInfo> supportsEntity() {
		return UserContactInfo.class;
	}

	@Override
	public Class<UserContactInfoModel> supportsModel() {
		return UserContactInfoModel.class;
	}

}
