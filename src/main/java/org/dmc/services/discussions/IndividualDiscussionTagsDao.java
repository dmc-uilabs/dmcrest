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

public class IndividualDiscussionTagsDao {

	private static final String LOGTAG = IndividualDiscussionTagsDao.class.getName();

	public IndividualDiscussionTag createIndividualDiscussionTag(IndividualDiscussionTag tag) throws DMCServiceException {
		final IndividualDiscussionTag retObj = new IndividualDiscussionTag();
		final Connection connection = DBConnector.connection();
		final Util util = Util.getInstance();

		try {
			connection.setAutoCommit(false);
			final String discussionHelpfulQuery = "INSERT into individual_discussions_tags (individual_discussion_id, name) values ( ?, ? )";

			final PreparedStatement preparedStatementQuery = DBConnector.prepareStatement(discussionHelpfulQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatementQuery.setInt(1, Integer.parseInt(tag.getIndividualDiscussionId()));
			preparedStatementQuery.setString(2, tag.getName());

			final int rowsAffected_interface = preparedStatementQuery.executeUpdate();
			if (rowsAffected_interface != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to add individual discussion tag " + tag.toString());
			}
			final int id = util.getGeneratedKey(preparedStatementQuery, "id");

			retObj.setId(Integer.toString(id));
			retObj.setIndividualDiscussionId(tag.getIndividualDiscussionId());
			retObj.setName(tag.getName());

		} catch (SQLException se) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(LOGTAG, e.getMessage());
			}
			if (se.getMessage().startsWith(
					"ERROR: insert or update on table \"individual_discussions_tags\" violates foreign key constraint \"individualdiscussionstags_individualdiscussionid_fk\"")) {
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

	public List<IndividualDiscussionTag> getTagsForSingleDiscussionId(Integer limit, String order, String sort, String individualDiscussionId) throws DMCServiceException {
		final Connection connection = DBConnector.connection();
		final List<IndividualDiscussionTag> tags = new ArrayList<IndividualDiscussionTag>();

		try {
			connection.setAutoCommit(false);

			final ArrayList<String> columnsInIndividualDiscussionsTagsTable = new ArrayList<String>();
			columnsInIndividualDiscussionsTagsTable.add("id");
			columnsInIndividualDiscussionsTagsTable.add("individual_discussion_id");
			columnsInIndividualDiscussionsTagsTable.add("name");

			String tagQuery = "SELECT * FROM individual_discussions_tags WHERE individual_discussion_id = ?";

			if (sort == null) {
				tagQuery += " ORDER BY id";
			} else if (!columnsInIndividualDiscussionsTagsTable.contains(sort)) {
				tagQuery += " ORDER BY id";
			} else {
				tagQuery += " ORDER BY " + sort;
			}

			if (order == null) {
				tagQuery += " ASC";
			} else if (!order.equals("ASC") && !order.equals("DESC")) {
				tagQuery += " ASC";
			} else {
				tagQuery += " " + order;
			}

			if (limit == null) {
				tagQuery += " LIMIT ALL";
			} else if (limit < 0) {
				tagQuery += " LIMIT 0";
			} else {
				tagQuery += " LIMIT " + limit;
			}

			final PreparedStatement preparedStatement = DBConnector.prepareStatement(tagQuery);
			preparedStatement.setInt(1, Integer.parseInt(individualDiscussionId));
			preparedStatement.execute();
			final ResultSet resultSet = preparedStatement.getResultSet();

			while (resultSet.next()) {
				final IndividualDiscussionTag retObj = new IndividualDiscussionTag();

				retObj.setId(Integer.toString(resultSet.getInt("id")));
				retObj.setIndividualDiscussionId(Integer.toString(resultSet.getInt("individual_discussion_id")));
				retObj.setName(resultSet.getString("name"));

				tags.add(retObj);
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

		return tags;
	}

}
