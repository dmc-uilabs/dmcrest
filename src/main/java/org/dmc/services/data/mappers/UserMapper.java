package org.dmc.services.data.mappers;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserContactInfo;
import org.dmc.services.data.entities.UserRoleAssignment;
import org.dmc.services.data.models.UserContactInfoModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.repositories.OrganizationRepository;
import org.dmc.services.data.repositories.OrganizationUserRepository;
import org.dmc.services.security.SecurityRoles;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserModel> {

	@Inject
	private OrganizationRepository organizationRepository;

	@Inject
	private OrganizationUserRepository organizationUserRepository;

	@Override
	public User mapToEntity(UserModel model) {
		User entity = null;

		if (model != null) {
			Mapper<UserContactInfo, UserContactInfoModel> mapper;
			mapper = mapperFactory.mapperFor(UserContactInfo.class, UserContactInfoModel.class);

			entity = copyProperties(model, new User());
			entity.setRealname(model.getDisplayName());
			entity.setUserContactInfo(mapper.mapToEntity(model.getUserContactInfo()));

			if (model.getCompanyId() != null) {
				OrganizationUser orgUserEntity = organizationUserRepository
						.findByUserIdAndOrganizationId(entity.getId(), model.getCompanyId());

				if (orgUserEntity == null) {
					orgUserEntity = new OrganizationUser();
					orgUserEntity.setOrganization(organizationRepository.findOne(model.getCompanyId()));
				}

				entity.setOrganizationUser(orgUserEntity);
				entity.getOrganizationUser().setUser(entity);
			}
		}
		return entity;
	}

	@Override
	public UserModel mapToModel(User entity) {
		UserModel model = null;

		if (entity != null) {
			Mapper<UserContactInfo, UserContactInfoModel> contactInfoMapper;
			contactInfoMapper = mapperFactory.mapperFor(UserContactInfo.class, UserContactInfoModel.class);

			model = copyProperties(entity, new UserModel());
			model.setDisplayName(entity.getRealname());
			model.setAccountId(entity.getId());
			model.setProfileId(entity.getId());

			if (entity.getRoles() != null && !entity.getRoles().isEmpty()) {
				model.setIsDMDIIMember(entity.getRoles().stream().anyMatch(this::orgIsDMDIIMember));
				mapEntityRoles(entity, model);
			} else {
				model.setIsDMDIIMember(false);
			}

			model.setUserContactInfo(contactInfoMapper.mapToModel(entity.getUserContactInfo()));
			model.setTermsConditions(entity.getTermsAndCondition() != null);

			model.setCompanyId((entity.getOrganizationUser() == null) ?
					null :
					entity.getOrganizationUser().getOrganization().getId());
		}
		return model;
	}

	private void mapEntityRoles(User entity, UserModel model) {
		Map<Integer, String> roles = new HashMap<>();

		for (UserRoleAssignment assignment : entity.getRoles()) {
			if (assignment.getRole().getRole().equals(SecurityRoles.SUPERADMIN)) {
				roles.put(0, SecurityRoles.SUPERADMIN);
				model.setIsDMDIIMember(true);
			} else {
				roles.put(assignment.getOrganization().getId(), assignment.getRole().getRole());
			}
		}
		model.setRoles(roles);
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
