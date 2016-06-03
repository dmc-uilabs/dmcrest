package org.dmc.services.mappers;

import org.dmc.services.entities.DMDIIInstituteInvolvement;
import org.dmc.services.entities.User;
import org.dmc.services.models.DMDIIInstituteInvolvementModel;
import org.dmc.services.models.UserModel;
import org.springframework.beans.BeanUtils;

public class DMDIIInstituteInvolvementMapper extends AbstractMapper<DMDIIInstituteInvolvement, DMDIIInstituteInvolvementModel> {

	@Override
	public DMDIIInstituteInvolvement mapToEntity(DMDIIInstituteInvolvementModel model) {
		DMDIIInstituteInvolvement entity = new DMDIIInstituteInvolvement();
		BeanUtils.copyProperties(model, entity);
		
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		entity.setUser(userMapper.mapToEntity(model.getUser()));
		
		return entity;
	}

	@Override
	public DMDIIInstituteInvolvementModel mapToModel(DMDIIInstituteInvolvement entity) {
		DMDIIInstituteInvolvementModel model = new DMDIIInstituteInvolvementModel();
		BeanUtils.copyProperties(entity, model);
		
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		model.setUser(userMapper.mapToModel(entity.getUser()));
		
		return model;
	}

	@Override
	public Class<DMDIIInstituteInvolvement> supportsEntity() {
		return DMDIIInstituteInvolvement.class;
	}

	@Override
	public Class<DMDIIInstituteInvolvementModel> supportsModel() {
		return DMDIIInstituteInvolvementModel.class;
	}

}
