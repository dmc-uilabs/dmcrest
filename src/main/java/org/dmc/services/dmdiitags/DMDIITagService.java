package org.dmc.services.dmdiitags;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.AreaOfExpertise;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.AreaOfExpertiseModel;
import org.dmc.services.data.repositories.DMDIIAreaOfExpertiseRepository;
import org.springframework.stereotype.Service;

@Service
public class DMDIITagService {

	@Inject
	private DMDIIAreaOfExpertiseRepository dmdiiAreaOfExpertiseRepository;

	@Inject
	private MapperFactory mapperFactory;

	public List<AreaOfExpertiseModel> findAll() {
		Mapper<AreaOfExpertise, AreaOfExpertiseModel> mapper = mapperFactory.mapperFor(AreaOfExpertise.class, AreaOfExpertiseModel.class);
		return mapper.mapToModel(dmdiiAreaOfExpertiseRepository.findAll());
	}
}
