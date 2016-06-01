package org.dmc.services.partnerdmdiiprojects;

import org.springframework.util.Assert;

public class DMDIIProjectMapper implements EntityModelMapper<DMDIIProjectEntity, DMDIIProject> {

	@Override
	public DMDIIProject entityToModel(DMDIIProjectEntity entity) {
		Assert.notNull(entity);
		DMDIIProject model = new DMDIIProject();
		model.setId(entity.getId());
		model.setAwardedDate(entity.getAwardedDate());
		model.setPrimeOrganization(entity.getPrimeOrganization());
		model.setPrincipalPointOfContact(entity.getPrincipalPointOfContact());
		model.setProjectStatus(entity.getProjectStatus());
		model.setProjectSummary(entity.getProjectSummary());
		model.setProjectTitle(entity.getProjectTitle());
		return model;
	}

	@Override
	public DMDIIProjectEntity modelToEntity(DMDIIProject model) {
		Assert.notNull(model);
		DMDIIProjectEntity entity = new DMDIIProjectEntity();
		entity.setId(model.getId());
		entity.setAwardedDate(model.getAwardedDate());
		entity.setPrimeOrganization(model.getPrimeOrganization());
		entity.setPrincipalInvestigator(model.getPrincipalInvestigator());
		entity.setPrincipalPointOfContact(model.getPrincipalPointOfContact());
		entity.setProjectStatus(model.getProjectStatus());
		entity.setProjectSummary(model.getProjectSummary());
		entity.setProjectTitle(model.getProjectTitle());
		return entity;
	}
}
