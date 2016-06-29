package org.dmc.services.resources;

import java.util.List;
import javax.inject.Inject;
import org.dmc.services.data.entities.ResourceLab;
import org.dmc.services.data.entities.ResourceProject;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ResourceLabModel;
import org.dmc.services.data.models.ResourceProjectModel;
import org.dmc.services.data.repositories.ResourceLabRepository;
import org.springframework.stereotype.Service;


@Service
public class ResourceLabService {

	@Inject
	private ResourceLabRepository resourceLabRepository;
	
	@Inject
	private MapperFactory mapperFactory;


	//Gets all assessments
	public List<ResourceLabModel> getAll() {
		Mapper<ResourceLab, ResourceLabModel> mapper = mapperFactory.mapperFor(ResourceLab.class, ResourceLabModel.class);
		return mapper.mapToModel(resourceLabRepository.findAll());
	}

	//Gets a specific assessment
	public ResourceLabModel get(Integer id) {
		Mapper<ResourceLab, ResourceLabModel> mapper = mapperFactory.mapperFor(ResourceLab.class, ResourceLabModel.class);
		return mapper.mapToModel(resourceLabRepository.findOne(id));
	}
	
	public ResourceLabModel create(ResourceLabModel assessment) {
		Mapper<ResourceLab, ResourceLabModel> mapper = mapperFactory.mapperFor(ResourceLab.class, ResourceLabModel.class);
		ResourceLab entity = mapper.mapToEntity(assessment);
		entity = resourceLabRepository.save(entity);
		return mapper.mapToModel(entity);
	}
	
	//deletes an lab
	public ResourceLabModel remove(Integer id) {
		Mapper<ResourceLab, ResourceLabModel> mapper = mapperFactory.mapperFor(ResourceLab.class, ResourceLabModel.class);
		ResourceLab entity = resourceLabRepository.findOne(id);
		resourceLabRepository.delete(entity);
		return mapper.mapToModel(entity);
	}
	
	
	

}
