package org.dmc.services;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.DMDIIDocumentTag;
import org.dmc.services.data.entities.DMDIIQuickLink;
import org.dmc.services.data.entities.QDMDIIDocument;
import org.dmc.services.data.entities.QDMDIIQuickLink;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.DMDIIDocumentTagModel;
import org.dmc.services.data.repositories.DMDIIDocumentRepository;
import org.dmc.services.data.repositories.DMDIIDocumentTagRepository;
import org.dmc.services.data.repositories.DMDIIQuickLinkRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.recentupdates.RecentUpdateController;
import org.dmc.services.verification.Verification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DMDIIDocumentService {

	@Inject
	private DMDIIDocumentRepository dmdiiDocumentRepository;

	@Inject
	private DMDIIDocumentTagRepository dmdiiDocumentTagRepository;

	@Inject
	private DMDIIQuickLinkRepository dmdiiQuickLinkRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private MapperFactory mapperFactory;

	@Inject
	private DocumentLinkService documentLinkService;

	private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

	private final String logTag = DMDIIDocumentService.class.getName();

	private Verification verify = new Verification();

	public List<DMDIIDocumentModel> filter(Map filterParams, Integer pageNumber, Integer pageSize) throws InvalidFilterParameterException, DMCServiceException {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams));
		List<DMDIIDocument> results = dmdiiDocumentRepository.findAll(where, new PageRequest(pageNumber, pageSize)).getContent();

		if (results.get(0) != null) {
			results = results.stream().map(dmdiiDocument -> renewLinkIfExpired(dmdiiDocument)).collect(Collectors.toList());
		}

		return mapper.mapToModel(results);
	}

	public List<DMDIIDocumentModel> getDMDIIDocumentsByDMDIIProject (Integer dmdiiProjectId, Integer pageNumber, Integer pageSize) throws DMCServiceException {
		Assert.notNull(dmdiiProjectId);

			ServiceLogger.log("getting all dmdii docs pby project id ", "In getAllDMDIIDocumentsByDMDIIProjectId: " + dmdiiProjectId);
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> documents = dmdiiDocumentRepository.findByDmdiiProjectId(new PageRequest(pageNumber, pageSize), dmdiiProjectId).getContent();

		if (documents.get(0) != null) {
			documents = documents.stream().map(dmdiiDocument -> renewLinkIfExpired(dmdiiDocument)).collect(Collectors.toList());
		}

		return mapper.mapToModel(documents);
	}

	public DMDIIDocumentModel findOne(Integer dmdiiDocumentId) throws DMCServiceException {
		Assert.notNull(dmdiiDocumentId);
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> docList = Collections.singletonList(dmdiiDocumentRepository.findOne(dmdiiDocumentId));

		if (docList.get(0).getExpires().toLocalDateTime().isBefore(LocalDateTime.now())) {
			renewDocumentLink(dmdiiDocumentId, documentLinkService.getHoursFromNow(1));
			return findOne(dmdiiDocumentId);
		}

		return mapper.mapToModel(docList.get(0));
	}

	public DMDIIDocument findOneEntity(Integer id) throws DMCServiceException {
		Assert.notNull(id);
		DMDIIDocument docEntity = dmdiiDocumentRepository.findOne(id);

		if (docEntity.getExpires().toLocalDateTime().isBefore(LocalDateTime.now())) {
			renewDocumentLink(id, documentLinkService.getHoursFromNow(1));
			return findOneEntity(id);
		}

		return docEntity;
	}

	public DMDIIDocumentModel findMostRecentStaticFileByFileTypeId (Integer fileTypeId) throws DMCServiceException {
		Assert.notNull(fileTypeId);
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> docList = Collections.singletonList(dmdiiDocumentRepository.findTopByFileTypeOrderByModifiedDesc(fileTypeId));

		if (docList.get(0) != null) {
			docList = docList.stream().map(dmdiiDocument -> renewLinkIfExpired(dmdiiDocument)).collect(Collectors.toList());
		}

		return mapper.mapToModel(docList.get(0));

	}

	public DMDIIDocumentModel findMostRecentDocumentByFileTypeIdAndDMDIIProjectId (Integer fileTypeId, Integer dmdiiProjectId) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> docList = Collections.singletonList(dmdiiDocumentRepository.findTopByFileTypeAndDmdiiProjectIdOrderByModifiedDesc(fileTypeId, dmdiiProjectId));

		if (docList.get(0) != null) {
			docList = docList.stream().map(dmdiiDocument -> renewLinkIfExpired(dmdiiDocument)).collect(Collectors.toList());
		}

		return mapper.mapToModel(docList.get(0));
	}

	public DMDIIDocumentModel save (DMDIIDocumentModel doc, BindingResult result) throws DMCServiceException {
		         	ServiceLogger.log("SSS         ", "Attempting to verify DMDII document");
		Mapper<DMDIIDocument, DMDIIDocumentModel> docMapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);

		DMDIIDocument docEntity = docMapper.mapToEntity(doc);
		User userEntity = this.userRepository.findOne(doc.getOwnerId());
		docEntity.setOwner(userEntity);

		Timestamp now = new Timestamp(System.currentTimeMillis());

		if (docEntity.getExpires() == null) {
			docEntity.setExpires(documentLinkService.getHoursFromNow(1));
		}
		docEntity.setIsDeleted(false);
		docEntity.setVerified(false);
		docEntity.setModified(now);
		docEntity.setVersion(1);

		docEntity = dmdiiDocumentRepository.save(docEntity);

		ServiceLogger.log(logTag, "Attempting to verify DMDII document");
		//Verify the document
		String temp = verify.verify(docEntity.getId(), docEntity.getDocumentUrl(), "dmdii_document", userEntity.getUsername(), "ProjectOfDMDII", "Documents", "id", "url");
		ServiceLogger.log(logTag, "Verification Machine Response: " + temp);

		RecentUpdateController recentUpdateController = new RecentUpdateController();
		recentUpdateController.addRecentUpdate(docEntity);

		return docMapper.mapToModel(docEntity);
	}

	public List<DMDIIDocumentTagModel> getAllTags() {
		Mapper<DMDIIDocumentTag, DMDIIDocumentTagModel> tagMapper = mapperFactory.mapperFor(DMDIIDocumentTag.class, DMDIIDocumentTagModel.class);
		return tagMapper.mapToModel(dmdiiDocumentTagRepository.findAll());
	}

	public DMDIIDocumentTagModel saveDocumentTag(DMDIIDocumentTagModel tag) {
		Mapper<DMDIIDocumentTag, DMDIIDocumentTagModel> tagMapper = mapperFactory.mapperFor(DMDIIDocumentTag.class, DMDIIDocumentTagModel.class);
		return tagMapper.mapToModel(dmdiiDocumentTagRepository.save(tagMapper.mapToEntity(tag)));
	}

	private Collection<Predicate> getFilterExpressions(Map<String, String> filterParams) throws InvalidFilterParameterException {
		Collection<Predicate> expressions = new ArrayList<Predicate>();

		expressions.addAll(tagFilter(filterParams.get("tags")));

		return expressions;
	}

	private Collection<Predicate> tagFilter(String tagIds) throws InvalidFilterParameterException {
		if(tagIds.equals(null))
			return new ArrayList<Predicate>();

		Collection<Predicate> returnValue = new ArrayList<Predicate>();
		String[] tags = tagIds.split(",");
		Integer tagIdInt = null;

		for(String tag: tags) {
			try{
				tagIdInt = Integer.parseInt(tag);
			} catch(NumberFormatException e) {
				throw new InvalidFilterParameterException("tags", Integer.class);
			}

			returnValue.add(QDMDIIDocument.dMDIIDocument.tags.any().id.eq(tagIdInt));
		}
		return returnValue;
	}

	@Transactional
	public DMDIIDocumentModel delete (Integer dmdiiDocumentId) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		DMDIIDocument dmdiiDocument = this.dmdiiDocumentRepository.findOne(dmdiiDocumentId);
		//check to see if the document is a quicklink, if so delete quicklink
		if(dmdiiQuickLinkRepository.countByDoc(dmdiiDocument) > 0) {
			dmdiiQuickLinkRepository.deleteByDMDIIDocumentId(dmdiiDocumentId);
		}

		DMDIIDocument docEntity = dmdiiDocumentRepository.findOne(dmdiiDocumentId);

		docEntity.setIsDeleted(true);

		docEntity = dmdiiDocumentRepository.save(docEntity);

		return mapper.mapToModel(docEntity);
	}

	@Transactional
	public void deleteDMDIIDocumentsByDMDIIProjectId(Integer dmdiiProjectId) {
		List<DMDIIDocument> docs = dmdiiDocumentRepository.findByDmdiiProjectIdAndIsDeletedFalse(new PageRequest(0, Integer.MAX_VALUE), dmdiiProjectId).getContent();
		docs.stream().forEach(n -> n.setIsDeleted(true));
		dmdiiDocumentRepository.save(docs);
	}

	@Transactional
	public DMDIIDocumentModel update(DMDIIDocumentModel doc) throws IllegalArgumentException {
		Assert.notNull(doc);
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);

		DMDIIDocument docEntity = mapper.mapToEntity(doc);
		DMDIIDocument oldEntity = dmdiiDocumentRepository.findOne(doc.getId());
		Assert.notNull(oldEntity);

		docEntity.setExpires(oldEntity.getExpires());
		docEntity.setModified(new Timestamp(System.currentTimeMillis()));
		Integer oldVersion = oldEntity.getVersion();
		docEntity.setVersion(oldVersion++);
		docEntity.setVerified(oldEntity.getVerified());

		// Insert update for all modified fields
		RecentUpdateController recentUpdateController = new RecentUpdateController();
		recentUpdateController.addRecentUpdate(docEntity, oldEntity);

		docEntity= dmdiiDocumentRepository.save(docEntity);


		return mapper.mapToModel(docEntity);
	}

	/**
	 * Removes all unverified document records that are a week old.
	 * <p>
	 * This is scheduled to run every day at 1:06 AM.
	 * The pattern is a list of six single space-separated fields: representing second, minute, hour, day, month, weekday.
	 */
	@Scheduled(cron = "0 6 1 * * ?")
	@Transactional(rollbackFor = DMCServiceException.class)
	protected void removeUnverifiedDocuments() {
		logger.info("Removing unverified document records.");
		LocalDateTime lastWeek = LocalDate.now().atStartOfDay().minusWeeks(1);

		List<DMDIIDocument> documents = this.dmdiiDocumentRepository
				.findAllByVerifiedIsFalseAndModifiedBefore(Timestamp.valueOf(lastWeek));

		for (DMDIIDocument document : documents) {
			try {
				document.setIsDeleted(true);

				logger.info("Removing old unverified document with owner id: {} and url: {}", document.getOwner().getId(), document.getDocumentUrl());
				//check to see if this is a quicklink document
				Predicate where = QDMDIIQuickLink.dMDIIQuickLink.doc().eq(document);
				DMDIIQuickLink link = dmdiiQuickLinkRepository.findOne(where);
				if(link != null) {
					this.dmdiiQuickLinkRepository.delete(link);
				}

				this.dmdiiDocumentRepository.delete(document);
			} catch (DMCServiceException ex) {
				logger.error("Error occurred while removing old unverified document", ex);
			}

		}
	}

	/**
	 * Refreshes all documents that are about to expire.
	 * Documents that are active and have less than or equal to 1 day left for expiration are refreshed.
	 * <p>
	 * This is scheduled to run every day at 1:06 AM.
	 * The pattern is a list of six single space-separated fields: representing second, minute, hour, day, month, weekday.
	 */
	@Scheduled(cron = "0 6 1 * * ?")
	@Transactional(rollbackFor = DMCServiceException.class)
	protected void refreshDocuments() {
		logger.info("Refreshing documents in AWS.");
		LocalDateTime future = LocalDate.now().atStartOfDay().plusDays(2);
		List<DMDIIDocument> documents = this.dmdiiDocumentRepository
				.findAllByVerifiedIsTrueAndIsDeletedIsFalseAndExpiresBefore(Timestamp.valueOf(future));

		for (DMDIIDocument document : documents) {
			try {
				Timestamp expiration = documentLinkService.getHoursFromNow(1);
				String path = AWSConnector.returnKeyNameFromURL(document.getDocumentUrl());
				String newURL = AWSConnector.refreshURL(path, expiration);

				LocalDateTime nextMonth = LocalDate.now().atStartOfDay().plusMonths(1).minusDays(1);

				document.setDocumentUrl(newURL);
				document.setExpires(expiration);

				logger.info("Refreshing document with owner id: {} and new url: {}", document.getOwner().getId(), document.getDocumentUrl());

				this.dmdiiDocumentRepository.save(document);

			} catch (DMCServiceException ex) {
				logger.error("Error occurred while refreshing document", ex);
			}
		}
	}

	@Transactional(rollbackFor = DMCServiceException.class)
	protected String renewDocumentLink(Integer documentId, Timestamp expiration) throws DMCServiceException {

		DMDIIDocument documentToRenew = this.dmdiiDocumentRepository.findOne(documentId);

		try {

			String path = AWSConnector.returnKeyNameFromURL(documentToRenew.getDocumentUrl());
			String newUrl = AWSConnector.refreshURL(path, expiration);

			documentToRenew.setDocumentUrl(newUrl);
			documentToRenew.setExpires(expiration);

			this.dmdiiDocumentRepository.save(documentToRenew);

			return newUrl;


		} catch (DMCServiceException ex) {
			ServiceLogger.logException("Error occurred while refreshing document", ex);
			return "There was an error refreshing the document.";
		}
	}

	private DMDIIDocument renewLinkIfExpired(DMDIIDocument doc) throws DMCServiceException {
		if (doc.getExpires().toLocalDateTime().isBefore(LocalDateTime.now())) {
			Timestamp expiration = documentLinkService.getHoursFromNow(1);
			doc.setDocumentUrl(renewDocumentLink(doc.getId(), expiration));
			doc.setExpires(expiration);
			ServiceLogger.log("Document Refresh", "Document URL refreshed for dmdii Document " + doc.getId());
		}
		return doc;
	}
}
