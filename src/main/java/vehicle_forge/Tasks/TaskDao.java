package vehicle_forge;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;

import org.json.JSONObject;
import org.json.JSONException;

public class TaskDao {
	private final String logTag = TaskDao.class.getName();
	private ResultSet resultSet;

	public TaskDao() {
	}

	public vehicle_forge.Task getTask(String  taskID) {
		try {
			resultSet = DBConnector.executeQuery("select "
					+" T.project_task_id,"
					+ " \'unknown\' as title,"
					 +" T.group_project_id,"
					 +" G.group_name as project_title,"
					 +" U.user_name as assignee,"
					 +" U.user_name as created_by,"
					 +" T.end_date as end_date,"
					 +" T.priority " 
					 +" from project_task T ,users U,groups G,project_assigned_to p"
					 +" where T.created_by=U.user_id and G.group_id=T.group_project_id "
					 +" and p.project_assigned_id=T.group_project_id"
					 +" and p.project_task_id=T.project_task_id and T.project_task_id = "+ taskID);
			
			String priority = "";
			int id = 0, group_project_id = 0;
			String title = "", projectTitle = "", assignee = "", date = "";
			
			while (resultSet.next()) {
				
				id = resultSet.getInt("project_task_id");
				title = resultSet.getString("title");
				group_project_id = resultSet.getInt("group_project_id");
				projectTitle = resultSet.getString("project_title");
				assignee = resultSet.getString("assignee");
				
				Date releaseDate = new Date(1000L * resultSet.getLong("end_date"));
				
				/*ServiceLogger.log(logTag, "\nID: " + id + "\nTitle: " + title + 
						"\nDesc: " + description + "\n"
						+  "\nOwner: " + owner);
				ServiceLogger.log(logTag, "Service Name - " + resultSet.toString());
				*/
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY hh:mm:ss");
				
				date = formatter.format(releaseDate);
				
				
				Date today = new Date();
				Date dueDate = new Date(resultSet.getLong("priority") * 1000L);
				
				formatter = new SimpleDateFormat("dd-MM-YYYY");
				String now = formatter.format(today);
				String dueOn = formatter.format(dueDate);
				
				if (now.equals(dueOn))
					priority = "today";
				else{
					formatter = new SimpleDateFormat("dd-MM-YYYY hh:mm:ss");
					priority = formatter.format(dueDate);
				}
				
				
			}
			return new vehicle_forge.Task(
					id,
					title, 
						new TaskProject(
								group_project_id,projectTitle
								), 
						assignee,
						assignee,
						date,
						priority);
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
		
	}

	/*
	 Task Fields
	id : alphanumeric
	title : string
	project : object 
	     id
	     title
	assignee : string
	reporter : string
	dueDate : MM/DD/YY 
	priority : 'Today', '<Date>'
	 */
	public Id createTask(String jsonStr) {

		int id = -99999;
		

		/**
		 * Foreign Key Constraints on table project_task: 
		 * - created_by -> users table
		 * - status_id -> table to be identified
		 * */
		ServiceLogger.log(logTag, "ID in createTask: " + id);
		try {
			JSONObject json = new JSONObject(jsonStr);
			
			String title = json.getString("title");
			String description = json.getString("description");
			//Integer priority = json.getInt("priority");
			Integer priority = new Integer(1);
			//String assignee = new String("assignee");
			Integer endDate = json.getInt("dueDate");
			//String reporter = json.getInt("reporter");
			Integer createdBy = new Integer("102");
			Integer groupId = json.getInt("projectId");
			//Integer statusId = json.getInt("status_id");
			//always assign project task = open
			Integer statusId = new Integer(1);

			//SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-YYYY hh:mm:ss");
			//long millisOfDate = (new Date()).getTime();
			
			String query = "INSERT INTO project_task (summary, details, priority, end_date, created_by, group_project_id, status_id)" + 
					"values ( ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setString(1, title);
			preparedStatement.setString(2, description);
			preparedStatement.setInt(3, priority);
			preparedStatement.setInt(4, endDate);
			preparedStatement.setInt(5, createdBy);
			preparedStatement.setInt(6, groupId);
			preparedStatement.setInt(7, statusId);
			preparedStatement.executeUpdate();

			query = "select currval('project_task_pk_seq') as id";
			resultSet = DBConnector.executeQuery(query);
			while (resultSet.next()) {
				id = resultSet.getInt("id");
			}
		}
		catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		catch (JSONException e) {
			ServiceLogger.log(logTag, e.getMessage());
			return null;
		}
		return new Id.IdBuilder(id)
		.build();
	}
}
