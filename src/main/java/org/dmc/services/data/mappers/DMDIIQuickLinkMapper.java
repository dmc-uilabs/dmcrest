package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.DMDIIQuickLink;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.dmc.services.data.models.DMDIIQuickLinkModel;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DMDIIQuickLinkMapper extends AbstractMapper<DMDIIQuickLink, DMDIIQuickLinkModel>{

	@Override
	public DMDIIQuickLink mapToEntity(DMDIIQuickLinkModel model) {
		Assert.notNull(model);
		DMDIIQuickLink entity = copyProperties(model, new DMDIIQuickLink());
		
		Mapper<DMDIIDocument, DMDIIDocumentModel> docMapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		
		entity.setDoc(docMapper.mapToEntity(model.getDoc()));
		
		return entity;
	}

	@Override
	public DMDIIQuickLinkModel mapToModel(DMDIIQuickLink entity) {
		Assert.notNull(entity);
		DMDIIQuickLinkModel model = copyProperties(entity, new DMDIIQuickLinkModel());
		
		Mapper<DMDIIDocument, DMDIIDocumentModel> docMapper = mapperFactory.mapperFor(DMDIIDocument.class, DMDIIDocumentModel.class);
		
		model.setDoc(docMapper.mapToModel(entity.getDoc()));
		
		return model;
	}

	@Override
	public Class<DMDIIQuickLink> supportsEntity() {
		return DMDIIQuickLink.class;
	}

	@Override
	public Class<DMDIIQuickLinkModel> supportsModel() {
		return DMDIIQuickLinkModel.class;
	}

}
