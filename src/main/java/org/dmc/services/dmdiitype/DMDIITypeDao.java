package org.dmc.services.dmdiitype;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface DMDIITypeDao extends CrudRepository<DMDIIType, Integer> {

	List<DMDIIType> findAll();
	
}
