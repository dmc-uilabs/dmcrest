package org.dmc.services;

import java.util.Set;

import javax.inject.Inject;

import org.dmc.services.data.entities.AppSubmission;
import org.dmc.services.data.entities.ApplicationTag;
import org.dmc.services.data.entities.QAppSubmission;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.AppSubmissionModel;
import org.dmc.services.data.models.ApplicationTagModel;
import org.dmc.services.data.repositories.AppSubmissionRepository;
import org.dmc.services.data.repositories.ApplicationTagRepository;
import org.dmc.services.exceptions.AlreadyExistsException;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.mysema.query.types.Predicate;

@Service
public class AppSubmissionService {
	
	@Inject
	private AppSubmissionRepository appSubmissionRepository;
	
	@Inject
	private ApplicationTagRepository applicationTagRepository;
	
	@Inject
	private MapperFactory mapperFactory;
	
	public AppSubmissionModel findOne(Integer appSubmissionId) {
		Mapper<AppSubmission, AppSubmissionModel> appSubmissionMapper = mapperFactory.mapperFor(AppSubmission.class, AppSubmissionModel.class);
		return appSubmissionMapper.mapToModel(appSubmissionRepository.findOne(appSubmissionId));
	}

	public AppSubmissionModel save(AppSubmissionModel appSubmissionModel) {
		Predicate appNamePredicate = QAppSubmission.appSubmission.appName.eq(appSubmissionModel.getAppName());
		Boolean appNameExists = appSubmissionRepository.count(appNamePredicate) > 0;
		
		if (appNameExists) {
			throw new AlreadyExistsException("AppSubmission with app name '" + appSubmissionModel.getAppName() + "' already exists");
		}
		
		Mapper<AppSubmission, AppSubmissionModel> appSubmissionMapper = mapperFactory.mapperFor(AppSubmission.class, AppSubmissionModel.class);
		
		AppSubmission entity = appSubmissionMapper.mapToEntity(appSubmissionModel);
		// TODO: Replace this placeholder value
		entity.setAssignedTo("Alex M");
		AppSubmission saved = appSubmissionRepository.save(entity);
		return appSubmissionMapper.mapToModel(saved);
	}
	
	public Set<ApplicationTagModel> getApplicationTags() {
		Mapper<ApplicationTag, ApplicationTagModel> applicationTagMapper = mapperFactory.mapperFor(ApplicationTag.class, ApplicationTagModel.class);
		
		return Sets.newHashSet(applicationTagMapper.mapToModel(applicationTagRepository.findAll()));
	}
	
	public Set<String> getAppNames() {
		return appSubmissionRepository.getAppNames();
	}

}
