package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.DMDIIDocumentTag;

public interface DMDIIDocumentTagRepository extends BaseRepository<DMDIIDocumentTag, Integer> {

	List<DMDIIDocumentTag> findAll();

	DMDIIDocumentTag save(DMDIIDocumentTag tag);

}
