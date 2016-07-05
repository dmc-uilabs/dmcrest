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

	private final String logTag = IndividualDiscussionTagsDao.class.getName();

	public IndividualDiscussionTag createIndividualDiscussionTag(IndividualDiscussionTag tag) throws DMCServiceException {
		IndividualDiscussionTag retObj = new IndividualDiscussionTag();
		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();

		try {
			connection.setAutoCommit(false);
			String discussionHelpfulQuery = "INSERT into individual_discussions_tags (individual_discussion_id, name) values ( ?, ? )";

			PreparedStatement preparedStatementQuery = DBConnector.prepareStatement(discussionHelpfulQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatementQuery.setInt(1, Integer.parseInt(tag.getIndividualDiscussionId()));
			preparedStatementQuery.setString(2, tag.getName());

			int rowsAffected_interface = preparedStatementQuery.executeUpdate();
			if (rowsAffected_interface != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to add individual discussion tag " + tag.toString());
			}
			int id = util.getGeneratedKey(preparedStatementQuery, "id");

			retObj.setId(Integer.toString(id));
			retObj.setIndividualDiscussionId(tag.getIndividualDiscussionId());
			retObj.setName(tag.getName());

		} catch (SQLException se) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
			if (se.getMessage().startsWith(
					"ERROR: insert or update on table \"individual_discussions_tags\" violates foreign key constraint \"individualdiscussionstags_individualdiscussionid_fk\"")) {
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

	public List<IndividualDiscussionTag> getTagsForSingleDiscussionId(Integer limit, String order, String sort, String individualDiscussionId) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		IndividualDiscussionTag retObj = null;
		List<IndividualDiscussionTag> tags = new ArrayList<IndividualDiscussionTag>();

		try {
			connection.setAutoCommit(false);

			ArrayList<String> columnsInIndividualDiscussionsTagsTable = new ArrayList<String>();
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

			PreparedStatement preparedStatement = DBConnector.prepareStatement(tagQuery);
			preparedStatement.setInt(1, Integer.parseInt(individualDiscussionId));
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getResultSet();

			while (resultSet.next()) {
				retObj = new IndividualDiscussionTag();

				retObj.setId(Integer.toString(resultSet.getInt("id")));
				retObj.setIndividualDiscussionId(Integer.toString(resultSet.getInt("individual_discussion_id")));
				retObj.setName(resultSet.getString("name"));

				tags.add(retObj);
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

		return tags;
	}

}
