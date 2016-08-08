package org.dmc.services.dmdiimember;

import javax.inject.Inject;

import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.repositories.OrganizationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

	@Inject
	private OrganizationRepository organizationRepository;

	@Inject
	private MapperFactory mapperFactory;


	public OrganizationModel save(OrganizationModel organizationModel) {
		Mapper<Organization, OrganizationModel> mapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);

		Organization organizationEntity = mapper.mapToEntity(organizationModel);

		if(organizationEntity.getId() == null) {
			organizationEntity = organizationRepository.save(organizationEntity);
		} else {
			Organization existingOrganization = organizationRepository.findOne(organizationEntity.getId());
			BeanUtils.copyProperties(organizationEntity, existingOrganization);
			organizationEntity = organizationRepository.save(existingOrganization);
		}

		return mapper.mapToModel(organizationEntity);

	}

	public Organization save(Organization organization) {
		return organizationRepository.save(organization);
	}
	
	public OrganizationModel findOne(Integer id) {
		Mapper<Organization, OrganizationModel> mapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);
		return mapper.mapToModel(organizationRepository.findOne(id));
	}
}
