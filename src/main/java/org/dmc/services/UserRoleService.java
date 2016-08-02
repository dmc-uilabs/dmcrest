package org.dmc.services;

import javax.inject.Inject;

import org.dmc.services.data.entities.UserRole;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.UserRoleModel;
import org.dmc.services.data.repositories.UserRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

	@Inject
	private UserRoleRepository userRoleRepository;

	@Inject
	private MapperFactory mapperFactory;

	public UserRoleModel findOne(Integer id) {
		Mapper<UserRole, UserRoleModel> mapper = mapperFactory.mapperFor(UserRole.class, UserRoleModel.class);
		return mapper.mapToModel(userRoleRepository.findOne(id));
	}

	public UserRoleModel save (UserRoleModel userRoleModel) {
		Mapper<UserRole, UserRoleModel> mapper = mapperFactory.mapperFor(UserRole.class, UserRoleModel.class);
		return mapper.mapToModel(userRoleRepository.save(mapper.mapToEntity(userRoleModel)));
	}

	public UserRoleModel findByUserId(Integer dmdiiMemberId) {
		Mapper<UserRole, UserRoleModel> mapper = mapperFactory.mapperFor(UserRole.class, UserRoleModel.class);
		return mapper.mapToModel(userRoleRepository.findByUserId(dmdiiMemberId));
	}

//	public UserRoleModel createCompanyAdmin(Integer userId, Integer organizationId) {
//		Mapper<UserRole, UserRoleModel> mapper = mapperFactory.mapperFor(UserRole.class, UserRoleModel.class);
//		UserRole role = new UserRole();
//		role.setOrganizationId(organizationId);
//		role.setRoleId(2);
//		role.setUserId(orgUserModel.getUserId());
//		userRoleRepository.save(role);
//	}
}
