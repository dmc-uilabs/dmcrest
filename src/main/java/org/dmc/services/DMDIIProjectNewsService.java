package org.dmc.services;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIProjectNews;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIProjectNewsModel;
import org.dmc.services.data.repositories.DMDIIProjectNewsRepository;
import org.springframework.stereotype.Service;

@Service
public class DMDIIProjectNewsService {

	@Inject
	DMDIIProjectNewsRepository dmdiiProjectNewsRepository;
	
	@Inject
	MapperFactory mapperFactory;
	
	public DMDIIProjectNewsModel save(DMDIIProjectNewsModel projectNews) {
		Mapper<DMDIIProjectNews, DMDIIProjectNewsModel> mapper = mapperFactory.mapperFor(DMDIIProjectNews.class, DMDIIProjectNewsModel.class);
		
		DMDIIProjectNews projectNewsEntity = mapper.mapToEntity(projectNews);
		
		projectNewsEntity = dmdiiProjectNewsRepository.save(projectNewsEntity);
		
		return mapper.mapToModel(projectNewsEntity);
	}
}
