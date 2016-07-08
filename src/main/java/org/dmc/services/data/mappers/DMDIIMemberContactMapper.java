package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIContactType;
import org.dmc.services.data.entities.DMDIIMemberContact;
import org.dmc.services.data.models.DMDIIContactTypeModel;
import org.dmc.services.data.models.DMDIIMemberContactModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DMDIIMemberContactMapper extends AbstractMapper<DMDIIMemberContact, DMDIIMemberContactModel> {

	@Override
	public DMDIIMemberContact mapToEntity(DMDIIMemberContactModel model) {
		if (model == null) return null;
		
		DMDIIMemberContact entity = copyProperties(model, new DMDIIMemberContact());
		Mapper<DMDIIContactType, DMDIIContactTypeModel> mapper = mapperFactory.mapperFor(DMDIIContactType.class, DMDIIContactTypeModel.class);

		entity.setContactType(mapper.mapToEntity(model.getContactType()));
		return entity;
	}

	@Override
	public DMDIIMemberContactModel mapToModel(DMDIIMemberContact entity) {
		if (entity == null) return null;
		
		DMDIIMemberContactModel model = copyProperties(entity, new DMDIIMemberContactModel());
		Mapper<DMDIIContactType, DMDIIContactTypeModel> mapper = mapperFactory.mapperFor(DMDIIContactType.class, DMDIIContactTypeModel.class);

		model.setContactType(mapper.mapToModel(entity.getContactType()));
		return model;
	}

	@Override
	public Class<DMDIIMemberContact> supportsEntity() {
		return DMDIIMemberContact.class;
	}

	@Override
	public Class<DMDIIMemberContactModel> supportsModel() {
		return DMDIIMemberContactModel.class;
	}

}
