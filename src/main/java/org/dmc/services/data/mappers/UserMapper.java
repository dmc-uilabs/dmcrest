package org.dmc.services.data.mappers;

import java.util.HashMap;
import java.util.Map;

import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserContactInfo;
import org.dmc.services.data.entities.UserRoleAssignment;
import org.dmc.services.data.models.UserContactInfoModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.models.UserRoleAssignmentModel;
import org.dmc.services.security.SecurityRoles;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserModel> {

	@Override
	public User mapToEntity(UserModel model) {
		if(model == null)
			return null;

		User entity = copyProperties(model, new User());

		Mapper<UserRoleAssignment, UserRoleAssignmentModel> roleMapper = mapperFactory.mapperFor(UserRoleAssignment.class, UserRoleAssignmentModel.class);
		Mapper<UserContactInfo, UserContactInfoModel> contactInfoMapper = mapperFactory.mapperFor(UserContactInfo.class, UserContactInfoModel.class);
		entity.setRoles(roleMapper.mapToEntity(model.getRoles()));
		entity.setUserContactInfo(contactInfoMapper.mapToEntity(model.getUserContactInfo()));

		return entity;
	}

	@Override
	public UserModel mapToModel(User entity) {
		if(entity == null)
			return null;

		UserModel model = copyProperties(entity, new UserModel());

		Mapper<UserContactInfo, UserContactInfoModel> contactInfoMapper = mapperFactory.mapperFor(UserContactInfo.class, UserContactInfoModel.class);
		Mapper<UserRoleAssignment, UserRoleAssignmentModel> roleMapper = mapperFactory.mapperFor(UserRoleAssignment.class, UserRoleAssignmentModel.class);
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
