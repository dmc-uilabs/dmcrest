package org.dmc.services.data.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.dmc.services.data.entities.Directory;
import org.dmc.services.data.entities.Document;
import org.dmc.services.data.entities.DocumentClass;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.User;

public interface DocumentRepository extends BaseRepository<Document, Integer> {

	List<Document> findAllByVerifiedIsFalseAndModifiedBefore(Timestamp modified);

	List<Document> findAllByVerifiedIsTrueAndIsDeletedIsFalseAndExpiresBefore(Timestamp expires);
	
	Document findFirstByParentTypeAndDocClassAndOwnerOrderByModifiedDesc(DocumentParentType parentType, DocumentClass docClass, User owner);

	List<Document> findByDirectoryAndIsDeletedIsFalse(Directory dir);
}
