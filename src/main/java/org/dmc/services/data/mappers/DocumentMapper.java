package org.dmc.services.data.mappers;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentTag;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.DocumentTagModel;
import org.dmc.services.data.repositories.DocumentTagRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentMapper extends AbstractMapper<Document, DocumentModel> {

	@Inject
	private UserRepository userRepository;

	@Inject
	private DocumentTagRepository documentTagRepository;

	@Override
	public Document mapToEntity(DocumentModel model) {
		if (model == null) return null;
		Document entity = copyProperties(model, new Document());

		entity.setOwner(userRepository.findOne(model.getOwnerId()));

		List<DocumentTagModel> documentTagModels = model.getTags();
		List<DocumentTag> documentTags = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(documentTagModels)) {
			for (DocumentTagModel documentTagModel : documentTagModels) {
				String tagName = documentTagModel.getTagName();
				DocumentTag documentTag = this.documentTagRepository.findByTagName(tagName);
				if (documentTag == null) {
					documentTag = new DocumentTag();
					documentTag.setTagName(tagName);
				}
				documentTags.add(documentTag);
			}
		}
		entity.setTags(documentTags);

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
