package org.dmc.services.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.users.UserDao;

public class TaskDao {
    private static final String logTag = TaskDao.class.getName();
    private ResultSet resultSet;

    public TaskDao() {
    }

    public Task getTask(String taskID, String userEPPN) throws DMCServiceException {
        if (queryTask(taskID)) {
            return readTaskFromResultSet();
        }
        return null;

    }

    public Id createTask(Task task, String userEPPN) throws DMCServiceException {
        Connection connection = DBConnector.connection();
        // let's start a transaction
        try {
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            return null;
        }

        try {
            final Id id = insertTask(task, userEPPN);
            assignTask(id.getId(), task.getAssignee());
            return id;
        } catch (DMCServiceException e) {
            ServiceLogger.log(logTag, "got Exception in createTask: " + e.getMessage());
            if (connection != null) {
                try {
                    ServiceLogger.log(logTag,
                            "Transaction Profile Update Rolled back");
                    connection.rollback();
                } catch (SQLException ex) {
                    ServiceLogger.log(logTag, ex.getMessage());
                }
            }
            throw e;
        } finally {
            // let's end the transaction
            if (null != connection) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    // don't really need to do anything
                }
            }
        }
    }
    /*
     * Task Fields id : alphanumeric title : string project : object id title
     * assignee : string reporter : string dueDate : MM/DD/YY priority :
     * 'Today', '<Date>'
     */
    private Id insertTask(Task task, String userEPPN) {

        int id = -99999;

        /**
         * Foreign Key Constraints on table project_task: - created_by -> users
         * table - status_id -> table to be identified
         */
        ServiceLogger.log(logTag, "ID in insertTask: " + id);
        try {
            int userID = UserDao.getUserID(userEPPN);
            String title = task.getTitle();
            String description = task.getAdditionalDetails();
            Integer priority = task.getPriority();
            Long endDate = task.getDueDate();
            Integer createdBy = userID;
            Integer groupId = task.getTaskProject().getId();
            Integer statusId = TaskStatus.StatusValues.valueOf(task.getStatus().toUpperCase()).getValue();

            String query = "INSERT INTO project_task (summary, details, priority, end_date, created_by, group_project_id, status_id)"
                    + "values ( ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, priority);
            preparedStatement.setLong(4, endDate);
            preparedStatement.setInt(5, createdBy);
            preparedStatement.setInt(6, groupId);
            preparedStatement.setInt(7, statusId);
            preparedStatement.executeUpdate();

            query = "select currval('project_task_pk_seq') as id";
            resultSet = DBConnector.executeQuery(query);
            while (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            ServiceLogger.log(logTag, "create task ID in insertTask: " + id);
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            return null;
        } catch (JSONException e) {
            ServiceLogger.log(logTag, e.getMessage());
            return null;
        }
        return new Id.IdBuilder(id).build();
    }

    public boolean assignTask(Integer taskId, String assignee) throws DMCServiceException {
        ServiceLogger.log(logTag, "assign task " + taskId + " to " + assignee);
        try {
            if (null == assignee) return true; // nothing to insert

            int assigneeId = UserDao.getUserID(assignee);
            if (assigneeId <= 0) {
                ServiceLogger.log(logTag, "assignee " + assignee + " not found: " + assigneeId);
                throw new DMCServiceException(DMCError.OtherSQLError, "user " + assignee + " not found");
            }
            final String query = createAssignTaskQuery();
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, taskId);
            preparedStatement.setInt(2, assigneeId);
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
        }
    }

    private boolean queryAllTasks() {
        final String query = createTaskListQuery(null, null);
        ServiceLogger.log(logTag, "query for all tasks: " + query);
        resultSet = DBConnector.executeQuery(query);
        return (null != resultSet);
    }

    /*
     * Sample Query: select x.*, y.assignee from (select T.project_task_id,
     * 'unknown' as title, T.group_project_id, G.group_name as project_title,
     * U.user_name as created_by, T.end_date as end_date, T.priority, T.summary,
     * T.details from project_task T, users U, groups G where
     * T.created_by=U.user_id and G.group_id=T.group_project_id and
     * T.project_task_id = 2) x LEFT OUTER JOIN (select p.*, U.user_name as
     * assignee \ from project_assigned_to p, users U where p.assigned_to_id =
     * U.user_id and p.project_task_id = 2) y on
     * (y.project_assigned_id=x.group_project_id and
     * y.project_task_id=x.project_task_id)
     */
    private boolean queryTask(String taskID) {
        String query = createTaskListQuery(null, Integer.parseInt(taskID));
        String sql = "select x.*, y.assignee "
                + "from (select  T.project_task_id, T.summary as title, T.group_project_id, G.group_name as project_title, U.user_name as created_by, "
                + "      T.end_date as end_date, T.priority, T.details, S.status_name as status "
                + "      from project_task T, users U, groups G, project_status S "
                + "      where T.created_by=U.user_id "
                + "      and G.group_id=T.group_project_id "
                + "      and T.project_task_id = ?"
                + "      and T.status_id = S.status_id) x "
                + "LEFT OUTER JOIN "
                + "	    (select p.*, U.user_name as assignee "
                + "      from project_assigned_to p, users U "
                + "      where p.assigned_to_id = U.user_id "
                + "      and p.project_task_id = ?) y "
                + "on (y.project_assigned_id=x.group_project_id "
                + "and y.project_task_id=x.project_task_id) ";
        ServiceLogger.log(logTag, "queryTask old sql = " + sql);
        ServiceLogger.log(logTag, "queryTask sql = " + query);
        try {
            PreparedStatement stmt = DBConnector.prepareStatement(query);
//            int taskID_int = Integer.parseInt(taskID);
//            stmt.setInt(1, taskID_int);
//            stmt.setInt(2, taskID_int);
            resultSet = stmt.executeQuery();
            ServiceLogger.log(logTag, "queried for task");
            if (null != resultSet) {
                ServiceLogger.log(logTag, "have at least one result");
                return (resultSet.next());
            }
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
        }
        return false;
    }

    private boolean queryTasksForProject(int projectId) {
        final String query = createTaskListQuery(projectId, null);
        ServiceLogger.log(logTag, "query for task list for project: " + query);
        resultSet = DBConnector.executeQuery(query);
        return null != resultSet;
    }

    private String createTaskListQuery(Integer projectId, Integer taskId) {
        String query = "select " 
                + "T.project_task_id," 
                + "\'unknown\' as title,"
                + "T.group_project_id," 
                + "G.group_name as project_title," 
                + "x.user_name as assignee,"
                + "U.user_name as created_by," 
                + "T.end_date," 
                + "T.priority, " 
                + "T.summary, " 
                + "T.details, "
                + "S.status_name as status "
                + "from project_task T "
                + "LEFT JOIN (select * from Users U2, project_assigned_to p, groups G2 "
                + "where U2.user_id = p.assigned_to_id and G2.group_id = p.project_assigned_id) x "
                + "on T.project_task_id = x.project_task_id, "
                + "users U,groups G, project_status S "
                + "where T.created_by=U.user_id and G.group_id=T.group_project_id "
                + "and T.status_id = S.status_id ";
        if (null != projectId) {
            query += "and G.group_id = " + projectId + " ";
        }
        if (null != taskId) { 
            query += "and T.project_task_id = " + taskId + " ";
        }
        return query;
    }

    private String createAssignTaskQuery() {
        final String query = "insert into project_assigned_to(project_task_id, assigned_to_id) values (?, ?)";
        return query;
    }

    public ArrayList<Task> getTaskList() throws DMCServiceException {
        if (queryAllTasks()) {
            return createListFromResultSet();
        } else {
            throw new DMCServiceException(DMCError.OtherSQLError, "failed to execute query to get tasks");
        }
    }

    public ArrayList<Task> getTaskList(int projectId) throws DMCServiceException {
        if (queryTasksForProject(projectId)) {
            return createListFromResultSet();
        } else {
            throw new DMCServiceException(DMCError.OtherSQLError, "failed to execute query to get tasks");
        }
    }

    private ArrayList<Task> createListFromResultSet() throws DMCServiceException {
        ArrayList<Task> tasks = new ArrayList<Task>();
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
        } catch (DMCServiceException dmce) {
            throw dmce;
        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to process task list query results");
        }
    }

    private Task readTaskFromResultSet() throws DMCServiceException {
        ServiceLogger.log(logTag, "reading results");
        try {
            int id = resultSet.getInt("project_task_id");
            String title = resultSet.getString("title");
            int group_project_id = resultSet.getInt("group_project_id");
            String projectTitle = resultSet.getString("project_title");
            String assignee = resultSet.getString("assignee");
            String assigneeId = assignee;
            String details = resultSet.getString("details");
            int priority = resultSet.getInt("priority");

            long dueDate = resultSet.getLong("end_date");
            Date releaseDate = new Date(1000L * resultSet.getLong("end_date"));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            String status = resultSet.getString("status");

            TaskProject taskProject = new TaskProject(group_project_id, projectTitle);

            Task task = new Task(Integer.toString(id), title, taskProject, assignee, assigneeId, assignee, assigneeId,
                    dueDate, details, status, priority);
            ServiceLogger.log(logTag, "should have a Task object now: " + task.toString());
            return task;
        } catch (Exception e) {
            ServiceLogger.log(logTag, "problem reading task resultset: " + e.getMessage());
            throw new DMCServiceException(DMCError.OtherSQLError, "unable to process task result");
        }
    }
}