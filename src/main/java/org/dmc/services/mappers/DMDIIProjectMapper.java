package org.dmc.services.mappers;

import org.dmc.services.partnerdmdiiprojects.DMDIIProject;
import org.dmc.services.partnerdmdiiprojects.DMDIIProjectEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DMDIIProjectMapper implements Mapper<DMDIIProjectEntity, DMDIIProject> {

	@Override
	public DMDIIProjectEntity mapToEntity(DMDIIProject model) {
		Assert.notNull(model);
		DMDIIProjectEntity entity = new DMDIIProjectEntity();
		BeanUtils.copyProperties(model, entity);
		return entity;
	}

	@Override
	public DMDIIProject mapToModel(DMDIIProjectEntity entity) {
		Assert.notNull(entity);
		DMDIIProject model = new DMDIIProject();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public Class<DMDIIProjectEntity> supportsEntity() {
		return DMDIIProjectEntity.class;
	}

	@Override
	public Class<DMDIIProject> supportsModel() {
		return DMDIIProject.class;
	}

}
