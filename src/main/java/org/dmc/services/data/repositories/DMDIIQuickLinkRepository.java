package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIDocument;
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
	@Query(value = "DELETE from dmdii_quick_link WHERE id = :id", nativeQuery = true)
	Integer deleteById(@Param("id") Integer id);

	Long countByDoc(DMDIIDocument dmdiiDocument);

	@Modifying
	@Query(value = "DELETE from dmdii_quick_link WHERE dmdii_document_id = :dmdiiDocumentId", nativeQuery = true)
	Integer deleteByDMDIIDocumentId(@Param("dmdiiDocumentId") Integer dmdiiDocumentId);
}
