package org.dmc.services.data.mappers;


import org.dmc.services.data.entities.ResourceBay;
import org.dmc.services.data.entities.ResourceMachine;
import org.dmc.services.data.models.ResourceBayModel;
import org.dmc.services.data.models.ResourceMachineModel;

import org.springframework.stereotype.Component;


//Custom mappers are only needed if you have complex (many to one, one to many) relationships. If not, no need
@Component
public class ResourceMachineMapper extends AbstractMapper<ResourceMachine, ResourceMachineModel> {

 //In this case, one bay can have many machines. We are mapping machines to the bay.
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
