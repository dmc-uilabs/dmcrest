package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.Organization;

public interface OrganizationRepository extends BaseRepository<Organization, Integer> {

	Organization findOne(Integer id);

	Organization save(Organization organization);
}
