package org.dmc.services.partnerdmdiiprojects;

import java.util.List;

import org.dmc.services.entities.DMDIIProject;

public interface DMDIIProjectRepository extends BaseRepository<DMDIIProject, Integer> {

	List<DMDIIProject> findByPrimeOrganizationId(Integer primeOrganizationId);
}
