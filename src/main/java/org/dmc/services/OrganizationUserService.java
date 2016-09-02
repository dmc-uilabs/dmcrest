package org.dmc.services;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.OrganizationUser;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.OrganizationUserModel;
import org.dmc.services.data.repositories.OrganizationUserRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganizationUserService {

	@Inject
	private OrganizationUserRepository organizationUserRepository;

	@Inject
	private MapperFactory mapperFactory;

	public List<OrganizationUserModel> getUsersByOrganizationId (Integer organizationId) {
		Mapper<OrganizationUser, OrganizationUserModel> mapper = mapperFactory.mapperFor(OrganizationUser.class, OrganizationUserModel.class);
		return mapper.mapToModel(organizationUserRepository.findByOrganizationId(organizationId));
	}

	public OrganizationUserModel getOrganizationUserByUserId(Integer userId) {
		Mapper<OrganizationUser, OrganizationUserModel> mapper = mapperFactory.mapperFor(OrganizationUser.class, OrganizationUserModel.class);
		return mapper.mapToModel(organizationUserRepository.findByUserId(userId));
	}

	public OrganizationUserModel getByUserId (Integer userId) {
		Mapper<OrganizationUser, OrganizationUserModel> mapper = mapperFactory.mapperFor(OrganizationUser.class, OrganizationUserModel.class);
		return mapper.mapToModel(organizationUserRepository.findByUserId(userId));
	}

	public OrganizationUserModel getByUserIdAndOrganizationId(Integer userId, Integer organizationId) {
		Mapper<OrganizationUser, OrganizationUserModel> mapper = mapperFactory.mapperFor(OrganizationUser.class, OrganizationUserModel.class);
		return mapper.mapToModel(organizationUserRepository.findByUserIdAndOrganizationId(userId, organizationId));
	}

	public OrganizationUserModel saveOrganizationUser(OrganizationUserModel model) {
		Mapper<OrganizationUser, OrganizationUserModel> mapper = mapperFactory.mapperFor(OrganizationUser.class, OrganizationUserModel.class);
		return mapper.mapToModel(organizationUserRepository.save(mapper.mapToEntity(model)));
	}

	public Integer getNumberOfVerifiedUsers(Integer organizationId) {
		return organizationUserRepository.findNumberOfVerifiedUsersByOrganizationId(organizationId);
	}

	@Transactional
	public OrganizationUser createVerifiedOrganizationUser(User user, Organization organization) {
		// delete existing organization_user records
		organizationUserRepository.deleteByUserId(user.getId());
		return organizationUserRepository.save(new OrganizationUser(user, organization, true));
	}

}
