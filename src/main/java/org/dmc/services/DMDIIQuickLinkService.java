package org.dmc.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.DMDIIQuickLink;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.DMDIIQuickLinkModel;
import org.dmc.services.data.repositories.DMDIIDocumentRepository;
import org.dmc.services.data.repositories.DMDIIQuickLinkRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
public class DMDIIQuickLinkService {

	@Inject
	private DMDIIQuickLinkRepository dmdiiQuickLinkRepository;
	
	@Inject
	private DMDIIDocumentRepository dmdiiDocumentRespository;

	@Inject
	private DMDIIDocumentService dmdiiDocumentService;
	
	@Inject
	private DMDIIDocumentRepository dmdiiDocumentRepository;

	@Inject
	private MapperFactory mapperFactory;

	public DMDIIQuickLinkModel findOne (Integer id) throws DMCServiceException {
		Mapper<DMDIIQuickLink, DMDIIQuickLinkModel> mapper = mapperFactory.mapperFor(DMDIIQuickLink.class, DMDIIQuickLinkModel.class);
		return mapper.mapToModel(dmdiiQuickLinkRepository.findOne(id));
	}

	public DMDIIQuickLinkModel save (DMDIIQuickLinkModel link, BindingResult result) throws DMCServiceException {
		Mapper<DMDIIQuickLink, DMDIIQuickLinkModel> linkMapper = mapperFactory.mapperFor(DMDIIQuickLink.class, DMDIIQuickLinkModel.class);
		Mapper<DMDIIDocument, DMDIIDocumentModel> docMapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);

		DMDIIQuickLink linkEntity = linkMapper.mapToEntity(link);

		if(link.getDoc() != null) {
			DMDIIDocument docEntity = docMapper.mapToEntity(dmdiiDocumentService.save(link.getDoc(), result));

			linkEntity.setDoc(docEntity);
		}
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		linkEntity.setCreated(now);

		return linkMapper.mapToModel(dmdiiQuickLinkRepository.save(linkEntity));
	}

	public List<DMDIIQuickLinkModel> getDMDIIQuickLinks(Integer limit) throws DMCServiceException {
		Mapper<DMDIIQuickLink, DMDIIQuickLinkModel> linkMapper = mapperFactory.mapperFor(DMDIIQuickLink.class, DMDIIQuickLinkModel.class);
		Mapper<DMDIIDocument, DMDIIDocumentModel> docMapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);

		List<DMDIIQuickLinkModel> freshLinks = linkMapper.mapToModel(dmdiiQuickLinkRepository.findAllByOrderByCreatedDesc(new PageRequest(0, limit)).getContent());
		
		for(DMDIIQuickLinkModel link : freshLinks) {
			if(link.getDoc() != null) {
				DMDIIDocumentModel docModel = dmdiiDocumentService.findOne(link.getDoc().getId());
				link.setDoc(docModel);
			}
		}
		return freshLinks;
	}

	@Transactional
	public Boolean delete(Integer id) {
		
		DMDIIQuickLink linkEntity = dmdiiQuickLinkRepository.findOne(id);
		
		if(linkEntity.getDoc() != null) {
			DMDIIDocument docEntity = dmdiiDocumentRespository.findOne(linkEntity.getDoc().getId());
			docEntity.setIsDeleted(true);
			dmdiiDocumentRepository.save(docEntity);
		}
		
		if(dmdiiQuickLinkRepository.deleteById(id) == 1)
			return true;
		else
			return false;
	}
}
