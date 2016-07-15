package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIDocumentTag;

public interface DMDIIDocumentTagRepository extends BaseRepository<DMDIIDocumentTag, Integer> {

	DMDIIDocumentTag save(DMDIIDocumentTag tag);

}
