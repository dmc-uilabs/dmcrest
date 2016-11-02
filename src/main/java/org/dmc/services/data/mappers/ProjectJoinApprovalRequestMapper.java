package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.ProjectJoinApprovalRequest;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.MiniUserModel;
import org.dmc.services.data.models.ProjectJoinApprovalRequestModel;
import org.springframework.stereotype.Component;


@Component
public class ProjectJoinApprovalRequestMapper extends AbstractMapper<ProjectJoinApprovalRequest, ProjectJoinApprovalRequestModel> {

	@Override
	public ProjectJoinApprovalRequest mapToEntity(ProjectJoinApprovalRequestModel model) {
		if (model == null) return null;
		
		Mapper<User, MiniUserModel> userMapper = mapperFactory.mapperFor(User.class, MiniUserModel.class);
		
		ProjectJoinApprovalRequest entity = copyProperties(model, new ProjectJoinApprovalRequest());
		entity.setUser(userMapper.mapToEntity(model.getUser()));
		
		return entity;
	}

	@Override
	public ProjectJoinApprovalRequestModel mapToModel(ProjectJoinApprovalRequest entity) {
		if (entity == null) return null;
		
		Mapper<User, MiniUserModel> userMapper = mapperFactory.mapperFor(User.class, MiniUserModel.class);
		
		ProjectJoinApprovalRequestModel model = copyProperties(entity, new ProjectJoinApprovalRequestModel());
		model.setUser(userMapper.mapToModel(entity.getUser()));
		
		return model;
	}

	@Override
	public Class<ProjectJoinApprovalRequest> supportsEntity() {
		return ProjectJoinApprovalRequest.class;
	}

	@Override
	public Class<ProjectJoinApprovalRequestModel> supportsModel() {
		return ProjectJoinApprovalRequestModel.class;
	}

}
