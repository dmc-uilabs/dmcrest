package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.DMDIIAreaOfExpertise;

import java.util.List;

public interface DMDIIAreaOfExpertiseRepository extends BaseRepository<DMDIIAreaOfExpertise, Integer> {

	List<DMDIIAreaOfExpertise> findAll();

}
