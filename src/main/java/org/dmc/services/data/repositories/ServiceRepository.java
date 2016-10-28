package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.ServiceEntity;

public interface ServiceRepository extends BaseRepository<ServiceEntity, Integer>{

	List<ServiceEntity> findByProjectId(Integer projectId);

}
