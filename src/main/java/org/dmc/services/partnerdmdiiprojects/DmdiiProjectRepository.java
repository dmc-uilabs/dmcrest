package org.dmc.services.partnerdmdiiprojects;

import java.util.List;

import org.dmc.services.partnerdmdiiprojects.DmdiiProjectEntity;

public interface DmdiiProjectRepository extends BaseRepository<DmdiiProjectEntity, Integer> {

	List<DmdiiProjectEntity> findByPrimeOrganizationId(Integer primeOrganizationId);
}
