package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.AreaOfExpertise;

public interface DMDIIAreaOfExpertiseRepository extends BaseRepository<AreaOfExpertise, Integer> {

	List<AreaOfExpertise> findAll();

}
