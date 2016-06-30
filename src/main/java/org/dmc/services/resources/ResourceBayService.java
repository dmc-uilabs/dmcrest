package org.dmc.services.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.entities.ResourceBay;
import org.dmc.services.data.entities.ResourceMachine;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ResourceMachineModel;
import org.dmc.services.data.models.ResourceBayModel;
import org.dmc.services.data.repositories.ResourceBayRepository;
import org.dmc.services.data.repositories.ResourceMachineRepository;

import org.springframework.stereotype.Service;


@Service
public class ResourceBayService {

	@Inject
	private ResourceBayRepository resourceBayRepository;
	
	@Inject
	private ResourceMachineRepository resourceMachineRepository;

	
	@Inject
	private MapperFactory mapperFactory;


	//Gets all bays
	public List<ResourceBayModel> getAll() {
		Mapper<ResourceBay, ResourceBayModel> mapper = mapperFactory.mapperFor(ResourceBay.class, ResourceBayModel.class);
		return mapper.mapToModel(resourceBayRepository.findAll());
	}

	//Gets a specific bay
	public ResourceBayModel get(Integer id) {
		Mapper<ResourceBay, ResourceBayModel> mapper = mapperFactory.mapperFor(ResourceBay.class, ResourceBayModel.class);
		return mapper.mapToModel(resourceBayRepository.findOne(id));
	}
	
	//Gets a specific machine
		public ResourceMachineModel getMachine(Integer id) {
			Mapper<ResourceMachine, ResourceMachineModel> mapper = mapperFactory.mapperFor(ResourceMachine.class, ResourceMachineModel.class);
			return mapper.mapToModel(resourceMachineRepository.findOne(id));
		}
	
	//create a bay
	public ResourceBayModel create(ResourceBayModel bay) {
		Mapper<ResourceBay, ResourceBayModel> mapper = mapperFactory.mapperFor(ResourceBay.class, ResourceBayModel.class);
		ResourceBay entity = mapper.mapToEntity(bay);
		entity = resourceBayRepository.save(entity);
		return mapper.mapToModel(entity);
	}
	
	//create a machine 
	public ResourceMachineModel createBayMachine(Integer bayId, ResourceMachineModel machine) {
		Mapper<ResourceMachine, ResourceMachineModel> mapper = mapperFactory.mapperFor(ResourceMachine.class, ResourceMachineModel.class);
		ResourceMachine entity = mapper.mapToEntity(machine);
		
		//Get the associated bay 
		ResourceBay bay = resourceBayRepository.findOne(bayId);
		
		//Set the bay number 
		entity.setBay(bay);
		entity = resourceMachineRepository.save(entity);
		return mapper.mapToModel(entity);
	}
		
	
	//deletes an bay
	public ResourceBayModel remove(Integer id) {
		Mapper<ResourceBay, ResourceBayModel> mapper = mapperFactory.mapperFor(ResourceBay.class, ResourceBayModel.class);
		ResourceBay entity = resourceBayRepository.findOne(id);
		resourceBayRepository.delete(entity);
		return mapper.mapToModel(entity);
	}
	
	//deletes an machine
	public ResourceMachineModel removeMachine(Integer id) {
		Mapper<ResourceMachine, ResourceMachineModel> mapper = mapperFactory.mapperFor(ResourceMachine.class, ResourceMachineModel.class);
		ResourceMachine entity = resourceMachineRepository.findOne(id);
		resourceMachineRepository.delete(entity);
		return mapper.mapToModel(entity);
	}


	
}
	