package org.dmc.services.mappers;

import org.dmc.services.entities.DMDIIRndFocus;
import org.dmc.services.models.DMDIIRndFocusModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DMDIIMemberRndFocusMapper extends AbstractMapper<DMDIIRndFocus, DMDIIRndFocusModel> {

	@Override
	public DMDIIRndFocus mapToEntity(DMDIIRndFocusModel model) {
		DMDIIRndFocus entity = new DMDIIRndFocus();
		BeanUtils.copyProperties(model, entity);
		return entity;
	}

	@Override
	public DMDIIRndFocusModel mapToModel(DMDIIRndFocus entity) {
		DMDIIRndFocusModel model = new DMDIIRndFocusModel();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public Class<DMDIIRndFocus> supportsEntity() {
		return DMDIIRndFocus.class;
	}

	@Override
	public Class<DMDIIRndFocusModel> supportsModel() {
		return DMDIIRndFocusModel.class;
	}

}
