package org.dmc.services.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;

import javax.xml.ws.http.HTTPException;
import org.springframework.http.HttpStatus;


public class UserOnboardingDao {
    
    private static final String logTag = UserOnboardingDao.class.getName();
    
    public static UserOnboarding getUserOnboarding(int userId) {
        boolean profile = false;
        boolean account = false;
        boolean company = false;
        boolean storefront = false;

        try {
            ServiceLogger.log(logTag, "getUserOnboarding, user id: " + userId);
            String getOnboardingStatus = "SELECT * FROM onboarding_status WHERE user_id = ?";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(getOnboardingStatus);
            preparedStatement.setInt(1, userId);
            
            ServiceLogger.log(logTag, "getUserOnboarding, user id: " + userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                profile = resultSet.getBoolean("profile");
                account = resultSet.getBoolean("account");
                company = resultSet.getBoolean("company");
                storefront = resultSet.getBoolean("storefront");
            }
        } catch(SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
			return new UserOnboarding(false, false, false, false);
        }
        return new UserOnboarding(profile, account, company, storefront);
    }

    public boolean setUserOnboarding(int userId, UserOnboarding userOnboarding) {
        ServiceLogger.log(logTag, "setUserOnboarding, user id: " + userId + " to " + userOnboarding.toString());
        
        try {
            String setOnboardingStatus = "UPDATE onboarding_status "+
                                         "SET profile = ?, account = ?, company = ?, storefront = ? "+
                                         "WHERE user_id = ?";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(setOnboardingStatus);
            preparedStatement.setBoolean(1, userOnboarding.getProfile());
            preparedStatement.setBoolean(2, userOnboarding.getAccount());
            preparedStatement.setBoolean(3, userOnboarding.getCompany());
            preparedStatement.setBoolean(4, userOnboarding.getStorefront());
            preparedStatement.setInt(5, userId);
            
            if(preparedStatement.executeUpdate() != 1) {
                throw new SQLException("Unable to update onboarding_status" +
                                       " for user_id: " + userId + " to " + userOnboarding.toString());
            }
        } catch(SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            return false;
        }
        return true;
    }

    
    public boolean deleteUserOnboarding(int userId) {
        ServiceLogger.log(logTag, "deleteUserOnboarding, user id: " + userId);
        
        try {
            String deleteOnboardingStatus = "DELETE FROM onboarding_status WHERE user_id = ?";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(deleteOnboardingStatus);
            preparedStatement.setInt(1, userId);
            
            if(preparedStatement.executeUpdate() != 1) {
                throw new SQLException("Unable to delete onboarding_status" +
                                       " for user_id: " + userId);
            }
        } catch(SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            return false;
        }
        return true;
    }
    
    
    
    
    public void setProfile(int userId, boolean value) throws HTTPException {
        ServiceLogger.log(logTag, "setProfile, user id: " + userId + " to " + value);
        setBoolean(userId, "profile", value);
    }

    public void setAccount(int userId, boolean value) throws HTTPException {
        ServiceLogger.log(logTag, "setAccount, user id: " + userId + " to " + value);
        setBoolean(userId, "account", value);
    }

    public void setCompany(int userId, boolean value) throws HTTPException {
        ServiceLogger.log(logTag, "setCompany, user id: " + userId + " to " + value);
        setBoolean(userId, "company", value);
    }

    public void setStorefront(int userId, boolean value) throws HTTPException {
        ServiceLogger.log(logTag, "setStorefront, user id: " + userId + " to " + value);
        setBoolean(userId, "storefront", value);
    }

    // The function below only updates one field on a record.
    // It is a helper function for the attribute setters.
    private boolean setBoolean(int userId, String identifier, boolean value) throws HTTPException {
        ServiceLogger.log(logTag, "setBoolean, user id: " + userId + " " + identifier + " = " + value);

        int recordsUpdated = 0;
        try {
            String getOnboardingStatus = "UPDATE onboarding_status SET " + identifier + " = ? WHERE user_id = ?";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(getOnboardingStatus);
            preparedStatement.setBoolean(1, value);
            preparedStatement.setInt(2, userId);
            
            recordsUpdated = preparedStatement.executeUpdate();
            
            if(recordsUpdated != 1) {
                throw new SQLException("Unable to update onboarding_status." + identifier +
                                       " for user_id: " + userId + " to " + value);
            }
        } catch(SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new HTTPException(HttpStatus.UNAUTHORIZED.value());
//            return false;
        }
        return true;
    }

}
