package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DMDIIDocumentRepository extends BaseRepository<DMDIIDocument, Integer> {

	Page<DMDIIDocument> findByDmdiiProjectIdAndIsDeletedFalse(Pageable pageable, Integer dmdiiProjectId);

	Page<DMDIIDocument> findByDmdiiProjectId(Pageable pageable, Integer dmdiiProjectId);

	Page<DMDIIDocument> findByIsDeletedFalse(Pageable pageable);

	DMDIIDocument findTopByFileTypeOrderByModifiedDesc(Integer fileType);

	DMDIIDocument findTopByFileTypeAndDmdiiProjectIdOrderByModifiedDesc(Integer fileTypeId, Integer dmdiiProjectId);
	
	@Query("SELECT d from DMDIIDocument d WHERE d.id = :dmdiiDocumentId")
	DMDIIDocument findOne (@Param("dmdiiDocumentId") Integer dmdiiDocumentId);

}
