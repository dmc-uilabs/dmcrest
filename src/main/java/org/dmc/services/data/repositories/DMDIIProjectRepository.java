package org.dmc.services.data.repositories;

import java.util.Date;

import org.dmc.services.data.entities.DMDIIProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DMDIIProjectRepository extends BaseRepository<DMDIIProject, Integer> {

	Page<DMDIIProject> findByPrimeOrganizationId(Pageable pageable, Integer primeOrganizationId);

	Page<DMDIIProject> findByAwardedDate(Pageable pageable, Date startDate);

	Page<DMDIIProject> findByProjectStatusId(Pageable pageable, Integer projectStatusId);
	
	Page<DMDIIProject> findByProjectTitleLikeIgnoreCase(Pageable pageable, String title);
}
