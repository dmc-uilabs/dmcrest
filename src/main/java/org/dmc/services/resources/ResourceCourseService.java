package org.dmc.services.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.entities.ResourceAssessment;
import org.dmc.services.data.entities.ResourceCourse;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ResourceAssessmentModel;
import org.dmc.services.data.models.ResourceCourseModel;
import org.dmc.services.data.repositories.ResourceCourseRepository;
import org.springframework.stereotype.Service;


@Service
public class ResourceCourseService {

	@Inject
	private ResourceCourseRepository resourceCourseRepository;
	
	@Inject
	private MapperFactory mapperFactory;


	//Gets all courses
	public List<ResourceCourseModel> getAll() {
		Mapper<ResourceCourse, ResourceCourseModel> mapper = mapperFactory.mapperFor(ResourceCourse.class, ResourceCourseModel.class);
		return mapper.mapToModel(resourceCourseRepository.findAll());
	}

	//Gets a specific courses
	public ResourceCourseModel get(Integer id) {
		Mapper<ResourceCourse, ResourceCourseModel> mapper = mapperFactory.mapperFor(ResourceCourse.class, ResourceCourseModel.class);
		return mapper.mapToModel(resourceCourseRepository.findOne(id));
	}
	
	
	//create a course
	public ResourceCourseModel create(ResourceCourseModel assessment) {
		Mapper<ResourceCourse, ResourceCourseModel> mapper = mapperFactory.mapperFor(ResourceCourse.class, ResourceCourseModel.class);
		ResourceCourse entity = mapper.mapToEntity(assessment);
		entity = resourceCourseRepository.save(entity);
		return mapper.mapToModel(entity);
	}
	
	//deletes an course
		public ResourceCourseModel remove(Integer id) {
			Mapper<ResourceCourse, ResourceCourseModel> mapper = mapperFactory.mapperFor(ResourceCourse.class, ResourceCourseModel.class);
			ResourceCourse entity = resourceCourseRepository.findOne(id);
			resourceCourseRepository.delete(entity);
			return mapper.mapToModel(entity);
		}

	
	
	

}
