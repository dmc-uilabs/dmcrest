package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.Document;

import java.sql.Timestamp;
import java.util.List;

public interface DocumentRepository extends BaseRepository<Document, Integer> {

	List<Document> findAllByVerifiedIsFalseAndModifiedBefore(Timestamp modified);

	List<Document> findAllByVerifiedIsTrueAndIsDeletedIsFalseAndExpiresBefore(Timestamp expires);

}
