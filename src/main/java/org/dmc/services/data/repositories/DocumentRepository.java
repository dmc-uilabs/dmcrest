package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.Directory;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentClass;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.User;

import java.sql.Timestamp;
import java.util.List;

public interface DocumentRepository extends BaseRepository<Document, Integer> {

	List<Document> findAllByVerifiedIsFalseAndModifiedBefore(Timestamp modified);

	List<Document> findAllByVerifiedIsTrueAndIsDeletedIsFalseAndExpiresBefore(Timestamp expires);

	Document findFirstByParentTypeAndDocClassAndOwnerOrderByModifiedDesc(DocumentParentType parentType, DocumentClass docClass, User owner);

	List<Document> findByDirectory(Directory dir);

	List<Document> findByParentTypeAndParentId(DocumentParentType parentType, Integer parentId);

	Document findFirstById(Integer documentId);
}
