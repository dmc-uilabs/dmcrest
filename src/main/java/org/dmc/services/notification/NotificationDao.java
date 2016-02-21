package org.dmc.services.notification;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;

import org.json.JSONObject;
import org.json.JSONException;

import org.dmc.services.DBConnector;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;

public class NotificationDao {
	private final String logTag = NotificationDao.class.getName();
	private ResultSet resultSet;

	public NotificationDao() {
	}


    public ArrayList<Notification> getNotificationsList(String userEPPN) {
        return getNotificationsList(null, null, userEPPN);
    }
    
	public ArrayList<Notification> getNotificationsList(String period, String type, String userEPPN)
	{
		ServiceLogger.log(logTag, "getNotificationsList for period : " + period + " type: " + type + " called by: " + userEPPN);
        ArrayList<Notification> notificationsList = new ArrayList<Notification>();
		

//		try {
//            // query notifications
//            
////			String query = "INSERT INTO project_task (summary, details, priority, end_date, created_by, group_project_id, status_id)" + 
////					"values ( ?, ?, ?, ?, ?, ?, ?)";
////
////			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
////			preparedStatement.setString(1, title);
////			preparedStatement.setString(2, description);
////			preparedStatement.setInt(3, priority);
////			preparedStatement.setInt(4, endDate);
////			preparedStatement.setInt(5, createdBy);
////			preparedStatement.setInt(6, groupId);
////			preparedStatement.setInt(7, statusId);
////			preparedStatement.executeUpdate();
////
////			query = "select currval('project_task_pk_seq') as id";
////			resultSet = DBConnector.executeQuery(query);
//			while (resultSet.next()) {
//                // pack notifications together
//				id = resultSet.getInt("id");
//			}
//		}
//		catch (SQLException e) {
//			ServiceLogger.log(logTag, e.getMessage());
//			return null;
//		}
//		catch (JSONException e) {
//			ServiceLogger.log(logTag, e.getMessage());
//			return null;
//		}
		return notificationsList;
	}
}