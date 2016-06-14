package org.dmc.services;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.repositories.DMDIIProjectRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DMDIIProjectService {

	@Inject
	private DMDIIProjectRepository dmdiiProjectRepository;
	
	@Inject
	private MapperFactory mapperFactory;
	
	public List<DMDIIProjectModel> findDmdiiProjectsByPrimeOrganizationId (Integer primeOrganizationId, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByPrimeOrganizationId(new PageRequest(pageNumber, pageSize), primeOrganizationId).getContent());
	}

	public List<DMDIIProjectModel> findDMDIIProjectsByAwardedDate(Date awardedDate, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByAwardedDate(new PageRequest(pageNumber, pageSize), awardedDate).getContent());
	}

	public List<DMDIIProjectModel> findDMDIIProjectsByProjectStatusId(Integer projectStatusId, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByProjectStatusId(new PageRequest(pageNumber, pageSize), projectStatusId).getContent());
	}

	public List<DMDIIProjectModel> getAllDMDIIProjects(Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findAll(new PageRequest(pageNumber, pageSize)).getContent());
	}
	
	public List<DMDIIProjectModel> findByTitle(String title, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByProjectTitleLikeIgnoreCase(new PageRequest(pageNumber, pageSize), "%"+title+"%").getContent());
	}
}
