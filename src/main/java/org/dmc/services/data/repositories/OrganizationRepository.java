package org.dmc.services.data.repositories;

import com.mysema.query.types.Predicate;
import org.dmc.services.data.entities.Organization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganizationRepository extends BaseRepository<Organization, Integer> {

	List<Organization> findAll(Predicate predicate);
	
	@Query(value = "SELECT * FROM organization WHERE organization_id = ?1", nativeQuery = true)
	Organization findDeleted(Integer id);
}
