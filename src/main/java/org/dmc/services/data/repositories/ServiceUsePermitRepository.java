package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.ServiceUsePermit;

public interface ServiceUsePermitRepository extends BaseRepository<ServiceUsePermit, Integer> {

	List<ServiceUsePermit> findByOrganizationId(Integer orgId);
	
	List<ServiceUsePermit> findByServiceId(Integer serviceId);
	
	ServiceUsePermit findByOrganizationIdAndServiceId(Integer orgId, Integer serviceId);
	
	ServiceUsePermit findByUserIdAndServiceId(Integer userId, Integer serviceId);
}
