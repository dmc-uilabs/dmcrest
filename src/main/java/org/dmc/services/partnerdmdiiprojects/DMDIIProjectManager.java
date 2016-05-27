package org.dmc.services.partnerdmdiiprojects;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DMDIIProjectManager {

	@Autowired
	private DMDIIProjectRepository dmdiiProjectRepository;
	
	@Autowired
	private DMDIIProjectMapper dmdiiProjectMapper;
	
	public List<DMDIIProject> findDmdiiProjectsByPrimeOrganizationId (Integer primeOrganizationId) {
		List<DMDIIProjectEntity> dmdiiProjects = Collections.emptyList();
		
		if(primeOrganizationId != null) {
			dmdiiProjects = dmdiiProjectRepository.findByPrimeOrganizationId(primeOrganizationId);
		}
		
		return dmdiiProjectMapper.entitiesToModels(dmdiiProjects);
	}
}
