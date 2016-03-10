package org.dmc.services.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
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
            ServiceLogger.log(logTag, "getUserOnboarding, start, user id: " + userId);
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
    
}
