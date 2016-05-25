package org.dmc.services.partnerdmdiiprojects;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DmdiiProjectManager {

	@Autowired
	private DmdiiProjectRepository dmdiiProjectRepository;
	
	@Autowired
	private DmdiiProjectMapper dmdiiProjectMapper;
	
	public List<DmdiiProject> findDmdiiProjectsByPrimeOrganizationId (Integer primeOrganizationId) {
		List<DmdiiProjectEntity> dmdiiProjects = Collections.emptyList();
		
		if(primeOrganizationId != null) {
			dmdiiProjects = dmdiiProjectRepository.findByPrimeOrganizationId(primeOrganizationId);
		}
		
		return dmdiiProjectMapper.entitiesToModels(dmdiiProjects);
	}
}
