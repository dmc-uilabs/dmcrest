package org.dmc.services.accounts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import org.dmc.services.Config;
import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;


class AccountsDao {
    
    private final String logTag = AccountsDao.class.getName();
    
    public UserAccount getUserAccount(int user_id) {
        UserAccount userAccount = new UserAccount();
        
        String query = "SELECT * FROM users WHERE user_id = ?";
        
        try {
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, user_id);
            
            ServiceLogger.log(logTag, "getUserAccount, user_id: " + user_id);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // get results
                userAccount.setId(Integer.toString(user_id));
                userAccount.setCompanyId(Integer.toString(-1)); // need to figure out company
                userAccount.setProfileId(Integer.toString(user_id));
                
                userAccount.setDisplayName(resultSet.getString("realname"));
                userAccount.setFirstName(resultSet.getString("firstname"));
                userAccount.setLastName(resultSet.getString("lastname"));
                userAccount.setEmail(resultSet.getString("email"));
                userAccount.setDeactivated(resultSet.getBoolean("status"));
                userAccount.setLocation(resultSet.getString("address2"));
                userAccount.setTimezone("none");
                userAccount.setPrivacy(new UserAccountPrivacy());
            }
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
        }
        
        return userAccount;
    }
    
}