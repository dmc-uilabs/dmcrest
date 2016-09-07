package org.dmc.services.data.repositories;

import java.util.List;

import org.dmc.services.data.entities.OrganizationUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrganizationUserRepository extends BaseRepository<OrganizationUser, Integer> {

	List<OrganizationUser> findByOrganizationId (Integer organizationId);

	OrganizationUser findByUserId(Integer userId);

	OrganizationUser findByUserIdAndOrganizationId(Integer userId, Integer organizationId);

	@Query(value = "select count(user_id) from organization_user where is_verified is true and organization_id = :organizationId", nativeQuery = true)
	Integer findNumberOfVerifiedUsersByOrganizationId(@Param("organizationId") Integer organizationId);

	@Modifying
	@Query(value = "delete from organization_user where user_id = :userId and organization_id = :organizationId", nativeQuery = true)
	Integer deleteByUserIdAndOrganizationId(@Param("userId") Integer userId, @Param("organizationId") Integer organizationId);

	@Modifying
	@Query(value = "delete from organization_user where user_id = :userId", nativeQuery = true)
	Integer deleteByUserId(@Param("userId") Integer userId);

}
