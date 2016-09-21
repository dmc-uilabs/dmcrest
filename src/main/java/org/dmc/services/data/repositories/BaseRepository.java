package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;


@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, S extends Serializable> extends JpaRepository<T, S>, QueryDslPredicateExecutor<T> {

}
