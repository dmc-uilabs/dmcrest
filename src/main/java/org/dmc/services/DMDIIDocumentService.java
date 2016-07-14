package org.dmc.services;

import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIDocumentModel;
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
	private DMDIIProjectService dmdiiProjectService;

	@Inject
	private MapperFactory mapperFactory;

	public List<DMDIIDocumentModel> findPage(Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> documents = dmdiiDocumentRepository.findByIsDeletedFalse(new PageRequest(pageNumber, pageSize)).getContent();
		return mapper.mapToModel(documents);
	}

	public List<DMDIIDocumentModel> getUndeletedDMDIIDocuments(Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		List<DMDIIDocument> documents = dmdiiDocumentRepository.findByIsDeletedFalse(new PageRequest(pageNumber, pageSize)).getContent();
		return mapper.mapToModel(documents);
	}

	public List<DMDIIDocumentModel> getDMDIIDocumentsByDMDIIProject (Integer dmdiiProjectId, Integer pageNumber, Integer pageSize) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		return mapper.mapToModel(dmdiiDocumentRepository.findByDmdiiProjectIdAndIsDeletedFalse(new PageRequest(pageNumber, pageSize), dmdiiProjectId).getContent());
	}

	public DMDIIDocumentModel getDMDIIDocumentByDMDIIDocumentId(Integer dmdiiDocumentId) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> mapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		return mapper.mapToModel(dmdiiDocumentRepository.findOne(dmdiiDocumentId));
	}

	public DMDIIDocumentModel save(DMDIIDocumentModel doc) {
		Mapper<DMDIIDocument, DMDIIDocumentModel> docMapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		return docMapper.mapToModel(dmdiiDocumentRepository.save(docMapper.mapToEntity(doc)));
	}
}
