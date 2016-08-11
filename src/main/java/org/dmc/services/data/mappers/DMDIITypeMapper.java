package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIIType;
import org.dmc.services.data.entities.DMDIITypeCategory;
import org.dmc.services.data.models.DMDIITypeCategoryModel;
import org.dmc.services.data.models.DMDIITypeModel;
import org.springframework.stereotype.Component;

@Component
public class DMDIITypeMapper extends AbstractMapper<DMDIIType, DMDIITypeModel> {

	@Override
	public DMDIIType mapToEntity(DMDIITypeModel model) {
		if (model == null) return null;
		DMDIIType entity = copyProperties(model, new DMDIIType());
		Mapper<DMDIITypeCategory, DMDIITypeCategoryModel> categoryMapper = getCategoryMapper();
		entity.setDmdiiTypeCategory(categoryMapper.mapToEntity(model.getDmdiiTypeCategory()));
		return entity;
	}

	@Override
	public DMDIITypeModel mapToModel(DMDIIType entity) {
		if (entity == null) return null;
		DMDIITypeModel model = copyProperties(entity, new DMDIITypeModel());
		Mapper<DMDIITypeCategory, DMDIITypeCategoryModel> categoryMapper = getCategoryMapper();
		model.setDmdiiTypeCategory(categoryMapper.mapToModel(entity.getDmdiiTypeCategory()));
		return model;
	}

	@Override
	public Class<DMDIIType> supportsEntity() {
		return DMDIIType.class;
	}

	@Override
	public Class<DMDIITypeModel> supportsModel() {
		return DMDIITypeModel.class;
	}

	private Mapper<DMDIITypeCategory, DMDIITypeCategoryModel> getCategoryMapper() {
		return mapperFactory.mapperFor(DMDIITypeCategory.class, DMDIITypeCategoryModel.class);
	}

}
