package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.DMDIIDocument;
import org.dmc.services.data.entities.ResourceMachine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ResourceMachineRepository extends BaseRepository<ResourceMachine, Integer> {
	
	List<ResourceMachine> findBybay_id(Integer bayId);

	
}
