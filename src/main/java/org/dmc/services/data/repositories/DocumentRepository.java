package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentParentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentRepository extends BaseRepository<Document, Integer> {

	List<Document> findByParentTypeAndParentId(DocumentParentType parentType, Integer parentId, Pageable page);
	
	List<Document> findByParentTypeAndParentIdAndDocClass(DocumentParentType parentType, Integer parentId, Integer docClass, Pageable page);
	
	Document findTopByDocClassOrderByModifiedDesc(Integer docClass);
	
	//Document findTopByOrganizationIdAndDocClassOrderByModifiedDesc(Integer organizationId, Integer docClass);
	
	//Page<Document> findByOrganizationIdAndDocClassOrderByModifiedDesc(Integer organizationId, Integer docClass, Pageable page);
	
	//Long countByOrganizationIdAndDocClass(Integer organizationId, Integer docClass);
}
