package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.ServerAccess;
//import org.dmc.services.data.entities.User;

import java.util.List;

public interface ServerAccessRepository extends BaseRepository<ServerAccess, Integer> {

	ServerAccess findOneByName(String name);
}
