package org.dmc.services.data.mappers;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.DocumentTag;
import org.dmc.services.data.entities.ResourceGroup;
import org.dmc.services.data.entities.ResourceType;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.entities.UserFavorite;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.DocumentTagModel;
import org.dmc.services.data.models.SimpleUserModel;
import org.dmc.services.data.repositories.DirectoryRepository;
import org.dmc.services.data.repositories.DocumentTagRepository;
import org.dmc.services.data.repositories.ResourceGroupRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.security.UserPrincipal;
import org.dmc.services.userfavorite.UserFavoriteDao;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
		entity.setResourceType(ResourceType.DOCUMENT);

		if (model.getDirectoryId() != null)
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

		List<SimpleUserModel> vips = model.getVips();
		List<User> vipEntities = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(vips)) {
			for (SimpleUserModel simpleVip : vips) {
				User vip = userRepository.findOne(simpleVip.getId());
				vipEntities.add(vip);
			}
		}
		entity.setVips(vipEntities);

		if (model.getAccessLevel() != null) {

			if (model.getAccessLevel() != null && !DocumentParentType.SERVICE.equals(model.getParentType())) {
				//set resource groups from accessLevel
				List<ResourceGroup> docGroups = new ArrayList<>();
				ResourceGroup group;
				switch (model.getAccessLevel()) {
					case SecurityRoles.ADMIN:
						group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(model.getParentType(),
								model.getParentId(), SecurityRoles.ADMIN);
						docGroups.add(group);
						group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(model.getParentType(),
								model.getParentId(), SecurityRoles.MEMBER);
						docGroups.add(group);
						break;
					case SecurityRoles.MEMBER:
						group = resourceGroupRepository.findByParentTypeAndParentIdAndRole(model.getParentType(),
								model.getParentId(), SecurityRoles.MEMBER);
						docGroups.add(group);
						break;
					case SecurityRoles.PUBLIC:
						entity.setIsPublic(true);
						break;
				}
				entity.setResourceGroups(docGroups);
			}
		}

		return entity;
	}

	@Override
	public DocumentModel mapToModel(Document entity) {
		if (entity == null) return null;
		Mapper<User, SimpleUserModel> userMapper = mapperFactory.mapperFor(User.class, SimpleUserModel.class);

		final UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final UserFavoriteDao userFavoriteDao = new UserFavoriteDao();

		DocumentModel model = copyProperties(entity, new DocumentModel(), new String[]{"resourceType", "resourceGroups"});
		List<ResourceGroup> groups = entity.getResourceGroups();

		model.setOwnerId(entity.getOwner().getId());
		model.setOwnerDisplayName(entity.getOwner().getRealname());

		List<SimpleUserModel> vips = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(entity.getVips())) {
			for (User vip : entity.getVips()) {
				vips.add(userMapper.mapToModel(vip));
			}
		}
		model.setVips(vips);

		if(entity.getDirectory() != null){
			model.setDirectoryId(entity.getDirectory().getId());
		}

		if (CollectionUtils.isNotEmpty(groups)) {
			for (ResourceGroup group : groups) {
				String accessLevel = null;
				if (group.getRole().equals(SecurityRoles.ADMIN)) {
					accessLevel = "ADMIN";
				} else if (group.getRole().equals(SecurityRoles.MEMBER)) {
					accessLevel = "MEMBER";
				}

				model.setAccessLevel(accessLevel);
			}
		}

		if (entity.getIsPublic()) {
			model.setAccessLevel("PUBLIC");
		}

		final ArrayList<UserFavorite> userFavorites = userFavoriteDao.getUserFavorites(user.getId(), 2);
		if (userFavorites.stream().anyMatch(userFavorite -> userFavorite.getContentId().equals(model.getId()))) {
			model.setFavorited(true);
		} else {
			model.setFavorited(false);
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
