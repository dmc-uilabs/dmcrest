package org.dmc.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentClass;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.DocumentTag;
import org.dmc.services.data.entities.QDocument;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DocumentModel;
import org.dmc.services.data.models.DocumentTagModel;
import org.dmc.services.data.repositories.DocumentRepository;
import org.dmc.services.data.repositories.DocumentTagRepository;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.verification.Verification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

@Service
public class DocumentService {

	@Inject
	private DocumentRepository documentRepository;
	
	@Inject
	private DocumentTagRepository documentTagRepository;
	
	@Inject
	private MapperFactory mapperFactory;
	
	private final String logTag = DocumentService.class.getName();
	
	private Verification verify = new Verification();
	
	public List<DocumentModel> filter(Map filterParams, Integer recent, Integer pageNumber, Integer pageSize) throws InvalidFilterParameterException, DMCServiceException {
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams));
		List<Document> results;
		
		if(recent != null) {
			results = documentRepository.findAll(where, new PageRequest(0, recent, new Sort(new Order (Direction.DESC, "modified")))).getContent();
		}
		else {
			results = documentRepository.findAll(where, new PageRequest(pageNumber, pageSize)).getContent();
		}
		
		if(results.size() == 0) return null;
		
		results = refreshDocuments(results);
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
		
		if(docList.size() == 0) return null;
		
		docList = refreshDocuments(docList);
		return mapper.mapToModel(docList.get(0));
	}
	
	public DocumentModel save (DocumentModel doc, BindingResult result) throws DMCServiceException, IllegalArgumentException {
		Assert.notNull(doc);
		Mapper<Document, DocumentModel> docMapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		String folder = "APPLICATION";
		
		if(doc.getParentType() != null) {
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
		
		docEntity = documentRepository.save(docEntity);
		
		ServiceLogger.log(logTag, "Attempting to verify document");
		//Verify the document
		String temp = verify.verify(docEntity.getId(), docEntity.getDocumentUrl(), "document", docEntity.getOwner().getUsername(), folder, "Documents", "id", "url");
		ServiceLogger.log(logTag, "Verification Machine Response: " + temp);
		
		return docMapper.mapToModel(docEntity);
	}
	
	public DocumentModel delete (Integer documentId) throws IllegalArgumentException {
		Assert.notNull(documentId);
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		
		Document docEntity = documentRepository.findOne(documentId);
		
		docEntity.setIsDeleted(true);
		
		docEntity = documentRepository.save(docEntity);
		
		return mapper.mapToModel(docEntity);
	}
	
	public DocumentModel update (DocumentModel doc) throws IllegalArgumentException {
		Assert.notNull(doc);
		Mapper<Document, DocumentModel> mapper = mapperFactory.mapperFor(Document.class, DocumentModel.class);
		
		Document docEntity = mapper.mapToEntity(doc);
		Document oldEntity = documentRepository.findOne(doc.getId());
		
		docEntity.setExpires(oldEntity.getExpires());
		docEntity.setModified(new Timestamp(System.currentTimeMillis()));
		
		docEntity = documentRepository.save(docEntity);
		
		return mapper.mapToModel(docEntity);
	}

	private Collection<Predicate> getFilterExpressions(Map<String, String> filterParams) throws InvalidFilterParameterException {
		Collection<Predicate> expressions = new ArrayList<Predicate>();

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
		if(tagIds ==null) {
			return new ArrayList<Predicate>();
		}

		Collection<Predicate> returnValue = new ArrayList<Predicate>();
		String[] tags = tagIds.split(",");
		Integer tagIdInt;

		for(String tag: tags) {
			try{
				tagIdInt = Integer.parseInt(tag);
			} catch(NumberFormatException e) {
				throw new InvalidFilterParameterException("tags", Integer.class);
			}

			returnValue.add(QDocument.document.tags.any().id.eq(tagIdInt));
		}
		return returnValue;
	}
	
	private Predicate parentTypeFilter(String parentType) throws InvalidFilterParameterException {
		if(parentType == null) return null;
		
		DocumentParentType eType;
		
		try {
			eType = DocumentParentType.valueOf(parentType);
		} catch (Exception e) {
			throw new InvalidFilterParameterException("parentType", DocumentParentType.class);
		}
		
		return QDocument.document.parentType.eq(eType);
	}
	
	private Predicate parentIdFilter(String parentId) throws InvalidFilterParameterException {
		if(parentId == null) return null;
		Integer parentIdInt;
		
		try {
			parentIdInt = Integer.parseInt(parentId);
		} catch(NumberFormatException e) {
			throw new InvalidFilterParameterException("parentId", Integer.class);
		}
		
		return QDocument.document.parentId.eq(parentIdInt);
	}
	
	private Predicate docClassFilter(String docClass) throws InvalidFilterParameterException {
		if(docClass == null) return null;
		
		DocumentClass eType;
		
		try {
			eType = DocumentClass.valueOf(docClass);
		} catch (Exception e) {
			throw new InvalidFilterParameterException("docClass", DocumentClass.class);
		}
		
		return QDocument.document.docClass.eq(eType);
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
