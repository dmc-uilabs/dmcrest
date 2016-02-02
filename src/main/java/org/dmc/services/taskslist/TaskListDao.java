package org.dmc.services.taskslist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.dmc.services.DBConnector;
import org.dmc.services.ServiceLogger;
import org.dmc.services.tasks.Task;
import org.dmc.services.tasks.TaskProject;

public class TaskListDao {
	private final String logTag = TaskListDao.class.getName();
	private ResultSet resultSet;

	public TaskListDao() {
	}
	public ArrayList<Task> getTaskList() {
		ArrayList<Task> tasks=new ArrayList<Task>();
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
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
				
				date = formatter.format(releaseDate);
				
				Date today = new Date();
				Date dueDate = new Date(resultSet.getLong("priority") * 1000L);
				
				formatter = new SimpleDateFormat("dd-MM-yyyy");
				String now = formatter.format(today);
				String dueOn = formatter.format(dueDate);
				
				if (now.equals(dueOn))
					priority = "today";
				else{
					formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
					priority = formatter.format(dueDate);
				}
				
				
				tasks.add(new Task(
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
	
	public ArrayList<Task> getTaskList(int projectId) {
		ArrayList<Task> tasks=new ArrayList<Task>();
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
					 +" and p.project_task_id=T.project_task_id and G.group_id = " + projectId);
			
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
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
				
				date = formatter.format(releaseDate);
				
				Date today = new Date();
				Date dueDate = new Date(resultSet.getLong("priority") * 1000L);
				
				formatter = new SimpleDateFormat("dd-MM-yyyy");
				String now = formatter.format(today);
				String dueOn = formatter.format(dueDate);
				
				if (now.equals(dueOn))
					priority = "today";
				else{
					formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
					priority = formatter.format(dueDate);
				}
				
				
				tasks.add(new Task(
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