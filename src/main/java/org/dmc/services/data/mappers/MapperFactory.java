package org.dmc.services.data.mappers;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.dmc.services.data.entities.BaseEntity;
import org.dmc.services.data.models.BaseModel;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

@Component
public class MapperFactory {
	
	@Inject
	private ApplicationContext applicationContext;

	private static final Logger LOGGER = Logger.getLogger("Mapper");

	private Table<Class<? extends BaseEntity>, Class<? extends BaseModel>, Class<? extends Mapper>> mapperTable = HashBasedTable.create();
	
	public void registerMapper(Mapper<? extends BaseEntity, ? extends BaseModel> mapper) {
		if (mapperTable.contains(mapper.supportsEntity(), mapper.supportsModel())) {
			String msg = "Duplicate mapper registration for " + mapper.supportsEntity().getName() + " and " + mapper.supportsModel().getName();
			LOGGER.log(Level.SEVERE, msg);
			throw new MapperRegistrationException(msg);
		}
		mapperTable.put(mapper.supportsEntity(), mapper.supportsModel(), mapper.getClass());
	}

	@SuppressWarnings("rawtypes")
	public Mapper mapperFor(Class<? extends BaseEntity> entityClass, Class<? extends BaseModel> modelClass) {
		Mapper mapper = null;
		Class<? extends Mapper> mapperClass = mapperTable.get(entityClass, modelClass);
		
		if (mapperClass != null) {
			mapper = applicationContext.getBean(mapperClass);
		}
		
		if (mapper == null) {
			LOGGER.info("No mapper found for entity " + entityClass.getName() + " and model " + modelClass.getName() + ". Returning default mapper.");
			mapper = new DefaultMapper(entityClass, modelClass);
		}
		
		return mapper;
	}
	
	
	
	@SuppressWarnings("serial")
	public class MapperRegistrationException extends Error {
		
		public MapperRegistrationException(String msg) {
			super(msg);
		}
	}
}
