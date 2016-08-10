package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends BaseRepository<User, Integer> {
	
	User findFirstByUsername(String username);

	@Query("SELECT u FROM OrganizationUser ou JOIN ou.user u JOIN ou.organization o"
			+ " WHERE o.id = :organizationId")
	List<User> findByOrganizationUserOrganizationId(@Param("organizationId") Integer organizationId);


}
