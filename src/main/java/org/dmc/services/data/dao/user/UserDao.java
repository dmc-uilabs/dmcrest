package org.dmc.services.data.dao.user;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.users.User;
import org.dmc.services.users.UserOnboarding;
import org.springframework.http.HttpStatus;

import javax.xml.ws.http.HTTPException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDao {

	private final static String logTag = UserDao.class.getName();

	public User patchUser(String userEPPN, User patchUser) throws HTTPException {
		ServiceLogger.log(logTag, "patchUser User: " + userEPPN + "\n" + patchUser.toString());

		int userId = -1;

		try {
			userId = getUserID(userEPPN);
		} catch (SQLException e) {
			throw new HTTPException(HttpStatus.BAD_REQUEST.value());
		}

		if (userId != patchUser.getAccountId()) {
			throw new HTTPException(HttpStatus.BAD_REQUEST.value());
		}

		// Updating displayName;
		// not updating accountId, profileId, companyId, role, termsConditions because they are set by other functions
		Connection connection = DBConnector.connection();
		try {
			connection.setAutoCommit(false);

			String query = "UPDATE users SET realname = '%s' WHERE user_id = '%s'";
			DBConnector.jdbcTemplate().execute(String.format(query, patchUser.getDisplayName(), userId));
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, patchUser.getDisplayName());
			preparedStatement.setInt(2, userId);
			preparedStatement.executeUpdate();

			//ToDo: need to update
			//        private UserNotifications notifications;
			//        private UserRunningServices runningServices;
			//        private UserMessages messages;
			UserOnboarding patchUserOnboarding = patchUser.getOnboarding();
			if (!patchUserOnboarding.patch(userId)) {
				connection.rollback();
				throw new SQLException("Unable to update user_id: " + userId + " onboarding status");
			}
			connection.commit();
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException ex) {
			}
		}

		return patchUser;
	}

	public static int getUserID(String userEPPN) throws SQLException {
		String query = "select user_id from users where user_name = ?;";
		final Integer userId = DBConnector.jdbcTemplate().queryForObject(query, Integer.class, userEPPN);
		return userId;
	}

	public static String getUserName(int userID) throws SQLException {
		String query = "select user_name from users where user_id = ?";
		final String userName = DBConnector.jdbcTemplate().queryForObject(query, String.class, userID);
		return userName;
	}
}
