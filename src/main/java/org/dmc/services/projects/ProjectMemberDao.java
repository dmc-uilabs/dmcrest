package org.dmc.services.projects;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ResourceGroupService;
import org.dmc.services.ServiceLogger;
import org.dmc.services.company.CompanyDao;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.profile.Profile;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.utils.SQLUtils;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.inject.Inject;

@Component
public class ProjectMemberDao {

    private Connection connection;
    private final String logTag = ProjectMemberDao.class.getName();
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private ResourceGroupService resourceGroupService;
    
    @Inject
    private ProjectDao projectDao;

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

        final ArrayList<Profile> members = new ArrayList<Profile>();

        try {

            // User should only have access to users within the same company
            // User may even have to be an admin of some sort, but that will
            // depend on requirements
            // Integer organizationId = CompanyUserUtil.getOrgId(userEPPN);

            final String query = "SELECT u.user_id, u.user_name, u.realname, u.title, u.phone, "
                    + "u.email, u.address, u.image, u.people_resume "
                    + "FROM organization_dmdii_member dmdii, organization_user orgu, users u "
                    + "WHERE u.user_id = orgu.user_id " + "AND orgu.organization_id = dmdii.organization_id "
                    + "AND dmdii.expire_date >= now() ";
            // + "AND orgu.organization_id = ?";

            final PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            // preparedStatement.setInt(1, organizationId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                final Profile profile = new Profile();

                final String userId = resultSet.getString("user_id");
                profile.setId(userId);
                profile.setDisplayName(resultSet.getString("realname"));
                profile.setJobTitle(resultSet.getString("title"));
                profile.setPhone(resultSet.getString("phone"));
                profile.setEmail(resultSet.getString("email"));
                profile.setLocation(resultSet.getString("address"));
                profile.setImage(resultSet.getString("image"));
                profile.setDescription(resultSet.getString("people_resume"));

                // get company
                final int companyId = CompanyDao.getUserCompanyId(Integer.parseInt(userId));
                profile.setCompany(Integer.toString(companyId));

                members.add(profile);
            }
        } catch (SQLException se) {
            throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
        }

        return members;
    }

    // sample query for fforgeadmin user (102)
    // select gjr.group_id, gjr.user_id, gjr.request_id, gjr.request_date,
    // gjr.accept_date, gjr.reject_date, u.firstname, u.lastname
    // from group_join_request gjr
    // join users u on gjr.user_id = u.user_id
    // where gjr.group_id in (SELECT adr.home_group_id from pfo_role adr join
    // pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = 102)
    public ArrayList<ProjectMember> getProjectMembers(String projectList, String memberList, Boolean accept, String userEPPN) throws DMCServiceException {

        try {

            final int userId = UserDao.getUserID(userEPPN);
            final String projectMembersQuery = createGetProjectMembersQuery(accept, projectList, userId, memberList);
            final ArrayList<ProjectMember> list = getProjectsMembersFromQuery(projectMembersQuery);
            return list;
        } catch (SQLException se) {
            throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
        }
    }

    public ArrayList<ProjectMember> getProjectMembersByInvitation(String projectMemberRequestId, String userEPPN) throws DMCServiceException {

        try {

            final int userId = UserDao.getUserID(userEPPN);
            ProjectMember memberRequest = new ProjectMember();
            memberRequest.setId(projectMemberRequestId);

            final String projectMembersQuery = createGetProjectMembersQuery(null, memberRequest.getProjectId(), userId, memberRequest.getProfileId());
            final ArrayList<ProjectMember> list = getProjectsMembersFromQuery(projectMembersQuery);
            return list;
        } catch (SQLException se) {
            throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
        }
    }

    private String createAcceptClause(Boolean accept) {
        
        if (accept != null) {
            if (accept.booleanValue()) {
                return "gjr.accept_date is not null";
            } else {
                return "gjr.accept_date is null and gjr.reject_date is null";
            }
        }
        return null;
    }

    private String createGetProjectMembersQuery(Boolean accept, String projectList, Integer userId, String userMemberIdList) {
        // requesting user must be administrator of the project to get the
        // list of members, unless user is requesting own membership.

        String projectMembersQuery = "SELECT gjr.group_id, gjr.user_id, gjr.requester_id, gjr.request_date, gjr.accept_date, gjr.reject_date, u.realname, ur.realname requester_name "
                + "FROM group_join_request gjr " 
                + "JOIN users u ON gjr.user_id = u.user_id "
                + "JOIN users ur ON gjr.requester_id = ur.user_id ";
        final String membershipRequiredClause = "gjr.group_id in (SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = "
                + userId + ")";

        ArrayList<String> clauses =  new ArrayList<String>();
        if (null != projectList ) {
            if (SQLUtils.isListValidIntegers(projectList)) {
                clauses.add("gjr.group_id in (" + projectList + ")");
            } else {
                throw new DMCServiceException(DMCError.BadURL, "invalid projects: " + projectList);
            }
        }
        if (null != userMemberIdList) {
            clauses.add(createInvitationClause(userMemberIdList));
        }
        if (null == projectList && null == userMemberIdList && (null!= accept && false == accept)) {
            clauses.add("gjr.user_id = " + userId);
        } else if (null == projectList && null == userMemberIdList) {
            clauses.add(membershipRequiredClause);
        } else if (!isIdInInvitationList(Integer.toString(userId), userMemberIdList)) {
            clauses.add(membershipRequiredClause);
        }

        final String acceptClause = createAcceptClause(accept);
        if (null != acceptClause) {
            clauses.add(acceptClause);
        }
        
        if (clauses.size() > 0) {
            projectMembersQuery += "WHERE " + clauses.get(0);
            for (int i = 1; i < clauses.size(); ++i) {
                projectMembersQuery += " AND " + clauses.get(i);
            }
        }
        return projectMembersQuery;
    }

    private ArrayList<ProjectMember> getProjectsMembersFromQuery(String query) {
        try {
            ServiceLogger.log(logTag, "getProjectsMembersFromQuery query = " + query);
            final ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final ProjectMember member = new ProjectMember();
                member.setProfileId(Integer.toString(resultSet.getInt("user_id")));
                member.setProjectId(Integer.toString(resultSet.getInt("group_id")));
                final Timestamp acceptTimestamp = resultSet.getTimestamp("accept_date");
                final Timestamp rejectTimestamp = resultSet.getTimestamp("reject_date");
                
                if (null != acceptTimestamp) {
                    member.setAccept(true);
                } else {
                    member.setAccept(false);
                }
                
                if (null != rejectTimestamp) {
                	member.setRejected(true);
                } else {
                	member.setRejected(false);
                }
                
                member.setFromProfileId(Integer.toString(resultSet.getInt("requester_id")));
                member.setFrom(resultSet.getString("requester_name"));
                final Timestamp requestTimestamp = resultSet.getTimestamp("request_date");
                member.setDate(requestTimestamp.getTime());
                list.add(member);
            }
            return list;
        } catch (SQLException se) {
            ServiceLogger.log(logTag, se.getMessage());
            return null;
        }
    }

    public ProjectMember createProjectMemberRequest(ProjectMember member, String userEPPN)
            throws SQLException, Exception {
        final ProjectMember createdMember = new ProjectMember();
        
        final PostProjectJoinRequest request = new PostProjectJoinRequest();
        request.setProfileId(member.getId().split("-")[1]);
        request.setProjectId(member.getProjectId());
        /**
         * Note: this is rather messy, but there already exists code to handle insertions in to project_join_requests
         * so I am extracting the data and sending to that function call in ProjectDao
         */
        
        projectDao.createProjectJoinRequest(request, userEPPN);
        createdMember.setAccept(member.getFromProfileId().equals(member.getProfileId()) ? true : false);
        createdMember.setDate(System.currentTimeMillis());
        createdMember.setFrom(userEPPN);
        String useridAsString = Integer.toString(UserDao.getUserID(userEPPN));
        createdMember.setFromProfileId(useridAsString);
        createdMember.setProfileId(member.getProfileId());
        createdMember.setProjectId(member.getProjectId());
        return createdMember;
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
    public ProjectMember acceptMemberInProject(String requestId, String userEPPN) throws DMCServiceException {
        try {

            final int userId = UserDao.getUserID(userEPPN);
            final RequestIdParser parser = new RequestIdParser(requestId);
            final int projectId = parser.getProjectId();
            final int requesterId = parser.getRequesterId();
            final int memberId = parser.getMemberId();

            if (memberId != userId)
                throw new DMCServiceException(DMCError.UnknownUser, "Not the correct user");
            
            final boolean ok = isUserProjectAdmin(projectId, requesterId);
            if (!ok) {
                throw new DMCServiceException(DMCError.NotProjectAdmin,
                        userEPPN + " does not have permission to invite new members, you must be a project Admin");
            }
            
            final int roleId = GetRole(projectId, "Project Member");
            return acceptMember(memberId, roleId, projectId, requesterId);

        } catch (SQLException e) {
            throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
        }
    }

    public boolean isUserProjectAdmin(int projectId, int userId) throws DMCServiceException {
        boolean isAdmin = false;
        try {
            final String checkRequesterAuthority = "SELECT adr.home_group_id " + "FROM pfo_role adr "
                    + "JOIN pfo_user_role adu ON adr.role_id = adu.role_id " + "WHERE adu.user_id = ? "
                    + "AND adr.role_name = 'Admin' " + "AND adr.home_group_id = ?";
            final PreparedStatement preparedStatement = DBConnector.prepareStatement(checkRequesterAuthority);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, projectId);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isAdmin = true;
            }
            resultSet.close();
        } catch (SQLException se) {
            throw new DMCServiceException(DMCError.OtherSQLError,
                    "unable to check if user is admin: " + userId + " for project " + projectId);
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
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                roleId = resultSet.getInt(1);
            }
            resultSet.close();
        } catch (SQLException se) {
            throw new DMCServiceException(DMCError.Generic,
                    se.getMessage() + ": unable to query role: " + roleName + " for project " + projectId);
        }
        return roleId;
    }

    private ProjectMember acceptMember(int memberId, int roleId, int projectId, int fromUserId)
            throws DMCServiceException {
        connection = DBConnector.connection();

        // let's start a transaction
        try {
            connection.setAutoCommit(false);
            // requesting user must be administrator of the project to get the
            // list of members.
            final String acceptMemberQuery = "INSERT into pfo_user_role (user_id, role_id) values ( ?, ?)";
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
                throw new DMCServiceException(DMCError.MemberNotAssignedToProject,
                        "unable to assign " + memberId + " to Project Member role in project " + projectId);
            }

            String updateGroupJoinRequest = "UPDATE group_join_request SET accept_date = now(), reject_date = null "
                    + "WHERE user_id = ? " + "AND group_id = ? ";
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
                throw new DMCServiceException(DMCError.NoExistingRequest,
                        "no existing request to join the project " + projectId + " for memberId " + memberId);
            } else {
                
                //bolt on resource access for project members
                User user = userRepository.getOne(memberId);
                resourceGroupService.addResourceGroup(user, DocumentParentType.PROJECT, projectId, SecurityRoles.MEMBER);
            	
            }

            return findMemberRequest(memberId, projectId, fromUserId);
        } catch (SQLException se) {
            ServiceLogger.log(logTag, se.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e) {
            }

            throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());

        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
            }
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
    public ProjectMember rejectMemberInProject(String requestId, String userEPPN) throws DMCServiceException {

        try {
            final RequestIdParser parser = new RequestIdParser(requestId);
            final int projectId = parser.getProjectId();
            final int memberId = parser.getMemberId();
            final int requesterId = parser.getRequesterId();

            final int userLookup = UserDao.getUserID(userEPPN);

            final boolean isCallerAdmin = isUserProjectAdmin(projectId, userLookup);
            if (!isCallerAdmin && userLookup != memberId) {
                throw new DMCServiceException(DMCError.NotProjectAdmin,
                        userEPPN + " does not have permission to remove this user, you must be a project Admin or the invitee");
            }

            if (isUserProjectAdmin(projectId, memberId)) {
                int count = getCountOfMembersWithRole(projectId, "Admin");
                if (count < 2) {
                    throw new DMCServiceException(DMCError.OnlyProjectAdmin,
                            "user " + userEPPN + " is the only Admin of project " + projectId
                                    + " Another member must be added to Admin role.");
                }
            }

            return rejectRequest(memberId, projectId, requesterId, userEPPN);

        } catch (DMCServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new DMCServiceException(DMCError.Generic, e.getMessage());
        }
    }

    private int getCountOfMembersWithRole(int projectId, String roleName) throws Exception {
        String getRoleQuery = "SELECT COUNT(role_id) from pfo_role where role_name = ? and home_group_id = ?";

        PreparedStatement preparedStatement = DBConnector.prepareStatement(getRoleQuery);
        preparedStatement.setString(1, roleName);
        preparedStatement.setInt(2, projectId);
        final ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            final int count = resultSet.getInt(1);
            return count;
        }
        resultSet.close();

        return -1;
    }

    // have already validated that user is allowed to delete this member
    private ProjectMember deleteExistingMember(int memberId, int projectId, int fromUserId, String fromUsername) throws SQLException {
        connection = DBConnector.connection();
        connection.setAutoCommit(false);
        // let's start a transaction
        try {
            removeMemberFromProject(memberId, projectId, fromUserId, fromUsername);
            rejectProjectMemberRequest(memberId, projectId, fromUserId, fromUsername);
            return findMemberRequest(memberId, projectId, fromUserId);
        } catch (SQLException se) {
            ServiceLogger.log(logTag, se.getMessage());
            connection.rollback();
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to remove user " + memberId + " from project " + projectId);

        } finally {
            connection.setAutoCommit(true);
        }
    }

    // have already validated that user is allowed to delete this member
    private ProjectMember rejectRequest(int memberId, int projectId, int fromUserId, String userEPPN) throws SQLException {
        connection = DBConnector.connection();
        connection.setAutoCommit(false);
        // let's start a transaction
        try {
            rejectProjectMemberRequest(memberId, projectId, fromUserId, userEPPN);
            return findMemberRequest(memberId, projectId, fromUserId);
        } catch (SQLException se) {
            ServiceLogger.log(logTag, se.getMessage());
            connection.rollback();
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to remove user " + memberId + " from project " + projectId);

        } finally {
            connection.setAutoCommit(true);
        }
    }

    // this is a helper method and should be called from another method that wraps it in a transaction
    private ProjectMember findMemberRequest(int memberId, int projectId, int fromUserId) throws SQLException {
        String projectMembersQuery = createGetProjectMembersQuery(null, Integer.toString(projectId), fromUserId, Integer.toString(memberId));

        final ArrayList<ProjectMember> projectMemberList = getProjectsMembersFromQuery(projectMembersQuery);
        for (ProjectMember member : projectMemberList) {
            if (member.getProfileId().equals(Integer.toString(memberId))) {
                return member;
            }
        }
        connection.rollback();
        throw new DMCServiceException(DMCError.BadURL, "expected to find request " + projectId + "-" + memberId + "-" + fromUserId + " and failed");
    }
    // this is a helper method and should be called from another method that wraps it in a transaction
    private void removeMemberFromProject(int memberId, int projectId, int fromUserId, String fromUsername) throws SQLException {
        // requesting user must be administrator of the project to get the
        // list of members.
        final String deleteMemberQuery = "DELETE from pfo_user_role where user_id = ? and role_id in (select role_id from pfo_role where home_group_id = ?)";
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
            throw new DMCServiceException(DMCError.MemberNotAssignedToProject, "error trying to remove user " + memberId + " from role in project " + projectId);
        }
    }

    // this is a helper method and should be called from another method that wraps it in a transaction
    private void rejectProjectMemberRequest(int memberId, int projectId, int fromUserId, String fromUsername) throws SQLException {
        final String recordRejectQuery = "UPDATE group_join_request SET accept_date = null, reject_date = now() where user_id = ? and group_id = ?";
        final PreparedStatement preparedStatement = DBConnector.prepareStatement(recordRejectQuery);
        preparedStatement.setInt(1, memberId);
        preparedStatement.setInt(2, projectId);
        final int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected < 1) {
            // if we have < 1, then nothing happened, and we asked to update something that didn't exist
            // so rollback shouldn't cost us anything.
            // if we have > 1 that the user has more requests for this project and we are rejecting all of them
            connection.rollback();
            throw new DMCServiceException(DMCError.BadURL, "no project request for " + memberId + " in project " + projectId);
        } else {
            
            User user = userRepository.findOne(memberId);
            resourceGroupService.removeResourceGroup(user, DocumentParentType.PROJECT, projectId, SecurityRoles.MEMBER);
        	
        }
    }

    private static boolean isIdInInvitationList(String id, String list) {
        if (null != list) {
            final String[] items = list.split(",");
            for (String item : items) {
                if (item.equals(id)) return true;
            }
        }
        return false;
    }

    private String createInvitationClause(String userMemberIdList) {
        if (null == userMemberIdList || userMemberIdList.length() == 0) return "";
        String[] ids = userMemberIdList.split(",");
        for (String id : ids) {
            try {
                Integer.parseInt(id);       // checking that we don't throw NumberFormatException, if ids become GUIDs, would need a different check
            } catch (Exception e) {
                throw new DMCServiceException(DMCError.BadURL, "invalid invitation member id: " + id);
            }
        }

        return "gjr.user_id in (" + userMemberIdList + ") ";
    }

}
