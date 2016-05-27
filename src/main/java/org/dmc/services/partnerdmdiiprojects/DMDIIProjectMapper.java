package org.dmc.services.partnerdmdiiprojects;

import org.springframework.util.Assert;

public class DMDIIProjectMapper implements EntityModelMapper<DMDIIProjectEntity, DMDIIProject> {

	@Override
	public DMDIIProject entityToModel(DMDIIProjectEntity entity) {
		Assert.notNull(entity);
		DMDIIProject model = new DMDIIProject();
		
		
		return model;
	}

	@Override
	public DMDIIProjectEntity modelToEntity(DMDIIProject model) {
		Assert.notNull(model);
		DMDIIProjectEntity entity = new DMDIIProjectEntity();
		
		return entity;
	}
}
