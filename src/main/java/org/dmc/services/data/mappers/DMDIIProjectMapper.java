package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.UserModel;
import org.springframework.stereotype.Component;


@Component
public class DMDIIProjectMapper extends AbstractMapper<DMDIIProject, DMDIIProjectModel> {

	@Override
	public DMDIIProject mapToEntity(DMDIIProjectModel model) {
		DMDIIProject entity = copyProperties(model, new DMDIIProject());
		
		Mapper<DMDIIMember, DMDIIMemberModel> memberMapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		
		entity.setPrimeOrganization(memberMapper.mapToEntity(model.getPrimeOrganization()));
		entity.setPrincipalInvestigator(userMapper.mapToEntity(model.getPrincipalInvestigator()));
		entity.setPrincipalPointOfContact(userMapper.mapToEntity(model.getPrincipalPointOfContact()));
		
		return entity;
	}

	@Override
	public DMDIIProjectModel mapToModel(DMDIIProject entity) {
		DMDIIProjectModel model = copyProperties(entity, new DMDIIProjectModel());
		
		Mapper<DMDIIMember, DMDIIMemberModel> memberMapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		
		model.setPrimeOrganization(memberMapper.mapToModel(entity.getPrimeOrganization()));
		model.setPrincipalInvestigator(userMapper.mapToModel(entity.getPrincipalInvestigator()));
		model.setPrincipalPointOfContact(userMapper.mapToModel(entity.getPrincipalPointOfContact()));
		
		return model;
	}

	@Override
	public Class<DMDIIProject> supportsEntity() {
		return DMDIIProject.class;
	}

	@Override
	public Class<DMDIIProjectModel> supportsModel() {
		return DMDIIProjectModel.class;
	}

}
