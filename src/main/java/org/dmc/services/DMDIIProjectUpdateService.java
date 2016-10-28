package org.dmc.services;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.DMDIIProjectUpdate;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.DMDIIProjectUpdateModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.repositories.DMDIIProjectUpdateRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DMDIIProjectUpdateService {

	@Inject
	private DMDIIProjectUpdateRepository dmdiiProjectUpdateRepository;

	@Inject
	private DMDIIProjectService dmdiiProjectService;

	@Inject
	private UserService userService;

	@Inject
	private MapperFactory mapperFactory;

	public List<DMDIIProjectUpdateModel> getDMDIIProjectUpdatesByProjectId(Integer limit, Integer projectId) {
		Mapper<DMDIIProjectUpdate, DMDIIProjectUpdateModel> mapper = mapperFactory.mapperFor(DMDIIProjectUpdate.class, DMDIIProjectUpdateModel.class);
		List<DMDIIProjectUpdate> updates = dmdiiProjectUpdateRepository.findByProjectIdOrderByDateDescIdDesc(new PageRequest(0, limit), projectId).getContent();
		return mapper.mapToModel(updates);
	}

	public DMDIIProjectUpdateModel save(DMDIIProjectUpdateModel update) {
		Mapper<DMDIIProjectUpdate, DMDIIProjectUpdateModel> updateMapper = mapperFactory.mapperFor(DMDIIProjectUpdate.class, DMDIIProjectUpdateModel.class);
		Mapper<DMDIIProject, DMDIIProjectModel> projectMapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);

		DMDIIProjectUpdate updateEntity = updateMapper.mapToEntity(update);
		DMDIIProject projectEntity = projectMapper.mapToEntity(dmdiiProjectService.findOne(update.getDmdiiProject()));
		User userEntity = userMapper.mapToEntity(userService.findOne(update.getCreator()));

		updateEntity.setProject(projectEntity);
		updateEntity.setCreator(userEntity);

		updateEntity = dmdiiProjectUpdateRepository.save(updateEntity);

		return updateMapper.mapToModel(updateEntity);
	}
	
	public void delete(Integer updateId) {
		DMDIIProjectUpdate updateEntity = dmdiiProjectUpdateRepository.findOne(updateId);
		updateEntity.setIsDeleted(true);
		dmdiiProjectUpdateRepository.save(updateEntity);
	}

}
