package org.dmc.services.data.mappers;

import javax.inject.Inject;

import org.dmc.services.UserService;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.models.OrganizationUserModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.dmdiimember.OrganizationService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class OrganizationUserMapper extends AbstractMapper<OrganizationUser, OrganizationUserModel> {

	@Inject
	private UserService userService;
	
	@Inject
	private OrganizationService organizationService;

	@Override
	public OrganizationUser mapToEntity(OrganizationUserModel model) {
		Assert.notNull(model);
		OrganizationUser entity = copyProperties(model, new OrganizationUser());
		
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<Organization, OrganizationModel> orgMapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);
		
		entity.setUser(userMapper.mapToEntity(userService.findOne(model.getUserId())));
		entity.setOrganization(orgMapper.mapToEntity(organizationService.findOne(model.getOrganizationId())));
		
		return entity;
	}

	@Override
	public OrganizationUserModel mapToModel(OrganizationUser entity) {
		Assert.notNull(entity);
		
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
