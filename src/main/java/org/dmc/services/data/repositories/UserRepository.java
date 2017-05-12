package org.dmc.services.data.repositories;

import org.dmc.services.data.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends BaseRepository<User, Integer> {

	User findFirstByUsername(String username);

	// @Query("SELECT u FROM OrganizationUser AS ou JOIN ou.user u JOIN ou.organization o"
	// 		+ " WHERE o.id = :organizationId")
	@Query(value = "SELECT  u.user_id, u.user_name, u.accept_term_cond_time, u.user_pw, u.realname, u.title, u.firstname, u.lastname, u.email, u.address, u.phone, u.about_me, u.people_resume, u.add_date, u.timezone, u.user_contact_info_id, d.url as image  "
		+ " FROM organization_user AS ou "
		+ " JOIN users u on u.user_id = ou.user_id  "
		+ " JOIN organization o on o.organization_id = ou.organization_id "
		+ "LEFT JOIN   "
		+ "		(select parent_id, max(id) as id  "
		+ "		from document d   "
		+ "		where parent_type = 'USER' and doc_class = 'IMAGE'  "
		+ "		group by parent_id) maxdocs on maxdocs.parent_id = u.user_id  "
		+ "LEFT JOIN  document d on parent_type = 'USER'   "
		+ "		and doc_class = 'IMAGE'   "
		+ "		and maxdocs.id = d.id  "
		+ " WHERE o.organization_id = :organizationId", nativeQuery = true)
	List<User> findByOrganizationUserOrganizationId(@Param("organizationId") Integer organizationId);


	@Query(value = "SELECT  u.user_id, u.user_name, u.accept_term_cond_time, u.user_pw, u.realname, u.title, u.firstname, u.lastname, u.email, u.address, u.phone, u.about_me, u.people_resume, u.add_date, u.timezone, u.user_contact_info_id, d.url as image"
			+ " FROM organization_user AS ou"
			+ " JOIN users u on u.user_id = ou.user_id"
			+ " JOIN organization o on o.organization_id = ou.organization_id"
			+ " LEFT JOIN"
			+ " (select parent_id, max(id) as id"
			+ " from document d"
			+ " where parent_type = 'USER' and doc_class = 'IMAGE'"
			+ " group by parent_id) maxdocs on maxdocs.parent_id = u.user_id"
			+ " LEFT JOIN  document d on parent_type = 'USER'"
			+ " and doc_class = 'IMAGE'"
			+ " and maxdocs.id = d.id"
			+ " WHERE o.organization_id = :organizationId AND u.realname ILIKE :displayName", nativeQuery = true)
	List<User> findByOrganizationUserOrganizationIdLikeDisplayName(@Param("organizationId") Integer organizationId, @Param("displayName") String displayName);

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
