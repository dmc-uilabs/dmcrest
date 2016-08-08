package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.OrganizationUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrganizationUserRepository extends BaseRepository<OrganizationUser, Integer> {

	List<OrganizationUser> findByOrganizationId (Integer organizationId);

	OrganizationUser findByUserId(Integer userId);

	OrganizationUser findByUserIdAndOrganizationId(Integer userId, Integer organizationId);

	@Query(value = "select count(user_id) from organization_user where is_verified is true and organization_id = :organizationId", nativeQuery = true)
	Integer findNumberOfVerifiedUsersByOrganizationId(@Param("organizationId") Integer organizationId);

}
