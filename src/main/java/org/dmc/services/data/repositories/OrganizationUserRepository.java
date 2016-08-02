package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.OrganizationUser;

public interface OrganizationUserRepository extends BaseRepository<OrganizationUser, Integer> {

	List<OrganizationUser> findByOrganizationId (Integer organizationId);
}
