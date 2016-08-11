package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIDocumentTag;

import java.util.List;

public interface DMDIIDocumentTagRepository extends BaseRepository<DMDIIDocumentTag, Integer> {

	List<DMDIIDocumentTag> findAll();

	DMDIIDocumentTag save(DMDIIDocumentTag tag);

}
