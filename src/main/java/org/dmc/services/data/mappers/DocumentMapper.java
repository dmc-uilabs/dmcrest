package org.dmc.services.data.mappers;

import javax.inject.Inject;

import org.dmc.services.data.entities.Document;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper extends AbstractMapper<Document, DocumentModel> {

	@Inject
	private UserRepository userRepository;

	@Override
	public Document mapToEntity(DocumentModel model) {
		if (model == null) return null;

		Document entity = copyProperties(model, new Document());

		entity.setOwner(userRepository.findOne(model.getOwnerId()));

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
