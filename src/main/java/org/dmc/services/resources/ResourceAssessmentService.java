package org.dmc.services.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.ResourceAssessment;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ResourceAssessmentModel;
import org.dmc.services.data.repositories.ResourceAssessmentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class ResourceAssessmentService {

	@Inject
	private ResourceAssessmentRepository resourceAssessmentRepository;
	
	@Inject
	private MapperFactory mapperFactory;


	//Gets all assessments
	public List<ResourceAssessmentModel> getAllAssessments() {
		Mapper<ResourceAssessment, ResourceAssessmentModel> mapper = mapperFactory.mapperFor(ResourceAssessment.class, ResourceAssessmentModel.class);
		return mapper.mapToModel(resourceAssessmentRepository.findAll());
	}

	//Gets a specific assessment
	public ResourceAssessmentModel getAssessment(Integer id) {
		Mapper<ResourceAssessment, ResourceAssessmentModel> mapper = mapperFactory.mapperFor(ResourceAssessment.class, ResourceAssessmentModel.class);
		return mapper.mapToModel(resourceAssessmentRepository.findOne(id));
	}
	
	//Creates an Assessment
	public ResourceAssessmentModel createAssessment(ResourceAssessmentModel assessment) {
		Mapper<ResourceAssessment, ResourceAssessmentModel> mapper = mapperFactory.mapperFor(ResourceAssessment.class, ResourceAssessmentModel.class);
		ResourceAssessment entity = mapper.mapToEntity(assessment);
		entity = resourceAssessmentRepository.save(entity);
		return mapper.mapToModel(entity);
	}
	
	
	

}
