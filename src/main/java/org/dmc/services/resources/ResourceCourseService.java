package org.dmc.services.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.entities.ResourceCourse;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ResourceCourseModel;
import org.dmc.services.data.repositories.ResourceCourseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class ResourceCourseService {

	@Inject
	private ResourceCourseRepository resourceCourseRepository;
	
	@Inject
	private MapperFactory mapperFactory;


	//Gets all assessments
	public List<ResourceCourseModel> getAll() {
		Mapper<ResourceCourse, ResourceCourseModel> mapper = mapperFactory.mapperFor(ResourceCourse.class, ResourceCourseModel.class);
		return mapper.mapToModel(resourceCourseRepository.findAll());
	}

	//Gets a specific assessment
	public ResourceCourseModel get(Integer id) {
		Mapper<ResourceCourse, ResourceCourseModel> mapper = mapperFactory.mapperFor(ResourceCourse.class, ResourceCourseModel.class);
		return mapper.mapToModel(resourceCourseRepository.findOne(id));
	}
	
	
	

}
