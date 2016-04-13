package org.dmc.services.tasks;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;
import org.json.JSONException;

import org.dmc.services.DBConnector;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;

public class TaskDao {
	private final String logTag = TaskDao.class.getName();
	private ResultSet resultSet;

	public TaskDao() {
	}

	public Task getTask(String  taskID) 
	{
		if (queryTask(taskID)) {
			return readTaskFromResultSet();
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
	public Id createTask(String jsonStr) 
	{

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
	private boolean queryAllTasks()
	{
		resultSet = DBConnector.executeQuery("select "
				+"T.project_task_id,"
				+ "\'unknown\' as title,"
				 +"T.group_project_id,"
				 +"G.group_name as project_title,"
				 +"U.user_name as assignee,"
				 +"U.user_name as created_by,"
				 +"T.end_date,"
				 +"T.priority, " 
				 +"T.summary, " 
				 +"T.details " 
				 +"from project_task T ,users U,groups G,project_assigned_to p"
				 +" where T.created_by=U.user_id and G.group_id=T.group_project_id "
				 +" and p.project_assigned_id=T.group_project_id"
				 +" and p.project_task_id=T.project_task_id ");
		return (null != resultSet);
	}
/*
 * Sample Query:
 * select x.*, y.assignee 
 * from (select  T.project_task_id, 'unknown' as title, T.group_project_id, G.group_name as project_title, U.user_name as created_by, 
 *       T.end_date as end_date, T.priority, T.summary, T.details
 *       from project_task T, users U, groups G
 *       where T.created_by=U.user_id 
 *       and G.group_id=T.group_project_id 
 *       and  T.project_task_id = 2) x
 * LEFT OUTER JOIN
 * 	    (select p.*, U.user_name as assignee \
 *       from project_assigned_to p, users U 
 *       where p.assigned_to_id = U.user_id 
 *       and p.project_task_id = 2) y 
 * on (y.project_assigned_id=x.group_project_id 
 * and y.project_task_id=x.project_task_id)
 */
	private boolean queryTask(String taskID)
	{
		String sql = 
				 "select x.*, y.assignee " 
		 	   + "from (select  T.project_task_id, 'unknown' as title, T.group_project_id, G.group_name as project_title, U.user_name as created_by, " 
			   + "      T.end_date as end_date, T.priority, T.summary, T.details "
			   + "      from project_task T, users U, groups G "
			   + "      where T.created_by=U.user_id "
			   + "      and G.group_id=T.group_project_id " 
			   + "      and  T.project_task_id = ?) x "
			   + "LEFT OUTER JOIN "
			   + "	    (select p.*, U.user_name as assignee "
			   + "      from project_assigned_to p, users U "
			   + "      where p.assigned_to_id = U.user_id "
			   + "      and p.project_task_id = ?) y "
			   + "on (y.project_assigned_id=x.group_project_id " 
			   + "and y.project_task_id=x.project_task_id) ";
		ServiceLogger.log(logTag, "queryTask sql = "  + sql);
		try {
			PreparedStatement stmt = DBConnector.prepareStatement(sql);
			int taskID_int = Integer.parseInt(taskID);
			stmt.setInt(1, taskID_int);
			stmt.setInt(2, taskID_int);
			resultSet = stmt.executeQuery();
			if (null != resultSet) {
				return (resultSet.next());
			}
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return false;
	}
	private boolean queryTasksForProject(int projectId)
	{
		resultSet = DBConnector.executeQuery("select "
				+"T.project_task_id,"
				+ "\'unknown\' as title,"
				 +"T.group_project_id,"
				 +"G.group_name as project_title,"
				 +"U.user_name as assignee,"
				 +"U.user_name as created_by,"
				 +"T.end_date,"
				 +"T.priority, " 
				 +"T.summary, " 
				 +"T.details " 
				 +"from project_task T ,users U,groups G,project_assigned_to p"
				 +" where T.created_by=U.user_id and G.group_id=T.group_project_id "
				 +" and p.project_assigned_id=T.group_project_id"
				 +" and p.project_task_id=T.project_task_id and G.group_id = " + projectId);
		return null != resultSet;
	}
	public ArrayList<Task> getTaskList() 
	{
		if (queryAllTasks()) {
			return createListFromResultSet();
		} else {
			return new ArrayList<Task>();
		}
	}
	
	public ArrayList<Task> getTaskList(int projectId) 
	{
		if (queryTasksForProject(projectId)) {
			return createListFromResultSet();
		} else {
			return new ArrayList<Task>();
		}
	}
	
	private ArrayList<Task> createListFromResultSet()
	{
		ArrayList<Task> tasks=new ArrayList<Task>();
		ServiceLogger.log(logTag, "in createListFromResultSet: ");
		int countItems = 0;
		try {	
			while (resultSet.next()) {
				
				Task task = readTaskFromResultSet();
				if (null != task) {
					tasks.add(task);
					countItems++;
					ServiceLogger.log(logTag, "adding item " + countItems);
				}
			}
			return tasks;
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return tasks;
	}


	private Task readTaskFromResultSet()
	{
		try {
			int id = resultSet.getInt("project_task_id");
			String title = resultSet.getString("title");
			int group_project_id = resultSet.getInt("group_project_id");
			String projectTitle = resultSet.getString("project_title");
			String assignee = resultSet.getString("assignee");
			String summary = resultSet.getString("summary");
			String details = resultSet.getString("details");
			int priority = resultSet.getInt("priority");
			
			Date releaseDate = new Date(1000L * resultSet.getLong("end_date"));
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			String dueDateText = formatter.format(releaseDate);
			
			TaskProject taskProject = new TaskProject(group_project_id,projectTitle); 
	
			Task task = new Task(
								id,
								title, 
								taskProject,
								assignee,
								assignee,
								dueDateText,
								priority,
								summary,
								details);
			return task;
		} catch (Exception e) {
			return null;
		}
	}
}