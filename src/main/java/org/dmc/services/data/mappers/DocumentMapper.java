package org.dmc.services.data.mappers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentTag;
import org.dmc.services.data.entities.ResourceGroup;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.DocumentTagModel;
import org.dmc.services.data.repositories.DirectoryRepository;
import org.dmc.services.data.repositories.DocumentTagRepository;
import org.dmc.services.data.repositories.ResourceGroupRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.security.SecurityRoles;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper extends AbstractMapper<Document, DocumentModel> {

	@Inject
	private UserRepository userRepository;

	@Inject
	private DocumentTagRepository documentTagRepository;

	@Inject
	private ResourceGroupRepository resourceGroupRepository;

	@Inject
	private DirectoryRepository directoryRepository;

	@Override
	public Document mapToEntity(DocumentModel model) {
		if (model == null) return null;
		Document entity = copyProperties(model, new Document(), new String[]{"ownerDisplayName"});

		entity.setOwner(userRepository.findOne(model.getOwnerId()));

		if(model.getDirectoryId() != null)
			entity.setDirectory(directoryRepository.findOne(model.getDirectoryId()));

		List<DocumentTagModel> documentTagModels = model.getTags();
		List<DocumentTag> documentTags = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(documentTagModels)) {
			for (DocumentTagModel documentTagModel : documentTagModels) {
				String tagName = documentTagModel.getTagName();
				tagName = StringUtils.lowerCase(tagName);
				DocumentTag documentTag = this.documentTagRepository.findByTagName(tagName);
				if (documentTag == null) {
					documentTag = new DocumentTag();
					documentTag.setTagName(tagName);
				}
				documentTags.add(documentTag);
			}
		}
		entity.setTags(documentTags);

		if (model.getAccessLevel() != null) {
			//set resource groups from accessLevel
			List<ResourceGroup> docGroups = new ArrayList<>();
			ResourceGroup group;
			switch (model.getAccessLevel()) {
			case SecurityRoles.ADMIN:
				group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(model.getParentType(),
						model.getParentId(), SecurityRoles.ADMIN);
				docGroups.add(group);
				break;
			case SecurityRoles.MEMBER:
				group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(model.getParentType(),
						model.getParentId(), SecurityRoles.ADMIN);
				docGroups.add(group);
				group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(model.getParentType(),
						model.getParentId(), SecurityRoles.MEMBER);
				docGroups.add(group);
				break;
			}
			entity.setResourceGroups(docGroups);
		}
		return entity;
	}

	@Override
	public DocumentModel mapToModel(Document entity) {
		if (entity == null) return null;

		DocumentModel model = copyProperties(entity, new DocumentModel());

		model.setOwnerId(entity.getOwner().getId());
		model.setOwnerDisplayName(entity.getOwner().getRealname());

		if(entity.getDirectory() != null){
			model.setDirectoryId(entity.getDirectory().getId());
		}

		if (CollectionUtils.isNotEmpty(entity.getResourceGroups())) {
			for (ResourceGroup group : entity.getResourceGroups()) {
				String accessLevel = null;
				if (group.getRole().equals(SecurityRoles.ADMIN)) {
					accessLevel = "ADMIN";
					break;
				} else if (group.getRole().equals(SecurityRoles.MEMBER)) {
					accessLevel = "MEMBER";
				}

				model.setAccessLevel(accessLevel);
			}
		}
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
