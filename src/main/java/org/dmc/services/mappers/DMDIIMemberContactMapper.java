package org.dmc.services.mappers;

import org.dmc.services.entities.DMDIIContactType;
import org.dmc.services.entities.DMDIIMemberContact;
import org.dmc.services.models.DMDIIContactTypeModel;
import org.dmc.services.models.DMDIIMemberContactModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DMDIIMemberContactMapper extends AbstractMapper<DMDIIMemberContact, DMDIIMemberContactModel> {

	@Override
	public DMDIIMemberContact mapToEntity(DMDIIMemberContactModel model) {
		DMDIIMemberContact entity = copyProperties(model, new DMDIIMemberContact());
		Mapper<DMDIIContactType, DMDIIContactTypeModel> mapper = mapperFactory.mapperFor(DMDIIContactType.class, DMDIIContactTypeModel.class);

		entity.setContactType(mapper.mapToEntity(model.getContactType()));
		return entity;
	}

	@Override
	public DMDIIMemberContactModel mapToModel(DMDIIMemberContact entity) {
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
