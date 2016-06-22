package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIMemberEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface DMDIIMemberEventRepository extends BaseRepository<DMDIIMemberEvent, Integer> {
	
	@Query("SELECT e FROM DMDIIMemberEvent e WHERE e.date > CURRENT_TIMESTAMP() ORDER BY e.date ASC")
	Page<DMDIIMemberEvent> findFutureEvents(Pageable page);

}
