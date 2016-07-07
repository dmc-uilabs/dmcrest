package org.dmc.services.data.mappers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.dmc.services.data.entities.BaseEntity;
import org.dmc.services.data.entities.SecuredEntity;
import org.dmc.services.data.models.BaseModel;
import org.dmc.services.security.MapperFieldSecurityAspect;
import org.springframework.beans.BeanUtils;

public class DefaultMapper implements Mapper<BaseEntity, BaseModel> {
	
	private Class<? extends BaseEntity> entityClass;
	private Class<? extends BaseModel> modelClass;
	private MapperFieldSecurityAspect fieldSecurity = new MapperFieldSecurityAspect();
	
	private static final Logger LOGGER = Logger.getLogger("Mapper");
	
	public DefaultMapper(Class<? extends BaseEntity> entityClass, Class<? extends BaseModel> modelClass) {
		this.entityClass = entityClass;
		this.modelClass = modelClass;
	}

	@Override
	public BaseEntity mapToEntity(BaseModel model) {
		if (model == null) return null;
		BaseEntity entity = null;
		try {
			entity = entityClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, "Attempt to instantiate " + entityClass.getName() + " in default mapper failed. Please ensure entity has default constructor");
			throw new MapperInstantiationException(e);
		}
		BeanUtils.copyProperties(model, entity);
		return entity;
	}

	@Override
	public BaseModel mapToModel(BaseEntity entity) {
		if (entity == null) return null;
		
		// DefaultMapper is not a spring managed bean, so we cannot use AoP to nullify protected fields
		// We have to do so manually here
		if (SecuredEntity.class.isAssignableFrom(entity.getClass())) {
			try {
				fieldSecurity.nullifyProtectedFields((SecuredEntity) entity);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				LOGGER.log(Level.SEVERE, "Exception in DefaultMapper attempting to map " + entity.getClass() + " to " + modelClass, e);
				return null;
			} 
		}
		
		BaseModel model = null;
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, "Attempt to instantiate " + modelClass.getName() + " in default mapper failed. Please ensure model has default constructor");
			throw new MapperInstantiationException(e);
		}
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public Class supportsEntity() {
		return entityClass;
	}

	@Override
	public Class supportsModel() {
		return modelClass;
	}
	
	@SuppressWarnings("serial")
	public class MapperInstantiationException extends Error {
		
		public MapperInstantiationException(Throwable t) {
			super(t);
		}
		
	}

}
