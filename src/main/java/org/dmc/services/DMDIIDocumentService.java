package org.dmc.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.DMDIIDocumentTag;
import org.dmc.services.data.entities.QDMDIIDocument;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.DMDIIDocumentTagModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.repositories.DMDIIDocumentRepository;
import org.dmc.services.data.repositories.DMDIIDocumentTagRepository;
import org.dmc.services.exceptions.InvalidFilterParameterException;
import org.dmc.services.verification.Verification;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

@Service
public class DMDIIDocumentService {

	@Inject
	private DMDIIDocumentRepository dmdiiDocumentRepository;

	@Inject
	private DMDIIDocumentTagRepository dmdiiDocumentTagRepository;
	
	@Inject
	private UserService userService;

	@Inject
	private MapperFactory mapperFactory;
	
	private final String logTag = DMDIIDocumentService.class.getName();
	
	private Verification verify = new Verification();

	public List<DMDIIDocumentModel> filter(Map filterParams, Integer pageNumber, Integer pageSize) throws InvalidFilterParameterException, DMCServiceException {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		Predicate where = ExpressionUtils.allOf(getFilterExpressions(filterParams));
		List<DMDIIDocument> results = dmdiiDocumentRepository.findAll(where, new PageRequest(pageNumber, pageSize)).getContent();
		results = refreshDocuments(results);
		return mapper.mapToModel(results);
	}

	public List<DMDIIDocumentModel> findPage(Integer pageNumber, Integer pageSize) throws DMCServiceException {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> documents = dmdiiDocumentRepository.findAll(new PageRequest(pageNumber, pageSize)).getContent();
		
		documents = refreshDocuments(documents);
		
		return mapper.mapToModel(documents);
	}
	
	public List<DMDIIDocumentModel> getUndeletedDMDIIDocuments(Integer pageNumber, Integer pageSize) throws DMCServiceException {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> documents = dmdiiDocumentRepository.findByIsDeletedFalse(new PageRequest(pageNumber, pageSize)).getContent();
		
		documents = refreshDocuments(documents);
		return mapper.mapToModel(documents);
	}
	
	public List<DMDIIDocumentModel> getDMDIIDocumentsByDMDIIProject (Integer dmdiiProjectId, Integer pageNumber, Integer pageSize) throws DMCServiceException {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> documents = dmdiiDocumentRepository.findByDmdiiProjectId(new PageRequest(pageNumber, pageSize), dmdiiProjectId).getContent();
		
		documents = refreshDocuments(documents);
		return mapper.mapToModel(documents);
	}

	public DMDIIDocumentModel findOne(Integer dmdiiDocumentId) throws DMCServiceException {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> docList = Collections.singletonList(dmdiiDocumentRepository.findOne(dmdiiDocumentId));
		
		docList = refreshDocuments(docList);
		return mapper.mapToModel(docList.get(0));
	}

	public DMDIIDocument findOneEntity(Integer id) throws DMCServiceException {
		DMDIIDocument docEntity = dmdiiDocumentRepository.findOne(id);
		
		return docEntity;
	}
	
	public DMDIIDocumentModel findMostRecentStaticFileByFileTypeId (Integer fileTypeId) throws DMCServiceException {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> docList = Collections.singletonList(dmdiiDocumentRepository.findTopByFileTypeOrderByModifiedDesc(fileTypeId));
		
		docList = refreshDocuments(docList);
		return mapper.mapToModel(docList.get(0));
		
	}
	
	public DMDIIDocumentModel findMostRecentDocumentByFileTypeIdAndDMDIIProjectId (Integer fileTypeId, Integer dmdiiProjectId) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> docList = Collections.singletonList(dmdiiDocumentRepository.findTopByFileTypeAndDmdiiProjectIdOrderByModifiedDesc(fileTypeId, dmdiiProjectId));
		
		docList = refreshDocuments(docList);
		return mapper.mapToModel(docList.get(0));
	}
	
	public DMDIIDocumentModel save (DMDIIDocumentModel doc, BindingResult result) throws DMCServiceException {
		
		Mapper<DMDIIDocument, DMDIIDocumentModel> docMapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		
		DMDIIDocument docEntity = docMapper.mapToEntity(doc);
		User userEntity = userMapper.mapToEntity(userService.findOne(doc.getOwnerId()));
		docEntity.setOwner(userEntity);
		
		//current time plus one month
		Long duration = 1000L * 60L * 60L * 24L * 30L;
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Timestamp expires = new Timestamp(now.getTime() + duration);
		
		docEntity.setExpires(expires);
		docEntity.setIsDeleted(false);
		docEntity.setVerified(false);
		docEntity.setModified(now);
		
		docEntity = dmdiiDocumentRepository.save(docEntity);
		
		ServiceLogger.log(logTag, "Attempting to verify DMDII document");
		//Verify the document
		String temp = verify.verify(docEntity.getId(), docEntity.getDocumentUrl(), "dmdii_document", userEntity.getUsername(), "ProjectOfDMDII", "Documents", "id", "url");
		ServiceLogger.log(logTag, "Verification Machine Response: " + temp);
		
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
	
	private List<DMDIIDocument> refreshDocuments (List<DMDIIDocument> docs) throws DMCServiceException {
		List<DMDIIDocument> freshDocs = new ArrayList<DMDIIDocument>();
		//Refresh check
		for (DMDIIDocument doc : docs) {
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
				doc = dmdiiDocumentRepository.save(doc);
			}
			
			freshDocs.add(doc);
		}
		
		return freshDocs;
	}
}
