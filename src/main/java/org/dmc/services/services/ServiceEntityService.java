package org.dmc.services.services;

import org.dmc.services.data.entities.ServiceEntity;
import org.dmc.services.data.repositories.ServiceRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class ServiceEntityService {

	@Inject
	private ServiceRepository serviceRepo;

	public ServiceEntity getService(Integer serviceId) {
		return serviceRepo.findOne(serviceId);
	}



}
