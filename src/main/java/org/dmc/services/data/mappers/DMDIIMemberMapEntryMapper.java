package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.models.DMDIIMemberMapEntryModel;
import org.springframework.stereotype.Component;

@Component
public class DMDIIMemberMapEntryMapper extends AbstractMapper<DMDIIMember, DMDIIMemberMapEntryModel> {

	@Override
	public DMDIIMember mapToEntity(DMDIIMemberMapEntryModel model) {
		throw new UnsupportedOperationException("Type DMDIIMemberMapEntryModel cannot be mapped to DMDIIMember entity");
	}

	@Override
	public DMDIIMemberMapEntryModel mapToModel(DMDIIMember entity) {
		if (entity == null) return null;
		
		DMDIIMemberMapEntryModel model = new DMDIIMemberMapEntryModel();
		model.setId(entity.getId());
		model.setName(entity.getOrganization().getName());
		model.setState(entity.getOrganization().getAddress().getState());
		return model;
	}

	@Override
	public Class<DMDIIMember> supportsEntity() {
		return DMDIIMember.class;
	}

	@Override
	public Class<DMDIIMemberMapEntryModel> supportsModel() {
		return DMDIIMemberMapEntryModel.class;
	}

}
