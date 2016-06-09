package org.dmc.services.dmdiimember;

import org.dmc.services.data.entities.DMDIIMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface DMDIIMemberDao extends CrudRepository<DMDIIMember, Integer> {

	Page<DMDIIMember> findAll(Pageable pageable);

	DMDIIMember findOne(Integer id);

	Page<DMDIIMember> findByDmdiiTypeId(Pageable pageable, Integer dmdiiTypeId);
	
	Page<DMDIIMember> findByDmdiiTypeDmdiiTypeCategoryId(Pageable pageable, Integer dmdiiTypeCategoryId);
	
	Page<DMDIIMember> findByDmdiiTypeTier(Pageable pageable, Integer dmdiiTypeTier);

	DMDIIMember save(DMDIIMember member);

}
