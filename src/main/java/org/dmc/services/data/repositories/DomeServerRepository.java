package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DomeServer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DomeServerRepository extends BaseRepository<DomeServer, Integer> {
	
	public DomeServer findById(Integer id);
	
	@Query("Select distinct s From DomeServer s"
			+ " 	Left Join s.accessList sal"
			+ " 	Left Join sal.users u" 
			+ " where u.id = :id or s.userId = :id")
	public Page<DomeServer> findAllServers(@Param("id") Integer id, Pageable page);
	
	@Query("SELECT serverURL FROM DomeServer s"
			+ " WHERE s.id = :id")
	public String getServerURLById(@Param("id") Integer id);

}
