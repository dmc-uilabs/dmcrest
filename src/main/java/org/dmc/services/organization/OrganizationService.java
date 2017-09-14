package org.dmc.services.organization;

import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.query.ListSubQuery;
import org.dmc.services.DMDIIDocumentService;
import org.dmc.services.DMDIIProjectService;
import org.dmc.services.OrganizationUserService;
import org.dmc.services.ResourceGroupService;
import org.dmc.services.data.entities.AreaOfExpertise;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.Organization;
import org.dmc.services.data.entities.QDMDIIMember;
import org.dmc.services.data.entities.QDocument;
import org.dmc.services.data.entities.QOrganization;
import org.dmc.services.data.entities.ResourceGroup;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIProjectModel;
import org.dmc.services.data.models.OrganizationModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.repositories.AreaOfExpertiseRepository;
import org.dmc.services.data.repositories.DocumentRepository;
import org.dmc.services.data.repositories.OrganizationRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.recentupdates.RecentUpdateController;
import org.dmc.services.roleassignment.UserRoleAssignmentService;
import org.dmc.services.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.dmc.services.exceptions.InvalidFilterParameterException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;

import static org.dmc.services.predicates.OrganizationPredicates.likeAreasOfExpertise;
import static org.dmc.services.predicates.OrganizationPredicates.likeDesiredAreasOfExpertise;
import static org.dmc.services.predicates.OrganizationPredicates.likeOrganizationName;
import static org.dmc.services.predicates.Predicates.buildPredicate;

@Service
public class OrganizationService {

	@Inject
	private OrganizationRepository organizationRepository;

	@Inject
	private AreaOfExpertiseRepository areaOfExpertiseRepository;

	@Inject
	private MapperFactory mapperFactory;

	@Inject
	private UserRepository userRepository;

	@Inject
	private OrganizationUserService organizationUserService;

	@Inject
	private UserRoleAssignmentService userRoleAssignmentService;

	@Inject
	private DMDIIProjectService dmdiiProjectService;

	@Inject
	private DMDIIDocumentService dmdiiDocumentService;

	@Inject
	private ResourceGroupService resourceGroupService;

	@Inject
	private DocumentRepository documentRepository;

	public Page<OrganizationModel> filter(PageRequest pageRequest, List<String> names, List<Integer> areasOfExpertise,
	                                      List<Integer> desiredAreasOfExpertise) {

		Mapper<Organization, OrganizationModel> mapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);

		Predicate where = buildPredicate(
				likeOrganizationName(names),
				likeAreasOfExpertise(areasOfExpertise),
				likeDesiredAreasOfExpertise(desiredAreasOfExpertise)
		);

		Page<Organization> organizations = organizationRepository.findAll(where, pageRequest);
		List<OrganizationModel> organizationModels = mapper.mapToModel(organizations.getContent());

		return new PageImpl<>(organizationModels, pageRequest, organizations.getTotalElements());
	}

	@Transactional
	public OrganizationModel save(OrganizationModel organizationModel) {
		Mapper<Organization, OrganizationModel> mapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);

		Organization organizationEntity = mapper.mapToEntity(organizationModel);

		// Check each of the tags to see if they're new or not. If new, they're saved separately through their own repository.
		List<AreaOfExpertise> aTags = organizationEntity.getAreasOfExpertise();
		List<AreaOfExpertise> dTags = organizationEntity.getDesiredAreasOfExpertise();

		for (int i = 0; i < aTags.size(); i++) {
			if (aTags.get(i).getId() == null) {
				aTags.set(i, areaOfExpertiseRepository.save(aTags.get(i)));
			}
		}

		for (int i = 0; i < dTags.size(); i++) {
			if (dTags.get(i).getId() == null) {
				dTags.set(i, areaOfExpertiseRepository.save(dTags.get(i)));
			}
		}

		organizationEntity.setAreasOfExpertise(aTags);
		organizationEntity.setDesiredAreasOfExpertise(dTags);

		// if organization is being created, save it and set the user saving as company admin
		if (organizationEntity.getId() == null) {
			organizationEntity = organizationRepository.save(organizationEntity);
			User userEntity = userRepository.findOne(((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
			organizationUserService.createVerifiedOrganizationUser(userEntity, organizationEntity);
			userRoleAssignmentService.assignInitialCompanyAdmin(userEntity, organizationEntity);

			//add ResourceGroups for new Organization
			resourceGroupService.newCreate(DocumentParentType.ORGANIZATION, organizationEntity.getId());

			//add user to admin resource group
			resourceGroupService.addUserResourceGroup(userEntity, DocumentParentType.ORGANIZATION, organizationEntity.getId(), "ADMIN");

		} else {
			Organization existingOrg = this.organizationRepository.findOne(organizationEntity.getId());
			organizationEntity.setLogoImage(existingOrg.getLogoImage());

			RecentUpdateController recentUpdateController = new RecentUpdateController();
			recentUpdateController.addRecentUpdate(organizationEntity, existingOrg);

			organizationEntity = organizationRepository.save(organizationEntity);
		}

		return mapper.mapToModel(organizationEntity);

	}

	public Organization save(Organization organization) {
		return organizationRepository.save(organization);
	}

	public String findMyVPC() {

		Mapper<User, UserModel> mapper = mapperFactory.mapperFor(User.class, UserModel.class);

		User currentUser = userRepository.findOne(
				((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

		UserModel currentUserModel = mapper.mapToModel(currentUser);
		return findById(currentUserModel.getCompanyId()).getProductionCapabilities();
	}

	public OrganizationModel findById(Integer id) {
		Mapper<Organization, OrganizationModel> mapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);
		return mapper.mapToModel(organizationRepository.findOne(id));
	}

	public List<OrganizationModel> findAll() {
		Mapper<Organization, OrganizationModel> mapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);
		return mapper.mapToModel(organizationRepository.findAll());
	}

	public List<OrganizationModel> findNonDmdiiMembers() {
		ListSubQuery<Integer> subQuery = new JPASubQuery().from(QDMDIIMember.dMDIIMember).list(QDMDIIMember.dMDIIMember.organization().id);
		Predicate predicate = QOrganization.organization.id.notIn(subQuery);

		Mapper<Organization, OrganizationModel> mapper = mapperFactory.mapperFor(Organization.class, OrganizationModel.class);
		return mapper.mapToModel(organizationRepository.findAll(predicate));
	}

	@Transactional
	public void delete(Integer organizationId) throws InvalidFilterParameterException {
		List<DMDIIProjectModel> projectModels = dmdiiProjectService.findDmdiiProjectsByPrimeOrganizationId(organizationId, 0, Integer.MAX_VALUE);
		projectModels.stream().map(n -> n.getId()).forEach(dmdiiDocumentService::deleteDMDIIDocumentsByDMDIIProjectId);
		organizationRepository.delete(organizationId);

		List<User> users = userRepository.findByOrganizationUserOrganizationId(organizationId);

		for(User user: users) {
			if(user.getResourceGroups() != null) {
				Iterator<ResourceGroup> iterator = user.getResourceGroups().iterator();
				while(iterator.hasNext()) {
					ResourceGroup group = iterator.next();
					if(DocumentParentType.ORGANIZATION.equals(group.getParentType()) && organizationId.equals(group.getParentId())) {
						iterator.remove();
					}
				}
			}
		}

		userRepository.save(users);


		//remove organization documents
		Predicate where = QDocument.document.parentType.eq(DocumentParentType.ORGANIZATION).and(QDocument.document.parentId.eq(organizationId));
		List<Document> orgDocs = (List<Document>) documentRepository.findAll(where);
		for(Document doc : orgDocs) {
			doc.getResourceGroups().clear();
			doc.setIsDeleted(true);
			documentRepository.save(doc);
		}

		//remove associated resource groups
		resourceGroupService.removeAll(DocumentParentType.ORGANIZATION, organizationId);
	}
}
