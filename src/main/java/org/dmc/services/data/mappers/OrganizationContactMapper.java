package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.OrganizationContact;
import org.dmc.services.data.entities.OrganizationContactType;
import org.dmc.services.data.models.OrganizationContactModel;
import org.dmc.services.data.models.OrganizationContactTypeModel;
import org.springframework.stereotype.Component;

@Component
public class OrganizationContactMapper extends AbstractMapper<OrganizationContact, OrganizationContactModel> {

	@Override
	public OrganizationContact mapToEntity(OrganizationContactModel model) {
		if (model == null) return null;

		OrganizationContact entity = copyProperties(model, new OrganizationContact());
		Mapper<OrganizationContactType, OrganizationContactTypeModel> mapper = mapperFactory.mapperFor(OrganizationContactType.class, OrganizationContactTypeModel.class);

		entity.setContactType(mapper.mapToEntity(model.getContactType()));
		return entity;
	}

	@Override
	public OrganizationContactModel mapToModel(OrganizationContact entity) {
		if (entity == null) return null;

		OrganizationContactModel model = copyProperties(entity, new OrganizationContactModel());
		Mapper<OrganizationContactType, OrganizationContactTypeModel> mapper = mapperFactory.mapperFor(OrganizationContactType.class, OrganizationContactTypeModel.class);

		model.setContactType(mapper.mapToModel(entity.getContactType()));
		return model;
	}

	@Override
	public Class<OrganizationContact> supportsEntity() {
		return OrganizationContact.class;
	}

	@Override
	public Class<OrganizationContactModel> supportsModel() {
		return OrganizationContactModel.class;
	}

}
