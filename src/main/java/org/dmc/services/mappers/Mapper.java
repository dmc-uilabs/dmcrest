package org.dmc.services.mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.dmc.services.entities.BaseEntity;
import org.dmc.services.models.BaseModel;

public interface Mapper<T extends BaseEntity, S extends BaseModel> {

	T mapToEntity(S model);
	
	default Collection<T> mapToEntity(Collection<S> models) {
		return models.stream()
				.map((n) -> mapToEntity(n))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	S mapToModel(T entity);
	
	default Collection<S> mapToModel(Collection<T> entities) {
		return entities.stream()
				.map((n) -> mapToModel(n))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	Class<T> supportsEntity();
	
	Class<S> supportsModel();
	
}
