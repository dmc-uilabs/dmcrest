package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DMDIIProjectRepository extends BaseRepository<DMDIIProject, Integer> {

	Page<DMDIIProject> findByPrimeOrganizationId(Pageable pageable, Integer primeOrganizationId);

	Long countByPrimeOrganizationId(Integer primeOrganizationId);

	Page<DMDIIProject> findByAwardedDate(Pageable pageable, Date startDate);

	Long countByAwardedDate(Date startDate);

	Page<DMDIIProject> findByProjectStatusId(Pageable pageable, Integer statusId);

	Page<DMDIIProject> findByProjectTitleLikeIgnoreCase(Pageable pageable, String title);

	@Query("SELECT p FROM DMDIIProject p WHERE UPPER(p.projectTitle) LIKE UPPER(:searchTerm) OR CONCAT(LPAD(p.rootNumber::text, 2, '0'), '-', LPAD(p.callNumber::text, 2, '0'), '-', LPAD(p.projectNumber::text, 2, '0') LIKE :searchTerm")
	Page<DMDIIProject> findByProjectTitleLikeIgnoreCaseOrProjectNumberContainsIgnoreCase(Pageable pageable, @Param("searchTerm") String searchTerm);

	@Query("SELECT COUNT(p) FROM DMDIIProject p WHERE UPPER(p.projectTitle) LIKE UPPER(:searchTerm) OR CONCAT(p.rootNumber, '-', p.callNumber, '-', p.projectNumber) LIKE :searchTerm")
	Long countByProjectTitleLikeIgnoreCaseOrProjectNumberContainsIgnoreCase(@Param("searchTerm") String searchTerm);

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

	@Query(value = "SELECT * FROM dmdii_project dp JOIN dmdii_project_contributing_company cc on " +
			"cc.dmdii_project_id=dp.id WHERE cc.contributing_company_id = :dmdiiMemberId AND "
			+ "current_timestamp BETWEEN dp.awarded_date AND dp.end_date", nativeQuery = true)
	List<DMDIIProject> findActiveByContributingCompanyId(@Param("dmdiiMemberId") Integer dmdiiMemberId);
}
