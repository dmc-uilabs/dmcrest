package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.DMDIITypeCategory;
import org.dmc.services.data.models.DMDIITypeCategoryModel;
import org.dmc.services.data.models.DMDIITypeModel;
import org.dmc.services.dmdiitype.DMDIIType;

public class DMDIITypeMapper extends AbstractMapper<DMDIIType, DMDIITypeModel> {

	@Override
	public DMDIIType mapToEntity(DMDIITypeModel model) {
		DMDIIType entity = copyProperties(model, new DMDIIType());
		Mapper<DMDIITypeCategory, DMDIITypeCategoryModel> categoryMapper = getCategoryMapper();
		entity.setDmdiiTypeCategory(categoryMapper.mapToEntity(model.getDmdiiTypeCategory()));
		return entity;
	}

	@Override
	public DMDIITypeModel mapToModel(DMDIIType entity) {
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
