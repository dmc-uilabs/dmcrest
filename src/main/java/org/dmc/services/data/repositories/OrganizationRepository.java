package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.Organization;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRepository extends CrudRepository<Organization, Integer> {

	Organization findOne(Integer id);

	Organization save(Organization organization);
}
