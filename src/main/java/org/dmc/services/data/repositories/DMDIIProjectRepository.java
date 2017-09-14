package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mysema.query.types.Predicate;

import java.util.Date;
import java.util.List;

public interface DMDIIProjectRepository extends BaseRepository<DMDIIProject, Integer> {

	@Query("SELECT p FROM DMDIIProject p WHERE p.primeOrganization.id = :primeOrganizationId AND p.isEvent = false")
	Page<DMDIIProject> findByPrimeOrganizationId(Pageable pageable, @Param("primeOrganizationId") Integer primeOrganizationId);

	@Query("SELECT COUNT (p) FROM DMDIIProject p WHERE p.primeOrganization.id = :primeOrganizationId AND p.isEvent = false")
	Long countByPrimeOrganizationId(@Param("primeOrganizationId") Integer primeOrganizationId);

	Page<DMDIIProject> findByAwardedDate(Pageable pageable, Date startDate);

	Long countByAwardedDate(Date startDate);

	Page<DMDIIProject> findByProjectStatusId(Pageable pageable, Integer statusId);

	Page<DMDIIProject> findByProjectTitleLikeIgnoreCase(Pageable pageable, String title);

	@Query("SELECT p FROM DMDIIProject p WHERE UPPER(p.projectTitle) LIKE UPPER(:searchTerm) OR CONCAT(p.rootNumber, '-', p.callNumber, '-', p.projectNumber) LIKE :searchTerm")
	Page<DMDIIProject> findByProjectTitleLikeIgnoreCaseOrProjectNumberContainsIgnoreCase(Pageable pageable, @Param("searchTerm") String searchTerm);

	@Query("SELECT COUNT(p) FROM DMDIIProject p WHERE UPPER(p.projectTitle) LIKE UPPER(:searchTerm) OR CONCAT(p.rootNumber, '-', p.callNumber, '-', p.projectNumber) LIKE :searchTerm")
	Long countByProjectTitleLikeIgnoreCaseOrProjectNumberContainsIgnoreCase(@Param("searchTerm") String searchTerm);

	Long countByProjectTitleLikeIgnoreCase(String title);

	@Query("SELECT p FROM DMDIIProject p WHERE p.primeOrganization.id = :dmdiiMemberId AND p.isEvent = false AND " +
			"CURRENT_TIMESTAMP() BETWEEN p.awardedDate AND p.endDate")
	Page<DMDIIProject> findByPrimeOrganizationIdAndIsActive(Pageable pageable, @Param("dmdiiMemberId") Integer dmdiiMemberId);

	@Query("SELECT COUNT (p) FROM DMDIIProject p WHERE p.primeOrganization.id = :dmdiiMemberId AND p.isEvent = false AND " +
			"CURRENT_TIMESTAMP() BETWEEN p.awardedDate AND p.endDate")
	Long countByPrimeOrganizationIdAndIsActive(@Param("dmdiiMemberId") Integer dmdiiMemberId);

	// @Query(value = "SELECT * FROM dmdii_project dp JOIN dmdii_project_contributing_company cc on " +
	// 		"cc.dmdii_project_id=dp.id WHERE cc.contributing_company_id = :dmdiiMemberId", nativeQuery = true)
	@Query(value = "SELECT dp FROM DMDIIProject dp WHERE EXISTS (select cc FROM dp.contributingCompanies cc WHERE cc.id = :dmdiiMemberId) AND dp.isEvent = false")
	List<DMDIIProject> findByContributingCompanyId(@Param("dmdiiMemberId") Integer dmdiiMemberId);

	// @Query(value = "SELECT * FROM dmdii_project dp JOIN dmdii_project_contributing_company cc on " +
	// 		"cc.dmdii_project_id=dp.id WHERE cc.contributing_company_id = :dmdiiMemberId AND "
	// 		+ "current_timestamp BETWEEN dp.awarded_date AND dp.end_date", nativeQuery = true)
	@Query(value = "SELECT dp FROM DMDIIProject dp WHERE EXISTS (select cc FROM dp.contributingCompanies cc WHERE cc.id = :dmdiiMemberId) AND dp.isEvent = false AND "
			+ "CURRENT_TIMESTAMP() BETWEEN dp.awardedDate AND dp.endDate")
	List<DMDIIProject> findActiveByContributingCompanyId(@Param("dmdiiMemberId") Integer dmdiiMemberId);
}
