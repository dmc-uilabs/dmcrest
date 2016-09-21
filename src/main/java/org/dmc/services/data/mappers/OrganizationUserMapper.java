package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.models.OrganizationUserModel;
import org.dmc.services.data.repositories.OrganizationRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class OrganizationUserMapper extends AbstractMapper<OrganizationUser, OrganizationUserModel> {

	@Inject
	private UserRepository userRepository;

	@Inject
	private OrganizationRepository organizationRepository;

	@Override
	public OrganizationUser mapToEntity(OrganizationUserModel model) {
		if (model == null) return null;

		OrganizationUser entity = copyProperties(model, new OrganizationUser());

		entity.setUser(userRepository.findOne(model.getUserId()));
		entity.setOrganization(organizationRepository.findOne(model.getOrganizationId()));

		return entity;
	}

	@Override
	public OrganizationUserModel mapToModel(OrganizationUser entity) {
		if (entity == null) return null;

		OrganizationUserModel model = copyProperties(entity, new OrganizationUserModel());

		model.setUserId(entity.getUser().getId());
		model.setOrganizationId(entity.getOrganization().getId());

		return model;
	}

	@Override
	public Class<OrganizationUser> supportsEntity() {
		return OrganizationUser.class;
	}

	@Override
	public Class<OrganizationUserModel> supportsModel() {
		return OrganizationUserModel.class;
	}


}
