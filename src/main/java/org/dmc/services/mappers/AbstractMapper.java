package org.dmc.services.mappers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dmc.services.entities.BaseEntity;
import org.dmc.services.models.BaseModel;

public abstract class AbstractMapper<T extends BaseEntity, S extends BaseModel> implements Mapper<T, S> {

	@Inject
	protected MapperFactory mapperFactory;
	
	@PostConstruct
	private void init() {
		mapperFactory.registerMapper(this);
	}
	
}
