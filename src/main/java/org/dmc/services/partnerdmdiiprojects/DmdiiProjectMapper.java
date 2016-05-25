package org.dmc.services.partnerdmdiiprojects;

import org.springframework.util.Assert;

public class DmdiiProjectMapper implements EntityModelMapper<DmdiiProjectEntity, DmdiiProject> {

	@Override
	public DmdiiProject entityToModel(DmdiiProjectEntity entity) {
		Assert.notNull(entity);
		DmdiiProject model = new DmdiiProject();
		
		return model;
	}

	@Override
	public DmdiiProjectEntity modelToEntity(DmdiiProject model) {
		// TODO Auto-generated method stub
		return null;
	}
}
