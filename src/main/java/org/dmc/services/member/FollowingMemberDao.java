package org.dmc.services.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.utils.SQLUtils;

public class FollowingMemberDao {

    private static final String LOGTAG = FollowingMemberDao.class.getName();

    public ArrayList<FollowingMember> followingMembersGet(String accountId, String idList, String profileList, Integer limit, Integer start, String order, String sort, String userEPPN) {

        try {
            final int requesterId = UserDao.getUserID(userEPPN);
            final ArrayList<FollowingMember> list = new ArrayList<FollowingMember>();
            String query = "select follower, followed from user_following ";
    
            ArrayList<String> clauses =  new ArrayList<String>();
            if (null != accountId) {
                final String requesterIdText = Integer.toString(requesterId);
                if (!requesterIdText.equals(accountId)) {
                    ServiceLogger.log(LOGTAG, "invalid request for user " + userEPPN + " " + requesterIdText + " != " + accountId);
                    throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, "invalid request for user " + userEPPN);
                }
                clauses.add("follower = " + requesterId);
            }
    
            if (null != profileList) {
                if (SQLUtils.isListValidIntegers(profileList)) {
                    clauses.add("followed in " + profileList);
                }
            }
    
            query += SQLUtils.formatClauses(clauses, true);
    
            final ArrayList<String> validSortFields = new ArrayList<String>();
            validSortFields.add("follower");
            validSortFields.add("followed");
            query += SQLUtils.buildOrderByClause(order,  sort, validSortFields);
            query += SQLUtils.buildLimitClause(limit);
            query += SQLUtils.buildOffsetClause(start);
    
            PreparedStatement resultStatement = DBConnector.prepareStatement(query);
            final ResultSet resultSet = resultStatement.executeQuery();
            while (resultSet.next()) {
                final FollowingMember followResult = readFollowResultSet(resultSet);
                list.add(followResult);
            }
            return list;
        } catch (DMCServiceException dmce) {
            throw dmce;
        } catch (Exception e) {
            String msg = "bad data or other unexpected error: " + e.getMessage();
            ServiceLogger.log(LOGTAG, msg);
            throw new DMCServiceException(DMCError.BadURL, msg);
        }
    }

    public FollowingMember followingMembersPost(PostFollowingMember followRequest, String userEPPN) {
        final Connection connection = DBConnector.connection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            ServiceLogger.log(LOGTAG, "unexpected database access error" + ex.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, "unexpected database access error" + ex.getMessage());
        }

        try {
            if (null == followRequest) {
                ServiceLogger.log(LOGTAG, "no follow request sent");
                throw new DMCServiceException(DMCError.BadURL, "no follow request sent");
            }

            final int requesterId = UserDao.getUserID(userEPPN);
            final String requesterIdText = Integer.toString(requesterId);
            if (!requesterIdText.equals(followRequest.getAccountId())) {
                ServiceLogger.log(LOGTAG, "invalid request for user " + userEPPN + " " + requesterIdText + " != " + followRequest.getAccountId());
                throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, "invalid request for user " + userEPPN);
            }

            final int followedId = Integer.parseInt(followRequest.getProfileId());
            final String query = "insert into user_following (follower, followed) values (?, ?) ";

            ServiceLogger.log(LOGTAG, "follow query: " + query);
            ServiceLogger.log(LOGTAG, "requester/follower id: " + requesterId);
            ServiceLogger.log(LOGTAG, "followed id: " + followedId);
            PreparedStatement statement = DBConnector.prepareStatement(query);
            statement.setInt(1, requesterId);
            statement.setInt(2, followedId);
            try {
                statement.executeUpdate();
            } catch (SQLException sqle) {
                // if we already followed, then just continue
                if (!sqle.getMessage().contains("duplicate key")) {
                    ServiceLogger.log(LOGTAG, "Unable to follow " + sqle.getMessage());
                    throw new DMCServiceException(DMCError.OtherSQLError, "Unable to follow " + sqle.getMessage());
                }
            }

            final String resultQuery = "select follower, followed from user_following where follower = ? and followed = ?";
            PreparedStatement resultStatement = DBConnector.prepareStatement(resultQuery);
            resultStatement.setInt(1, requesterId);
            resultStatement.setInt(2,  followedId);
            final ResultSet resultSet = resultStatement.executeQuery();
            if (resultSet.next()) {
                final FollowingMember followResult = readFollowResultSet(resultSet);
                connection.commit();
                return followResult;
            } else {
                ServiceLogger.log(LOGTAG, "unable to add follow request - no entry found after insert");
                throw new DMCServiceException(DMCError.OtherSQLError, "unable to add follow request");
            }
        } catch (DMCServiceException dmce) {
            try {
                connection.rollback();
                throw dmce;
            } catch (SQLException re) {
                throw new DMCServiceException(dmce.getError(), dmce.getMessage() + " AND unable to rollback " + re.getMessage());
            }
        }   catch (Exception e) {
            String msg = "bad data or other unexpected error: " + e.getMessage();
            ServiceLogger.log(LOGTAG, msg);
            try {
                connection.rollback();
                throw new DMCServiceException(DMCError.BadURL, msg);
            } catch (SQLException re) {
                throw new DMCServiceException(DMCError.BadURL, msg + " AND unable to rollback " + re.getMessage());
            }
        } finally {
            if (null != connection) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    // don't really need to do anything
                }
            }
        }
    }

    private FollowingMember readFollowResultSet(ResultSet resultSet) throws Exception {
        final FollowingMember item = new FollowingMember();
        item.setAccountId(Integer.toString(resultSet.getInt("follower")));
        item.setProfileId(Integer.toString(resultSet.getInt("followed")));
        return item;
    }
}
