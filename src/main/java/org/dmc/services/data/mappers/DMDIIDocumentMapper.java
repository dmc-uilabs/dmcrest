package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.models.DMDIIDocumentModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DMDIIDocumentMapper extends AbstractMapper<DMDIIDocument, DMDIIDocumentModel> {

	@Override
	public DMDIIDocument mapToEntity(DMDIIDocumentModel model) {
		Assert.notNull(model);
		DMDIIDocument entity = new DMDIIDocument();
		BeanUtils.copyProperties(model, entity);
		return entity;
	}

	@Override
	public DMDIIDocumentModel mapToModel(DMDIIDocument entity) {
		Assert.notNull(entity);
		DMDIIDocumentModel model = new DMDIIDocumentModel();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public Class<DMDIIDocument> supportsEntity() {
		return DMDIIDocument.class;
	}

	@Override
	public Class<DMDIIDocumentModel> supportsModel() {
		return DMDIIDocumentModel.class;
	}

}
