package org.dmc.services.data.repositories;

import com.mysema.query.types.Predicate;
import org.dmc.services.data.entities.Organization;

import java.util.List;

public interface OrganizationRepository extends BaseRepository<Organization, Integer> {
	List<Organization> findAll(Predicate predicate);
}
