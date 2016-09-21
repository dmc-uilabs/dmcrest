package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentRepository extends BaseRepository<Document, Integer> {

	@Query("SELECT d FROM Document d JOIN Organization o ON d.parentId = o.organization_id WHERE o.organization_id = :organizationId")
	List<Document> findByOrganizationId(@Param("organizationId") Integer organizationId);
	
	Document findTopByFileTypeOrderByModifiedDesc(Integer fileType);
	
	//Document findTopByOrganizationIdAndFileTypeOrderByModifiedDesc(Integer organizationId, Integer fileType);
	
	//Page<Document> findByOrganizationIdAndFileTypeOrderByModifiedDesc(Integer organizationId, Integer fileType, Pageable page);
	
	//Long countByOrganizationIdAndFileType(Integer organizationId, Integer fileType);
}
