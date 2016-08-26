package org.dmc.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.repositories.DocumentRepository;
import org.dmc.services.verification.Verification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

@Service
public class DocumentService {

	@Inject
	private DocumentRepository documentRepository;
	
	@Inject
	private UserService userService;
	
	@Inject
	private MapperFactory mapperFactory;
	
	private final String logTag = DocumentService.class.getName();
	
	private Verification verify = new Verification();
	
	public DocumentModel findOne(Integer documentId) throws DMCServiceException {
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		List<Document> docList = Collections.singletonList(documentRepository.findOne(documentId));
		
		docList = refreshDocuments(docList);
		return mapper.mapToModel(docList.get(0));
	}
	
	public List<DocumentModel> getDocumentsByOrganization (Integer organizationId) {
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		List<Document> docs = documentRepository.findByOrganizationIdAndIsDeletedFalse(organizationId);
		
		docs = refreshDocuments(docs);
		return mapper.mapToModel(docs);
	}
	
	public DocumentModel findMostRecentDocumentByFileTypeIdAndOrganizationId (Integer fileTypeId, Integer organizationId) {
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		List<Document> docs = Collections.singletonList(documentRepository.findTopByOrganizationIdAndFileTypeOrderByModifiedDesc(organizationId, fileTypeId));
		
		docs = refreshDocuments(docs);
		return mapper.mapToModel(docs.get(0));
	}
	
	public List<DocumentModel> findDocumentsByOrganizationIdAndFileTypeId(Integer organizationId, Integer fileTypeId) {
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		List<Document> docs = documentRepository.findByOrganizationIdAndFileTypeOrderByModifiedDesc(organizationId, fileTypeId);
		
		docs = refreshDocuments(docs);
		return mapper.mapToModel(docs);
	}
	
	public DocumentModel save (DocumentModel doc, BindingResult result) throws DMCServiceException {
		Mapper<Document, DocumentModel> docMapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		
		Document docEntity = docMapper.mapToEntity(doc);
		User userEntity = userMapper.mapToEntity(userService.findOne(doc.getOwnerId()));
		docEntity.setOwner(userEntity);
		
		//thirty days in milliseconds
		Long duration = 1000L * 60L * 60L * 24L * 30L;
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Timestamp expires = new Timestamp(now.getTime() + duration);
		
		docEntity.setExpires(expires);
		docEntity.setIsDeleted(false);
		docEntity.setVerified(false);
		docEntity.setModified(now);
		
		docEntity = documentRepository.save(docEntity);
		
		ServiceLogger.log(logTag, "Attempting to verify document");
		//Verify the document
		String temp = verify.verify(docEntity.getId(), docEntity.getDocumentUrl(), "document", userEntity.getUsername(), "Companies", "Documents", "id", "url");
		ServiceLogger.log(logTag, "Verification Machine Response: " + temp);
		
		return docMapper.mapToModel(docEntity);
	}
	
	public DocumentModel delete (Integer documentId) {
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		
		Document docEntity = documentRepository.findOne(documentId);
		
		docEntity.setIsDeleted(true);
		
		docEntity = documentRepository.save(docEntity);
		
		return mapper.mapToModel(docEntity);
	}
	
	public Long countDocumentsByOrganizationIdAndFileType(Integer organizationId, Integer fileType) {
		Assert.notNull(organizationId);
		return documentRepository.countByOrganizationIdAndFileType(organizationId, fileType);
	}
	
	private List<Document> refreshDocuments (List<Document> docs) throws DMCServiceException {
		List<Document> freshDocs = new ArrayList<Document>();
		//Refresh check
		for (Document doc : docs) {
			if(AWSConnector.isTimeStampExpired(doc.getExpires())) {
				//Get path from URL
				String path = AWSConnector.createPath(doc.getDocumentUrl());
				
				//Refresh URL
				String newURL = AWSConnector.refreshURL(path);
				
				//create a timestamp
				Timestamp expires = new Timestamp(Calendar.getInstance().getTime().getTime());
				
				//add a month
				expires.setTime(expires.getTime() + (1000*60*60*24*30));
				
				//update the entity
				doc.setDocumentUrl(newURL);
				doc.setExpires(expires);
				doc = documentRepository.save(doc);
			}
			
			freshDocs.add(doc);
		}
		
		return freshDocs;
	}
}
