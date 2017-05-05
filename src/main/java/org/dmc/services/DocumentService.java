package org.dmc.services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.entities.Directory;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentClass;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.DocumentTag;
import org.dmc.services.data.entities.QDocument;
import org.dmc.services.data.entities.ResourceGroup;
import org.dmc.services.data.entities.ResourceType;
import org.dmc.services.data.entities.ServiceEntity;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.DocumentTagModel;
import org.dmc.services.data.repositories.DMDIIDocumentRepository;
import org.dmc.services.data.repositories.DirectoryRepository;
import org.dmc.services.data.repositories.DocumentRepository;
import org.dmc.services.data.repositories.DocumentTagRepository;
import org.dmc.services.data.repositories.ServiceRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.email.EmailService;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.notification.NotificationService;
import org.dmc.services.security.PermissionEvaluationHelper;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.security.UserPrincipal;
import org.dmc.services.verification.Verification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

@Service
public class DocumentService {

	private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

	@Inject
	private DocumentRepository documentRepository;

	@Inject
	private DMDIIDocumentRepository dmdiiDocumentRepository;

	@Inject
	private DocumentTagRepository documentTagRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private MapperFactory mapperFactory;

	@Inject
	private ParentDocumentService parentDocumentService;

	@Inject
	private ResourceAccessService resourceAccessService;

	@Inject
	private ServiceRepository serviceRepository;

	@Inject
	private DirectoryRepository directoryRepository;

	@Inject
	private EmailService emailService;

	@Inject
	private ResourceGroupService resourceGroupService;

	@Inject
	private NotificationService notificationService;

	private final String logTag = DocumentService.class.getName();

	private Verification verify = new Verification();

	public List<DocumentModel> filter(Map filterParams, Integer pageNumber, Integer pageSize, String userEPPN) throws InvalidFilterParameterException, DMCServiceException {
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		User owner = userRepository.findByUsername(userEPPN);

		Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams, owner));
		List<Document> results;
		List<Document> returnList = new ArrayList<>();

		results = documentRepository.findAll(where, new PageRequest(0, Integer.MAX_VALUE, new Sort(new Order(Direction.DESC, "modified")))).getContent();

		if (results.size() == 0) return null;
		//check for access
		//superadmin's see everything
		if(owner.getRoles().stream().anyMatch(r->r.getRole().getRole().equals(SecurityRoles.SUPERADMIN))) {
			List<DocumentModel> returnModels = mapper.mapToModel(pagify(results, pageNumber, pageSize));
			for (DocumentModel d : returnModels) {
				if (hasVersions(d.getId())) {
					d.setHasVersions(true);
				} else {
					d.setHasVersions(false);
				}
			}
			return returnModels;
		}

		for(Document doc : results) {
			//check for access
			if (resourceAccessService.hasAccess(ResourceType.DOCUMENT, doc, owner)) {
				returnList.add(doc);
			}
		}

		if (returnList.size() == 0) return null;

		List<DocumentModel> returnModels = mapper.mapToModel(pagify(returnList, pageNumber, pageSize));

		for (DocumentModel d : returnModels) {
			if (hasVersions(d.getId())) {
				d.setHasVersions(true);
			} else {
				d.setHasVersions(false);
			}
		}

		return returnModels;
	}

	private List<Document> pagify(List<Document> docs, Integer pageNumber, Integer pageSize) {
		Integer upperLowerBound = pageSize;
		Integer lowerUpperBound;
		if (pageNumber != 0) {
			lowerUpperBound = (pageNumber * pageSize);
		} else {
			lowerUpperBound = 0;
		}

		List<Document> returnList = new ArrayList<Document>(docs);
		if (lowerUpperBound != 0) {
			//clear the low end
			returnList.subList(0, lowerUpperBound).clear();
		}

		if (upperLowerBound < returnList.size()) {
			//clear the upper end
			returnList.subList(upperLowerBound, returnList.size()).clear();
		}

		return returnList;
	}

	public Long count(Map filterParams, String userEPPN) throws InvalidFilterParameterException {
		User owner = userRepository.findByUsername(userEPPN);
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams, owner));
		return documentRepository.count(where);
	}

	public DocumentModel findOne(Integer documentId) throws DMCServiceException {
		Assert.notNull(documentId);
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		List<Document> docList = Collections.singletonList(documentRepository.findOne(documentId));

		if (docList.size() == 0) return null;

		DocumentModel retModel =mapper.mapToModel(docList.get(0));

		if(hasVersions(retModel.getId())) {
			retModel.setHasVersions(true);
		} else {
			retModel.setHasVersions(false);
		}

		return retModel;
	}

	public List<DocumentModel> findByDirectory(Integer directoryId) {
		Assert.notNull(directoryId);
		Mapper<Document, DocumentModel> documentMapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		User currentUser = userRepository.findOne(
				((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
		List<Document> results;
		List<Document> returnList = new ArrayList<>();

		Directory directory = directoryRepository.findOne(directoryId);
		if(directory != null) {
			Predicate baseDocsOnly = QDocument.document.version.eq(0);
			Predicate byDirectory = QDocument.document.directory().eq(directory);
			Predicate where = ExpressionUtils.allOf(baseDocsOnly, byDirectory);
			results = documentRepository.findAll(where, new PageRequest(0, Integer.MAX_VALUE, new Sort(new Order(Direction.DESC, "modified")))).getContent();

			for(Document doc : results) {
				if(resourceAccessService.hasAccess(ResourceType.DOCUMENT, doc, currentUser)) {
					returnList.add(doc);
				}
			}
		}

		if (returnList.size() == 0) return null;

		List<DocumentModel> returnModels = documentMapper.mapToModel(returnList);

		for (DocumentModel d : returnModels) {
			if (hasVersions(d.getId())) {
				d.setHasVersions(true);
			} else {
				d.setHasVersions(false);
			}
		}

		return returnModels;
	}

	public DocumentModel save(DocumentModel doc) throws DMCServiceException, IllegalArgumentException {
		Assert.notNull(doc);
		Mapper<Document, DocumentModel> docMapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		String folder = "APPLICATION";

		if (doc.getParentType() != null) {
			folder = doc.getParentType().toString();
		}


		Document docEntity = docMapper.mapToEntity(doc);

		//thirty days in milliseconds
		Long duration = 1000L * 60L * 60L * 24L * 30L;

		Timestamp now = new Timestamp(System.currentTimeMillis());
		Timestamp expires = new Timestamp(now.getTime() + duration);

		docEntity.setExpires(expires);
		docEntity.setIsDeleted(false);
		docEntity.setVerified(false);
		if (doc.getAccessLevel() == null) {
			docEntity.setIsPublic(false);
		}
		docEntity.setModified(now);
		docEntity.setVersion(0);

		docEntity = documentRepository.save(docEntity);
		this.parentDocumentService.updateParents(docEntity);

		docEntity.setBaseDocId(docEntity.getId());
		docEntity = documentRepository.save(docEntity);

		logger.debug("Attempting to verify document");
		//Verify the document
		String temp = verify.verify(docEntity.getId(), docEntity.getDocumentUrl(), "document", docEntity.getOwner().getUsername(), folder, "Documents", "id", "url");
		logger.debug("Verification Machine Response: " + temp);

		return docMapper.mapToModel(docEntity);
	}

	public DocumentModel delete(Integer documentId) throws IllegalArgumentException {
		Assert.notNull(documentId);
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);

		Document docEntity = documentRepository.findOne(documentId);
		Assert.notNull(docEntity);

		docEntity.setIsDeleted(true);

		docEntity = documentRepository.save(docEntity);
		this.parentDocumentService.updateParents(docEntity);

		return mapper.mapToModel(docEntity);
	}

	public DocumentModel update(DocumentModel doc) throws IllegalArgumentException {
		Assert.notNull(doc);
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);

		Document docEntity = mapper.mapToEntity(doc);
		Document oldEntity = documentRepository.findOne(doc.getId());
		Assert.notNull(oldEntity);

		docEntity.setExpires(oldEntity.getExpires());
		docEntity.setModified(new Timestamp(System.currentTimeMillis()));

		docEntity = resourceGroupService.updateDocumentResourceGroups(docEntity, doc.getAccessLevel());

		docEntity = documentRepository.save(docEntity);
		this.parentDocumentService.updateParents(docEntity);

		return mapper.mapToModel(docEntity);
	}

	@Transactional
	public Document updateVerifiedDocument(Integer documentId, String verifiedUrl, boolean verified) {
		Document document = this.documentRepository.findOne(documentId);
		document.setDocumentUrl(verifiedUrl);
		document.setVerified(verified);

		this.documentRepository.save(document);
		this.parentDocumentService.updateParents(document);

		return document;
	}

	// public ResponseEntity shareDocument(Integer documentId, Integer userId, Boolean dmdii) {
	// 	String documentUrl;
	// 	String documentName;
	//
	// 	if (dmdii) {
	// 		documentUrl = getDMDIIDocumentUrl(documentId);
	// 		documentName = getDMDIIDocumentName(documentId);
	// 	} else {
	// 		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	// 		User user = this.userRepository.findByUsername(userPrincipal.getUsername());
	//
	// 		Document document = this.documentRepository.findOne(documentId);
	//
	// 		if (!this.resourceAccessService.hasAccess(ResourceType.DOCUMENT, document, user)) {
	// 			throw new AccessDeniedException("User does not have permission to share document");
	// 		}
	//
	// 		documentUrl = document.getDocumentUrl();
	// 		documentName = document.getDocumentName();
	// 	}
	//
	// 	User userToShareWith = this.userRepository.findOne(userId);
	// 	String key = AWSConnector.createPath(documentUrl);
	// 	String presignedUrl = AWSConnector.generatePresignedUrl(key,
	// 			Date.from(LocalDate.now().plusDays(7).atStartOfDay().toInstant(ZoneOffset.UTC)));
	//
	// 	HashMap<String, String> params = new HashMap<String, String>();
	// 	params.put("presignedUrl", presignedUrl);
	// 	params.put("documentName", documentName);
	//
	// 	return this.emailService.sendEmail(userToShareWith, 2, params);
	// }

	public ResponseEntity shareDocument(Integer documentId, String userIdentifier, Boolean internal, Boolean dmdii) {
		String documentUrl;
		String documentName;
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = this.userRepository.findByUsername(userPrincipal.getUsername());

		if (dmdii) {
			documentUrl = getDMDIIDocumentUrl(documentId);
			documentName = getDMDIIDocumentName(documentId);
		} else {

			Document document = this.documentRepository.findOne(documentId);
			if (!this.resourceAccessService.hasAccess(ResourceType.DOCUMENT, document, user)) {
				throw new AccessDeniedException("User does not have permission to share document");
			}

			documentUrl = document.getDocumentUrl();
			documentName = document.getDocumentName();
		}

		User userToShareWith;

		String key = AWSConnector.createPath(documentUrl);
		String presignedUrl = AWSConnector.generatePresignedUrl(key,
		Date.from(LocalDate.now().plusDays(7).atStartOfDay().toInstant(ZoneOffset.UTC)));

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("presignedUrl", presignedUrl);
		params.put("documentName", documentName);

		if (internal) {
			userToShareWith = this.userRepository.findOne(Integer.parseInt(userIdentifier));
			notificationService.createForSharedDocument(user, userToShareWith, presignedUrl);
		} else {
			userToShareWith = new User();
			userToShareWith.setFirstName(userIdentifier);
			userToShareWith.setLastName("");
			userToShareWith.setEmail(userIdentifier);
		}

		return this.emailService.sendEmail(userToShareWith, 2, params);
	}

	private String getDMDIIDocumentUrl(Integer documentId) {
		DMDIIDocument document = this.dmdiiDocumentRepository.getOne(documentId);
		if (document.getDmdiiProject() != null && document.getAccessLevel() != null) {
			List<DMDIIMember> projectMembers = new ArrayList<>();
			projectMembers.add(document.getDmdiiProject().getPrimeOrganization());
			projectMembers.addAll(document.getDmdiiProject().getContributingCompanies());

			List<Integer> projectMemberIds = projectMembers.stream().map((n) -> n.getOrganization().getId()).collect(Collectors.toList());
			if (!PermissionEvaluationHelper.userMeetsProjectAccessRequirement(document.getAccessLevel(), projectMemberIds)) {
				throw new AccessDeniedException("User does not have permission to share document");
			}
		}
		return document.getDocumentUrl();
	}

	private String getDMDIIDocumentName(Integer documentId) {
		DMDIIDocument document = this.dmdiiDocumentRepository.getOne(documentId);
		if (document.getDmdiiProject() != null && document.getAccessLevel() != null) {
			List<DMDIIMember> projectMembers = new ArrayList<>();
			projectMembers.add(document.getDmdiiProject().getPrimeOrganization());
			projectMembers.addAll(document.getDmdiiProject().getContributingCompanies());

			List<Integer> projectMemberIds = projectMembers.stream().map((n) -> n.getOrganization().getId()).collect(Collectors.toList());
			if (!PermissionEvaluationHelper.userMeetsProjectAccessRequirement(document.getAccessLevel(), projectMemberIds)) {
				throw new AccessDeniedException("User does not have permission to share document");
			}
		}
		return document.getDocumentName();
	}

	private Collection<Predicate> getFilterExpressions(Map<String, String> filterParams, User owner) throws InvalidFilterParameterException {
		Collection<Predicate> expressions = new ArrayList<>();

		Predicate baseDocsOnly = QDocument.document.version.eq(0);

		expressions.addAll(tagFilter(filterParams.get("tags")));
		expressions.add(parentTypeFilter(filterParams.get("parentType")));
		expressions.add(parentIdFilter(filterParams.get("parentId")));
		expressions.add(docClassFilter(filterParams.get("docClass")));
		expressions.add(baseDocsOnly);

		return expressions;
	}

	public List<DocumentTagModel> getAllTags() {
		Mapper<DocumentTag, DocumentTagModel> tagMapper = mapperFactory.mapperFor(DocumentTag.class, DocumentTagModel.class);
		return tagMapper.mapToModel(documentTagRepository.findAll());
	}

	private Collection<Predicate> tagFilter(String tagIds) throws InvalidFilterParameterException {
		if (tagIds == null) {
			return new ArrayList<>();
		}

		Collection<Predicate> returnValue = new ArrayList<>();
		String[] tags = tagIds.split(",");
		Integer tagIdInt;

		for (String tag : tags) {
			try {
				tagIdInt = Integer.parseInt(tag);
			} catch (NumberFormatException e) {
				throw new InvalidFilterParameterException("tags", Integer.class);
			}

			returnValue.add(QDocument.document.tags.any().id.eq(tagIdInt));
		}
		return returnValue;
	}

	private Collection<Predicate> resourceGroupFilter(List<ResourceGroup> resourceGroups) {

		Collection<Predicate> returnValue = new ArrayList<>();
		Collection<Integer> groupIds = new ArrayList<>();

		for (ResourceGroup group : resourceGroups) {
			groupIds.add(group.getId());
		}

		returnValue.add(QDocument.document.resourceGroups.any().id.in(groupIds));

		return returnValue;
	}

	private Predicate parentTypeFilter(String parentType) throws InvalidFilterParameterException {
		if (parentType == null) return null;

		DocumentParentType eType;

		try {
			eType = DocumentParentType.valueOf(parentType);
		} catch (Exception e) {
			throw new InvalidFilterParameterException("parentType", DocumentParentType.class);
		}

		return QDocument.document.parentType.eq(eType);
	}

	private Predicate parentIdFilter(String parentId) throws InvalidFilterParameterException {
		if (parentId == null) return null;
		Integer parentIdInt;

		try {
			parentIdInt = Integer.parseInt(parentId);
		} catch (NumberFormatException e) {
			throw new InvalidFilterParameterException("parentId", Integer.class);
		}

		return QDocument.document.parentId.eq(parentIdInt);
	}

	private Predicate docClassFilter(String docClass) throws InvalidFilterParameterException {
		if (docClass == null) return null;

		DocumentClass eType;

		try {
			eType = DocumentClass.valueOf(docClass);
		} catch (Exception e) {
			throw new InvalidFilterParameterException("docClass", DocumentClass.class);
		}

		return QDocument.document.docClass.eq(eType);
	}

	/**
	 * Removes all unverified document records that are a week old.
	 * <p>
	 * This is scheduled to run every day at 1:01 AM.
	 * The pattern is a list of six single space-separated fields: representing second, minute, hour, day, month, weekday.
	 */
	@Scheduled(cron = "0 1 1 * * ?")
	@Transactional(rollbackFor = DMCServiceException.class)
	protected void removeUnverifiedDocuments() {
		logger.info("Removing unverified document records.");
		LocalDateTime lastWeek = LocalDate.now().atStartOfDay().minusWeeks(1);

		List<Document> documents = this.documentRepository
				.findAllByVerifiedIsFalseAndModifiedBefore(Timestamp.valueOf(lastWeek));

		for (Document document : documents) {
			try {
				document.setIsDeleted(true);
				this.parentDocumentService.updateParents(document);

				logger.info("Removing old unverified document with owner id: {} and url: {}", document.getOwner().getId(), document.getDocumentUrl());

				this.documentRepository.delete(document);
			} catch (DMCServiceException ex) {
				logger.error("Error occurred while removing old unverified document", ex);
			}

		}
	}

	/**
	 * Refreshes all documents that are about to expire.
	 * Documents that are active and have less than or equal to 1 day left for expiration are refreshed.
	 * <p>
	 * This is scheduled to run every day at 1:01 AM.
	 * The pattern is a list of six single space-separated fields: representing second, minute, hour, day, month, weekday.
	 */
	@Scheduled(cron = "0 1 1 * * ?")
	@Transactional(rollbackFor = DMCServiceException.class)
	protected void refreshDocuments() {
		logger.info("Refreshing documents in AWS.");
		LocalDateTime future = LocalDate.now().atStartOfDay().plusDays(2);
		List<Document> documents = this.documentRepository
				.findAllByVerifiedIsTrueAndIsDeletedIsFalseAndExpiresBefore(Timestamp.valueOf(future));

		for (Document document : documents) {
			try {
				String path = AWSConnector.createPath(document.getDocumentUrl());
				String newURL = AWSConnector.refreshURL(path);

				LocalDateTime nextMonth = LocalDate.now().atStartOfDay().plusMonths(1).minusDays(1);

				document.setDocumentUrl(newURL);
				document.setExpires(Timestamp.valueOf(nextMonth));

				logger.info("Refreshing document with owner id: {} and new url: {}", document.getOwner().getId(), document.getDocumentUrl());

				this.documentRepository.save(document);
				this.parentDocumentService.updateParents(document);

			} catch (DMCServiceException ex) {
				logger.error("Error occurred while refreshing document", ex);
			}
		}
	}

	public List<Document> findServiceDocumentsByProjectId (Integer projectId) {
		List<ServiceEntity> services = serviceRepository.findByProjectId(projectId);
		List<Document> documents = new ArrayList<>();

		for(ServiceEntity service : services) {
			documents.addAll(documentRepository.findByParentTypeAndParentId(DocumentParentType.SERVICE, service.getId()));
		}

		return documents;
	}

	public List<DocumentModel> cloneDocuments (List<Integer> docIds, Integer newParentId, String userEPPN) {
		Assert.notNull(newParentId);
		Assert.isTrue(CollectionUtils.isNotEmpty(docIds));
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);

		User newOwner = userRepository.findByUsername(userEPPN);
		List<Document> newDocs = new ArrayList<>();

		for (Integer docId : docIds) {
			Document oldDoc = documentRepository.findOne(docId);
			Document newDoc = new Document();
			List<DocumentTag> newTags = new ArrayList<>();

			Timestamp now = new Timestamp(System.currentTimeMillis());

			newDoc.setOwner(newOwner);
			newDoc.setExpires(oldDoc.getExpires());
			newDoc.setIsDeleted(false);
			newDoc.setIsPublic(oldDoc.getIsPublic());
			newDoc.setModified(now);
			newDoc.setVersion(0);
			newDoc.setDocClass(oldDoc.getDocClass());
			newDoc.setDocumentName(oldDoc.getDocumentName());
			newDoc.setDocumentUrl(oldDoc.getDocumentUrl());
			newDoc.setParentType(oldDoc.getParentType());
			newDoc.setResourceType(oldDoc.getResourceType());
			for (DocumentTag tag : oldDoc.getTags()) {
				newTags.add(tag);
			}
			newDoc.setTags(newTags);

			newDoc.setParentId(newParentId);

			newDoc = documentRepository.save(newDoc);
			newDocs.add(newDoc);
		}

		return mapper.mapToModel(newDocs);
	}

	public List<DocumentModel> getVersions (Integer docId, String userEPPN) throws IllegalAccessException {
		Assert.notNull(docId);
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		User requester = userRepository.findByUsername(userEPPN);
		Document docEntity = documentRepository.findOne(docId);
		Integer baseDocId = docEntity.getBaseDocId();

		if (resourceAccessService.hasAccess(ResourceType.DOCUMENT, docEntity, requester)) {
			Predicate where = QDocument.document.baseDocId.eq(baseDocId);
			List<Document> documents = this.documentRepository
					.findAll(where,
							new PageRequest(0, Integer.MAX_VALUE, new Sort(new Order(Direction.ASC, "version"))))
					.getContent();

			if(!CollectionUtils.isEmpty(documents)) {
				return mapper.mapToModel(documents);
			}
		} else {
			throw new IllegalAccessException("User does not have access to base document");
		}

		return null;
	}

	private Integer nextVersion (Integer baseDocId) {
		Predicate where = QDocument.document.baseDocId.eq(baseDocId);
		List<Document> documents = this.documentRepository.findAll(where, new PageRequest(0, Integer.MAX_VALUE, new Sort(new Order(Direction.DESC, "version")))).getContent();

		return documents.get(0).getVersion() + 1;
	}

	public DocumentModel createNewVersion (DocumentModel doc, String userEPPN) throws IllegalAccessException {
		Assert.notNull(doc);
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		User requester = userRepository.findByUsername(userEPPN);
		Document docEntity = mapper.mapToEntity(doc);
		Document baseEntity = documentRepository.findOne(doc.getBaseDocId());
		String folder = "APPLICATION";

		if (doc.getParentType() != null) {
			folder = doc.getParentType().toString();
		}

		//thirty days in milliseconds
		Long duration = 1000L * 60L * 60L * 24L * 30L;

		Timestamp now = new Timestamp(System.currentTimeMillis());
		Timestamp expires = new Timestamp(now.getTime() + duration);

		if (resourceAccessService.hasAccess(ResourceType.DOCUMENT, baseEntity, requester)) {
			docEntity.setExpires(expires);
			docEntity.setModified(now);
			docEntity.setId(null);
			docEntity.setIsDeleted(false);
			docEntity.setVerified(false);
			docEntity.setVersion(nextVersion(doc.getBaseDocId()));

			docEntity = documentRepository.save(docEntity);

			logger.debug("Attempting to verify document");
			//Verify the document
			String temp = verify.verify(docEntity.getId(), docEntity.getDocumentUrl(), "document", docEntity.getOwner().getUsername(), folder, "Documents", "id", "url");
			logger.debug("Verification Machine Response: " + temp);
			return mapper.mapToModel(docEntity);
		} else {
			throw new IllegalAccessException("User does not have access to base document");
		}
	}

	private Boolean hasVersions(Integer docId) {
		Predicate where = QDocument.document.baseDocId.eq(docId);
		if(documentRepository.count(where) > 1L) {
			return true;
		}

		return false;
	}
}
