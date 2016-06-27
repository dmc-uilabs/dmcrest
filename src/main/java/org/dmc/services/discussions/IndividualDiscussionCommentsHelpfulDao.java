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

public class IndividualDiscussionCommentsHelpfulDao {

	private final String logTag = DiscussionDao.class.getName();

	public IndividualDiscussionCommentHelpful createIndividualDiscussionCommentHelpful(IndividualDiscussionCommentHelpful commentHelpful) throws DMCServiceException {
		IndividualDiscussionCommentHelpful retObj = new IndividualDiscussionCommentHelpful();
		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();

		try {
			connection.setAutoCommit(false);
			String discussionHelpfulQuery = "INSERT into individual_discussions_comments_helpful (account_id, comment_id, helpful) values ( ?, ?, ? )";

			PreparedStatement preparedStatementQuery = DBConnector.prepareStatement(discussionHelpfulQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatementQuery.setInt(1, Integer.parseInt(commentHelpful.getAccountId()));
			preparedStatementQuery.setInt(2, Integer.parseInt(commentHelpful.getCommentId()));
			preparedStatementQuery.setBoolean(3, commentHelpful.getHelpful());

			int rowsAffected_interface = preparedStatementQuery.executeUpdate();
			if (rowsAffected_interface != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to add individual discussion comment helpful vote " + commentHelpful.toString());
			}
			int id = util.getGeneratedKey(preparedStatementQuery, "id");

			retObj.setId(Integer.toString(id));
			retObj.setAccountId(commentHelpful.getAccountId());
			retObj.setCommentId(commentHelpful.getCommentId());
			retObj.setHelpful(commentHelpful.getHelpful());
			
		} catch (SQLException se) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
			if (se.getMessage().startsWith("ERROR: insert or update on table \"individual_discussions_comments_helpful\" violates foreign key constraint \"individualdiscussionscommentshelpful_accountid_fk\"")) {
				throw new DMCServiceException(DMCError.InvalidAccountId, se.getMessage());
			} else if (se.getMessage().startsWith("ERROR: insert or update on table \"individual_discussions_comments_helpful\" violates foreign key constraint \"individualdiscussionscommentshelpful_commentid_fk\"")) {
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

}
