package org.dmc.services.data.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.dmc.services.DMDIIProjectService;
import org.dmc.services.UserService;
import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.springframework.stereotype.Component;

@Component
public class DMDIIDocumentMapper extends AbstractMapper<DMDIIDocument, DMDIIDocumentModel> {

	@Inject
	private DMDIIProjectService dmdiiProjectService;

	@Inject
	private UserService userService;


	@Override
	public DMDIIDocument mapToEntity(DMDIIDocumentModel model) {
		if (model == null) return null;
		DMDIIDocument entity = copyProperties(model, new DMDIIDocument());

		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<DMDIIProject, DMDIIProjectModel> projectMapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);

		entity.setOwner(userMapper.mapToEntity(userService.findOne(model.getOwnerId())));
		if(model.getDmdiiProjectId() != null)
			entity.setDMDIIProject(projectMapper.mapToEntity(dmdiiProjectService.findOne(model.getDmdiiProjectId())));
		else
			entity.setDMDIIProject(null);

		return entity;
	}

	@Override
	public DMDIIDocumentModel mapToModel(DMDIIDocument entity) {
		if (entity == null) return null;
		
		if (entity.getDmdiiProject() != null && entity.getAccessLevel() != null) {
			List<DMDIIMember> projectMembers = new ArrayList<DMDIIMember>();
			projectMembers.add(entity.getDmdiiProject().getPrimeOrganization());
			projectMembers.addAll(entity.getDmdiiProject().getContributingCompanies());
			
			List<Integer> projectMemberIds = projectMembers.stream().map((n) -> n.getOrganization().getId()).collect(Collectors.toList());
			if (!PermissionEvaluationHelper.userMeetsProjectAccessRequirement(entity.getAccessLevel(), projectMemberIds)) {
				return null;
			}
		}

		DMDIIDocumentModel model = copyProperties(entity, new DMDIIDocumentModel());

		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<DMDIIProject, DMDIIProjectModel> projectMapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);

		model.setOwnerId(entity.getOwner().getId());
		if(entity.getDMDIIProject() != null)
			model.setDmdiiProjectId(entity.getDMDIIProject().getId());
		else
			model.setDmdiiProjectId(null);

		return model;
	}
	
	@Override
	public Class<DMDIIDocument> supportsEntity() {
		return DMDIIDocument.class;
	}

	@Override
	public Class<DMDIIDocumentModel> supportsModel() {
		return DMDIIDocumentModel.class;
	}

}
