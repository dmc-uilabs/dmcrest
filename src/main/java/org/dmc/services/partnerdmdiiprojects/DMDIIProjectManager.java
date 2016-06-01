package org.dmc.services.partnerdmdiiprojects;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dmc.services.entities.DMDIIProject;
import org.dmc.services.mappers.Mapper;
import org.dmc.services.mappers.MapperFactory;
import org.dmc.services.models.DMDIIProjectModel;
import org.springframework.stereotype.Service;

@Service
public class DMDIIProjectManager {

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
}
