package org.dmc.services.data.mappers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.dmc.services.data.entities.BaseEntity;
import org.dmc.services.data.models.BaseModel;

public interface Mapper<T extends BaseEntity, S extends BaseModel> {

	T mapToEntity(S model);
	
	default List<T> mapToEntity(Collection<S> models) {
		if (models == null) return null;
		return models.stream()
				.map((n) -> mapToEntity(n))
				.collect(Collectors.toList());
	}
	
	S mapToModel(T entity);
	
	default List<S> mapToModel(Collection<T> entities) {
		if (entities == null) return null;
		return entities.stream()
				.map((n) -> mapToModel(n))
				.collect(Collectors.toList());
	}
	
	Class<T> supportsEntity();
	
	Class<S> supportsModel();
	
}
