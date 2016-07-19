package org.dmc.services.dmdiitags;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIAreaOfExpertise;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIAreaOfExpertiseModel;
import org.dmc.services.data.repositories.DMDIIAreaOfExpertiseRepository;
import org.springframework.stereotype.Service;

@Service
public class DMDIITagService {

	@Inject
	private DMDIIAreaOfExpertiseRepository dmdiiAreaOfExpertiseRepository;

	@Inject
	private MapperFactory mapperFactory;

	public List<DMDIIAreaOfExpertiseModel> findAll() {
		Mapper<DMDIIAreaOfExpertise, DMDIIAreaOfExpertiseModel> mapper = mapperFactory.mapperFor(DMDIIAreaOfExpertise.class, DMDIIAreaOfExpertiseModel.class);
		return mapper.mapToModel(dmdiiAreaOfExpertiseRepository.findAll());
	}
}
