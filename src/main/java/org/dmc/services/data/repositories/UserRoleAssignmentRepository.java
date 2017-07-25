package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.UserRoleAssignment;
import org.dmc.services.data.entities.UserRoleAssignmentRO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleAssignmentRepository extends BaseRepository<UserRoleAssignment, Integer> {
	
	void deleteByUserIdAndOrganizationId(Integer userId, Integer organizationId);
	
	@Query("SELECT u FROM UserRoleAssignmentRO u WHERE "
			+ "u.id = (SELECT MAX(uu.id) from UserRoleAssignmentRO uu WHERE user_id = :id)")
	UserRoleAssignmentRO findByUserId(@Param ("id") Integer id);

}
