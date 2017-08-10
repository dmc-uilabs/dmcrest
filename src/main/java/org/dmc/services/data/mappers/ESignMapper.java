package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.ESignDocument;
import org.dmc.services.data.models.ESignModel;

import org.springframework.stereotype.Component;

@Component
public class ESignMapper extends AbstractMapper<ESignDocument, ESignModel> {

  @Override
  public ESignDocument mapToEntity(ESignModel model) {
    ESignDocument entity = copyProperties(model, new ESignDocument());
    return entity;
  }

  @Override
	public ESignModel mapToModel(ESignDocument entity) {
		ESignModel model = copyProperties(entity, new ESignModel());
		return model;
	}

  @Override
	public Class<ESignDocument> supportsEntity() {
		return ESignDocument.class;
	}

	@Override
	public Class<ESignModel> supportsModel() {
		return ESignModel.class;
	}
}
