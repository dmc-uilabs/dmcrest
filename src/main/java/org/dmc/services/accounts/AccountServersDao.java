package org.dmc.services.accounts;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.users.UserDao;
import org.dmc.services.sharedattributes.Util;



import org.springframework.http.HttpStatus;
import javax.xml.ws.http.HTTPException;

class AccountServersDao {
	private final String logTag = AccountServersDao.class.getName();

	/**
     Create a new record for a new server registered by a user
     @param UserAccountServer - information of server being registered
	 @param String - user unique user name
     @return UserAccountServer
     @throws HTTPException
     **/
	public UserAccountServer postUserAccountServer(UserAccountServer userAccountServer, String userEPPN) throws HTTPException {
		int user_id_lookedup = -1;
		int serverId = -1;
		Util util = Util.getInstance();
		
        ServiceLogger.log(logTag, "In postUserAccountServer for userEPPN: " + userEPPN);
        
		user_id_lookedup = getAutorizedUserId(userAccountServer, userEPPN);
        
        try {
            // update user's record in users table
            String createUserAccountServerQuery = "INSERT INTO servers (url, user_id, alias) VALUES (?, ?, ?)";  // ToDo store status
						
            PreparedStatement preparedStatement = DBConnector.prepareStatement(createUserAccountServerQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, userAccountServer.getIp());
            preparedStatement.setInt(2, user_id_lookedup);
            preparedStatement.setString(3, userAccountServer.getName());
            
            //preparedStatement.executeUpdate();
			
			if(preparedStatement.executeUpdate() != 1) {
                throw new SQLException("Unable to update servers" +
                                       " for user_id: " + user_id_lookedup + " to " + userAccountServer.toString());
            }
			
			serverId = util.getGeneratedKey(preparedStatement, "server_id");
			
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
            throw new HTTPException(HttpStatus.UNAUTHORIZED.value()); // ToDo need to determine what HTTP error to throw
		}
		
		userAccountServer.setId(Integer.toString(serverId));
		
		// ToDo: need to set status
		//userAccountServer.setStatus("");
		
        return userAccountServer;
	}
	
	
	public UserAccountServer getUserAccountServer(int serverID, String userEPPN) throws HTTPException {
        ServiceLogger.log(logTag, "In getUserAccountServer for userEPPN: " + userEPPN + " and server id " + serverID);

		Util util = Util.getInstance();
		UserAccountServer userAccountServer = new UserAccountServer();
		int user_id_lookedup = -1;
		
		try{
            user_id_lookedup = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new HTTPException(HttpStatus.UNAUTHORIZED.value()); // unknow user
        }
		
		// update user's record in users table
		String getUserAccountServerQuery = "SELECT * FROM servers WHERE user_id = ? AND server_id = ?";  // ToDo store status
			
		PreparedStatement preparedStatement = DBConnector.prepareStatement(getUserAccountServerQuery);
		try {
			preparedStatement.setInt(1, user_id_lookedup);
			preparedStatement.setInt(2, serverID);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				userAccountServer.setId(Integer.toString(resultSet.getInt("server_id")));
				userAccountServer.setIp(resultSet.getString("url"));
				userAccountServer.setAccountId(Integer.toString(resultSet.getInt("user_id")));
				userAccountServer.setName(resultSet.getString("alias"));
				// ToDo set status
//				userAccountServer.setStatus();
			} else {
				throw new HTTPException(HttpStatus.UNAUTHORIZED.value()); // no result set returned
			}
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		
        return userAccountServer;
	}

	
	
	public void deleteUserAccountServer(int serverID, String userEPPN) throws HTTPException {
        ServiceLogger.log(logTag, "In deleteUserAccountServer for userEPPN: " + userEPPN + " and server id " + serverID);
		
		Util util = Util.getInstance();
		int user_id_lookedup = -1;
		
		try{
            user_id_lookedup = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new HTTPException(HttpStatus.UNAUTHORIZED.value()); // unknow user
        }
				
		// update user's record in users table
		String deleteUserAccountServerQuery = "DELETE FROM servers WHERE user_id = ? AND server_id = ?";
		
		PreparedStatement preparedStatement = DBConnector.prepareStatement(deleteUserAccountServerQuery);
		try {
			preparedStatement.setInt(1, user_id_lookedup);
			preparedStatement.setInt(2, serverID);
			
			if(preparedStatement.executeUpdate() != 1) {
				throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value()); // single item not deleted
			}
			
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		
        return;
	}
	
	
	private int getAutorizedUserId(UserAccountServer userAccountServer, String userEPPN) {
		int user_id_lookedup = -1;
		try{
            user_id_lookedup = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new HTTPException(HttpStatus.UNAUTHORIZED.value()); // unknow user
        }
        
        // check if user has permission to update account
        if(user_id_lookedup != Integer.parseInt(userAccountServer.getAccountId())) {
			ServiceLogger.log(logTag, "Looked up user id " + user_id_lookedup +
							  " does not match account id of server " + userAccountServer.getAccountId());
            throw new HTTPException(HttpStatus.UNAUTHORIZED.value()); // account id and user id do not match
        }
		return user_id_lookedup;
	}

}