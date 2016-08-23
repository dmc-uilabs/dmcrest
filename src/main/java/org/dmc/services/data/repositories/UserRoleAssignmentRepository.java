package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.UserRoleAssignment;

public interface UserRoleAssignmentRepository extends BaseRepository<UserRoleAssignment, Integer> {
	
	void deleteByUserIdAndOrganizationId(Integer userId, Integer organizationId);

}
