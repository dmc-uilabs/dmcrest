package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIInstituteInvolvement;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.DMDIIInstituteInvolvementModel;
import org.dmc.services.data.models.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DMDIIInstituteInvolvementMapper extends AbstractMapper<DMDIIInstituteInvolvement, DMDIIInstituteInvolvementModel> {

	@Override
	public DMDIIInstituteInvolvement mapToEntity(DMDIIInstituteInvolvementModel model) {
		if (model == null) return null;
		
		DMDIIInstituteInvolvement entity = copyProperties(model, new DMDIIInstituteInvolvement());
		
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		entity.setUser(userMapper.mapToEntity(model.getUser()));
		
		return entity;
	}

	@Override
	public DMDIIInstituteInvolvementModel mapToModel(DMDIIInstituteInvolvement entity) {
		if (entity == null) return null;
		
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
