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
	private DMDIIMemberEventRepository dmdiiMemberEventRepository;
	
	@Inject
	private MapperFactory mapperFactory;
	
	public DMDIIMemberEventModel save(DMDIIMemberEventModel memberEvent) {
		Mapper<DMDIIMemberEvent, DMDIIMemberEventModel> mapper = mapperFactory.mapperFor(DMDIIMemberEvent.class,  DMDIIMemberEventModel.class);
		
		DMDIIMemberEvent memberEventEntity = mapper.mapToEntity(memberEvent);
		
		memberEventEntity = dmdiiMemberEventRepository.save(memberEventEntity);
		
		return mapper.mapToModel(memberEventEntity);
	}
	
	public DMDIIMemberEventModel delete(Integer eventId) {
		Mapper<DMDIIMemberEvent, DMDIIMemberEventModel> mapper = mapperFactory.mapperFor(DMDIIMemberEvent.class, DMDIIMemberEventModel.class);
		
		DMDIIMemberEvent eventEntity = dmdiiMemberEventRepository.findOne(eventId);
		
		eventEntity.setIsDeleted(true);
		eventEntity = dmdiiMemberEventRepository.save(eventEntity);
		
		return mapper.mapToModel(eventEntity);
	}
}
