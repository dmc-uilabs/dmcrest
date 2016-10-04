package org.dmc.services.data.mappers;

import javax.inject.Inject;

import org.dmc.services.UserService;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.UserModel;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper extends AbstractMapper<Document, DocumentModel> {
	
	@Inject
	private UserService userService;

	@Override
	public Document mapToEntity(DocumentModel model) {
		if (model == null) return null;
		Document entity = copyProperties(model, new Document());

		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);

		entity.setOwner(userMapper.mapToEntity(userService.findOne(model.getOwnerId())));

		return entity;
	}

	@Override
	public DocumentModel mapToModel(Document entity) {
		if (entity == null) return null;

		DocumentModel model = copyProperties(entity, new DocumentModel());
		
		model.setOwnerId(entity.getOwner().getId());
		
		return model;
	}
	
	@Override
	public Class<Document> supportsEntity() {
		return Document.class;
	}

	@Override
	public Class<DocumentModel> supportsModel() {
		return DocumentModel.class;
	}
}
