package org.dmc.services.data.repositories;

import java.io.Serializable;
import java.util.List;

import org.dmc.services.data.entities.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, S extends Serializable> extends JpaRepository<T, S> {


}
