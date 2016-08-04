package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.UserToken;

public interface UserTokenRepository extends BaseRepository<UserToken, Integer> {

	public UserToken findByUserId(Integer id);
}
