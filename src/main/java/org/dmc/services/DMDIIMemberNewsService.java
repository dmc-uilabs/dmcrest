package org.dmc.services;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIMemberNews;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIMemberNewsModel;
import org.dmc.services.data.repositories.DMDIIMemberNewsRepository;
import org.springframework.stereotype.Service;

@Service
public class DMDIIMemberNewsService {

	@Inject
	private DMDIIMemberNewsRepository dmdiiMemberNewsRepository;
	
	@Inject
	private MapperFactory mapperFactory;
	
	public DMDIIMemberNewsModel save(DMDIIMemberNewsModel memberNews) {
		Mapper<DMDIIMemberNews, DMDIIMemberNewsModel> mapper = mapperFactory.mapperFor(DMDIIMemberNews.class, DMDIIMemberNewsModel.class);
		
		DMDIIMemberNews memberNewsEntity = mapper.mapToEntity(memberNews);
		
		memberNewsEntity = dmdiiMemberNewsRepository.save(memberNewsEntity);
		
		return mapper.mapToModel(memberNewsEntity);
	}
}
