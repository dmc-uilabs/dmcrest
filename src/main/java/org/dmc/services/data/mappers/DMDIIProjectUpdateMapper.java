package org.dmc.services.data.mappers;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import org.dmc.services.DMDIIProjectService;
import org.dmc.services.UserService;
import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.DMDIIProjectItemAccessLevel;
import org.dmc.services.data.entities.DMDIIProjectUpdate;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.DMDIIProjectUpdateModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.springframework.security.core.context.SecurityContextHolder;
import org.dmc.services.security.UserPrincipal;
import org.dmc.services.data.repositories.UserRepository;

@Component
public class DMDIIProjectUpdateMapper extends AbstractMapper<DMDIIProjectUpdate, DMDIIProjectUpdateModel> {

	@Inject
	private DMDIIProjectService dmdiiProjectService;

	@Inject
	private UserService userService;

	@Inject
	private UserRepository userRepository;

	@Override
	public DMDIIProjectUpdate mapToEntity(DMDIIProjectUpdateModel model) {
		Assert.notNull(model);
		DMDIIProjectUpdate entity = copyProperties(model, new DMDIIProjectUpdate());

		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<DMDIIProject, DMDIIProjectModel> projectMapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);

		entity.setCreator(userMapper.mapToEntity(userService.findOne(model.getCreator())));
		entity.setProject(projectMapper.mapToEntity(dmdiiProjectService.findOne(model.getDmdiiProject())));
		entity.setDate(new DateTime(model.getCreated()).toDate());

		if (model.getAccessLevel() != null) {
			entity.setAccessLevel(model.getAccessLevel());
		}

		return entity;
	}

	@Override
	public DMDIIProjectUpdateModel mapToModel(DMDIIProjectUpdate entity) {
		Assert.notNull(entity);

		User currentUser = userRepository.findOne(
				((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

		if (entity.getAccessLevel() != null) {
			Boolean isAuthorized = PermissionEvaluationHelper.userMeetsProjectAccessRequirement(entity.getAccessLevel(), entity.getProject().getId(), currentUser);

			if (!isAuthorized) {
				return null;
			}
		}

		DMDIIProjectUpdateModel model = copyProperties(entity, new DMDIIProjectUpdateModel());

		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<DMDIIProject, DMDIIProjectModel> projectMapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);

		Format formatter = new SimpleDateFormat("yyyy-MM-dd");

		model.setCreator(entity.getCreator().getId());
		model.setDmdiiProject(entity.getProject().getId());
		model.setCreated(formatter.format(entity.getDate()));

		if (entity.getAccessLevel() != null) {
			model.setAccessLevel(entity.getAccessLevel().toString());
		}

		return model;
	}

	@Override
	public Class<DMDIIProjectUpdate> supportsEntity() {
		return DMDIIProjectUpdate.class;
	}

	@Override
	public Class<DMDIIProjectUpdateModel> supportsModel() {
		return DMDIIProjectUpdateModel.class;
	}
}
