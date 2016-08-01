package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIProjectUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DMDIIProjectUpdateRepository extends BaseRepository<DMDIIProjectUpdate, Integer>{

	Page<DMDIIProjectUpdate> findByProjectIdOrderByDateDesc(Pageable pageable, Integer projectId);

}
