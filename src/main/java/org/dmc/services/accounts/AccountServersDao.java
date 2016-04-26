package org.dmc.services.accounts;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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

}