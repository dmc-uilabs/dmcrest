package org.dmc.services.data.repositories;

import java.util.Date;
import java.util.List;

import org.dmc.services.data.entities.DMDIIProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DMDIIProjectRepository extends BaseRepository<DMDIIProject, Integer> {

	Page<DMDIIProject> findByPrimeOrganizationId(Pageable pageable, Integer primeOrganizationId);
	
	Long countByPrimeOrganizationId(Integer primeOrganizationId);

	Page<DMDIIProject> findByAwardedDate(Pageable pageable, Date startDate);
	
	Long countByAwardedDate(Date startDate);

	Page<DMDIIProject> findByProjectStatusId(Pageable pageable, Integer statusId);
	
	Page<DMDIIProject> findByProjectTitleLikeIgnoreCase(Pageable pageable, String title);
	
	Long countByProjectTitleLikeIgnoreCase(String title);

	@Query("SELECT p FROM DMDIIProject p WHERE p.primeOrganization.id = :dmdiiMemberId AND " +
			"CURRENT_TIMESTAMP() BETWEEN p.awardedDate AND p.endDate")
	Page<DMDIIProject> findByPrimeOrganizationIdAndIsActive(Pageable pageable, @Param("dmdiiMemberId") Integer dmdiiMemberId);

	@Query("SELECT COUNT (p) FROM DMDIIProject p WHERE p.primeOrganization.id = :dmdiiMemberId AND " +
			"CURRENT_TIMESTAMP() BETWEEN p.awardedDate AND p.endDate")
	Long countByPrimeOrganizationIdAndIsActive(@Param("dmdiiMemberId") Integer dmdiiMemberId);

	@Query(value = "SELECT * FROM dmdii_project dp JOIN dmdii_project_contributing_company cc on " +
			"cc.dmdii_project_id=dp.id WHERE cc.contributing_company_id = :dmdiiMemberId", nativeQuery = true)
	List<DMDIIProject> findByContributingCompanyId(@Param("dmdiiMemberId") Integer dmdiiMemberId);
}
