package org.dmc.services.mappers;

import org.dmc.services.entities.DMDIISkill;
import org.dmc.services.models.DMDIISkillModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DMDIIMemberSkillMapper extends AbstractMapper<DMDIISkill, DMDIISkillModel> {

	@Override
	public DMDIISkill mapToEntity(DMDIISkillModel model) {
		DMDIISkill entity = new DMDIISkill();
		BeanUtils.copyProperties(entity, model);
		return entity;
	}

	@Override
	public DMDIISkillModel mapToModel(DMDIISkill entity) {
		DMDIISkillModel model = new DMDIISkillModel();
		BeanUtils.copyProperties(model, entity);
		return model;
	}

	@Override
	public Class<DMDIISkill> supportsEntity() {
		return DMDIISkill.class;
	}

	@Override
	public Class<DMDIISkillModel> supportsModel() {
		return DMDIISkillModel.class;
	}

}
