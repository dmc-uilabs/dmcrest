package org.dmc.services.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;


class UserOnboardingDao {
    
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
    
    public void setProfile(int userId, boolean value) {
        ServiceLogger.log(logTag, "setProfile, user id: " + userId + " to " + value);
        setBoolean(userId, "profile", value);
    }

    public void setAccount(int userId, boolean value) {
        ServiceLogger.log(logTag, "setAccount, user id: " + userId + " to " + value);
        setBoolean(userId, "account", value);
    }

    public void setCompany(int userId, boolean value) {
        ServiceLogger.log(logTag, "setCompany, user id: " + userId + " to " + value);
        setBoolean(userId, "company", value);
    }

    public void setStorefront(int userId, boolean value) {
        ServiceLogger.log(logTag, "setStorefront, user id: " + userId + " to " + value);
        setBoolean(userId, "storefront", value);
    }

    
    private boolean setBoolean(int userId, String identifier, boolean value) {
        ServiceLogger.log(logTag, "setBoolean, user id: " + userId);
        
        try {
            String getOnboardingStatus = "UPDATE onboarding_status SET ? = ? WHERE user_id = ?";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(getOnboardingStatus);
            preparedStatement.setString(1, identifier);
            preparedStatement.setBoolean(2, value);
            preparedStatement.setInt(3, userId);
            if(preparedStatement.executeUpdate() != 1) {
                throw new SQLException("Unable to update onboarding_status." + identifier +
                                       " for user_id: " + userId + " to " + value);
            }
        } catch(SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            return false;
        }
        return true;
    }

}
