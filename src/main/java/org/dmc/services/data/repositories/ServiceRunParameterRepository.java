package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.ServiceRunParameter;

public interface ServiceRunParameterRepository extends BaseRepository<ServiceRunParameter, Integer> {
	
	public List<ServiceRunParameter> findByRunId(Integer runId);

}
