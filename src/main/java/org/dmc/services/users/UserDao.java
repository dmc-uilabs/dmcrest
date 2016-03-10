package org.dmc.services.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Connection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;

import org.json.JSONObject;
import org.json.JSONException;
import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.Id;
import org.dmc.services.Config;
import org.dmc.services.sharedattributes.Util;
import org.dmc.solr.SolrUtils;

import java.io.IOException;
import javax.xml.ws.http.HTTPException;
import org.springframework.http.HttpStatus;


public class UserDao {



	private final String logTag = UserDao.class.getName();
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

			ServiceLogger.log(logTag, "Done updating!");

			/*
			query = "SELECT currval('users_pk_seq') AS id";
			resultSet = DBConnector.executeQuery(query);
			while(resultSet.next()){
				id = resultSet.getInt("id");
			}*/
			
			id = util.getGeneratedKey(preparedStatement, "user_id");

            String createOnboardingStatus = "INSERT INTO onboarding_status(user_id, profile, account, company, storefront) "
            + "VALUES ( ?, ?, ?, ?, ? )";
			PreparedStatement preparedStatementCreateOnboardingStatus = DBConnector.prepareStatement(createOnboardingStatus);
			preparedStatementCreateOnboardingStatus.setInt(1, id);
			preparedStatementCreateOnboardingStatus.setBoolean(2, false);
			preparedStatementCreateOnboardingStatus.setBoolean(3, false);
			preparedStatementCreateOnboardingStatus.setBoolean(4, false);
			preparedStatementCreateOnboardingStatus.setBoolean(5, false);
			preparedStatementCreateOnboardingStatus.executeUpdate();
            // ToDo: check that record was created successfully.
            
			ServiceLogger.log(logTag, "User added: " + id);
			
			if (Config.IS_TEST == null){
				String indexResponse = ""; //SolrUtils.invokeFulIndexingUsers();
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
        
		PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
		preparedStatement.setString(1, userEPPN);
        preparedStatement.execute();
        
        ResultSet resultSet = preparedStatement.getResultSet();
		if (resultSet.next()) {
			//id = resultSet.getString("id");
			return resultSet.getInt("user_id");
		}
		// else no user in DB
		return -1;
    }
    
}
