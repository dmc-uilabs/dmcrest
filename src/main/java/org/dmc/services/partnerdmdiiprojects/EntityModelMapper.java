package org.dmc.services.partnerdmdiiprojects;

import org.dmc.services.partnerdmdiiprojects.BaseEntity;
import org.dmc.services.partnerdmdiiprojects.BaseModel;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface EntityModelMapper<E extends BaseEntity, M extends BaseModel> {

	/**
	 * Converts the given entity to the corresponding model object.
	 * @param entity the entity to convert
	 * @return the model created
	 */
	M entityToModel(E entity);

	/**
	 * Converts the given model to the corresponding entity object.
	 * @param model the model to convert
	 * @return the entity created
	 */
	E modelToEntity(M model);

	/**
	 * Converts a collection of entities into a collection of models.
	 *
	 * @param entities that parallel desired models
	 * @return the models that parallel given entities
	 */
	default List<M> entitiesToModels(Collection<E> entities) {
		return entities.stream().map(entity -> entityToModel(entity)).collect(Collectors.toList());
	}

	/**
	 * Converts a collection of models into a collection of entities.
	 *
	 * @param models that parallel desired entities
	 * @return the entities that parallel given models
	 */
	default List<E> modelsToEntities(Collection<M> models) {
		return models.stream().map(model -> modelToEntity(model)).collect(Collectors.toList());
	}
}

