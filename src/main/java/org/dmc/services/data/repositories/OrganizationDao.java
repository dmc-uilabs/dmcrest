package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.Organization;

import com.mysema.query.types.Predicate;

public interface OrganizationDao extends BaseRepository<Organization, Integer> {

	Organization findOne(Integer id);

	Organization save(Organization organization);
	
	List<Organization> findAll(Predicate predicate);
}
