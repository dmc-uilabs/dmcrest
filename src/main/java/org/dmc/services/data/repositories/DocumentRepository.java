package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentRepository extends BaseRepository<Document, Integer> {

	List<Document> findByOrganizationIdAndIsDeletedFalse(Integer organizationId);
	
	Document findTopByFileTypeOrderByModifiedDesc(Integer fileType);
	
	Document findTopByOrganizationIdAndFileTypeOrderByModifiedDesc(Integer organizationId, Integer fileType);
	
	Page<Document> findByOrganizationIdAndFileTypeOrderByModifiedDesc(Integer organizationId, Integer fileType, Pageable page);
	
	Long countByOrganizationIdAndFileType(Integer organizationId, Integer fileType);
}
