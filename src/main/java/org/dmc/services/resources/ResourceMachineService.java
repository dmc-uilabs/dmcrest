package org.dmc.services.resources;


import javax.inject.Inject;
import java.util.List;
import org.dmc.services.data.entities.ResourceBay;
import org.dmc.services.data.entities.ResourceMachine;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.ResourceMachineModel;
import org.dmc.services.data.repositories.ResourceMachineRepository;
import org.dmc.services.data.repositories.ResourceBayRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;




@Service
public class ResourceMachineService {
	

	@Inject
	private ResourceMachineRepository resourceMachineRepository;
	
	@Inject
	private ResourceBayRepository resourceBayRepository;

	@Inject
	private MapperFactory mapperFactory;
	
	
	//DONE
	//Gets a specific machine
	public List<ResourceMachineModel> getAllMachines(Integer BayId) {
		Mapper<ResourceMachine, ResourceMachineModel> mapper = mapperFactory.mapperFor(ResourceMachine.class, ResourceMachineModel.class);
		return mapper.mapToModel(resourceMachineRepository.findByBayId(new PageRequest(10, 10), BayId).getContent());
	}

	
	//DONE?
	//create a machine 
	public ResourceMachineModel createMachine(Integer bayId, ResourceMachineModel machineModel) {
		
		//Create mappers
		Mapper<ResourceMachine, ResourceMachineModel> machineMapper = mapperFactory.mapperFor(ResourceMachine.class, ResourceMachineModel.class);
		//Mapper<ResourceBay, ResourceBayModel> bayMapper = mapperFactory.mapperFor(ResourceBay.class, ResourceBayModel.class);

		//Convert to machine to entity
		ResourceMachine machineEntity = machineMapper.mapToEntity(machineModel);
	  
		//Get the associated bay 
		ResourceBay bayEntity = resourceBayRepository.findOne(bayId);

		//Add bay entity 
		machineEntity.setBay(bayEntity);
			
		//save changes
		machineEntity = resourceMachineRepository.save(machineEntity); 
		
		//Return the created machine
		return machineMapper.mapToModel(machineEntity);
	}
	
	//deletes all machine
	public Integer removeAllMachines(Integer BayId) {
		List<ResourceMachine> entity = resourceMachineRepository.findByBayId(new PageRequest(10, 10), BayId).getContent();
		resourceMachineRepository.deleteInBatch(entity);
		return BayId;
	}
	
	
	
	
	//DONE
	//deletes all bay machine
	public ResourceMachineModel removeMachine(Integer bayId, Integer machineId) {
		Mapper<ResourceMachine, ResourceMachineModel> mapper = mapperFactory.mapperFor(ResourceMachine.class, ResourceMachineModel.class);
		ResourceMachine entity = resourceMachineRepository.findOne(machineId);
		resourceMachineRepository.delete(entity);
		return mapper.mapToModel(entity);
	}
		
		
}
