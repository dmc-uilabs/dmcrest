package org.dmc.services.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.entities.ResourceCourse;
import org.dmc.services.data.entities.ResourceProject;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ResourceCourseModel;
import org.dmc.services.data.models.ResourceProjectModel;
import org.dmc.services.data.repositories.ResourceProjectRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class ResourceProjectService {

	@Inject
	private ResourceProjectRepository resourceProjectRepository;
	
	@Inject
	private MapperFactory mapperFactory;


	//Gets all projects
	public List<ResourceProjectModel> getAll() {
		Mapper<ResourceProject, ResourceProjectModel> mapper = mapperFactory.mapperFor(ResourceProject.class, ResourceProjectModel.class);
		return mapper.mapToModel(resourceProjectRepository.findAll());
	}

	//Gets a specific projects
	public ResourceProjectModel get(Integer id) {
		Mapper<ResourceProject, ResourceProjectModel> mapper = mapperFactory.mapperFor(ResourceProject.class, ResourceProjectModel.class);
		return mapper.mapToModel(resourceProjectRepository.findOne(id));
	}
	
	//Creates an project
	public ResourceProjectModel create(ResourceProjectModel assessment) {
		Mapper<ResourceProject, ResourceProjectModel> mapper = mapperFactory.mapperFor(ResourceProject.class, ResourceProjectModel.class);
		ResourceProject entity = mapper.mapToEntity(assessment);
		entity = resourceProjectRepository.save(entity);
		return mapper.mapToModel(entity);
	}
	

	//deletes an project
	public ResourceProjectModel remove(Integer id) {
		Mapper<ResourceProject, ResourceProjectModel> mapper = mapperFactory.mapperFor(ResourceProject.class, ResourceProjectModel.class);
		ResourceProject entity = resourceProjectRepository.findOne(id);
		resourceProjectRepository.delete(entity);
		return mapper.mapToModel(entity);
	}

	
	

}
