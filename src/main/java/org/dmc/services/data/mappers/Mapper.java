package org.dmc.services.data.mappers;

import org.dmc.services.data.entities.BaseEntity;
import org.dmc.services.data.models.BaseModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Mapper<T extends BaseEntity, S extends BaseModel> {

	T mapToEntity(S model);

	default List<T> mapToEntity(Collection<S> models) {
		List<T> entities;
		if (models == null) {
			entities = new ArrayList<>();
		} else {
			entities = models.stream().map(model -> mapToEntity(model)).filter(entity -> entity != null)
					.collect(Collectors.toList());
		}
		return entities;
	}

	S mapToModel(T entity);

	default List<S> mapToModel(Collection<T> entities) {
		List<S> models;
		if (entities == null) {
			models = new ArrayList<>();
		} else {
			models = entities.stream().map(entity -> mapToModel(entity)).filter(model -> model != null)
					.collect(Collectors.toList());
		}
		return models;
	}

	Class<T> supportsEntity();

	Class<S> supportsModel();
}
