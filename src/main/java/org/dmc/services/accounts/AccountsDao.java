package org.dmc.services.accounts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import org.dmc.services.Config;
import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.users.UserDao;

import javax.xml.ws.http.HTTPException;
import org.springframework.http.HttpStatus;

class AccountsDao {
    
    private final String logTag = AccountsDao.class.getName();
    
    public UserAccount getUserAccount(String user_id_string, String userEPPN) throws HTTPException {
        int user_id = Integer.parseInt(user_id_string);
        int user_id_lookedup = -1;
        
        try{
            user_id_lookedup = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
        }
        
        if(user_id_lookedup != user_id) {
            throw new HTTPException(HttpStatus.UNAUTHORIZED.value()); // user id for userEPPN does not match user_id_string, return default UserAccount
        }
        
        UserAccount userAccount = new UserAccount(user_id_string);
        
        String query = "SELECT * FROM users WHERE user_id = ?";
        
        try {
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            
            ServiceLogger.log(logTag, "getUserAccount, user_id: " + user_id + ", userEPPN: " + userEPPN);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // get results
                // userAccount.setId(Integer.toString(user_id)); //set in constructor
                userAccount.setCompanyId(Integer.toString(-1)); // need to figure out company
                userAccount.setProfileId(Integer.toString(user_id));
                
                userAccount.setDisplayName(resultSet.getString("realname"));
                userAccount.setFirstName(resultSet.getString("firstname"));
                userAccount.setLastName(resultSet.getString("lastname"));
                userAccount.setEmail(resultSet.getString("email"));
                userAccount.setDeactivated(resultSet.getBoolean("status"));
                userAccount.setLocation(resultSet.getString("address2"));
                userAccount.setTimezone(resultSet.getString("timezone"));
                userAccount.setPrivacy(new UserAccountPrivacy());
            }
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
        }
        
        return userAccount;
    }
    
    
    public UserAccount patchUserAccount(String user_id_string, UserAccount account, String userEPPN) throws HTTPException {
        int user_id_lookedup = -1;
        int account_user_id = Integer.parseInt(account.getId());
        int user_id_passedOnPath = Integer.parseInt(user_id_string);
        
        try{
            user_id_lookedup = UserDao.getUserID(userEPPN);
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
        }
        
        // check if user has permission to update account
        if(user_id_lookedup != account_user_id || user_id_lookedup != user_id_passedOnPath || account_user_id != user_id_passedOnPath) {
            throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
        }
        
        // the parameter account contains the full UserAccount object for the calling user
        ServiceLogger.log(logTag, "In patchUserAccount, patching user_id = " + user_id_string + ", userEPPN: " + userEPPN);
        
        try {
            // update user's record in users table
            String createAccountQuery = "UPDATE users SET realname = ?, firstname = ?, lastname = ?, email = ?, address2 = ?, timezone = ? WHERE user_id = ?";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(createAccountQuery);
            preparedStatement.setString(1, account.getDisplayName());
            preparedStatement.setString(2, account.getFirstName());
            preparedStatement.setString(3, account.getLastName());
            preparedStatement.setString(4, account.getEmail());
            // do not update status, this is an admin task
            preparedStatement.setString(5, account.getLocation());
            preparedStatement.setString(6, account.getTimezone());     // need to add time zone
            // need to create db table to store UserAccountPrivacy
            
            preparedStatement.setInt(7, Integer.parseInt(user_id_string)); // set user_id in WHERE clause
            
            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
        return account;
    }
}