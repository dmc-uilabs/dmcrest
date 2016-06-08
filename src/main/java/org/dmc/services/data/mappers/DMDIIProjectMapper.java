package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DMDIIProjectMapper extends AbstractMapper<DMDIIProject, DMDIIProjectModel> {

	@Override
	public DMDIIProject mapToEntity(DMDIIProjectModel model) {
		Assert.notNull(model);
		DMDIIProject entity = new DMDIIProject();
		BeanUtils.copyProperties(model, entity);
		return entity;
	}

	@Override
	public DMDIIProjectModel mapToModel(DMDIIProject entity) {
		Assert.notNull(entity);
		DMDIIProjectModel model = new DMDIIProjectModel();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public Class<DMDIIProject> supportsEntity() {
		return DMDIIProject.class;
	}

	@Override
	public Class<DMDIIProjectModel> supportsModel() {
		return DMDIIProjectModel.class;
	}

}
