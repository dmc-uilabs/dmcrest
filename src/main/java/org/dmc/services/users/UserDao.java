package org.dmc.services.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Connection;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.dmc.services.search.SearchException;
import org.dmc.services.search.SearchQueueImpl;
import org.dmc.solr.SolrUtils;
import org.json.JSONException;
import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.Id;
import org.dmc.services.Config;
import org.dmc.services.sharedattributes.Util;
import javax.xml.ws.http.HTTPException;
import org.springframework.http.HttpStatus;


public class UserDao {

	private final static String logTag = UserDao.class.getName();
	private ResultSet resultSet;

	public UserDao(){}

    public Id createUser(String userEPPN, String userFirstName, String userSurname, String userFullName, String userEmail){
    	Util util = Util.getInstance();
        if(userEPPN.equals("")) {
            // no user to create, so returning Id equal to negative 1.
            return new Id.IdBuilder(-1).build();
        }
        int id = -99999;
        try{

            String username = userEPPN;
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
			long millisOfDate = (new Date()).getTime();
			String email = userEmail;
			String password = "password";
			String realName = userFullName;
			String firstName = userFirstName;
			String lastName = userSurname;


			String query = "INSERT INTO users(user_name, email, user_pw, realname, add_date, firstname, lastname) "
					+ "VALUES ( ?, ?, ?, ?, ?, ?, ? )";
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, password);
			preparedStatement.setString(4, realName);
			preparedStatement.setInt(5, 0);
			preparedStatement.setString(6, firstName);
			preparedStatement.setString(7, lastName);
			preparedStatement.executeUpdate();

			ServiceLogger.log(logTag, "Done INSERT INTO users!");

			/*
			query = "SELECT currval('users_pk_seq') AS id";
			resultSet = DBConnector.executeQuery(query);
			while(resultSet.next()){
				id = resultSet.getInt("id");
			}*/
			
			id = util.getGeneratedKey(preparedStatement, "user_id");

			// create onboarding status
            UserOnboardingDao userOnboardingDao = new UserOnboardingDao();
            userOnboardingDao.createUserOnboarding(id);

			if (Config.IS_TEST == null){
                // Trigger solr indexing
                SearchQueueImpl.sendFullIndexingMessage(SolrUtils.CORE_GFORGE_USERS);
				ServiceLogger.log(logTag, "SolR indexing triggered for user: " + id);
			}

			ServiceLogger.log(logTag, "Creating User, returning ID: " + id);

            return new Id.IdBuilder(id).build();

		}
        /*
        catch(IOException e){
			ServiceLogger.log(logTag, e.getMessage());
			return new Id.IdBuilder(id).build();
		}
		*/
		catch(SQLException e){
			ServiceLogger.log(logTag, e.getMessage());
			return new Id.IdBuilder(id).build();
		}
		catch(JSONException j){
			ServiceLogger.log(logTag, j.getMessage());
			return new Id.IdBuilder(id).build();
		}
		catch(Exception ee){
			ServiceLogger.log(logTag, ee.getMessage());
			return new Id.IdBuilder(id).build();
		}
	}

    public User getUser(String userEPPN, String userFirstName, String userSurname, String userFullName, String userEmail){
        int userId = -1;
        String displayName = null;
        String userName = null;
        Timestamp termsAndConditionsTimeStamp = null;
        
        try {
            userId = getUserID(userEPPN);
            
            if(userId == -1) {
                // user does not exist, create new user account
                Id id = createUser(userEPPN, userFirstName, userSurname, userFullName, userEmail);
                userId = id.getId();
            }
            // user exists
            ServiceLogger.log(logTag, "Finding User ID: " + userId);
            String query = "select user_name, realname, accept_term_cond_time from users where user_id = ?";
        
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
        
            if (resultSet.next()) {
                //id = resultSet.getString("id");
                displayName = resultSet.getString("realname");
                userName = resultSet.getString("user_name");
                termsAndConditionsTimeStamp = resultSet.getTimestamp("accept_term_cond_time"); // get accept_term_cond_time time stamp
            }
			
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
        
        boolean termsAndConditions = false;
        if(termsAndConditionsTimeStamp != null) {
            termsAndConditions = true;
        }
        
        return new User(userId, userName, displayName, termsAndConditions);
    }
    
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

            if (Config.IS_TEST == null){
                // Trigger solr indexing
                SearchQueueImpl.sendFullIndexingMessage(SolrUtils.CORE_GFORGE_USERS);
                ServiceLogger.log(logTag, "SolR indexing triggered for user: " + userId);
            }


        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
        } catch (SearchException e) {
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
		String query = "select user_name from users where user_id = ?;";
		ServiceLogger.log(logTag, "user_name query: " + query);
		PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
		preparedStatement.setInt(1, userID);
		ServiceLogger.log(logTag, "set user_id parameter to : " + userID);
		final boolean ok = preparedStatement.execute();
		ServiceLogger.log(logTag, "execute status = " + ok);
		if (!ok)
			return null;
		
		ResultSet resultSet = preparedStatement.getResultSet();
		ServiceLogger.log(logTag, "resultSet = " + resultSet);
		if (null == resultSet)
			return null;
		
		if (resultSet.next()) {
			ServiceLogger.log(logTag, "resultSet.next() is true ");
			ServiceLogger.log(logTag, "resultSet.getString(user_name) = " + resultSet.getString("user_name"));
			// id = resultSet.getString("id");
			return resultSet.getString("user_name");
		}
		ServiceLogger.log(logTag,  "resultSet.next() is false, so return -1 for user_id");
		// else no user in DB
		return null;
	}
	
}
