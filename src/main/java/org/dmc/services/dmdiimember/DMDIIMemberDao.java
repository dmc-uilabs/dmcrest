package org.dmc.services.dmdiimember;

import java.util.Collection;
import java.util.List;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.repositories.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DMDIIMemberDao extends BaseRepository<DMDIIMember, Integer> {
	
	@Query("SELECT m.organization.id FROM DMDIIMember m "
			+ "WHERE m.dmdiiType.tier in (1,2) "
			+ "AND m.dmdiiType.dmdiiTypeCategory.category in ('Industry','Academic')")
	Collection<Integer> findTier1And2IndustryAndAcademicMemberOrganizationIds();

	@Query("SELECT m FROM DMDIIProject p JOIN p.primeOrganization m "
			+ "WHERE CURRENT_TIMESTAMP() BETWEEN p.awardedDate AND p.endDate")
	Page<DMDIIMember> findByHasActiveProjects(Pageable pageable);
	
	Page<DMDIIMember> findByOrganizationNameLikeIgnoreCase(Pageable pageable, String name);
	
	Long countByOrganizationNameLikeIgnoreCase(String name);

	@Query(value = "SELECT * FROM organization_dmdii_member dm JOIN dmdii_project_contributing_company dpcc on " +
					"dm.id=dpcc.contributing_company_id WHERE dpcc.dmdii_project_id = :projectId", nativeQuery = true)
	List<DMDIIMember> findByDMDIIProjectContributingCompanyDMDIIProject(@Param("projectId") Integer projectId);
	
	@Query("SELECT CASE WHEN COUNT(d) > 0 THEN 'true' ELSE 'false' END FROM DMDIIMember d where d.organization.id = :organizationId")
	Boolean existsByOrganizationId(@Param("organizationId") Integer organizationId);

}
