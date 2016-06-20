package org.dmc.services.dmdiimember;

import org.dmc.services.data.entities.DMDIIMember;
import org.dmc.services.data.repositories.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface DMDIIMemberDao extends BaseRepository<DMDIIMember, Integer> {

	Page<DMDIIMember> findByDmdiiTypeDmdiiTypeCategoryIdAndDmdiiTypeTier(Pageable pageable, Integer dmdiiTypeCategoryId, Integer dmdiiTypeTier);
	
	Page<DMDIIMember> findByDmdiiTypeDmdiiTypeCategoryId(Pageable pageable, Integer dmdiiTypeCategoryId);
	
	Page<DMDIIMember> findByDmdiiTypeTier(Pageable pageable, Integer dmdiiTypeTier);
	
	@Query("SELECT m FROM DMDIIProject p JOIN p.primeOrganization m "
			+ "WHERE CURRENT_TIMESTAMP() BETWEEN p.awardedDate AND p.endDate")
	Page<DMDIIMember> findByHasActiveProjects(Pageable pageable);
	
	Page<DMDIIMember> findByOrganizationNameLikeIgnoreCase(Pageable pageable, String name);

}
