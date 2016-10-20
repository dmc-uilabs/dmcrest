package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.DomeServer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DomeServerRepository extends BaseRepository<DomeServer, Integer> {
	
	public DomeServer findById(Integer id);
	
	@Query(value = "Select distinct"
			+ "   s.server_id, s.url, s.alias, s.user_id, s.port, s.status"
			+ "     , s.local_dome_user, s.dome_user_space, s.local_dome_user_password "
			+ " From Servers as s"
			+ " 	Left Join server_in_server_access_group ssa on s.server_id = ssa.server_id"
			+ " 	Left Join user_in_server_access_group usa on ssa.server_access_group_id = usa.server_access_group_id" 
			+ " where usa.user_id = :id or s.user_id = :id"
			+ " order by s.server_id", nativeQuery = true)
	public List<DomeServer> findAllServers(@Param("id") Integer id);

}
