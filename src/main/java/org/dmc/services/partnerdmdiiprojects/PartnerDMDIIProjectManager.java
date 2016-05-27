package org.dmc.services.partnerdmdiiprojects;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerDMDIIProjectManager {
	
	@Autowired
	private DMDIIProjectRepository dmdiiProjectRepository;
	
	@Autowired
	private DMDIIProjectMapper dmdiiProjectMapper;
	
	public List<DMDIIProject> findDmdiiProjectsByPartnerIdAndIsActive(Integer partnerId) {
		List<DMDIIProjectEntity> dmdiiEntities = Collections.emptyList();
		
		if(partnerId != null) {
			dmdiiEntities = dmdiiProjectRepository.findByPrimeOrganizationId(partnerId);
		}
		
		return dmdiiProjectMapper.entitiesToModels(dmdiiEntities);
	}
}
