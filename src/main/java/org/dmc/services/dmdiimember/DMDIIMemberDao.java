package org.dmc.services.dmdiimember;

import org.dmc.services.data.entities.DMDIIMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface DMDIIMemberDao extends CrudRepository<DMDIIMember, Integer> {

	Page<DMDIIMember> findAll(Pageable pageable);

	DMDIIMember findOne(Integer id);

	Page<DMDIIMember> findByDmdiiTypeId(Pageable pageable, Integer dmdiiTypeId);

	DMDIIMember save(DMDIIMember member);

}
