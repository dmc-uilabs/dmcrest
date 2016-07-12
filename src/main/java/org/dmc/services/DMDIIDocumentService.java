package org.dmc.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.UserModel;
import org.dmc.services.data.repositories.DMDIIDocumentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DMDIIDocumentService {

	@Inject
	private DMDIIDocumentRepository dmdiiDocumentRepository;
	
	@Inject
	private UserService userService;
	
	@Inject
	private MapperFactory mapperFactory;
	
	private AWSConnector AWS;
	
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
	
	public DMDIIDocumentModel save(DMDIIDocumentModel doc) throws DMCServiceException {
		Mapper<DMDIIDocument, DMDIIDocumentModel> docMapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		Mapper<User, UserModel> userMapper = mapperFactory.mapperFor(User.class, UserModel.class);
		
		String signedURL = "temp";
		signedURL = AWS.upload(doc.getDocumentUrl(), "ProjectOfDMDII", doc.getOwner().getUsername(), "Documents");
		String path = AWS.createPath(signedURL);
		
		DMDIIDocument docEntity = docMapper.mapToEntity(doc);
		User userEntity = userMapper.mapToEntity(userService.findOne(doc.getOwner().getId()));
		docEntity.setOwner(userEntity);
		docEntity.setDocumentUrl(signedURL);
		docEntity.setPath(path);
		
		docEntity = dmdiiDocumentRepository.save(docEntity);
		
		return docMapper.mapToModel(docEntity);
	}
	
	private List<DMDIIDocument> refreshDocuments (List<DMDIIDocument> docs) throws DMCServiceException {
		List<DMDIIDocument> freshDocs = new ArrayList<DMDIIDocument>();
		//Refresh check
		for (DMDIIDocument doc : docs) {
			if(AWS.isTimeStampExpired(doc.getExpires())) {
				//Refresh URL
				String newURL = AWS.refreshURL(doc.getPath());
				
				//create a timestamp
				Timestamp expires = new Timestamp(Calendar.getInstance().getTime().getTime());
				
				//add an hour
				expires.setTime(expires.getTime() + (1000*60*60));
				
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
