package org.dmc.services.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.entities.ResourceLab;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ResourceLabModel;
import org.dmc.services.data.repositories.ResourceLabRepository;
import org.springframework.data.domain.PageRequest;
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
	
	
	

}
