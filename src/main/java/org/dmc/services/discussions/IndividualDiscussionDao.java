package org.dmc.services.discussions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.services.GetDomeInterface;
import org.dmc.services.sharedattributes.Util;

public class IndividualDiscussionDao {

	private final String logTag = DiscussionDao.class.getName();

	public List<IndividualDiscussion> getListOfCommunityIndividualDiscussions(Integer limit, String order, String sort) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		IndividualDiscussion retObj = null;
		List<IndividualDiscussion> individualDiscussions = new ArrayList<IndividualDiscussion>();

		try {
			connection.setAutoCommit(false);

			ArrayList<String> columnsInIndividualDiscussionTable = new ArrayList<String>();
			columnsInIndividualDiscussionTable.add("id");
			columnsInIndividualDiscussionTable.add("title");
			columnsInIndividualDiscussionTable.add("created_by");
			columnsInIndividualDiscussionTable.add("created_at");
			columnsInIndividualDiscussionTable.add("account_id");
			columnsInIndividualDiscussionTable.add("project_id");

			String domeInterfacesQuery = "SELECT * FROM individual_discussions";

			if (sort == null) {
				domeInterfacesQuery += " ORDER BY id";
			} else if (!columnsInIndividualDiscussionTable.contains(sort)) {
				domeInterfacesQuery += " ORDER BY id";
			} else {
				domeInterfacesQuery += " ORDER BY " + sort;
			}

			if (order == null) {
				domeInterfacesQuery += " ASC";
			} else if (!order.equals("ASC") && !order.equals("DESC")) {
				domeInterfacesQuery += " ASC";
			} else {
				domeInterfacesQuery += " " + order;
			}

			if (limit == null) {
				domeInterfacesQuery += " LIMIT ALL";
			} else if (limit < 0) {
				domeInterfacesQuery += " LIMIT 0";
			} else {
				domeInterfacesQuery += " LIMIT " + limit;
			}

			PreparedStatement preparedStatement = DBConnector.prepareStatement(domeInterfacesQuery);
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getResultSet();

			while (resultSet.next()) {
				if (new BigDecimal(resultSet.getInt("project_id")) != null) {
					retObj = new IndividualDiscussion();

					retObj.setId(Integer.toString(resultSet.getInt("id")));
					retObj.setTitle(resultSet.getString("title"));
					retObj.setCreatedBy(resultSet.getString("created_by"));
					retObj.setCreatedAt(new BigDecimal(resultSet.getString("created_at")));
					retObj.setAccountId(new BigDecimal(resultSet.getInt("account_id")));
					retObj.setProjectId(new BigDecimal(resultSet.getInt("project_id")));

					individualDiscussions.add(retObj);
				}
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

		return individualDiscussions;
	}

	public IndividualDiscussion createIndividualDiscussion(IndividualDiscussion discussion) throws DMCServiceException {
		IndividualDiscussion retObj = new IndividualDiscussion();
		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();

		try {
			connection.setAutoCommit(false);

			String domeInterfaceQuery = "INSERT into individual_discussions (title, created_by, created_at, account_id, project_id) values ( ?, ?, ?, ?, ? )";

			PreparedStatement preparedStatementDomeInterfaceQuery = DBConnector.prepareStatement(domeInterfaceQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatementDomeInterfaceQuery.setString(1, discussion.getTitle());
			preparedStatementDomeInterfaceQuery.setString(2, discussion.getCreatedBy());
			preparedStatementDomeInterfaceQuery.setString(3, discussion.getCreatedAt().toString());
			preparedStatementDomeInterfaceQuery.setInt(4, discussion.getAccountId().intValue());
			preparedStatementDomeInterfaceQuery.setInt(5, discussion.getProjectId().intValue());

			int rowsAffected_interface = preparedStatementDomeInterfaceQuery.executeUpdate();
			if (rowsAffected_interface != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to add individual discussion " + discussion.toString());
			}
			int id = util.getGeneratedKey(preparedStatementDomeInterfaceQuery, "id");

			retObj.setId(Integer.toString(id));
			retObj.setTitle(discussion.getTitle());
			retObj.setCreatedBy(discussion.getCreatedBy());
			retObj.setCreatedAt(discussion.getCreatedAt());
			retObj.setAccountId(discussion.getAccountId());
			retObj.setProjectId(discussion.getProjectId());

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
	
	public IndividualDiscussion getSingleIndividualDiscussionFromId(String id) throws DMCServiceException {
		Connection connection = DBConnector.connection();
		IndividualDiscussion retObj = null;

		try {
			connection.setAutoCommit(false);
			String domeInterfacesQuery = "SELECT * FROM individual_discussions WHERE id = ?";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(domeInterfacesQuery);
			preparedStatement.setInt(1, Integer.parseInt(id));
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getResultSet();

			if (resultSet.next()) {
				retObj = new IndividualDiscussion();

				retObj.setId(Integer.toString(resultSet.getInt("id")));
				retObj.setTitle(resultSet.getString("title"));
				retObj.setCreatedBy(resultSet.getString("created_by"));
				retObj.setCreatedAt(new BigDecimal(resultSet.getString("created_at")));
				retObj.setAccountId(new BigDecimal(resultSet.getInt("account_id")));
				retObj.setProjectId(new BigDecimal(resultSet.getInt("project_id")));
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
	
	public IndividualDiscussionComment createIndividualDiscussionComment(IndividualDiscussionComment comment) throws DMCServiceException {
		IndividualDiscussionComment retObj = new IndividualDiscussionComment();
		Connection connection = DBConnector.connection();
		Util util = Util.getInstance();

		try {
			connection.setAutoCommit(false);
			String domeInterfaceQuery = "INSERT into individual_discussions_comments (individual_discussion_id, full_name, account_id, comment_id, avatar, reply, text, created_at, likes, dislikes) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

			PreparedStatement preparedStatementDomeInterfaceQuery = DBConnector.prepareStatement(domeInterfaceQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatementDomeInterfaceQuery.setInt(1, Integer.parseInt(comment.getIndividualDiscussionId()));
			preparedStatementDomeInterfaceQuery.setString(2, comment.getFullName());
			preparedStatementDomeInterfaceQuery.setInt(3, comment.getAccountId().intValue());
			preparedStatementDomeInterfaceQuery.setInt(4, comment.getCommentId().intValue());
			preparedStatementDomeInterfaceQuery.setString(5, comment.getAvatar());
			preparedStatementDomeInterfaceQuery.setBoolean(6, comment.getReply());
			preparedStatementDomeInterfaceQuery.setString(7, comment.getText());
			preparedStatementDomeInterfaceQuery.setString(8, comment.getCreatedAt().toString());
			preparedStatementDomeInterfaceQuery.setInt(9, comment.getLike().intValue());
			preparedStatementDomeInterfaceQuery.setInt(10, comment.getDislike().intValue());

			int rowsAffected_interface = preparedStatementDomeInterfaceQuery.executeUpdate();
			if (rowsAffected_interface != 1) {
				connection.rollback();
				throw new DMCServiceException(DMCError.OtherSQLError, "unable to add individual discussion comment " + comment.toString());
			}
			int id = util.getGeneratedKey(preparedStatementDomeInterfaceQuery, "id");

			retObj.setId(Integer.toString(id));
			retObj.setIndividualDiscussionId(comment.getIndividualDiscussionId());
			retObj.setFullName(comment.getFullName());
			retObj.setAccountId(comment.getAccountId());
			retObj.setCommentId(comment.getCommentId());
			retObj.setAvatar(comment.getAvatar());
			retObj.setReply(comment.getReply());
			retObj.setText(comment.getText());
			retObj.setCreatedAt(comment.getCreatedAt());
			retObj.setLike(comment.getLike());
			retObj.setDislike(comment.getDislike());
			
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
