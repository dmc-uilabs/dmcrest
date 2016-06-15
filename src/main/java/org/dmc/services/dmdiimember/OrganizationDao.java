package org.dmc.services.dmdiimember;

import org.dmc.services.data.entities.Organization;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationDao extends CrudRepository<Organization, Integer> {

	Organization findOne(Integer id);

	Organization save(Organization organization);
}
