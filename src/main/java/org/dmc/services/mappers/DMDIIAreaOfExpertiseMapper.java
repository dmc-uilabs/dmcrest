package org.dmc.services.mappers;

import org.dmc.services.entities.DMDIIAreaOfExpertise;
import org.dmc.services.models.DMDIIAreaOfExpertiseModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DMDIIAreaOfExpertiseMapper extends AbstractMapper<DMDIIAreaOfExpertise, DMDIIAreaOfExpertiseModel> {

	@Override
	public DMDIIAreaOfExpertise mapToEntity(DMDIIAreaOfExpertiseModel model) {
		DMDIIAreaOfExpertise entity = new DMDIIAreaOfExpertise();
		BeanUtils.copyProperties(model, entity);
		return entity;
	}

	@Override
	public DMDIIAreaOfExpertiseModel mapToModel(DMDIIAreaOfExpertise entity) {
		DMDIIAreaOfExpertiseModel model = new DMDIIAreaOfExpertiseModel();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public Class<DMDIIAreaOfExpertise> supportsEntity() {
		return DMDIIAreaOfExpertise.class;
	}

	@Override
	public Class<DMDIIAreaOfExpertiseModel> supportsModel() {
		return DMDIIAreaOfExpertiseModel.class;
	}
}
