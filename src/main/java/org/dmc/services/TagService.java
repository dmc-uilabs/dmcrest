package org.dmc.services;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.AreaOfExpertise;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.AreaOfExpertiseModel;
import org.dmc.services.data.repositories.AreaOfExpertiseRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {

	@Inject
	private AreaOfExpertiseRepository areaOfExpertiseRepository;

	@Inject
	private MapperFactory mapperFactory;

	public List<AreaOfExpertiseModel> getOrganizationTags() {
		Mapper<AreaOfExpertise, AreaOfExpertiseModel> mapper = mapperFactory.mapperFor(AreaOfExpertise.class, AreaOfExpertiseModel.class);
		return mapper.mapToModel(areaOfExpertiseRepository.findAll());
	}

	public List<AreaOfExpertiseModel> getDmdiiTags() {
		Mapper<AreaOfExpertise, AreaOfExpertiseModel> mapper = mapperFactory.mapperFor(AreaOfExpertise.class, AreaOfExpertiseModel.class);
		return mapper.mapToModel(areaOfExpertiseRepository.findByIsDmdiiTrue());
	}
}
