package org.dmc.services.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.dmc.services.ServiceLogger;


public class UserRunningServicesDao {
    
    
    
	private final String logTag = UserRunningServicesDao.class.getName();
	private ResultSet resultSet;
    
	public UserRunningServicesDao(){}
    
    
    public ArrayList<UserRunningServices> getUsersRunningServices(String userEPPN){
        int userId = -1;
//        "title": "Capacitor Bank For Ldr Mining Co",
//        "serviceId": 3,
//        "projectId": 2,
//        "currentStatus": {
//            "percentCompleted": "25",
//            "startDate": "09/20/2015",
//            "startTime": "11:15:33"
//        },
//        "$$hashKey": "object:56"
        
        
        try {
            userId = UserDao.getUserID(userEPPN);
//            
//            if(userId == -1) {
//                // user does not exist, return null user
//                return new User();
//            }
//            // user exists
//            String query = "select user_name, realname from users where user_id = ?";
//            
//            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
//            preparedStatement.setInt(1, userId);
//            preparedStatement.execute();
//            
//            ResultSet resultSet = preparedStatement.getResultSet();
//            
//            if (resultSet.next()) {
//                //id = resultSet.getString("id");
//                displayName = resultSet.getString("realname");
//                userName = resultSet.getString("user_name");
//            }
        } catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
        
        return new ArrayList<UserRunningServices>();
    }
    
}
