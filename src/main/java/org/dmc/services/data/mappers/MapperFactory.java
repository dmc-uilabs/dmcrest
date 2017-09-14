package org.dmc.services.data.mappers;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.dmc.services.data.entities.BaseEntity;
import org.dmc.services.data.models.BaseModel;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.dmc.services.ServiceLogger;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MapperFactory<T extends BaseEntity, S extends BaseModel> {

	@Inject
	private ApplicationContext applicationContext;

	private static final Logger LOGGER = Logger.getLogger("Mapper");

	private Table<Class<T>, Class<S>, Class<? extends Mapper>> mapperTable = HashBasedTable.create();

	public void registerMapper(Mapper<T, S> mapper) {
		if (mapperTable.contains(mapper.supportsEntity(), mapper.supportsModel())) {
			String msg = "Duplicate mapper registration for " + mapper.supportsEntity().getName() + " and " + mapper
					.supportsModel().getName();
			LOGGER.log(Level.SEVERE, msg);
			throw new MapperRegistrationError(msg);
		}
		mapperTable.put(mapper.supportsEntity(), mapper.supportsModel(), mapper.getClass());
	}

	public Mapper<T, S> mapperFor(Class<T> entityClass, Class<S> modelClass) {
		Mapper<T, S> mapper = null;
		Class<? extends Mapper> mapperClass = mapperTable.get(entityClass, modelClass);

		if (mapperClass != null) {
			mapper = applicationContext.getBean(mapperClass);
		}

		if (mapper == null) {
			//LOGGER.info("No mapper found for entity " + entityClass.getName() + " and model " + modelClass.getName()
			//		+ ". Returning default mapper.");
			mapper = new DefaultMapper<>(entityClass, modelClass);
		}

		return mapper;
	}

	@SuppressWarnings("serial")
	public static class MapperRegistrationError extends Error {
		MapperRegistrationError(String msg) {
			super(msg);
		}
	}
}
