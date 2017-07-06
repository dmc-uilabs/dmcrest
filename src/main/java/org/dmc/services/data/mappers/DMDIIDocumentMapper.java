package org.dmc.services.data.mappers;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dmc.services.DMDIIProjectService;
import org.dmc.services.UserService;
import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.DMDIIDocumentTag;
import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.DMDIIProject;
import org.dmc.services.data.entities.DMDIIProjectItemAccessLevel;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.DMDIIDocumentTagModel;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.repositories.DMDIIDocumentTagRepository;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.springframework.stereotype.Component;

import org.springframework.security.core.context.SecurityContextHolder;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.security.UserPrincipal;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.dmc.services.ServiceLogger;





@Component
public class DMDIIDocumentMapper extends AbstractMapper<DMDIIDocument, DMDIIDocumentModel> {

	@Inject
	private DMDIIProjectService dmdiiProjectService;

	@Inject
	private UserService userService;

	@Inject
	private DMDIIDocumentTagRepository tagRepository;

	@Inject
	private UserRepository userRepository;


	@Override
	public DMDIIDocument mapToEntity(DMDIIDocumentModel model) {
		if (model == null) return null;
		DMDIIDocument entity = copyProperties(model, new DMDIIDocument(), new String[]{"isDeleted", "verified"});

		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		Mapper<DMDIIProject, DMDIIProjectModel> projectMapper = mapperFactory.mapperFor(DMDIIProject.class, DMDIIProjectModel.class);

		entity.setOwner(userMapper.mapToEntity(userService.findOne(model.getOwnerId())));
		ServiceLogger.log(" in the access level for each document ", model.getDocumentName());

		if(model.getDmdiiProjectId() != null)
			entity.setDmdiiProject(projectMapper.mapToEntity(dmdiiProjectService.findOne(model.getDmdiiProjectId())));
		else
			entity.setDmdiiProject(null);

		if (model.getAccessLevel() != null) {
			ServiceLogger.log("in the access level for each document ", model.getAccessLevel());


			entity.setAccessLevel(model.getAccessLevel());
		}

		List<DMDIIDocumentTagModel> documentTagModels = model.getTags();
		List<DMDIIDocumentTag> documentTags = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(documentTagModels)) {
			for (DMDIIDocumentTagModel documentTagModel : documentTagModels) {
				String tagName = documentTagModel.getTagName();
				tagName = StringUtils.lowerCase(tagName);
				DMDIIDocumentTag documentTag = this.tagRepository.findByTagName(tagName);
				if (documentTag == null) {
					documentTag = new DMDIIDocumentTag();
					documentTag.setTagName(tagName);
				}
				documentTags.add(documentTag);
			}
		}
		entity.setTags(documentTags);


		return entity;
	}

	@Override
	public DMDIIDocumentModel mapToModel(DMDIIDocument entity) {
		Boolean isAuthorized = false;
		if (entity == null) return null;

		if (entity.getDmdiiProject() != null && entity.getAccessLevel() != null) {
			List<DMDIIMember> projectMembers = new ArrayList<DMDIIMember>();
			projectMembers.add(entity.getDmdiiProject().getPrimeOrganization());
			projectMembers.addAll(entity.getDmdiiProject().getContributingCompanies());

			List<Integer> projectMemberIds = projectMembers.stream().map((n) -> n.getOrganization().getId()).collect(Collectors.toList());

			Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
			User currentUser = userRepository.findOne(
					((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

			if (!PermissionEvaluationHelper.userMeetsProjectAccessRequirement(entity.getAccessLevel(), projectMemberIds, currentUser)) {
				return null;
			}
		}

		DMDIIDocumentModel model = copyProperties(entity, new DMDIIDocumentModel());

		model.setOwnerId(entity.getOwner().getId());
		if(entity.getDmdiiProject() != null)
			model.setDmdiiProjectId(entity.getDmdiiProject().getId());
		else
			model.setDmdiiProjectId(null);

		if (entity.getAccessLevel() != null) {
			model.setAccessLevel(entity.getAccessLevel().toString());
		}

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
