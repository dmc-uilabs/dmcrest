package org.dmc.services.data.mappers;

import java.text.Format;
import java.text.SimpleDateFormat;

import org.dmc.services.data.entities.DMDIIProjectEvent;
import org.dmc.services.data.models.DMDIIProjectEventModel;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DMDIIProjectEventMapper extends AbstractMapper<DMDIIProjectEvent, DMDIIProjectEventModel> {

	@Override
	public DMDIIProjectEvent mapToEntity(DMDIIProjectEventModel model) {
		Assert.notNull(model);
		DMDIIProjectEvent entity = copyProperties(model, new DMDIIProjectEvent());
		
		entity.setEventDate(new DateTime(model.getEventDate()).toDate());
		
		return entity;
	}

	@Override
	public DMDIIProjectEventModel mapToModel(DMDIIProjectEvent entity) {
		Assert.notNull(entity);
		DMDIIProjectEventModel model = copyProperties(entity, new DMDIIProjectEventModel());
		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		model.setEventDate(formatter.format(entity.getEventDate()));
		
		return model;
	}

	@Override
	public Class<DMDIIProjectEvent> supportsEntity() {
		return DMDIIProjectEvent.class;
	}

	@Override
	public Class<DMDIIProjectEventModel> supportsModel() {
		return DMDIIProjectEventModel.class;
	}

}
