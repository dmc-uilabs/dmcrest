package org.dmc.services.mappers;

import org.dmc.services.dmdiitype.DMDIIType;
import org.dmc.services.models.DMDIITypeModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DMDIIMemberTypeMapper extends AbstractMapper<DMDIIType, DMDIITypeModel> {

	@Override
	public DMDIIType mapToEntity(DMDIITypeModel model) {
		DMDIIType entity = new DMDIIType();
		BeanUtils.copyProperties(model, entity);
		return entity;
	}

	@Override
	public DMDIITypeModel mapToModel(DMDIIType entity) {
		DMDIITypeModel model = new DMDIITypeModel();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public Class<DMDIIType> supportsEntity() {
		return DMDIIType.class;
	}

	@Override
	public Class<DMDIITypeModel> supportsModel() {
		return DMDIITypeModel.class;
	}

}
