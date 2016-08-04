package org.dmc.services.data.mappers;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.OrganizationUserService;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserContactInfo;
import org.dmc.services.data.entities.UserRoleAssignment;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.models.OrganizationUserModel;
import org.dmc.services.data.models.UserContactInfoModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.dmdiimember.OrganizationService;
import org.dmc.services.security.SecurityRoles;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserModel> {

	@Inject
	private OrganizationService organizationService;

	@Inject
	private OrganizationUserService organizationUserService;

	@Override
	public User mapToEntity(UserModel model) {
		if (model == null) return null;

		Mapper<UserContactInfo, UserContactInfoModel> contactInfoMapper = mapperFactory.mapperFor(UserContactInfo.class, UserContactInfoModel.class);
		Mapper<Organization, OrganizationModel> orgMapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);
		Mapper<OrganizationUser, OrganizationUserModel> orgUserMapper = mapperFactory.mapperFor(OrganizationUser.class, OrganizationUserModel.class);

		User entity = copyProperties(model, new User());
		entity.setUserContactInfo(contactInfoMapper.mapToEntity(model.getUserContactInfo()));

		OrganizationUser orgUserEntity = orgUserMapper.mapToEntity(organizationUserService.getByUserIdAndOrganizationId(entity.getId(), model.getOrganization()));
		if(orgUserEntity == null) {
			orgUserEntity = new OrganizationUser();
			orgUserEntity.setOrganization(orgMapper.mapToEntity(organizationService.findOne(model.getOrganization())));
		}

		entity.setOrganizationUser(orgUserEntity);

		return entity;
	}

	@Override
	public UserModel mapToModel(User entity) {
		if (entity == null) return null;

		Mapper<UserContactInfo, UserContactInfoModel> contactInfoMapper = mapperFactory.mapperFor(UserContactInfo.class, UserContactInfoModel.class);

		UserModel model = copyProperties(entity, new UserModel());
		model.setDMDIIMember(entity.getRoles().stream().anyMatch(this::orgIsDMDIIMember));
		model.setUserContactInfo(contactInfoMapper.mapToModel(entity.getUserContactInfo()));

		Map<Integer, String> roles = new HashMap<Integer, String>();
		for (UserRoleAssignment assignment : entity.getRoles()) {
			if (assignment.getRole().getRole().equals(SecurityRoles.SUPERADMIN)) {
				roles.put(0, SecurityRoles.SUPERADMIN);
				model.setDMDIIMember(true);
			} else {
				roles.put(assignment.getOrganization().getId(), assignment.getRole().getRole());
			}
		}
		model.setRoles(roles);
		model.setOrganization(entity.getOrganizationUser().getOrganization().getId());

		return model;
	}

	private boolean orgIsDMDIIMember(UserRoleAssignment roleAssignment) {
		boolean isMember = false;

		if (roleAssignment.getOrganization() != null) {
			isMember = roleAssignment.getOrganization().getDmdiiMember() != null;
		}

		return isMember;
	}

	@Override
	public Class<User> supportsEntity() {
		return User.class;
	}

	@Override
	public Class<UserModel> supportsModel() {
		return UserModel.class;
	}

}
