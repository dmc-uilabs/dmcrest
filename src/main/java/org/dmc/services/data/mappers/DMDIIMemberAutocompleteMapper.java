package org.dmc.services.data.mappers;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.models.DMDIIMemberAutocompleteModel;
import org.dmc.services.dmdiimember.DMDIIMemberDao;
import org.springframework.stereotype.Component;

@Component
public class DMDIIMemberAutocompleteMapper extends AbstractMapper<DMDIIMember, DMDIIMemberAutocompleteModel> {

	@Inject
	private DMDIIMemberDao dmdiiMemberDao;

	@Override
	public DMDIIMember mapToEntity(DMDIIMemberAutocompleteModel model) {
		return dmdiiMemberDao.findOne(model.getId());
	}

	@Override
	public DMDIIMemberAutocompleteModel mapToModel(DMDIIMember entity) {
		return new DMDIIMemberAutocompleteModel(entity.getId(), entity.getOrganization().getName());
	}

	@Override
	public Class<DMDIIMember> supportsEntity() {
		return DMDIIMember.class;
	}

	@Override
	public Class<DMDIIMemberAutocompleteModel> supportsModel() {
		return DMDIIMemberAutocompleteModel.class;
	}

}
