package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.UserModel;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DMDIIDocumentMapper extends AbstractMapper<DMDIIDocument, DMDIIDocumentModel> {

	@Override
	public DMDIIDocument mapToEntity(DMDIIDocumentModel model) {
		if (model == null) return null;
		DMDIIDocument entity = copyProperties(model, new DMDIIDocument());
		
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<DMDIIProject, DMDIIProjectModel> projectMapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		
		entity.setOwner(userMapper.mapToEntity(model.getOwner()));
		entity.setDMDIIProject(projectMapper.mapToEntity(model.getDmdiiProject()));
		
		return entity;
	}

	@Override
	public DMDIIDocumentModel mapToModel(DMDIIDocument entity) {
		if (entity == null) return null;

		DMDIIDocumentModel model = copyProperties(entity, new DMDIIDocumentModel());
		
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<DMDIIProject, DMDIIProjectModel> projectMapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);
		
		model.setOwner(userMapper.mapToModel(entity.getOwner()));
		model.setDmdiiProject(projectMapper.mapToModel(entity.getDMDIIProject()));
		
		return model;
	}

	@Override
	public Class<DMDIIDocument> supportsEntity() {
		return DMDIIDocument.class;
	}

	@Override
	public Class<DMDIIDocumentModel> supportsModel() {
		return DMDIIDocumentModel.class;
	}

}
