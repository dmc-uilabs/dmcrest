package org.dmc.services.partnerdmdiiprojects;

import java.util.List;

import org.dmc.services.partnerdmdiiprojects.DMDIIProjectEntity;

public interface DMDIIProjectRepository extends BaseRepository<DMDIIProjectEntity, Integer> {

	List<DMDIIProjectEntity> findByPrimeOrganizationId(Integer primeOrganizationId);
}
