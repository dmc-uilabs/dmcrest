package org.dmc.services.data.mappers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dmc.services.data.entities.BaseEntity;
import org.dmc.services.data.models.BaseModel;
import org.springframework.beans.BeanUtils;

public abstract class AbstractMapper<T extends BaseEntity, S extends BaseModel> implements Mapper<T, S> {

	@Inject
	protected MapperFactory mapperFactory;

	@PostConstruct
	private void init() {
		mapperFactory.registerMapper(this);
	}

	protected S copyProperties(T source, S destination) {
		if (source == null) {
			return null;
		} else {
			BeanUtils.copyProperties(source, destination);
			return destination;
		}
	}

	protected S copyProperties(T source, S destination, String[] ignoredProperties) {
		if (source == null) {
			return null;
		} else {
			BeanUtils.copyProperties(source, destination, ignoredProperties);
			return destination;
		}
	}

	protected T copyProperties(S source, T destination) {
		if (source == null) {
			return null;
		} else {
			BeanUtils.copyProperties(source, destination);
			return destination;
		}
	}

	protected T copyProperties(S source, T destination, String[] ignoredProperties) {
		if (source == null) {
			return null;
		} else {
			BeanUtils.copyProperties(source, destination, ignoredProperties);
			return destination;
		}
	}
}
