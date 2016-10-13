package org.dmc.services.data.mappers;


import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.assertj.core.util.Sets;
import org.dmc.services.data.entities.AppSubmission;
import org.dmc.services.data.entities.ApplicationTag;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.models.AppSubmissionModel;
import org.dmc.services.data.models.ApplicationTagModel;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.repositories.ApplicationTagRepository;
import org.springframework.stereotype.Component;

@Component
public class AppSubmissionMapper extends AbstractMapper<AppSubmission, AppSubmissionModel> {
	
	@Inject
	private ApplicationTagRepository applicationTagRepository;

	@Override
	public AppSubmission mapToEntity(AppSubmissionModel model) {
		if (model == null) return null;
		
		Mapper<Document, DocumentModel> docMapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		Mapper<ApplicationTag, ApplicationTagModel> appTagMapper = mapperFactory.mapperFor(ApplicationTag.class, ApplicationTagModel.class);
		
		AppSubmission entity = copyProperties(model, new AppSubmission());
		
		entity.setApplication(docMapper.mapToEntity(model.getApplication()));
		entity.setScreenShots(Sets.newHashSet(docMapper.mapToEntity(model.getScreenShots())));
		entity.setAppIcon(docMapper.mapToEntity(model.getAppIcon()));
		entity.setAppDocuments(Sets.newHashSet(docMapper.mapToEntity(model.getAppDocuments())));
		
		Set<ApplicationTag> mappedTags = model.getAppTags().stream()
				.map((n) -> (n.getId() != null ? applicationTagRepository.findOne(n.getId()) : appTagMapper.mapToEntity(n)))
				.collect(Collectors.toSet());
		
		entity.setAppTags(mappedTags);
		
		return entity;
	}

	@Override
	public AppSubmissionModel mapToModel(AppSubmission entity) {
		if (entity == null) return null;
		
		Mapper<Document, DocumentModel> docMapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		Mapper<ApplicationTag, ApplicationTagModel> appTagMapper = mapperFactory.mapperFor(ApplicationTag.class, ApplicationTagModel.class);
		
		AppSubmissionModel model = copyProperties(entity, new AppSubmissionModel());
		
		model.setApplication(docMapper.mapToModel(entity.getApplication()));
		model.setScreenShots(Sets.newHashSet(docMapper.mapToModel(entity.getScreenShots())));
		model.setAppIcon(docMapper.mapToModel(entity.getAppIcon()));
		model.setAppTags(Sets.newHashSet(appTagMapper.mapToModel(entity.getAppTags())));
		model.setAppDocuments(Sets.newHashSet(docMapper.mapToModel(entity.getAppDocuments())));
		
		return model;
	}

	@Override
	public Class<AppSubmission> supportsEntity() {
		return AppSubmission.class;
	}

	@Override
	public Class<AppSubmissionModel> supportsModel() {
		return AppSubmissionModel.class;
	}

}
