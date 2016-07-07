package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.User;

public interface UserRepository extends BaseRepository<User, Integer> {

	User findFirstByUsername(String username);

}
