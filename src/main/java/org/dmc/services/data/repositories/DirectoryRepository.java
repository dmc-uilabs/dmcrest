package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.Directory;

public interface DirectoryRepository extends BaseRepository<Directory, Integer> {

	Directory findByName(String name);

	List<Directory> findByParent(Directory parent);

}
