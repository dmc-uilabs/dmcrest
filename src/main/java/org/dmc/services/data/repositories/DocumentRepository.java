package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mysema.query.types.Predicate;

import java.sql.Timestamp;
import java.util.List;

public interface DocumentRepository extends BaseRepository<Document, Integer> {

	List<Document> findAllByVerifiedIsFalseAndModifiedBefore(Timestamp modified);

	List<Document> findAllByVerifiedIsTrueAndIsDeletedIsFalseAndExpiresBefore(Timestamp expires);
	
	@Query(value = "SELECT * from Document AS d, User AS u " +
					"INNER JOIN d.resourceGroups AS rg " +
					"INNER JOIN u.resourceGroups " +
					"WHERE u.id = :userId")
	Page<Document> findAllowedDocuments (@Param("userId") Integer userId, Pageable page, Predicate where);

}
