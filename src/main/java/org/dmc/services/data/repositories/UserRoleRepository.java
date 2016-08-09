package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.UserRole;

public interface UserRoleRepository extends BaseRepository<UserRole, Integer>{

	UserRole findByUserId(Integer dmdiiMemberId);

	void deleteByUserId(Integer userId);

}
