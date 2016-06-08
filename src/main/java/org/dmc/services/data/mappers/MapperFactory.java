package org.dmc.services.data.mappers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.dmc.services.data.entities.BaseEntity;
import org.dmc.services.data.models.BaseModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

@Component
public class MapperFactory {

	private static final Logger LOGGER = Logger.getLogger("Mapper");

	private Table<Class<? extends BaseEntity>, Class<? extends BaseModel>, Mapper<? extends BaseEntity, ? extends BaseModel>> mapperTable = HashBasedTable.create();
	
	public void registerMapper(Mapper<? extends BaseEntity, ? extends BaseModel> mapper) {
		if (mapperTable.contains(mapper.supportsEntity(), mapper.supportsModel())) {
			String msg = "Duplicate mapper registration for " + mapper.supportsEntity().getName() + " and " + mapper.supportsModel().getName();
			LOGGER.log(Level.SEVERE, msg);
			throw new MapperRegistrationException(msg);
		}
		mapperTable.put(mapper.supportsEntity(), mapper.supportsModel(), mapper);
	}

	@SuppressWarnings("rawtypes")
	public Mapper mapperFor(Class<? extends BaseEntity> entityClass, Class<? extends BaseModel> modelClass) {
		Mapper<? extends BaseEntity, ? extends BaseModel> mapper = mapperTable.get(entityClass, modelClass);
		if (mapper != null) {
			return mapper;
		} else {
			LOGGER.info("No mapper found for entity " + entityClass.getName() + " and model " + modelClass.getName() + ". Returning default mapper.");
			return new Mapper<BaseEntity, BaseModel>() {

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

				@SuppressWarnings("unchecked")
				@Override
				public Class supportsEntity() {
					return entityClass;
				}

				@SuppressWarnings("unchecked")
				@Override
				public Class supportsModel() {
					return modelClass;
				}
				
			};
		}
	}
	
	@SuppressWarnings("serial")
	public class MapperInstantiationException extends Error {
		
		public MapperInstantiationException(Throwable t) {
			super(t);
		}
		
	}
	
	@SuppressWarnings("serial")
	public class MapperRegistrationException extends Error {
		
		public MapperRegistrationException(String msg) {
			super(msg);
		}
	}
}
