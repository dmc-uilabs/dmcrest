package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.ResourceLab;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ResourceLabRepository extends BaseRepository<ResourceLab, Integer> {
	
}
