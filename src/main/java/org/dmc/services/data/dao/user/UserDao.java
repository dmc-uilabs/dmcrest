package org.dmc.services.data.dao.user;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.users.User;
import org.dmc.services.users.UserOnboarding;
import org.springframework.http.HttpStatus;

import javax.xml.ws.http.HTTPException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserDao {

	private final static String logTag = UserDao.class.getName();

    public User patchUser(String userEPPN, User patchUser) throws HTTPException {
        ServiceLogger.log(logTag, "patchUser User: " + userEPPN + "\n" + patchUser.toString());
//        User patchedUser = null;
        int userId = -1;
        try {
            userId = getUserID(userEPPN);
        } catch(SQLException e) {
            throw new HTTPException(HttpStatus.UNAUTHORIZED.value());  // user not in database
        }
        
        if(userId != patchUser.getAccountId()) { //userEPPN and patchUser do not match
            throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
        }
        
        // Updating displayName;
        // not updating accountId, profileId, companyId, role, termsConditions because they are set by other functions
        Connection connection = DBConnector.connection();
        try {
            connection.setAutoCommit(false);
            
            String query = "UPDATE users SET realname = ? WHERE user_id = ?";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, patchUser.getDisplayName());
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
            
        //ToDo: need to update
            //        private UserNotifications notifications;
            //        private UserRunningServices runningServices;
            //        private UserMessages messages;
            UserOnboarding patchUserOnboarding = patchUser.getOnboarding();
            if(!patchUserOnboarding.patch(userId)) {
                connection.rollback();
                throw new SQLException("Unable to update user_id: " + userId + " onboarding status");
            }
            connection.commit();
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {}
        }
        
        return patchUser;
    }

    public static int getUserID(String userEPPN) throws SQLException {
        String query = "select user_id from users where user_name = ?;";
        ServiceLogger.log(logTag, "userid query: " + query + " for " + userEPPN);
        PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
        preparedStatement.setString(1, userEPPN);
        final boolean ok = preparedStatement.execute();
        if (!ok)
            return -1;

        ResultSet resultSet = preparedStatement.getResultSet();
        if (null == resultSet)
            return -1;

        if (resultSet.next()) {
            ServiceLogger.log(logTag, "resultSet.getInt(user_id) = " + resultSet.getInt("user_id"));
            // id = resultSet.getString("id");
            return resultSet.getInt("user_id");
        }
        ServiceLogger.log(logTag,  "user_id not found, so return -1 for user_id");
        // else no user in DB
        return -1;
    }

	public static String getUserName(int userID) throws SQLException {
		String query = "select user_name from users where user_id = ?";
		ServiceLogger.log(logTag, String.format("user_name query: %s", query));

		final String userName = DBConnector.jdbcTemplate().queryForObject(query, String.class, userID);
		ServiceLogger.log(logTag, String.format("Found user_name: %s", userName));

		return userName;
	}
	
}
