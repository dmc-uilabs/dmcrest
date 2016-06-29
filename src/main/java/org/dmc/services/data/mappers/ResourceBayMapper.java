package org.dmc.services.data.mappers;


import org.dmc.services.data.entities.ResourceBay;
import org.dmc.services.data.entities.ResourceMachine;
import org.dmc.services.data.models.ResourceBayModel;
import org.dmc.services.data.models.ResourceMachineModel;

import org.springframework.stereotype.Component;

@Component
public class ResourceBayMapper extends AbstractMapper<ResourceBay, ResourceBayModel> {

	@Override
	public ResourceBay mapToEntity(ResourceBayModel model) {
		ResourceBay entity = copyProperties(model, new ResourceBay(), new String[]{"machines"});

		Mapper<ResourceMachine, ResourceMachineModel> machineMapper = mapperFactory.mapperFor(ResourceMachine.class, ResourceMachineModel.class);
	
		entity.setMachines(machineMapper.mapToEntity(model.getMachines()));
		
		return entity;
	}

	@Override
	public ResourceBayModel mapToModel(ResourceBay entity) {
		ResourceBayModel model = copyProperties(entity, new ResourceBayModel(), new String[]{"machines"});

		Mapper<ResourceMachine, ResourceMachineModel> machineMapper = mapperFactory.mapperFor(ResourceMachine.class, ResourceMachineModel.class);
	
		model.setMachines(machineMapper.mapToModel(entity.getMachines()));

		return model;
	}

	@Override
	public Class<ResourceBay> supportsEntity() {
		return ResourceBay.class;
	}

	@Override
	public Class<ResourceBayModel> supportsModel() {
		return ResourceBayModel.class;
	}

}
