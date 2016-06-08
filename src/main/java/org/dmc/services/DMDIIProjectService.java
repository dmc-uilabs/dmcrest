package org.dmc.services;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.repositories.DMDIIProjectRepository;
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

	public List<DMDIIProjectModel> findDMDIIProjectsByAwardedDate(Date awardedDate) {
		List<DMDIIProject> dmdiiProjects = Collections.emptyList();
		
		if(awardedDate != null) {
			dmdiiProjects = dmdiiProjectRepository.findByAwardedDate(awardedDate);
		}
		
		return (List<DMDIIProjectModel>) mapper.mapToModel(dmdiiProjects);
	}

	public List<DMDIIProjectModel> findDMDIIProjectsByProjectStatusId(Integer projectStatusId) {
		List<DMDIIProject> dmdiiProjects = Collections.emptyList();
		
		if(projectStatusId != null) {
			dmdiiProjects = dmdiiProjectRepository.findByProjectStatusId(projectStatusId);
		}
		
		return (List<DMDIIProjectModel>) mapper.mapToModel(dmdiiProjects);
	}
}
