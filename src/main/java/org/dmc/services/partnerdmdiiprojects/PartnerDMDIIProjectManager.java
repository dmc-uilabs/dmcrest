package org.dmc.services.partnerdmdiiprojects;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dmc.services.mappers.Mapper;
import org.dmc.services.mappers.MapperFactory;
import org.springframework.stereotype.Service;

@Service
public class PartnerDMDIIProjectManager {
	
	@Inject
	private DMDIIProjectRepository dmdiiProjectRepository;
	
	@Inject
	private MapperFactory mapperFactory;
	
	private Mapper<DMDIIProjectEntity, DMDIIProject> mapper;
	
	@PostConstruct
	private void postConstruct() {
		this.mapper = mapperFactory.mapperFor(DMDIIProjectEntity.class, DMDIIProject.class);
	}
	
	public List<DMDIIProject> findDmdiiProjectsByPartnerIdAndIsActive(Integer partnerId) {
		List<DMDIIProjectEntity> dmdiiEntities = Collections.emptyList();
		
		if(partnerId != null) {
			dmdiiEntities = dmdiiProjectRepository.findByPrimeOrganizationId(partnerId);
		}
		
		return (List<DMDIIProject>) mapper.mapToModel(dmdiiEntities);
	}
}
