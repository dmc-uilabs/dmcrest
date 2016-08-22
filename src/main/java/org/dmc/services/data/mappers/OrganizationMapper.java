package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.Address;
import org.dmc.services.data.entities.AreaOfExpertise;
import org.dmc.services.data.entities.Award;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.models.AddressModel;
import org.dmc.services.data.models.AreaOfExpertiseModel;
import org.dmc.services.data.models.AwardModel;
import org.dmc.services.data.models.OrganizationModel;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper extends AbstractMapper<Organization, OrganizationModel> {

	@Override
	public Organization mapToEntity(OrganizationModel model) {
		if (model == null) return null;

		Organization entity = copyProperties(model, new Organization(), new String[]{"awards"});

		Mapper<Address, AddressModel> addressMapper = mapperFactory.mapperFor(Address.class, AddressModel.class);
		Mapper<Award, AwardModel> awardMapper = mapperFactory.mapperFor(Award.class, AwardModel.class);
		Mapper<AreaOfExpertise, AreaOfExpertiseModel> aoeMapper = mapperFactory.mapperFor(AreaOfExpertise.class, AreaOfExpertiseModel.class);

		entity.setAddress(addressMapper.mapToEntity(model.getAddress()));
		entity.setAwards(awardMapper.mapToEntity(model.getAwards()));
		entity.setAreasOfExpertise(aoeMapper.mapToEntity(model.getAreasOfExpertise()));
		entity.setDesiredAreasOfExpertise(aoeMapper.mapToEntity(model.getDesiredAreasOfExpertise()));

		return entity;
	}

	@Override
	public OrganizationModel mapToModel(Organization entity) {
		if (entity == null) return null;

		OrganizationModel model = copyProperties(entity, new OrganizationModel());

		Mapper<Address, AddressModel> addressMapper = mapperFactory.mapperFor(Address.class, AddressModel.class);
		Mapper<Award, AwardModel> awardMapper = mapperFactory.mapperFor(Award.class, AwardModel.class);
		Mapper<AreaOfExpertise, AreaOfExpertiseModel> aoeMapper = mapperFactory.mapperFor(AreaOfExpertise.class, AreaOfExpertiseModel.class);

		model.setAddress(addressMapper.mapToModel(entity.getAddress()));
		model.setAwards(awardMapper.mapToModel(entity.getAwards()));
		model.setAreasOfExpertise(aoeMapper.mapToModel(entity.getAreasOfExpertise()));
		model.setDesiredAreasOfExpertise(aoeMapper.mapToModel(entity.getDesiredAreasOfExpertise()));

		return model;
	}

	@Override
	public Class<Organization> supportsEntity() {
		return Organization.class;
	}

	@Override
	public Class<OrganizationModel> supportsModel() {
		return OrganizationModel.class;
	}

}
