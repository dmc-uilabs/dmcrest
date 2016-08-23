package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.BaseEntity;
import org.dmc.services.data.models.BaseModel;
import org.springframework.beans.BeanUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

class DefaultMapper<T extends BaseEntity, S extends BaseModel> implements Mapper {

	private Class<T> entityClass;
	private Class<S> modelClass;

	private static final Logger LOGGER = Logger.getLogger("Mapper");

	DefaultMapper(Class<T> entityClass, Class<S> modelClass) {
		this.entityClass = entityClass;
		this.modelClass = modelClass;
	}

	@Override
	public BaseEntity mapToEntity(BaseModel model) {
		BaseEntity entity = null;

		if (model != null) {
			try {
				entity = entityClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.log(Level.SEVERE, "Attempt to instantiate " + entityClass.getName()
						+ " in default mapper failed. Please ensure entity has default constructor");
				throw new MapperInstantiationError(e);
			}
			BeanUtils.copyProperties(model, entity);
		}
		return entity;
	}

	@Override
	public BaseModel mapToModel(BaseEntity entity) {
		BaseModel model = null;

		if (entity != null) {
			try {
				model = modelClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.log(Level.SEVERE, "Attempt to instantiate " + modelClass.getName()
						+ " in default mapper failed. Please ensure model has default constructor");
				throw new MapperInstantiationError(e);
			}
			BeanUtils.copyProperties(entity, model);
		}
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
	private static class MapperInstantiationError extends Error {
		MapperInstantiationError(Throwable t) {
			super(t);
		}
	}
}
