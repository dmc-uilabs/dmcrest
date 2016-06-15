package org.dmc.services.data.repositories;

import java.util.Date;
import java.util.List;

import org.dmc.services.data.entities.DMDIIProject;

public interface DMDIIProjectRepository extends BaseRepository<DMDIIProject, Integer> {

	List<DMDIIProject> findByPrimeOrganizationId(Integer primeOrganizationId);

	List<DMDIIProject> findByAwardedDate(Date startDate);

	List<DMDIIProject> findByProjectStatusId(Integer projectStatusId);
}
