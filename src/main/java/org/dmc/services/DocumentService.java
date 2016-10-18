package org.dmc.services;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.dmc.services.data.entities.*;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.DocumentTagModel;
import org.dmc.services.data.repositories.DocumentRepository;
import org.dmc.services.data.repositories.DocumentTagRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.verification.Verification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DocumentService {

	private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

	@Inject
	private DocumentRepository documentRepository;

	@Inject
	private DocumentTagRepository documentTagRepository;
	
	@Inject
	private UserRepository userRepository;

	@Inject
	private MapperFactory mapperFactory;

	@Inject
	private ParentDocumentService parentDocumentService;

	private final String logTag = DocumentService.class.getName();

	private Verification verify = new Verification();

	public List<DocumentModel> filter(Map filterParams, Integer recent, Integer pageNumber, Integer pageSize, String userEPPN) throws InvalidFilterParameterException, DMCServiceException {
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams));
		Integer userId = userRepository.findByUsername(userEPPN).getId();
		List<Document> results;

		if (recent != null) {
			results = documentRepository.findAllowedDocuments(userId, new PageRequest(0, recent, new Sort(new Order(Direction.DESC, "modified"))), where).getContent();
		} else {
			results = documentRepository.findAllowedDocuments(userId, new PageRequest(pageNumber, pageSize), where).getContent();
		}

		if (results.size() == 0) return null;

		return mapper.mapToModel(results);
	}

	public Long count(Map filterParams) throws InvalidFilterParameterException {
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams));
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
		docEntity.setModified(now);
		docEntity.setResourceTypeId(1);

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

		docEntity = documentRepository.save(docEntity);
		this.parentDocumentService.updateParents(docEntity);

		return mapper.mapToModel(docEntity);
	}

	@Transactional
	public Document updateVerifiedDocument(Integer documentId, String verifiedUrl, boolean verified){
		Document document = this.documentRepository.findOne(documentId);
		document.setDocumentUrl(verifiedUrl);
		document.setVerified(verified);

		this.documentRepository.save(document);
		this.parentDocumentService.delegateToParent(document);

		return document;
	}

	private Collection<Predicate> getFilterExpressions(Map<String, String> filterParams) throws InvalidFilterParameterException {
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

	public DocumentTagModel saveDocumentTag(DocumentTagModel tag) {
		Mapper<DocumentTag, DocumentTagModel> tagMapper = mapperFactory.mapperFor(DocumentTag.class, DocumentTagModel.class);
		return tagMapper.mapToModel(documentTagRepository.save(tagMapper.mapToEntity(tag)));
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
				this.parentDocumentService.delegateToParent(document);

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
				this.parentDocumentService.delegateToParent(document);

			} catch (DMCServiceException ex) {
				logger.error("Error occurred while refreshing document", ex);
			}
		}
	}

}
