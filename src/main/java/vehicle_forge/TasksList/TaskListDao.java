package vehicle_forge;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;

import org.json.JSONObject;
public class TaskListDao {
	private final String logTag = TaskListDao.class.getName();
	private ResultSet resultSet;

	public TaskListDao() {
	}
	public ArrayList<vehicle_forge.Task> getTaskList() {
		ArrayList<vehicle_forge.Task> tasks=new ArrayList<vehicle_forge.Task>();
		try {
			resultSet = DBConnector.executeQuery("select "
					+"T.project_task_id,"
					+ "\'unknown\' as title,"
					 +"T.group_project_id,"
					 +"G.group_name as project_title,"
					 +"U.user_name as assignee,"
					 +"U.user_name as created_by,"
					 +"T.end_date,"
					 +"T.priority " 
					 +"from project_task T ,users U,groups G,project_assigned_to p"
					 +" where T.created_by=U.user_id and G.group_id=T.group_project_id "
					 +" and p.project_assigned_id=T.group_project_id"
					 +" and p.project_task_id=T.project_task_id ");
			
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
				
				
				tasks.add(new vehicle_forge.Task(
						id,
						title, 
							new TaskProject(
									group_project_id,projectTitle
									), 
							assignee,
							assignee,
							date,
							priority));
			}
			return tasks;
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return tasks;
	}
}