package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.DMDIIAreaOfExpertise;

public interface DMDIIAreaOfExpertiseRepository extends BaseRepository<DMDIIAreaOfExpertise, Integer> {

	List<DMDIIAreaOfExpertise> findAll();

}
