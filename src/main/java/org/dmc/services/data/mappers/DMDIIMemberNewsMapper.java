package org.dmc.services.data.mappers;

import java.text.Format;
import java.text.SimpleDateFormat;

import org.dmc.services.data.entities.DMDIIMemberNews;
import org.dmc.services.data.models.DMDIIMemberNewsModel;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DMDIIMemberNewsMapper  extends AbstractMapper<DMDIIMemberNews, DMDIIMemberNewsModel>{

	@Override
	public DMDIIMemberNews mapToEntity(DMDIIMemberNewsModel model) {
		if (model == null) return null;
		
		DMDIIMemberNews entity = copyProperties(model, new DMDIIMemberNews());
				
		entity.setDateCreated(new DateTime(model.getDateCreated()).toDate());
		
		return entity;
	}

	@Override
	public DMDIIMemberNewsModel mapToModel(DMDIIMemberNews entity) {
		if (entity == null) return null;
		
		DMDIIMemberNewsModel model = copyProperties(entity, new DMDIIMemberNewsModel());
		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		model.setDateCreated(formatter.format(entity.getDateCreated()));
		
		return model;
	}

	@Override
	public Class<DMDIIMemberNews> supportsEntity() {
		return DMDIIMemberNews.class;
	}

	@Override
	public Class<DMDIIMemberNewsModel> supportsModel() {
		return DMDIIMemberNewsModel.class;
	}

}
