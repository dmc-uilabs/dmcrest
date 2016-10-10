package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.OrganizationAuthorizedIdp;

public interface OrganizationAuthorizedIdpRepository extends BaseRepository<OrganizationAuthorizedIdp, Integer> {

	OrganizationAuthorizedIdp findByIdpDomain(String idpDomain);
}
