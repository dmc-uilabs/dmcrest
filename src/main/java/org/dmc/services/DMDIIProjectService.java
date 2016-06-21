package org.dmc.services;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.DMDIIProjectNews;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.DMDIIProjectNewsModel;
import org.dmc.services.data.repositories.DMDIIProjectNewsRepository;
import org.dmc.services.data.repositories.DMDIIProjectRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DMDIIProjectService {

	@Inject
	private DMDIIProjectRepository dmdiiProjectRepository;
	
	@Inject
	private DMDIIProjectNewsRepository dmdiiProjectNewsRepository;
	
	@Inject
	private MapperFactory mapperFactory;
	
	public List<DMDIIProjectModel> findPage(Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		List<DMDIIProject> projects = dmdiiProjectRepository.findAll(new PageRequest(pageNumber, pageSize)).getContent();
		return mapper.mapToModel(projects);
	}
	
	public List<DMDIIProjectModel> findDmdiiProjectsByPrimeOrganizationId (Integer primeOrganizationId, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByPrimeOrganizationId(new PageRequest(pageNumber, pageSize), primeOrganizationId).getContent());
	}

	public List<DMDIIProjectModel> findDMDIIProjectsByAwardedDate(Date awardedDate, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByAwardedDate(new PageRequest(pageNumber, pageSize), awardedDate).getContent());
	}

	public List<DMDIIProjectModel> findDMDIIProjectsByStatusId(Integer statusId, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByStatusId(new PageRequest(pageNumber, pageSize), statusId).getContent());
	}
	
	public List<DMDIIProjectModel> findByTitle(String title, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findByProjectTitleLikeIgnoreCase(new PageRequest(pageNumber, pageSize), "%"+title+"%").getContent());
	}

	public DMDIIProjectModel findOne(Integer id) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		return mapper.mapToModel(dmdiiProjectRepository.findOne(id));
	}

	public DMDIIProjectModel save(DMDIIProjectModel project) {
		Mapper<DMDIIProject, DMDIIProjectModel> mapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		
		DMDIIProject projectEntity = mapper.mapToEntity(project);
		
		projectEntity = dmdiiProjectRepository.save(projectEntity);
		
		return mapper.mapToModel(projectEntity);
	}
	
	public List<DMDIIProjectNewsModel> getDmdiiProjectNews(Integer limit) {
		Mapper<DMDIIProjectNews, DMDIIProjectNewsModel> mapper = mapperFactory.mapperFor(DMDIIProjectNews.class, DMDIIProjectNewsModel.class);
		return mapper.mapToModel(dmdiiProjectNewsRepository.findAllByOrderByDateCreatedDesc(new PageRequest(0, limit)).getContent());
	}
}