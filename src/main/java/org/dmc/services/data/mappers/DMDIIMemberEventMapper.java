package org.dmc.services.data.mappers;

import java.text.Format;
import java.text.SimpleDateFormat;

import org.dmc.services.data.entities.DMDIIMemberEvent;
import org.dmc.services.data.models.DMDIIMemberEventModel;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DMDIIMemberEventMapper extends AbstractMapper<DMDIIMemberEvent, DMDIIMemberEventModel> {

	@Override
	public DMDIIMemberEvent mapToEntity(DMDIIMemberEventModel model) {
		if (model == null) return null;
		
		DMDIIMemberEvent entity = copyProperties(model, new DMDIIMemberEvent());
		
		entity.setDate(new DateTime(model.getDate()).toDate());
		
		return entity;
	}

	@Override
	public DMDIIMemberEventModel mapToModel(DMDIIMemberEvent entity) {
		if (entity == null) return null;
		
		DMDIIMemberEventModel model = copyProperties(entity, new DMDIIMemberEventModel());
		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		model.setDate(formatter.format(entity.getDate()));
		
		return model;
	}

	@Override
	public Class<DMDIIMemberEvent> supportsEntity() {
		return DMDIIMemberEvent.class;
	}

	@Override
	public Class<DMDIIMemberEventModel> supportsModel() {
		return DMDIIMemberEventModel.class;
	}

}
