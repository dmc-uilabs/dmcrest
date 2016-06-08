package org.dmc.services.accounts;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
		String createUserAccountServerQuery = "INSERT INTO servers (url, user_id, alias) VALUES (?, ?, ?)";
		
        try {
            // update user's record in users table
              // ToDo store status
						
            HttpURLConnection testConnection = (HttpURLConnection) new URL(userAccountServer.getIp() + "/getChildren")
		    		.openConnection();
		    
		    testConnection.setRequestMethod("GET");
		    
		    if (testConnection.getResponseCode() == HttpStatus.OK.value())
		    	userAccountServer.setStatus("online");
		    else
		    	userAccountServer.setStatus("offline");
            
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
		} catch (MalformedURLException e) {
			throw new HTTPException(HttpStatus.UNPROCESSABLE_ENTITY.value());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			 PreparedStatement preparedStatement = DBConnector.prepareStatement(createUserAccountServerQuery,
					 Statement.RETURN_GENERATED_KEYS);
			 try{
	         preparedStatement.setString(1, userAccountServer.getIp());
	         preparedStatement.setInt(2, user_id_lookedup);
	         preparedStatement.setString(3, userAccountServer.getName());
	            
	         //preparedStatement.executeUpdate();
				
	         if(preparedStatement.executeUpdate() != 1) {
	        	 throw new SQLException("Unable to update servers" +
	                       " for user_id: " + user_id_lookedup + " to " + userAccountServer.toString());
	            }
				
				serverId = util.getGeneratedKey(preparedStatement, "server_id");
			 } catch(SQLException e1){
				 ServiceLogger.log(logTag, e.getMessage());
		         throw new HTTPException(HttpStatus.UNAUTHORIZED.value()); 
			 }
		}
		
		userAccountServer.setId(Integer.toString(serverId));
		
		// ToDo: need to set status
		//userAccountServer.setStatus("");
		
        return userAccountServer;
	}
	
	
	
	
	/**
     Patch a record for an existing server registered by a user
     @param UserAccountServer - information of server being patched
     @param String - user unique user name
     @return UserAccountServer
     @throws HTTPException
     **/
	public UserAccountServer patchUserAccountServer(String serverIdStr, UserAccountServer userAccountServer, String userEPPN) throws HTTPException {
		int user_id_lookedup = -1;
		int serverId = Integer.parseInt(serverIdStr);
		Util util = Util.getInstance();
		
		ServiceLogger.log(logTag, "In patchUserAccountServer for userEPPN: " + userEPPN);
		
		try{
				user_id_lookedup = UserDao.getUserID(userEPPN);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new HTTPException(HttpStatus.UNAUTHORIZED.value()); // unknown user
		}
			
        
		//user_id_lookedup = getAutorizedUserId(userAccountServer, userEPPN);
        
		// check if user can update the server
		
		
		Integer auth_user_id = new Integer(-1), auth_server_id = new Integer(-1);	
		  
		String getUserAccountServerQuery = "SELECT user_id, server_id FROM servers WHERE user_id = ? AND server_id = ?";  // ToDo store status
		
		PreparedStatement checkAuthPreparedStatement = DBConnector.prepareStatement(getUserAccountServerQuery);
	    try {
	    	checkAuthPreparedStatement.setInt(1, user_id_lookedup);
	    	checkAuthPreparedStatement.setInt(2, serverId);
			
	       	ResultSet resultSet = checkAuthPreparedStatement.executeQuery();
	       	if(resultSet.next()) {
		    auth_server_id = resultSet.getInt("server_id");
		    auth_user_id = resultSet.getInt("user_id");
		 
	       	} else {
	       		//ServiceLogger.log(logTag, "get in patch throwing error");
	       		throw new HTTPException(HttpStatus.UNAUTHORIZED.value()); // no result set returned
	       	}
	    }catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		    throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} 
	    
		//UserAccountServer autorizedUserAccountServer = getUserAccountServer(serverId, userEPPN);
		if((auth_user_id.intValue() == -1 && auth_server_id.intValue() == -1) || 
				auth_server_id.intValue() != Integer.valueOf(userAccountServer.getId()).intValue() ||  // server's ids don't match OR
		   auth_user_id.intValue() != Integer.valueOf(userAccountServer.getAccountId()).intValue()) { // account ids don't match
			ServiceLogger.log(logTag, "retrieved UID = " + auth_user_id.toString() + "\tretrieved server ID = " + auth_server_id.toString());
			ServiceLogger.log(logTag, "expected UID = " + userAccountServer.getAccountId().toString() + "\texpected server ID = " + userAccountServer.getId().toString());
			throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
		}
		
		try {
		    // update user's record in users table
		    String createUserAccountServerQuery = "UPDATE servers SET url = ?, alias = ? WHERE user_id = ? AND server_id = ?";  // ToDo: update status
			
		    PreparedStatement preparedStatement = DBConnector.prepareStatement(createUserAccountServerQuery);
		    preparedStatement.setString(1, userAccountServer.getIp());
		    preparedStatement.setString(2, userAccountServer.getName());
		    preparedStatement.setInt(3, user_id_lookedup);
		    preparedStatement.setInt(4, Integer.parseInt(userAccountServer.getId()));
		    // ToDo: need to add status update
		    
		    HttpURLConnection testConnection = (HttpURLConnection) new URL(userAccountServer.getIp() + "/getChildren")
		    		.openConnection();
		    
		    testConnection.setRequestMethod("GET");
		    
		    if (testConnection.getResponseCode() == HttpStatus.OK.value())
		    	userAccountServer.setStatus("online");
		    else
		    	userAccountServer.setStatus("offline");
			
		    if(preparedStatement.executeUpdate() != 1) {
			throw new SQLException("Unable to update servers" +
					       " for user_id: " + user_id_lookedup + 
					       " to " + userAccountServer.toString());
		    }
		} catch (SQLException e) {
		    ServiceLogger.log(logTag, e.getMessage());
		    throw new HTTPException(HttpStatus.UNAUTHORIZED.value()); // ToDo need to determine what HTTP error to throw
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
	    	
    		throw new HTTPException(HttpStatus.UNPROCESSABLE_ENTITY.value()); //This URL is invalid, Error code 422 will mention
    		//that there is a semantic error
    		
    		
		} catch (IOException e){
			throw new HTTPException(HttpStatus.BAD_GATEWAY.value());
			//the communication with the looked up DOME server timed out
		}
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
		    String server = resultSet.getString("url");
		    userAccountServer.setIp(server);
		    userAccountServer.setAccountId(Integer.toString(resultSet.getInt("user_id")));
		    userAccountServer.setName(resultSet.getString("alias"));
		    
		    HttpURLConnection testConnection = (HttpURLConnection) new URL(server + "/getChildren")
		    		.openConnection();
		    
		    testConnection.setRequestMethod("GET");
		    
		    if (testConnection.getResponseCode() == HttpStatus.OK.value())
		    	userAccountServer.setStatus("online");
		    else
		    	userAccountServer.setStatus("offline");
		    
		    
		    // ToDo set status
//		     	userAccountServer.setStatus();
		} else {
		    throw new HTTPException(HttpStatus.UNAUTHORIZED.value()); // no result set returned
		}
	    } catch (SQLException e) {
		ServiceLogger.log(logTag, e.getMessage());
	       	throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
	    	
	    	throw new HTTPException(HttpStatus.UNPROCESSABLE_ENTITY.value()); //This URL is invalid, Error code 422 will mention
	    		//that there is a semantic error
	    		
	    		
		} catch (IOException e){
			throw new HTTPException(HttpStatus.BAD_GATEWAY.value());
			//The connection set up with the DOME server timed out
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