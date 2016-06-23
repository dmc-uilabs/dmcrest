package org.dmc.services.dmdiimember;

import java.util.List;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.repositories.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DMDIIMemberDao extends BaseRepository<DMDIIMember, Integer> {

	Page<DMDIIMember> findByDmdiiTypeDmdiiTypeCategoryIdAndDmdiiTypeTier(Pageable pageable, Integer dmdiiTypeCategoryId, Integer dmdiiTypeTier);
	
	Page<DMDIIMember> findByDmdiiTypeDmdiiTypeCategoryId(Pageable pageable, Integer dmdiiTypeCategoryId);
	
	Page<DMDIIMember> findByDmdiiTypeTier(Pageable pageable, Integer dmdiiTypeTier);
	
	@Query("SELECT m FROM DMDIIProject p JOIN p.primeOrganization m "
			+ "WHERE CURRENT_TIMESTAMP() BETWEEN p.awardedDate AND p.endDate")
	Page<DMDIIMember> findByHasActiveProjects(Pageable pageable);
	
	Page<DMDIIMember> findByOrganizationNameLikeIgnoreCase(Pageable pageable, String name);
	
	Long countByOrganizationNameLikeIgnoreCase(String name);

	@Query(value = "SELECT * FROM organization_dmdii_member dm JOIN dmdii_project_contributing_company dpcc on " +
					"dm.id=dpcc.contributing_company_id WHERE dpcc.dmdii_project_id = :projectId", nativeQuery = true)
	List<DMDIIMember> findByDMDIIProjectContributingCompanyDMDIIProject(@Param("projectId") Integer projectId);

}
