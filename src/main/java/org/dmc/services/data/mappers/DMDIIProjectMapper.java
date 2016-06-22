package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.DMDIIProjectFocusArea;
import org.dmc.services.data.entities.DMDIIProjectStatus;
import org.dmc.services.data.entities.DMDIIProjectThrust;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.DMDIIMemberModel;
import org.dmc.services.data.models.DMDIIProjectFocusAreaModel;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.DMDIIProjectStatusModel;
import org.dmc.services.data.models.DMDIIProjectThrustModel;
import org.dmc.services.data.models.UserModel;
import org.springframework.stereotype.Component;


@Component
public class DMDIIProjectMapper extends AbstractMapper<DMDIIProject, DMDIIProjectModel> {

	@Override
	public DMDIIProject mapToEntity(DMDIIProjectModel model) {
		DMDIIProject entity = copyProperties(model, new DMDIIProject());
		
		Mapper<DMDIIMember, DMDIIMemberModel> memberMapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<DMDIIProjectStatus, DMDIIProjectStatusModel> statusMapper = mapperFactory.mapperFor(DMDIIProjectStatus.class, DMDIIProjectStatusModel.class);
		Mapper<DMDIIProjectFocusArea, DMDIIProjectFocusAreaModel> focusMapper = mapperFactory.mapperFor(DMDIIProjectFocusArea.class, DMDIIProjectFocusAreaModel.class);
		Mapper<DMDIIProjectThrust, DMDIIProjectThrustModel> thrustMapper = mapperFactory.mapperFor(DMDIIProjectThrust.class, DMDIIProjectThrustModel.class);
		
		entity.setPrimeOrganization(memberMapper.mapToEntity(model.getPrimeOrganization()));
		entity.setPrincipalInvestigator(userMapper.mapToEntity(model.getPrincipalInvestigator()));
		entity.setPrincipalPointOfContact(userMapper.mapToEntity(model.getPrincipalPointOfContact()));
		entity.setProjectStatus(statusMapper.mapToEntity(model.getProjectStatus()));
		entity.setProjectFocusArea(focusMapper.mapToEntity(model.getProjectFocusArea()));
		entity.setProjectThrust(thrustMapper.mapToEntity(model.getProjectThrust()));
		
		return entity;
	}

	@Override
	public DMDIIProjectModel mapToModel(DMDIIProject entity) {
		DMDIIProjectModel model = copyProperties(entity, new DMDIIProjectModel());
		
		Mapper<DMDIIMember, DMDIIMemberModel> memberMapper = mapperFactory.mapperFor(DMDIIMember.class, DMDIIMemberModel.class);
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<DMDIIProjectStatus, DMDIIProjectStatusModel> statusMapper = mapperFactory.mapperFor(DMDIIProjectStatus.class, DMDIIProjectStatusModel.class);
		Mapper<DMDIIProjectFocusArea, DMDIIProjectFocusAreaModel> focusMapper = mapperFactory.mapperFor(DMDIIProjectFocusArea.class, DMDIIProjectFocusAreaModel.class);
		Mapper<DMDIIProjectThrust, DMDIIProjectThrustModel> thrustMapper = mapperFactory.mapperFor(DMDIIProjectThrust.class, DMDIIProjectThrustModel.class);
		
		model.setPrimeOrganization(memberMapper.mapToModel(entity.getPrimeOrganization()));
		model.setPrincipalInvestigator(userMapper.mapToModel(entity.getPrincipalInvestigator()));
		model.setPrincipalPointOfContact(userMapper.mapToModel(entity.getPrincipalPointOfContact()));
		model.setProjectStatus(statusMapper.mapToModel(entity.getProjectStatus()));
		model.setProjectFocusArea(focusMapper.mapToModel(entity.getProjectFocusArea()));
		model.setProjectThrust(thrustMapper.mapToModel(entity.getProjectThrust()));
		
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
