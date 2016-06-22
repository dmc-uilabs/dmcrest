package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIMemberUser;
import org.dmc.services.data.entities.DMDIIRole;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.DMDIIMemberUserModel;
import org.dmc.services.data.models.DMDIIRoleModel;
import org.dmc.services.data.models.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DMDIIMemberUserMapper extends AbstractMapper<DMDIIMemberUser, DMDIIMemberUserModel> {

	@Override
	public DMDIIMemberUser mapToEntity(DMDIIMemberUserModel model) {
		DMDIIMemberUser entity = copyProperties(model, new DMDIIMemberUser());
		
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<DMDIIRole, DMDIIRoleModel> dmdiiRoleMapper = mapperFactory.mapperFor(DMDIIRole.class, DMDIIRoleModel.class);
		
		entity.setRole(dmdiiRoleMapper.mapToEntity(model.getRole()));
		entity.setUser(userMapper.mapToEntity(model.getUser()));
		
		return entity;
	}

	@Override
	public DMDIIMemberUserModel mapToModel(DMDIIMemberUser entity) {
		DMDIIMemberUserModel model = copyProperties(entity, new DMDIIMemberUserModel());
		
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<DMDIIRole, DMDIIRoleModel> dmdiiRoleMapper = mapperFactory.mapperFor(DMDIIRole.class, DMDIIRoleModel.class);
		
		model.setRole(dmdiiRoleMapper.mapToModel(entity.getRole()));
		model.setUser(userMapper.mapToModel(entity.getUser()));
		
		return model;
	}

	@Override
	public Class<DMDIIMemberUser> supportsEntity() {
		return DMDIIMemberUser.class;
	}

	@Override
	public Class<DMDIIMemberUserModel> supportsModel() {
		return DMDIIMemberUserModel.class;
	}

}
