package org.dmc.services.repository;

import java.util.Date;
import java.util.List;

import org.dmc.services.entities.DMDIIProject;

public interface DMDIIProjectRepository extends BaseRepository<DMDIIProject, Integer> {

	List<DMDIIProject> findByPrimeOrganizationId(Integer primeOrganizationId);

	List<DMDIIProject> findByStartDate(Date startDate);
}
