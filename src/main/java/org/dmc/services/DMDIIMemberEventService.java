package org.dmc.services;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIMemberEvent;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIMemberEventModel;
import org.dmc.services.data.repositories.DMDIIMemberEventRepository;
import org.springframework.stereotype.Service;

@Service
public class DMDIIMemberEventService {

	@Inject
	private DMDIIMemberEventRepository dmdiiMemberEventsRepository;
	
	@Inject
	private MapperFactory mapperFactory;
	
	public DMDIIMemberEventModel save(DMDIIMemberEventModel memberEvent) {
		Mapper<DMDIIMemberEvent, DMDIIMemberEventModel> mapper = mapperFactory.mapperFor(DMDIIMemberEvent.class,  DMDIIMemberEventModel.class);
		
		DMDIIMemberEvent memberEventEntity = mapper.mapToEntity(memberEvent);
		
		memberEventEntity = dmdiiMemberEventsRepository.save(memberEventEntity);
		
		return mapper.mapToModel(memberEventEntity);
	}
}
