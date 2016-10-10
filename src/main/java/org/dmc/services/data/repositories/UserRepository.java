package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends BaseRepository<User, Integer> {
	
	User findFirstByUsername(String username);

	@Query("SELECT u FROM OrganizationUser AS ou JOIN ou.user u JOIN ou.organization o"
			+ " WHERE o.id = :organizationId")
	List<User> findByOrganizationUserOrganizationId(@Param("organizationId") Integer organizationId);


	@Query(value = "SELECT"
			+ "   u.user_id, u.user_name, u.realname, u.title, u.phone, u.email, u.address, u.image, u.people_resume"
			+ " FROM users AS u"
			+ "   JOIN organization_user orgu ON orgu.user_id = u.user_id"
			+ "   JOIN organization_dmdii_member dmdii on dmdii.organization_id = orgu.organization_id"
			+ " WHERE dmdii.expire_date >= now()", nativeQuery = true)
	List<User> findAllWhereDmdiiMemberExpiryDateIsAfterNow();

	User findByUsername(String username);
	
	@Query("SELECT ura.user FROM UserRoleAssignment ura WHERE ura.organization.id = :organizationId AND ura.role.role = :role")
	List<User> findByOrganizationIdAndRole(@Param("organizationId") Integer organizationId, @Param("role") String role);
}
