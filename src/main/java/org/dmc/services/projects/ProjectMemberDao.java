package org.dmc.services.projects;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.CompanyDao;
import org.dmc.services.company.CompanyUserUtil;
import org.dmc.services.users.UserDao;
import org.dmc.services.profile.Profile;

public class ProjectMemberDao {

	private Connection connection;
	private final String logTag = ProjectMemberDao.class.getName();
	private ResultSet resultSet;

	public ProjectMemberDao() {
	}

	/**
	 * Get Members - Currently returning all members' Profiles who are part of a
	 * DMDII Company
	 * 
	 * @param userEPPN
	 * @return
	 * @throws DMCServiceException
	 */
	public ArrayList<Profile> getMembers(String userEPPN) throws DMCServiceException {

		ArrayList<Profile> members = new ArrayList<Profile>();

		try {

			// User should only have access to users within the same company
			// User may even have to be an admin of some sort, but that will depend on requirements
//			Integer organizationId = CompanyUserUtil.getOrgId(userEPPN);
			
			String query = "SELECT u.user_id, u.user_name, u.realname, u.title, u.phone, "
					+ "u.email, u.address, u.image, u.people_resume "
					+ "FROM organization_dmdii_member dmdii, organization_user orgu, users u "
					+ "WHERE u.user_id = orgu.user_id " 
					+ "AND orgu.organization_id = dmdii.organization_id "
					+ "AND dmdii.expire_date >= now() ";
//					+ "AND orgu.organization_id = ?";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
//			preparedStatement.setInt(1, organizationId);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				Profile profile = new Profile();

				String userId = resultSet.getString("user_id");
				profile.setId(userId);
				profile.setDisplayName(resultSet.getString("realname"));
				profile.setJobTitle(resultSet.getString("title"));
				profile.setPhone(resultSet.getString("phone"));
				profile.setEmail(resultSet.getString("email"));
				profile.setLocation(resultSet.getString("address"));
				profile.setImage(resultSet.getString("image"));
				profile.setDescription(resultSet.getString("people_resume"));

				// get company
				CompanyDao companyDao = new CompanyDao();
				int companyId = companyDao.getUserCompanyId(Integer.parseInt(userId));
				profile.setCompany(Integer.toString(companyId));

				members.add(profile);
			}
		} catch (SQLException se) {
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
		}

		ServiceLogger.log(logTag, members.toString());
		return members;
	}
	
	// sample query for fforgeadmin user (102)
	// select gjr.group_id, gjr.user_id, gjr.request_id, gjr.request_date,
	// gjr.accept_date, gjr.reject_date, u.firstname, u.lastname
	// from group_join_request gjr
	// join users u on gjr.user_id = u.user_id
	// where gjr.group_id in (SELECT adr.home_group_id from pfo_role adr join
	// pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = 102)
	public ArrayList<ProjectMember> getProjectMembers(String userEPPN) throws DMCServiceException {
		
		ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();

		try {
			
			int userId = UserDao.getUserID(userEPPN);

			// requesting user must be administrator of the project to get the
			// list of members.

			String projectMembersQuery = "SELECT gjr.group_id, gjr.user_id, gjr.requester_id, gjr.request_date, gjr.accept_date, gjr.reject_date, u.realname, ur.realname requester_name "
					+ "FROM group_join_request gjr " + "JOIN users u ON gjr.user_id = u.user_id " + "JOIN users ur ON gjr.requester_id = ur.user_id "
					+ "WHERE gjr.group_id in (SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = " + userId
					+ " and adr.role_name = 'Admin') ";

			list = getProjectsMembersFromQuery(projectMembersQuery);
		} catch (SQLException se) {
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
		}
		return list;
	}

	// sample query for member 111 by fforgeadmin user (102), If by and member
	// are same, then do not use the AND clause.
	// select gjr.group_id, gjr.user_id, gjr.request_id, gjr.request_date,
	// gjr.accept_date, gjr.reject_date, u.firstname, u.lastname
	// from group_join_request gjr
	// join users u on gjr.user_id = u.user_id
	// where u.user_id = 111
	// AND gjr.group_id in (SELECT adr.home_group_id from pfo_role adr join
	// pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = 102)
	public ArrayList<ProjectMember> getProjectsForMember(String memberIdString, String userEPPN) throws DMCServiceException {

		ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();

		try {
			int userId = UserDao.getUserID(userEPPN);
			int memberId = UserDao.getUserID(memberIdString);

			// requesting user must be administrator of the project to get the
			// list of members.
			String projectMembersQuery = "SELECT gjr.group_id, gjr.user_id, gjr.requester_id, gjr.request_date, gjr.accept_date, gjr.reject_date, u.realname, ur.realname requester_name "
					+ "FROM group_join_request gjr " + "JOIN users u ON gjr.user_id = u.user_id " + "JOIN users ur ON gjr.requester_id = ur.user_id " + "WHERE u.user_id = " + memberId + " ";
			if (memberId != userId) {
				projectMembersQuery += "AND gjr.group_id in (SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = " + userId
						+ " and adr.role_name = 'Admin') ";
			}

			list = getProjectsMembersFromQuery(projectMembersQuery);
		} catch (SQLException se) {
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
		}
		return list;
	}

	// sample query for fforgeadmin user (102)
	// select gjr.group_id, gjr.user_id, gjr.request_id, gjr.request_date,
	// gjr.accept_date, gjr.reject_date, u.firstname, u.lastname
	// from group_join_request gjr
	// join users u on gjr.user_id = u.user_id
	// where gjr.group_id = 6
	// AND gjr.group_id in (SELECT adr.home_group_id from pfo_role adr join
	// pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = 102)
	public ArrayList<ProjectMember> getMembersForProject(String projectIdString, String userEPPN) throws DMCServiceException {
		ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();

		try {
			int userId = UserDao.getUserID(userEPPN);
			int projectId = Integer.parseInt(projectIdString);

			// requesting user must be administrator of the project to get the
			// list of members.
			String projectMembersQuery = "SELECT gjr.group_id, gjr.user_id, gjr.requester_id, gjr.request_date, gjr.accept_date, gjr.reject_date, u.realname, ur.realname requester_name "
					+ "FROM group_join_request gjr " + "JOIN users u ON gjr.user_id = u.user_id " + "JOIN users ur ON gjr.requester_id = ur.user_id " + "WHERE gjr.group_id = " + projectId + " "
					+ "AND gjr.group_id in (SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = " + userId
					+ " and adr.role_name = 'Admin') ";
			list = getProjectsMembersFromQuery(projectMembersQuery);
		} catch (SQLException e) {
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
		return list;
	}

	private ArrayList<ProjectMember> getProjectsMembersFromQuery(String query) {
		ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();
		try {
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.execute();

			resultSet = preparedStatement.getResultSet();
			while (resultSet.next()) {
				// id = resultSet.getString("id");
				ProjectMember member = new ProjectMember();
				member.setProfileId(Integer.toString(resultSet.getInt("user_id")));
				member.setProjectId(Integer.toString(resultSet.getInt("group_id")));
				Timestamp timestamp = resultSet.getTimestamp("accept_date");
				if (null != timestamp) {
					member.setAccept(true);
				} else {
					member.setAccept(false);
				}
				member.setFromProfileId(Integer.toString(resultSet.getInt("requester_id")));
				member.setFrom(resultSet.getString("requester_name"));
				timestamp = resultSet.getTimestamp("request_date");
				member.setDate(timestamp.getTime());
				list.add(member);
			}

		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			return null;
		}
		return list;
	}

	//
	// sample queries for userEPPN = 102 (fforgeadmin), memberId = 111
	// (testUser), and projectId = 6
	// first confirm that userEPPN is admin of project projectId
	// SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on
	// adr.role_id = adu.role_id where adu.user_id = 102 and adr.role_name =
	// 'Admin' and adr.home_group_id = 6)
	// next get Project Member role for projectId as role_id
	// SELECT r.role_id from pfo_role where r.home_group_id = 6 and r.role_name
	// = 'Project Member'
	// then insert into the pfo_user_role table
	// INSERT pfo_user_role (user_id, role_id) values (111, 27);
	//
	public ProjectMember addProjectMember(ProjectMember member, String userEPPN) throws DMCServiceException {
		try {
			int userId = UserDao.getUserID(userEPPN);
			int memberId = Integer.parseInt(member.getProfileId());
			int projectId = Integer.parseInt(member.getProjectId());
			return acceptMemberInProject(projectId, memberId, userId, userEPPN);
		} catch (SQLException e) {
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
	}

	public ProjectMember acceptMemberInProject(String projectIdString, String memberIdString, String userEPPN) throws DMCServiceException {
		try {
			int userId = UserDao.getUserID(userEPPN);
			int memberId = UserDao.getUserID(memberIdString);
			int projectId = Integer.parseInt(projectIdString);
			return acceptMemberInProject(projectId, memberId, userId, userEPPN);
		} catch (DMCServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new DMCServiceException(DMCError.Generic, e.getMessage());
		}
	}

	//
	// sample queries for userEPPN = 102 (fforgeadmin), memberId = 111
	// (testUser), and projectId = 6
	// first confirm that userEPPN is admin of project projectId
	// SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on
	// adr.role_id = adu.role_id where adu.user_id = 102 and adr.role_name =
	// 'Admin' and adr.home_group_id = 6)
	// next get Project Member role for projectId as role_id
	// SELECT r.role_id from pfo_role where r.home_group_id = 6 and r.role_name
	// = 'Project Member'
	// then insert into the pfo_user_role table
	// INSERT pfo_user_role (user_id, role_id) values (111, 27);
	//
	private ProjectMember acceptMemberInProject(int projectId, int memberId, int userId, String userName) throws DMCServiceException {
		boolean ok = IsRequesterAdmin(projectId, userId);
		
		if (!ok) {
			throw new DMCServiceException(DMCError.NotProjectAdmin, userId + " does not have permission to accept members, you must be a project Admin");
		}

		int roleId = GetRole(projectId, "Project Member");

		ProjectMember member = acceptMember(memberId, roleId, projectId, userId, userName);

		if (null == member) {
			throw new DMCServiceException(DMCError.Generic, "problem adding user " + memberId + " from project " + projectId);
		}
		
		return member;
	}
	
	public ProjectMember updateProjectMember(String memberId, ProjectMember member, String userEPPN) throws DMCServiceException {
		try {
			int userId = UserDao.getUserID(userEPPN);
			
			// Update member in DB where Id = memberID
			// Implementation pending, returning a new ProjectMember for now
			return new ProjectMember();
			
		} catch (SQLException e) {
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
	}

	private boolean IsRequesterAdmin(int projectId, int userId) throws DMCServiceException {
		boolean isAdmin = false;
		try {
			String checkRequesterAuthority = "SELECT adr.home_group_id "
					+ "FROM pfo_role adr "
					+ "JOIN pfo_user_role adu ON adr.role_id = adu.role_id "
					+ "WHERE adu.user_id = ? "
					+ "AND adr.role_name = 'Admin' "
					+ "AND adr.home_group_id = ?";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(checkRequesterAuthority);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, projectId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				isAdmin = true;
			}
			resultSet.close();
		} catch (SQLException se) {
			throw new DMCServiceException(DMCError.OtherSQLError, "unable to check if user is admin: " + userId + " for project " + projectId);
		}
		return isAdmin;
	}

	private int GetRole(int projectId, String roleName) throws DMCServiceException {
		int roleId = -1;
		try {
			String getRoleQuery = "SELECT role_id from pfo_role where role_name = ? and home_group_id = ?";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(getRoleQuery);
			preparedStatement.setString(1, roleName);
			preparedStatement.setInt(2, projectId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				roleId = resultSet.getInt(1);
			}
			resultSet.close();
		} catch (SQLException se) {
			throw new DMCServiceException(DMCError.Generic, se.getMessage() + ": unable to query role: " + roleName + " for project " + projectId);
		}
		return roleId;
	}

	private ProjectMember acceptMember(int memberId, int roleId, int projectId, int fromUserId, String userName) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		
		// let's start a transaction
		try {
			connection.setAutoCommit(false);
			// requesting user must be administrator of the project to get the
			// list of members.
			String acceptMemberQuery = "INSERT into pfo_user_role (user_id, role_id) values ( ?, ?)";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(acceptMemberQuery);
			preparedStatement.setInt(1, memberId);
			preparedStatement.setInt(2, roleId);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected != 1) {
				// if we have < 1, then nothing happened, probably member is
				// already in the project
				// so rollback shouldn't cost us anything.
				// if we have > 1 that would be weird
				connection.rollback();
				throw new DMCServiceException(DMCError.MemberNotAssignedToProject, "unable to assign " + memberId + " to Project Member role in project " + projectId);
			}

			String updateGroupJoinRequest = "UPDATE group_join_request SET accept_date = now(), reject_date = null " + "WHERE user_id = ? " + "AND group_id = ? ";
			preparedStatement = DBConnector.prepareStatement(updateGroupJoinRequest);
			preparedStatement.setInt(1, memberId);
			preparedStatement.setInt(2, projectId);
			rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected != 1) {
				// if we have < 1, then nothing happened, probably we didn't
				// have a group_join_request
				// so rollback shouldn't cost us anything.
				// if we have > 1 that would be weird
				connection.rollback();
				throw new DMCServiceException(DMCError.NoExistingRequest, "no existing request to join the project " + projectId + " for memberId " + memberId);
			}

			ArrayList<ProjectMember> projectMemberList = getMembersForProject(Integer.toString(projectId), userName);
			for (ProjectMember member : projectMemberList) {
				if (member.getProfileId().equals(Integer.toString(memberId))) {
					return member;
				}
			}
			connection.rollback();
			throw new DMCServiceException(DMCError.MemberNotAssignedToProject, "expected to find project member " + memberId + " in list of members for project " + projectId + " and failed");
		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e) {}
			
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());

		} finally {
			try {
				if (connection != null) {
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {}
		}
	}

	//
	// sample queries for userEPPN = 102 (fforgeadmin), memberId = 111
	// (testUser), and projectId = 6
	// first confirm that userEPPN is admin of project projectId
	// SELECT r.role_id from pfo_role where
	// make sure there is another admin if this user is an admin
	// SELECT count(r.role_id) from pfo_role where
	// then delete into the pfo_user_role table
	// DELETE pfo_user_role where user_id = 111;
	//
	public ProjectMember rejectMemberInProject(String projectIdString, String memberIdString, String userEPPN) throws DMCServiceException {

		try {
			int userId = UserDao.getUserID(userEPPN);
			int memberId = UserDao.getUserID(memberIdString);
			int projectId = Integer.parseInt(projectIdString);

			boolean ok = IsRequesterAdmin(projectId, userId);
			if (!ok) {
				throw new DMCServiceException(DMCError.NotProjectAdmin, userEPPN + " does not have permission to remove members, you must be a project Admin");
			}

			if (userId == memberId) {
				int count = GetCountOfMembersWithRole(projectId, "Admin");
				if (count < 2) {
					throw new DMCServiceException(DMCError.OnlyProjectAdmin, "user " + userEPPN + " is the only Admin of project " + projectId + " Another member must be added to Admin role.");
				}
			}

			ProjectMember member = deleteMember(memberId, projectId, userId, userEPPN);
			if (null == member) {
				throw new DMCServiceException(DMCError.Generic, "problem deleting user " + memberIdString + " from project " + projectIdString);
			}

			return member;

		} catch (DMCServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new DMCServiceException(DMCError.Generic, e.getMessage());
		}
	}

	private int GetCountOfMembersWithRole(int projectId, String roleName) throws Exception {
		int count = -1;
		String getRoleQuery = "SELECT COUNT(role_id) from pfo_role where role_name = ? and home_group_id = ?";

		PreparedStatement preparedStatement = DBConnector.prepareStatement(getRoleQuery);
		preparedStatement.setString(1, roleName);
		preparedStatement.setInt(2, projectId);
		resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			count = resultSet.getInt(1);
		}
		resultSet.close();
		
		return -1;
	}

	private ProjectMember deleteMember(int memberId, int projectId, int fromUserId, String fromUsername) throws Exception {
		Connection connection = DBConnector.connection();
		connection.setAutoCommit(false);
		// let's start a transaction
		try {
			// requesting user must be administrator of the project to get the
			// list of members.
			String deleteMemberQuery = "DELETE from pfo_user_role where user_id = ? and role_id in (select role_id from pfo_role where home_group_id = ?)";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(deleteMemberQuery);
			preparedStatement.setInt(1, memberId);
			preparedStatement.setInt(2, projectId);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected != 1) {
				// if we have < 1, then nothing happened, probably member is not
				// already in the project
				// so rollback shouldn't cost us anything.
				// if we have > 1 that would be weird
				connection.rollback();
				throw new Exception("error trying to remove user " + memberId + " from role in project " + projectId);
			}

			String recordRejectQuery = "UPDATE group_join_request SET accept_date = null, reject_date = now() where user_id = ? and group_id = ?";
			preparedStatement = DBConnector.prepareStatement(recordRejectQuery);
			preparedStatement.setInt(1, memberId);
			preparedStatement.setInt(2, projectId);
			rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected != 1) {
				// if we have < 1, then nothing happened, probably member is not
				// already in the project
				// so rollback shouldn't cost us anything.
				// if we have > 1 that would be weird
				connection.rollback();
				throw new Exception("error updating group join request to show memberId " + memberId + " rejects request for project " + projectId);
			}

			ArrayList<ProjectMember> projectMemberList = getMembersForProject(Integer.toString(projectId), fromUsername);
			for (ProjectMember member : projectMemberList) {
				if (member.getProfileId().equals(Integer.toString(memberId))) {
					return member;
				}
			}
			connection.rollback();
			throw new Exception("expected to find project member " + memberId + " in list of members (requests) for project " + projectId + " and failed");
		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			connection.rollback();
			throw new Exception("unable to remove user " + memberId + " from project " + projectId);

		} finally {
			connection.setAutoCommit(true);
		}
	}

}
