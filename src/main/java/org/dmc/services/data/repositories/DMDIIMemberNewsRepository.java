package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIMemberNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DMDIIMemberNewsRepository extends BaseRepository<DMDIIMemberNews, Integer> {
	
	Page<DMDIIMemberNews> findAllByOrderByDateCreatedDesc(Pageable page);

}
