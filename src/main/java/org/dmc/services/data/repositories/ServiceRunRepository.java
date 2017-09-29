package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.ServiceRun;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceRunRepository extends BaseRepository<ServiceRun, Integer> {
	
	public Page<ServiceRun> findByServiceIdIn(List<Integer> serviceIdList, Pageable page);

}
