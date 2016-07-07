package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIProjectEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DMDIIProjectEventsRepository extends BaseRepository<DMDIIProjectEvent, Integer>{

	Page<DMDIIProjectEvent> findAllByOrderByEventDateDesc(Pageable page);

}
