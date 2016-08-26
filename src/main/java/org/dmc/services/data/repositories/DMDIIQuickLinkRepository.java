package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIQuickLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DMDIIQuickLinkRepository extends BaseRepository<DMDIIQuickLink, Integer> {

	//@Query("SELECT * FROM DMDIIQuickLink ql ORDER BY ql.created DESC")
	Page<DMDIIQuickLink> findAllByOrderByCreatedDesc(Pageable page);
	
	@Modifying
	@Query(value="DELETE from dmdii_quick_link WHERE id = :id", nativeQuery = true)
	Integer deleteById(@Param("id") Integer id);
}
