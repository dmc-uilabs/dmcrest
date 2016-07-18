package org.dmc.services.data.mappers;

import java.text.Format;
import java.text.SimpleDateFormat;

import org.dmc.services.data.entities.DMDIIProjectNews;
import org.dmc.services.data.models.DMDIIProjectNewsModel;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DMDIIProjectNewsMapper extends AbstractMapper<DMDIIProjectNews, DMDIIProjectNewsModel> {

	@Override
	public DMDIIProjectNews mapToEntity(DMDIIProjectNewsModel model) {
		Assert.notNull(model);
		DMDIIProjectNews entity = copyProperties(model, new DMDIIProjectNews());
		
		entity.setDateCreated(new DateTime(model.getDateCreated()).toDate());
		
		return entity;
	}

	@Override
	public DMDIIProjectNewsModel mapToModel(DMDIIProjectNews entity) {
		Assert.notNull(entity);
		DMDIIProjectNewsModel model = copyProperties(entity, new DMDIIProjectNewsModel());
		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		model.setDateCreated(formatter.format(entity.getDateCreated()));
		
		return model;
	}

	@Override
	public Class<DMDIIProjectNews> supportsEntity() {
		return DMDIIProjectNews.class;
	}

	@Override
	public Class<DMDIIProjectNewsModel> supportsModel() {
		return DMDIIProjectNewsModel.class;
	}

}
