package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIProjectNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DMDIIProjectNewsRepository extends BaseRepository<DMDIIProjectNews, Integer> {

	Page<DMDIIProjectNews> findAllByOrderByDateCreatedDesc(Pageable page);

}
