package org.dmc.services.partnerdmdiiprojects;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerDmdiiProjectManager {
	
	@Autowired
	private DmdiiProjectRepository dmdiiProjectRepository;
	
	@Autowired
	private DmdiiProjectMapper dmdiiProjectMapper;
	
	public List<DmdiiProject> findDmdiiProjectsByPartnerIdAndIsActive(Integer partnerId) {
		List<DmdiiProjectEntity> dmdiiEntities = Collections.emptyList();
		
		if(partnerId != null) {
			dmdiiEntities = dmdiiProjectRepository.findByPrimeOrganizationId(partnerId);
		}
		
		return dmdiiProjectMapper.entitiesToModels(dmdiiEntities);
	}
}
