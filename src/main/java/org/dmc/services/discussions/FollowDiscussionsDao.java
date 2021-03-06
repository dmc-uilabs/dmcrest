package org.dmc.services.discussions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.data.dao.user.UserDao;

public class FollowDiscussionsDao {

    private static final String LOGTAG = FollowDiscussionsDao.class.getName();

    public FollowingIndividualDiscussion createFollowDiscussion(FollowingIndividualDiscussion followDiscussion,
            String userEPPN) throws DMCServiceException {
        final FollowingIndividualDiscussion retObj = new FollowingIndividualDiscussion();
        final Connection connection = DBConnector.connection();
        final Util util = Util.getInstance();
        UserDao.checkUserMatchesId(followDiscussion.getAccountId(), userEPPN);

        try {
            connection.setAutoCommit(false);

            final String followQuery = "INSERT into individual_discussions_following (individual_discussion_id, account_id) values ( ?, ? )";

            final PreparedStatement preparedStatementQuery = DBConnector.prepareStatement(followQuery,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatementQuery.setInt(1, Integer.parseInt(followDiscussion.getIndividualDiscussionId()));
            preparedStatementQuery.setInt(2, Integer.parseInt(followDiscussion.getAccountId()));

            final int rowsAffected_interface = preparedStatementQuery.executeUpdate();
            if (rowsAffected_interface != 1) {
                connection.rollback();
                throw new DMCServiceException(DMCError.OtherSQLError,
                        "unable to add individual discussion follow" + followDiscussion.toString());
            }
            final int id = util.getGeneratedKey(preparedStatementQuery, "id");

            retObj.setId(Integer.toString(id));
            retObj.setIndividualDiscussionId(followDiscussion.getIndividualDiscussionId());
            retObj.setAccountId(followDiscussion.getAccountId());
            connection.commit();

        } catch (SQLException se) {

            if (se.getMessage().contains("duplicate key value violates unique constraint")) {
                // this is ok, let's send the result back, but nothing to commit
                // because record already existed.
                return getFollowedIndividualDiscussion(followDiscussion.getAccountId(), followDiscussion.getIndividualDiscussionId(),userEPPN);
            }

            try {
                connection.rollback();
            } catch (SQLException e) {
                ServiceLogger.log(LOGTAG, e.getMessage());
                // don't need to throw this, but will throw the exception that started the catch below...
            }
            if (se.getMessage().startsWith(
                    "ERROR: insert or update on table \"individual_discussions_following\" violates foreign key constraint \"individualdiscussionsfollowing_accountid_fk\"")) {
                throw new DMCServiceException(DMCError.InvalidAccountId, se.getMessage());
            } else if (se.getMessage().startsWith(
                    "ERROR: insert or update on table \"individual_discussions_following\" violates foreign key constraint \"individualdiscussionsfollowing_individualdiscussionid_fk\"")) {
                throw new DMCServiceException(DMCError.InvalidDiscussionId, se.getMessage());
            } else {
                ServiceLogger.log(LOGTAG, se.getMessage());
                throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException se) {
                ServiceLogger.log(LOGTAG, se.getMessage());
            }
        }
        return retObj;
    }

    public void deleteFollowDiscussion(String followId, String userEPPN) throws DMCServiceException {
        final Connection connection = DBConnector.connection();

        try {
            connection.setAutoCommit(false);
            int userID = UserDao.getUserID(userEPPN);

            final String query = "DELETE FROM individual_discussions_following WHERE id = ? and account_id = ?";

            final PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(followId));
            preparedStatement.setInt(2, userID);
            final int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected < 1) {
                connection.rollback();
                throw new DMCServiceException(DMCError.DiscussionFollowNotFound,
                        "error trying to unfollow discussion with follow_id " + followId);
            }
            connection.commit();
        } catch (SQLException se) {
            ServiceLogger.log(LOGTAG, se.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e) {
                ServiceLogger.log(LOGTAG, e.getMessage());
            }
            throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException se) {
                ServiceLogger.log(LOGTAG, se.getMessage());
            }
        }
    }

    public List<FollowingIndividualDiscussion> getFollowedDiscussionsforAccount(String accountId,
            String individualDiscussionId, Integer limit, String order, String sort, String userEPPN)
                    throws DMCServiceException {
        final Connection connection = DBConnector.connection();
        final List<FollowingIndividualDiscussion> followedDiscussions = new ArrayList<FollowingIndividualDiscussion>();

        UserDao.checkUserMatchesId(accountId, userEPPN);

        try {
            connection.setAutoCommit(false);

            final ArrayList<String> columnsInIndividualDiscussionsFollowingTable = new ArrayList<String>();
            columnsInIndividualDiscussionsFollowingTable.add("id");
            columnsInIndividualDiscussionsFollowingTable.add("individual_discussion_id");
            columnsInIndividualDiscussionsFollowingTable.add("account_id");
            String followingQuery;
            PreparedStatement preparedStatement;

            if (individualDiscussionId == null) {
                followingQuery = "SELECT * FROM individual_discussions_following WHERE account_id = ?";

                if (sort == null) {
                    followingQuery += " ORDER BY id";
                } else if (!columnsInIndividualDiscussionsFollowingTable.contains(sort)) {
                    followingQuery += " ORDER BY id";
                } else {
                    followingQuery += " ORDER BY " + sort;
                }

                if (order == null) {
                    followingQuery += " ASC";
                } else if (!order.equals("ASC") && !order.equals("DESC")) {
                    followingQuery += " ASC";
                } else {
                    followingQuery += " " + order;
                }

                if (limit == null) {
                    followingQuery += " LIMIT ALL";
                } else if (limit < 0) {
                    followingQuery += " LIMIT 0";
                } else {
                    followingQuery += " LIMIT " + limit;
                }

                preparedStatement = DBConnector.prepareStatement(followingQuery);
                preparedStatement.setInt(1, Integer.parseInt(accountId));

            } else {
                followingQuery = "SELECT * FROM individual_discussions_following WHERE account_id = ? AND individual_discussion_id = ?";
                preparedStatement = DBConnector.prepareStatement(followingQuery);
                preparedStatement.setInt(1, Integer.parseInt(accountId));
                preparedStatement.setInt(2, Integer.parseInt(individualDiscussionId));
            }

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                final FollowingIndividualDiscussion discussionInTable = new FollowingIndividualDiscussion();

                discussionInTable.setId(Integer.toString(resultSet.getInt("id")));
                discussionInTable
                        .setIndividualDiscussionId(Integer.toString(resultSet.getInt("individual_discussion_id")));
                discussionInTable.setAccountId(Integer.toString(resultSet.getInt("account_id")));

                followedDiscussions.add(discussionInTable);
            }

        } catch (SQLException se) {
            ServiceLogger.log(LOGTAG, se.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e) {
                ServiceLogger.log(LOGTAG, e.getMessage());
            }
            throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException se) {
                ServiceLogger.log(LOGTAG, se.getMessage());
            }

        }

        return followedDiscussions;
    }

    public FollowingIndividualDiscussion getFollowedIndividualDiscussion(String accountId, String individualDiscussionId,
            String userEPPN) throws DMCServiceException {
        final List<FollowingIndividualDiscussion> followedDiscussions = getFollowedDiscussionsforAccount(accountId,
                individualDiscussionId, null, null, null, userEPPN);
        if (followedDiscussions.size() == 1) {
            return followedDiscussions.get(0);
        } else if (followedDiscussions.size() == 0) {
            return null;
        } else {
            throw new DMCServiceException(DMCError.OtherSQLError, "found too many results for follow discussions for "
                    + userEPPN + " and discussion id " + individualDiscussionId);
        }
    }

}
