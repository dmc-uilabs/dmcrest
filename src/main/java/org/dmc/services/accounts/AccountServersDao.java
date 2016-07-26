package org.dmc.services.accounts;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.ServiceLogger;
import org.dmc.services.users.UserDao;
import org.dmc.services.sharedattributes.Util;

import org.apache.http.client.*;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpStatus;
import javax.xml.ws.http.HTTPException;

class AccountServersDao {
	private static final String logTag = AccountServersDao.class.getName();
	private static final String GET = org.springframework.web.bind.annotation.RequestMethod.GET.toString(); // default for GET

	private static final int TIMEOUT = 5000; //timeout in milliseconds for servers


	/**
	 * 
	 * @param userAccountServer -- the server data, along with some user account data
	 * @return whether or not the server is online or offline -- @IMPROVEMENT get more detailed server usage information
	 * @throws IOException -- HTTPUrlConnections are only meant to properly handle 200 and 401 responses
	 * TODO: look into using Apache HTTPClient for more thorough usage, HTTPUrlConnection does not handle many error codes
	 * which we may face, due to DOME server issues
	 * @throws DMCServiceException 
	 * @throws MalformedURLException 
	 */
	private static String serverStatus(UserAccountServer userAccountServer) throws DMCServiceException, 
	MalformedURLException, IOException{
		String errorHeader = "In TEST SERVER STATUS: ";
		HttpURLConnection testConnection = (HttpURLConnection) new URL(userAccountServer.getIp())
				.openConnection();

		testConnection.setConnectTimeout(TIMEOUT); 
		
		testConnection.setRequestMethod(GET);
		int responseCode = 0;
		String responseMsg = null;
		try{
			responseCode = testConnection.getResponseCode();
		}
		catch (IOException responseCodeException){
			ServiceLogger.log(logTag, "RESPONSE_CODE caused IO error for user "  + userAccountServer.getAccountId() 
			+ ", ip " + userAccountServer.getIp());
			throw new DMCServiceException(DMCError.UnexpectedDOMEConnectionError, errorHeader + "I/O Exception from RESPONSE_CODE "
					+ "when attempting to connect to DOME Server " + userAccountServer.getIp());
		}
		try{
			responseMsg = testConnection.getResponseMessage();
		} catch (IOException responseMsgException){
			ServiceLogger.log(logTag, "RESPONSE_MESSAGE caused IO error for user "  + userAccountServer.getAccountId() 
			+ ", ip " + userAccountServer.getIp());
			throw new DMCServiceException(DMCError.UnexpectedDOMEConnectionError, errorHeader + "I/O Exception from RESPONSE_MESSAGE "
					+ "when attempting to connect to DOME Server " + userAccountServer.getIp());
		}

		ServiceLogger.log(logTag, "testing connection to server: " + userAccountServer.getIp()
		+ "\tResponse code: " + responseCode + "\tResponse Message: " + responseMsg);


		if (responseCode == HttpStatus.OK.value())
			return "online";
		else if (responseCode == HttpStatus.GATEWAY_TIMEOUT.value())
			return "offline"; //server IP not resolved
		else if (responseCode == HttpStatus.NOT_FOUND.value())
			return "server online, DOME not running"; //server is on, but DOME not running
		else{
			ServiceLogger.log(logTag, userAccountServer.getIp() + " -- Response code: " + responseCode
					+"\tResponse Message: " + responseMsg);
			throw new DMCServiceException(DMCError.UnexpectedDOMEError, errorHeader + "Unexpected error from DOME running on "
					+ userAccountServer.getIp() + " -- Response code: " + responseCode
					+"\tResponse Message: " + responseMsg);
		}


	}

	/**
     Create a new record for a new server registered by a user
     @param UserAccountServer - information of server being registered
	 @param String - user unique user name
     @return UserAccountServer
     @throws HTTPException
	 * @throws DMCServiceException 
	 **/
	public UserAccountServer postUserAccountServer(UserAccountServer userAccountServer, String userEPPN) throws DMCServiceException {
		int user_id_lookedup = -1;
		int serverId = -1;
		Util util = Util.getInstance();
		String errorHeader = "IN POST ACCOUNT SERVER: ";

		ServiceLogger.log(logTag, "In postUserAccountServer for userEPPN: " + userEPPN);
		

		user_id_lookedup = getAuthorizedUserId(userAccountServer, userEPPN);
		String createUserAccountServerQuery = "INSERT INTO servers (url, user_id, alias, local_dome_user, local_dome_user_password, dome_user_space) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try {
			// update user's record in users table
			// ToDo store status
			ServiceLogger.log(logTag, "BEFORE potential URL-update, IP is : " + userAccountServer.getIp());
			if (!userAccountServer.getIp().contains("DOMEApiServicesV7")){
				ServiceLogger.log(logTag, "NOTE: no DOME version specified, appending default version");
				userAccountServer.setIp(userAccountServer.getIp() + "/DOMEApiServicesV7/");
			}
			//here we add the version of DOME running. In the future, this should come from the frontend/user
			ServiceLogger.log(logTag, "AFTER potential URL-update, IP is : " + userAccountServer.getIp());
			String serverUserName = "ceed";
			String userPassword = "ceed";
			String domainSpace = "USER";
			
			/*
			 if (userAccountServer.getServerUserName() != null)
			 	serverUserName = userAccountServer.getServerUserName();
			 	
			 if (userAccountServer.getUserPassword() != null)
			 	userPassword = userAccountServer.getUserPassword();
			 	
			 if (userAccountServer.getDomainSpace() != null)
			 	domainSpace = userAccountServer.getDomainSpace();
			 	
			 */
			
			PreparedStatement preparedStatement = DBConnector.prepareStatement(createUserAccountServerQuery, 
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, userAccountServer.getIp());
			preparedStatement.setInt(2, user_id_lookedup);
			preparedStatement.setString(3, userAccountServer.getName());
			preparedStatement.setString(4, serverUserName);
			preparedStatement.setString(5, userPassword);
			preparedStatement.setString(6, domainSpace);


			if(preparedStatement.executeUpdate() != 1) {
				throw new DMCServiceException(DMCError.OtherSQLError, errorHeader 
						+ "Unable to add requested server for user " + userEPPN);
			}



			userAccountServer.setStatus(AccountServersDao.serverStatus(userAccountServer));



			serverId = util.getGeneratedKey(preparedStatement, "server_id");

		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, errorHeader 
					+ e.getMessage()); 
		} catch (SocketTimeoutException e){
			throw new DMCServiceException(DMCError.CannotConnectToDome, errorHeader + "connection to URL " 
					+ userAccountServer.getIp() + " timed out."); //response from DOME timed out
		}
		catch (MalformedURLException e) {
			throw new DMCServiceException(DMCError.BadURL, errorHeader + "Malformed DOME Server URL submitted"); //bad URL
		} catch (IOException e) {
			ServiceLogger.log(logTag, errorHeader + "caught 500 error -- details: " + e.getMessage());
			throw new DMCServiceException(DMCError.UnexpectedDOMEConnectionError, e.getMessage()); //other IO error
		}

		userAccountServer.setId(Integer.toString(serverId));


		return userAccountServer;
	}




	/**
     Patch a record for an existing server registered by a user
     @param UserAccountServer - information of server being patched
     @param String - user unique user name
     @return UserAccountServer
     @throws HTTPException
	 * @throws DMCServiceException 
	 **/
	public UserAccountServer patchUserAccountServer(String serverIdStr, UserAccountServer userAccountServer, String userEPPN) 
			throws DMCServiceException {
		int user_id_lookedup = getAuthorizedUserId(userAccountServer, userEPPN);
		int serverId = Integer.parseInt(serverIdStr);
		Util util = Util.getInstance();
		String errorHeader = "IN PATCH ACCOUNT SERVER: ";

		ServiceLogger.log(logTag, "In patchUserAccountServer for userEPPN: " + userEPPN);

		

		// check if user can update the server


		int auth_user_id = -1, auth_server_id = -1;	

		String getUserAccountServerQuery = "SELECT user_id, server_id FROM servers WHERE user_id = ? AND server_id = ?"; 
		// ToDo store status

		PreparedStatement checkAuthPreparedStatement = DBConnector.prepareStatement(getUserAccountServerQuery);
		try {
			checkAuthPreparedStatement.setInt(1, user_id_lookedup);
			checkAuthPreparedStatement.setInt(2, serverId);

			ResultSet resultSet = checkAuthPreparedStatement.executeQuery();
			if(resultSet.next()) {
				auth_server_id = resultSet.getInt("server_id");
				auth_user_id = resultSet.getInt("user_id");

			} else {
				ServiceLogger.log(logTag, "in patch, no record for user " + user_id_lookedup);
				throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, errorHeader + "No server record found");
				// no result set returned
			}
		}catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, errorHeader + e.getMessage()); 
		} 


		if(auth_server_id != Integer.parseInt(userAccountServer.getId()) ||
				auth_user_id != Integer.parseInt(userAccountServer.getAccountId())) { 
			ServiceLogger.log(logTag, "retrieved UID = " + auth_user_id + "\tretrieved server ID = " + auth_server_id);
			ServiceLogger.log(logTag, "expected UID = " + userAccountServer.getAccountId() 
			+ "\texpected server ID = " + userAccountServer.getId());

			throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, errorHeader + "unauthorized user");
		}

		try {
			// update user's record in users table
			String createUserAccountServerQuery = "UPDATE servers SET url = ?, "
					+ "alias = ? WHERE user_id = ? AND server_id = ?";  // ToDo: update status

			PreparedStatement preparedStatement = DBConnector.prepareStatement(createUserAccountServerQuery);
			
			if (!userAccountServer.getIp().contains("DOMEApiServicesV7"))
				userAccountServer.setIp(userAccountServer.getIp() + "/DOMEApiServicesV7/");
			//append DOME endpoint on target server's URL
			
			preparedStatement.setString(1, userAccountServer.getIp());
			preparedStatement.setString(2, userAccountServer.getName());
			preparedStatement.setInt(3, user_id_lookedup);
			preparedStatement.setInt(4, Integer.parseInt(userAccountServer.getId()));
			// ToDo: need to add status update

			userAccountServer.setStatus(AccountServersDao.serverStatus(userAccountServer));

			if(preparedStatement.executeUpdate() != 1) {
				throw new DMCServiceException(DMCError.CannotPatchDOMEServerEntry,errorHeader + "Unable to update servers" +
						" for user_id: " + user_id_lookedup + 
						" to " + userAccountServer.toString());
			}
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
			// ToDo need to determine what HTTP error to throw
		} catch (SocketTimeoutException e){
			throw new DMCServiceException(DMCError.CannotConnectToDome, errorHeader + "connection to URL " 
					+ userAccountServer.getIp() + " timed out."); //response from DOME timed out
		}
		catch (MalformedURLException e) {
			throw new DMCServiceException(DMCError.BadURL, errorHeader + "Malformed DOME Server URL submitted"); //bad URL
		} catch (IOException e) {
			ServiceLogger.log(logTag, errorHeader + "caught 500 error -- details: " + e.getMessage());
			throw new DMCServiceException(DMCError.UnexpectedDOMEConnectionError, e.getMessage()); //other IO error
		}

		return userAccountServer;
	}


	public UserAccountServer getUserAccountServer(int serverID, String userEPPN) throws DMCServiceException {
		ServiceLogger.log(logTag, "In getUserAccountServer for userEPPN: " + userEPPN + " and server id " + serverID);

		Util util = Util.getInstance();
		UserAccountServer userAccountServer = new UserAccountServer();
		String errorHeader = "GET ACCOUNT SERVER: ";
		
		int user_id_lookedup = getAuthorizedUserId(userEPPN);

		// update user's record in users table
		String getUserAccountServerQuery = "SELECT user_id, server_id, alias, url FROM servers "
				+ "WHERE user_id = ? AND server_id = ?";  // ToDo get status

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

				userAccountServer.setStatus(AccountServersDao.serverStatus(userAccountServer));



			} else {
				throw new DMCServiceException(DMCError.NoContentInQuery, "No content in query"); // no result set returned
			}
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, errorHeader + e.getMessage());
			// ToDo need to determine what HTTP error to throw
		} catch (SocketTimeoutException e){
			throw new DMCServiceException(DMCError.CannotConnectToDome, errorHeader + "-- connection to URL " 
					+ userAccountServer.getIp() + " timed out."); //response from DOME timed out
		}
		catch (MalformedURLException e) {
			throw new DMCServiceException(DMCError.BadURL, errorHeader + "-- Malformed DOME Server URL submitted"); //bad URL
		} catch (IOException e) {
			ServiceLogger.log(logTag, errorHeader + "-- caught 500 error -- details: " + e.getMessage());
			throw new DMCServiceException(DMCError.UnexpectedDOMEConnectionError, errorHeader 
					+ e.getMessage()); //other IO error
		}

		return userAccountServer;
	}



	public void deleteUserAccountServer(int serverID, String userEPPN) throws DMCServiceException {
		ServiceLogger.log(logTag, "In deleteUserAccountServer for userEPPN: " + userEPPN + " and server id " + serverID);
		String errorHeader = "DELETE ACCOUNT SERVER: ";
		Util util = Util.getInstance();
		
		int user_id_lookedup = getAuthorizedUserId(userEPPN);

		// update user's record in users table
		String deleteUserAccountServerQuery = "DELETE FROM servers WHERE user_id = ? AND server_id = ?";

		PreparedStatement preparedStatement = DBConnector.prepareStatement(deleteUserAccountServerQuery);
		try {
			preparedStatement.setInt(1, user_id_lookedup);
			preparedStatement.setInt(2, serverID);
			int change = preparedStatement.executeUpdate();
			if(change != 1) {
				throw new DMCServiceException(DMCError.CannotDeleteDOMEServerEntry, errorHeader 
						+ "number of changes were: " + change); // single item not deleted
			}

		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.OtherSQLError, errorHeader + e.getMessage());
			// ToDo need to determine what HTTP error to throw
		}
		return;
	}
	
	private int getAuthorizedUserId(String userEPPN) throws DMCServiceException{
		String errorHeader = "IN GET AUTHORIZED UID BY EPPN ONLY: ";
		try{
			return UserDao.getUserID(userEPPN);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.UnknownUser, errorHeader + "unknown user"); // unknown user
		}
		
	}


	private int getAuthorizedUserId(UserAccountServer userAccountServer, String userEPPN) throws DMCServiceException {
		int user_id_lookedup = -1;
		String errorHeader = "ACCOUNT SERVERS GET AUTHORIZED USER ID: ";
		try{
			user_id_lookedup = UserDao.getUserID(userEPPN);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			throw new DMCServiceException(DMCError.UnknownUser, errorHeader + e.getMessage()); // unknown user
		}

		// check if user has permission to update account
		if(user_id_lookedup != Integer.parseInt(userAccountServer.getAccountId())) {
			ServiceLogger.log(logTag, "Looked up user id " + user_id_lookedup +
					" does not match account id of server " + userAccountServer.getAccountId());
			throw new DMCServiceException(DMCError.UnauthorizedAccessAttempt, errorHeader 
					+ "Looked up user id " + user_id_lookedup +
					" does not match account id of server " + userAccountServer.getAccountId());
			// account id and user id do not match
		}
		return user_id_lookedup;
	}
}