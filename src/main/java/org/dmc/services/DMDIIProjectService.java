package org.dmc.services;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dmc.services.entities.DMDIIProject;
import org.dmc.services.mappers.Mapper;
import org.dmc.services.mappers.MapperFactory;
import org.dmc.services.models.DMDIIProjectModel;
import org.dmc.services.repository.DMDIIProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class DMDIIProjectService {

	@Inject
	private DMDIIProjectRepository dmdiiProjectRepository;
	
	@Inject
	private MapperFactory mapperFactory;
	
	private Mapper<DMDIIProject, DMDIIProjectModel> mapper;
	
	@PostConstruct
	private void postConstruct() {
		this.mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
	}
	
	public List<DMDIIProjectModel> findDmdiiProjectsByPrimeOrganizationId (Integer primeOrganizationId) {
		List<DMDIIProject> dmdiiProjects = Collections.emptyList();
		
		if(primeOrganizationId != null) {
			dmdiiProjects = dmdiiProjectRepository.findByPrimeOrganizationId(primeOrganizationId);
		}
		
		return (List<DMDIIProjectModel>) mapper.mapToModel(dmdiiProjects);
	}

	public List<DMDIIProjectModel> findDMDIIProjectsByStartDate(Date startDate) {
		List<DMDIIProject> dmdiiProjects = Collections.emptyList();
		
		if(startDate != null) {
			dmdiiProjects = dmdiiProjectRepository.findByStartDate(startDate);
		}
		
		return (List<DMDIIProjectModel>) mapper.mapToModel(dmdiiProjects);
	}

	public List<DMDIIProjectModel> findDMDIIProjectsByStatus(String status) {
		List<DMDIIProject> dmdiiProjects = Collections.emptyList();
		
		if(status != null) {
			dmdiiProjects = dmdiiProjectRepository.findByStatus(status);
		}
		
		return (List<DMDIIProjectModel>) mapper.mapToModel(dmdiiProjects);
	}
}
