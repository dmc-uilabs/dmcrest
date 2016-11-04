package org.dmc.services;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.dmc.services.data.entities.*;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.DocumentTagModel;
import org.dmc.services.data.repositories.DMDIIDocumentRepository;
import org.dmc.services.data.repositories.DocumentRepository;
import org.dmc.services.data.repositories.DocumentTagRepository;
import org.dmc.services.data.repositories.ServiceRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.email.EmailService;
import org.dmc.services.exceptions.InvalidFilterParameterException;
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

import javax.inject.Inject;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

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
	private EmailService emailService;

	private final String logTag = DocumentService.class.getName();

	private Verification verify = new Verification();

	public List<DocumentModel> filter(Map filterParams, Integer pageNumber, Integer pageSize, String userEPPN) throws InvalidFilterParameterException, DMCServiceException {
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		User owner = userRepository.findByUsername(userEPPN);

		Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams, owner));
		List<Document> results;
		List<Document> returnList = new ArrayList<>();

		results = documentRepository.findAll(where, new PageRequest(0, 5000, new Sort(new Order(Direction.DESC, "modified")))).getContent();

		if (results.size() == 0) return null;
		//check for access
		//superadmin's see everything
		if(owner.getRoles().stream().anyMatch(r->r.getRole().getRole().equals(SecurityRoles.SUPERADMIN))) {
			return mapper.mapToModel(pagify(results, pageNumber, pageSize));
		}
		
		for(Document doc : results) {
			//check for access
			if (resourceAccessService.hasAccess(ResourceType.DOCUMENT, doc, owner)) {
				returnList.add(doc);
			}
		}

		if (returnList.size() == 0) return null;

		return mapper.mapToModel(pagify(returnList, pageNumber, pageSize));
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

		return mapper.mapToModel(docList.get(0));
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
		docEntity.setIsPublic(false);
		docEntity.setModified(now);
		docEntity.setResourceType(ResourceType.DOCUMENT);
		docEntity.setVersion(1);

		docEntity = documentRepository.save(docEntity);
		this.parentDocumentService.updateParents(docEntity);

		ServiceLogger.log(logTag, "Attempting to verify document");
		//Verify the document
		String temp = verify.verify(docEntity.getId(), docEntity.getDocumentUrl(), "document", docEntity.getOwner().getUsername(), folder, "Documents", "id", "url");
		ServiceLogger.log(logTag, "Verification Machine Response: " + temp);

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
		Integer oldVersion = docEntity.getVersion();
		docEntity.setVersion(oldVersion++);

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

	public ResponseEntity shareDocument(Integer documentId, Integer userId, Boolean dmdii) {
		String documentUrl;

		if (dmdii) {
			documentUrl = getDMDIIDocumentUrl(documentId);
		} else {
			UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = this.userRepository.findByUsername(userPrincipal.getUsername());

			Document document = this.documentRepository.findOne(documentId);

			if (!this.resourceAccessService.hasAccess(ResourceType.DOCUMENT, document, user)) {
				throw new AccessDeniedException("User does not have permission to share document");
			}

			documentUrl = document.getDocumentUrl();
		}

		User userToShareWith = this.userRepository.findOne(userId);
		String key = AWSConnector.createPath(documentUrl);
		String presignedUrl = AWSConnector.generatePresignedUrl(key,
				Date.from(LocalDate.now().plusDays(7).atStartOfDay().toInstant(ZoneOffset.UTC)));

		return this.emailService.sendEmail(userToShareWith, 2, presignedUrl);
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

	private Collection<Predicate> getFilterExpressions(Map<String, String> filterParams, User owner) throws InvalidFilterParameterException {
		Collection<Predicate> expressions = new ArrayList<>();

		expressions.addAll(tagFilter(filterParams.get("tags")));
		expressions.add(parentTypeFilter(filterParams.get("parentType")));
		expressions.add(parentIdFilter(filterParams.get("parentId")));
		expressions.add(docClassFilter(filterParams.get("docClass")));

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

	public void duplicateDocumentsByParentTypeAndId (DocumentParentType oldParentType, Integer oldParentId, DocumentParentType newParentType, Integer newParentId) {
		List<Document> originalDocs = documentRepository.findByParentTypeAndParentId(oldParentType, oldParentId);

		if (originalDocs != null && !originalDocs.isEmpty()) {
			for (Document doc : originalDocs) {
				doc.setId(null);
				doc.setParentType(newParentType);
				doc.setParentId(newParentId);
				doc.setModified(new Timestamp(System.currentTimeMillis()));
				documentRepository.save(doc);
			}
		}
	}
}
