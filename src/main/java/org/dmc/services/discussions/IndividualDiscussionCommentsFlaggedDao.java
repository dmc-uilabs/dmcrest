package org.dmc.services.discussions;

import java.math.BigDecimal;
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

public class IndividualDiscussionCommentsFlaggedDao {

	private final String logTag = IndividualDiscussionCommentsFlaggedDao.class.getName();

	public IndividualDiscussionCommentFlagged createFlagForComment(IndividualDiscussionCommentFlagged flag) throws DMCServiceException {
		IndividualDiscussionCommentFlagged retObj = new IndividualDiscussionCommentFlagged();
		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();

		try {
			connection.setAutoCommit(false);

			String flagQuery = "INSERT into individual_discussions_comments_flagged (account_id, comment_id, reason, comment) values ( ?, ?, ?, ? )";

			PreparedStatement preparedStatementQuery = DBConnector.prepareStatement(flagQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatementQuery.setInt(1, Integer.parseInt(flag.getAccountId()));
			preparedStatementQuery.setInt(2, Integer.parseInt(flag.getCommentId()));
			preparedStatementQuery.setString(3, flag.getReason());
			preparedStatementQuery.setString(4, flag.getComment());

			int rowsAffected_interface = preparedStatementQuery.executeUpdate();
			if (rowsAffected_interface != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to add individual discussion comment flag " + flag.toString());
			}
			int id = util.getGeneratedKey(preparedStatementQuery, "id");

			retObj.setId(Integer.toString(id));
			retObj.setAccountId(flag.getAccountId());
			retObj.setCommentId(flag.getCommentId());
			retObj.setReason(flag.getReason());
			retObj.setComment(flag.getComment());

		} catch (SQLException se) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
			if (se.getMessage().startsWith("ERROR: insert or update on table \"individual_discussions_comments_flagged\" violates foreign key constraint \"individualdiscussionscommentsflagged_accountid_fk\"")) {
				throw new DMCServiceException(DMCError.InvalidAccountId, se.getMessage());
			} if (se.getMessage().startsWith("ERROR: insert or update on table \"individual_discussions_comments_flagged\" violates foreign key constraint \"individualdiscussionscommentsflagged_commentid_fk\"")) {
				throw new DMCServiceException(DMCError.InvalidCommentId, se.getMessage());
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

	public IndividualDiscussionCommentFlagged getCommentFlagged(String commentId, String accountId) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		IndividualDiscussionCommentFlagged retObj = null;

		try {
			connection.setAutoCommit(false);
			String flagQuery = "SELECT * FROM individual_discussions_comments_flagged WHERE account_id = ? AND comment_id = ?";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(flagQuery);
			preparedStatement.setInt(1, Integer.parseInt(accountId));
			preparedStatement.setInt(2, Integer.parseInt(commentId));
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getResultSet();

			if (resultSet.next()) {
				retObj = new IndividualDiscussionCommentFlagged();

				retObj.setId(Integer.toString(resultSet.getInt("id")));
				retObj.setAccountId(Integer.toString(resultSet.getInt("account_id")));
				retObj.setCommentId(Integer.toString(resultSet.getInt("comment_id")));
				retObj.setReason(resultSet.getString("reason"));
				retObj.setComment(resultSet.getString("comment"));
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

		return retObj;
	}
	
}
