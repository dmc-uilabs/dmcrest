package org.dmc.services.discussions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.xml.ws.http.HTTPException;

import org.dmc.services.DBConnector;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.Util;

public class DiscussionDao {

	private Connection connection;
	private Util util;
	private final String logTag = DiscussionDao.class.getName();

	public DiscussionDao() {
	}

	public Id createDiscussion(Discussion discussion, String userEPPN) throws HTTPException {

		connection = DBConnector.connection();
		PreparedStatement statement;
		util = Util.getInstance();
		int id = -99999;

		try {

			connection.setAutoCommit(false);

			String query = "INSERT INTO forum_messages(user_id, body, time_posted, topic_id, reply_to) VALUES ( ?, ?, ? , ?, ?)";
			statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, Integer.parseInt(discussion.getAccountId()));
			statement.setString(2, discussion.getMessage());
			statement.setInt(3, discussion.getCreatedAt().intValue());
			statement.setInt(4, Integer.parseInt(discussion.getProjectId()));
			statement.setInt(5, Integer.parseInt(discussion.getAccountId()));
			statement.executeUpdate();

			id = util.getGeneratedKey(statement, "message_id");
			ServiceLogger.log(logTag, "Creating discussion, returning ID: " + id);

			connection.commit();

		} catch (SQLException e) {
			ServiceLogger.log(logTag, "SQL EXCEPTION ----- " + e.getMessage());
			try {
				if (connection != null) {
					ServiceLogger.log(logTag, "createDiscussion transaction rolled back");
					connection.rollback();
				}
			} catch (SQLException ex) {
				ServiceLogger.log(logTag, ex.getMessage());
			}
			return null;
		} finally {
			try {
				if (connection != null) {
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				ServiceLogger.log(logTag, e.getMessage());
			}
		}

		return new Id.IdBuilder(id).build();

	}

	public Discussion getDiscussion(int discussionId) {
		return null;
	}
}
