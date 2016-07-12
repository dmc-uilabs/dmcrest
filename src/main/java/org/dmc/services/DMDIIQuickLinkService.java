package org.dmc.services;

import javax.inject.Inject;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.DMDIIQuickLink;
import org.dmc.services.data.mappers.Mapper;
import org.dmc.services.data.mappers.MapperFactory;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.DMDIIQuickLinkModel;
import org.dmc.services.data.repositories.DMDIIQuickLinkRepository;
import org.springframework.stereotype.Service;

@Service
public class DMDIIQuickLinkService {

	@Inject
	private DMDIIQuickLinkRepository dmdiiQuickLinkRepository;
	
	@Inject
	private DMDIIDocumentService dmdiiDocumentService;
	
	@Inject
	private MapperFactory mapperFactory;
	
	public DMDIIQuickLinkModel findOne (Integer id) throws DMCServiceException {
		Mapper<DMDIIQuickLink, DMDIIQuickLinkModel> mapper = mapperFactory.mapperFor(DMDIIQuickLink.class, DMDIIQuickLinkModel.class);
		return mapper.mapToModel(dmdiiQuickLinkRepository.findOne(id));
	}
	
	public DMDIIQuickLinkModel save (DMDIIQuickLinkModel link) throws DMCServiceException {
		Mapper<DMDIIQuickLink, DMDIIQuickLinkModel> linkMapper = mapperFactory.mapperFor(DMDIIQuickLink.class, DMDIIQuickLinkModel.class);
		Mapper<DMDIIDocument, DMDIIDocumentModel> docMapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		
		DMDIIQuickLink linkEntity = linkMapper.mapToEntity(link);
		DMDIIDocument docEntity = docMapper.mapToEntity(dmdiiDocumentService.findOne(link.getDoc().getId()));
		
		linkEntity.setDoc(docEntity);
		
		return linkMapper.mapToModel(linkEntity);
	}
}
