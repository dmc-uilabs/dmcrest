package org.dmc.services.partnerdmdiiprojects;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
	interface BaseRepository<T extends BaseEntity, S extends Serializable> extends JpaRepository<T, S> {

		/**
		 * Finds all entities of the given type that are active.
		 *
		 * @return the entities
		 */
		List<T> findByIsActiveIsTrue();
		T findByIdAndIsActiveIsTrue(int id);
}
