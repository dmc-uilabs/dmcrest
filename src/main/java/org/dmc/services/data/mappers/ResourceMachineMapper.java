package org.dmc.services.data.mappers;


import org.dmc.services.data.entities.ResourceBay;
import org.dmc.services.data.entities.ResourceMachine;
import org.dmc.services.data.models.ResourceBayModel;
import org.dmc.services.data.models.ResourceMachineModel;

import org.springframework.stereotype.Component;

@Component
public class ResourceMachineMapper extends AbstractMapper<ResourceMachine, ResourceMachineModel> {

	@Override
	public ResourceMachine mapToEntity(ResourceMachineModel model) {
		ResourceMachine entity = copyProperties(model, new ResourceMachine());
		Mapper<ResourceBay, ResourceBayModel> bayMapper = mapperFactory.mapperFor(ResourceBay.class, ResourceBayModel.class);
		entity.setBay(bayMapper.mapToEntity(model.getBay()));
		return entity;
	}
		
	@Override
	public ResourceMachineModel mapToModel(ResourceMachine entity) {
		ResourceMachineModel model = copyProperties(entity, new ResourceMachineModel());
		Mapper<ResourceBay, ResourceBayModel> bayMapper = mapperFactory.mapperFor(ResourceBay.class, ResourceBayModel.class);
		model.setBay(bayMapper.mapToModel(entity.getBay()));
		return model;
	}

	@Override
	public Class<ResourceMachine> supportsEntity() {
		return ResourceMachine.class;
	}

	@Override
	public Class<ResourceMachineModel> supportsModel() {
		return ResourceMachineModel.class;
	}
	
}
