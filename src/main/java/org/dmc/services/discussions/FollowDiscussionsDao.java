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

public class FollowDiscussionsDao {

	private final String logTag = FollowDiscussionsDao.class.getName();

	public FollowingIndividualDiscussion createFollowDiscussion(FollowingIndividualDiscussion followDiscussion) throws DMCServiceException {
		FollowingIndividualDiscussion retObj = new FollowingIndividualDiscussion();
		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();

		try {
			connection.setAutoCommit(false);

			String followQuery = "INSERT into individual_discussions_following (individual_discussion_id, account_id) values ( ?, ? )";

			PreparedStatement preparedStatementQuery = DBConnector.prepareStatement(followQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatementQuery.setInt(1, Integer.parseInt(followDiscussion.getIndividualDiscussionId()));
			preparedStatementQuery.setInt(2, Integer.parseInt(followDiscussion.getAccountId()));

			int rowsAffected_interface = preparedStatementQuery.executeUpdate();
			if (rowsAffected_interface != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to add individual discussion follow" + followDiscussion.toString());
			}
			int id = util.getGeneratedKey(preparedStatementQuery, "id");

			retObj.setId(Integer.toString(id));
			retObj.setIndividualDiscussionId(followDiscussion.getIndividualDiscussionId());
			retObj.setAccountId(followDiscussion.getAccountId());

		} catch (SQLException se) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
			if (se.getMessage().startsWith(
					"ERROR: insert or update on table \"individual_discussions_following\" violates foreign key constraint \"individualdiscussionsfollowing_accountid_fk\"")) {
				throw new DMCServiceException(DMCError.InvalidAccountId, se.getMessage());
			} else if (se.getMessage().startsWith(
					"ERROR: insert or update on table \"individual_discussions_following\" violates foreign key constraint \"individualdiscussionsfollowing_individualdiscussionid_fk\"")) {
				throw new DMCServiceException(DMCError.InvalidDiscussionId, se.getMessage());
			} else {
				ServiceLogger.log(logTag, se.getMessage());
				throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(logTag, se.getMessage());
			}
		}
		return retObj;
	}

	public void deleteFollowDiscussion(String followId) throws DMCServiceException {
		Connection connection = DBConnector.connection();

		try {
			connection.setAutoCommit(false);

			String query = "DELETE FROM individual_discussions_following WHERE id = ?";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(followId));
			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected < 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "error trying to unfollow discussion with follow_id " + followId);
			}

		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(logTag, se.getMessage());
			}
		}
	}
	
	public List<FollowingIndividualDiscussion> getFollowedDiscussionsforAccount(String accountId, String individualDiscussionId, Integer limit, String order, String sort) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		FollowingIndividualDiscussion retObj = null;
		List<FollowingIndividualDiscussion> followedDiscussions = new ArrayList<FollowingIndividualDiscussion>();

		try {
			connection.setAutoCommit(false);

			ArrayList<String> columnsInIndividualDiscussionsFollowingTable = new ArrayList<String>();
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
				retObj = new FollowingIndividualDiscussion();

				retObj.setId(Integer.toString(resultSet.getInt("id")));
				retObj.setIndividualDiscussionId(Integer.toString(resultSet.getInt("individual_discussion_id")));
				retObj.setAccountId(Integer.toString(resultSet.getInt("account_id")));

				followedDiscussions.add(retObj);
			}

		} catch (SQLException se) {
			ServiceLogger.log(logTag, se.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
			throw new DMCServiceException(DMCError.OtherSQLError, se.getMessage());

		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException se) {
				ServiceLogger.log(logTag, se.getMessage());
			}

		}

		return followedDiscussions;
	}

}
