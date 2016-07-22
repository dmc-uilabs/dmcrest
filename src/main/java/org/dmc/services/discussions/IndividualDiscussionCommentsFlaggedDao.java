package org.dmc.services.discussions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.UserDao;

public class IndividualDiscussionCommentsFlaggedDao {

	private static final String LOGTAG = IndividualDiscussionCommentsFlaggedDao.class.getName();

	public IndividualDiscussionCommentFlagged createFlagForComment(IndividualDiscussionCommentFlagged flag, String userEPPN) throws DMCServiceException {
		final IndividualDiscussionCommentFlagged retObj = new IndividualDiscussionCommentFlagged();
		final Connection connection = DBConnector.connection();
		final Util util = Util.getInstance();

		try {
			final int userID = UserDao.getUserID(userEPPN);
			if (userID != Integer.parseInt(flag.getAccountId())) {
				throw new DMCServiceException(DMCError.InvalidAccountId, "AccountId does not match userEPPN");
			}
		} catch (SQLException se) {
			ServiceLogger.log(LOGTAG, se.getMessage());
			throw new DMCServiceException(DMCError.UnknownUser, se.getMessage());
		}

		try {
			connection.setAutoCommit(false);

			final String flagQuery = "INSERT into individual_discussions_comments_flagged (account_id, comment_id, reason, comment) values ( ?, ?, ?, ? )";

			final PreparedStatement preparedStatementQuery = DBConnector.prepareStatement(flagQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatementQuery.setInt(1, Integer.parseInt(flag.getAccountId()));
			preparedStatementQuery.setInt(2, Integer.parseInt(flag.getCommentId()));
			preparedStatementQuery.setString(3, flag.getReason());
			preparedStatementQuery.setString(4, flag.getComment());

			final int rowsAffected_interface = preparedStatementQuery.executeUpdate();
			if (rowsAffected_interface != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to add individual discussion comment flag " + flag.toString());
			}
			final int id = util.getGeneratedKey(preparedStatementQuery, "id");

			retObj.setId(Integer.toString(id));
			retObj.setAccountId(flag.getAccountId());
			retObj.setCommentId(flag.getCommentId());
			retObj.setReason(flag.getReason());
			retObj.setComment(flag.getComment());

		} catch (SQLException se) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(LOGTAG, e.getMessage());
			}
			if (se.getMessage().startsWith(
					"ERROR: insert or update on table \"individual_discussions_comments_flagged\" violates foreign key constraint \"individualdiscussionscommentsflagged_accountid_fk\"")) {
				throw new DMCServiceException(DMCError.InvalidAccountId, se.getMessage());
			}
			if (se.getMessage().startsWith(
					"ERROR: insert or update on table \"individual_discussions_comments_flagged\" violates foreign key constraint \"individualdiscussionscommentsflagged_commentid_fk\"")) {
				throw new DMCServiceException(DMCError.InvalidCommentId, se.getMessage());
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

	public IndividualDiscussionCommentFlagged getCommentFlagged(String commentId, String accountId, String userEPPN) throws DMCServiceException {
		final Connection connection = DBConnector.connection();
		IndividualDiscussionCommentFlagged retObj = null;

		try {
			final int userID = UserDao.getUserID(userEPPN);
			if (userID != Integer.parseInt(accountId)) {
				throw new DMCServiceException(DMCError.InvalidAccountId, "AccountId does not match userEPPN");
			}
		} catch (SQLException se) {
			ServiceLogger.log(LOGTAG, se.getMessage());
			throw new DMCServiceException(DMCError.UnknownUser, se.getMessage());
		}

		try {
			connection.setAutoCommit(false);
			final String flagQuery = "SELECT * FROM individual_discussions_comments_flagged WHERE account_id = ? AND comment_id = ?";

			final PreparedStatement preparedStatement = DBConnector.prepareStatement(flagQuery);
			preparedStatement.setInt(1, Integer.parseInt(accountId));
			preparedStatement.setInt(2, Integer.parseInt(commentId));
			preparedStatement.execute();
			final ResultSet resultSet = preparedStatement.getResultSet();

			if (resultSet.next()) {
				retObj = new IndividualDiscussionCommentFlagged();

				retObj.setId(Integer.toString(resultSet.getInt("id")));
				retObj.setAccountId(Integer.toString(resultSet.getInt("account_id")));
				retObj.setCommentId(Integer.toString(resultSet.getInt("comment_id")));
				retObj.setReason(resultSet.getString("reason"));
				retObj.setComment(resultSet.getString("comment"));
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

		return retObj;
	}

}
