package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIQuickLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface DMDIIQuickLinkRepository extends BaseRepository<DMDIIQuickLink, Integer> {

	//@Query("SELECT * FROM DMDIIQuickLink ql ORDER BY ql.created DESC")
	Page<DMDIIQuickLink> findAllByOrderByCreatedDesc(Pageable page);
}
