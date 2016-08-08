package org.dmc.services.projects;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.dmc.services.Config;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.data.dao.user.UserDao;
import org.json.JSONObject;
import org.json.JSONException;

public class ProjectDao {

    private Connection connection;
    private final String logTag = ProjectDao.class.getName();
    private ResultSet resultSet;

    public ProjectDao() {
    }

    // get project info if user has a role in the project.
    public Project getProject(int projectId, String userEPPN) {

        // check if user has a role in project
        try {
            resultSet = null;
            if (!hasProjectRole(projectId, userEPPN)) {
                return null;
            }
            String query = getSelectProjectQuery();
            query += "WHERE g.group_id = ? ";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, projectId);

            ServiceLogger.log(logTag, "getProject, id: " + projectId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return readProjectInfoFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
        } finally {
            try {
                if (null != resultSet) {
                    resultSet.close();
                }
            } catch (Exception e2) {
                // don't really care now.
            }
        }
        return null;
    }

    // get any project the user is a member of
    public ArrayList<Project> getProjectList(String userEPPN) {

        ArrayList<Project> projects = new ArrayList<Project>();

        String query = getSelectProjectQuery();
        String groupIdList = "select * from (" + query + ") as project_info, (SELECT distinct pfo_role.home_group_id"
                + " FROM  pfo_role,  pfo_user_role, users" + " WHERE  pfo_role.role_id = pfo_user_role.role_id AND"
                + " pfo_role.home_group_id IS NOT NULL AND"
                + " pfo_user_role.user_id =users.user_id AND users.user_name = ?) as project_id"
                + " where project_info.id = project_id.home_group_id";

        ServiceLogger.log(logTag, "groupIdList: " + groupIdList);
        try {
            PreparedStatement preparedStatement = DBConnector.prepareStatement(groupIdList);
            preparedStatement.setString(1, userEPPN);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Project project = readProjectInfoFromResultSet(resultSet);
                ServiceLogger.log(logTag, "adding project : " + project.getId());
                projects.add(project);
            }
            return projects;
        }

        catch (SQLException e) {
            ServiceLogger.log(logTag, e.getMessage());
        } catch (Exception e) {
            ServiceLogger.log(logTag, e.getMessage());
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    // don't care
                }
            }
        }

        return null;
    }

    protected String getSelectProjectQuery() {
        String query = "SELECT g.group_id AS id, g.group_name AS title, x.firstname AS firstname, x.lastname AS lastname, "
                + "g.short_description AS description, g.due_date, s.msg_posted AS count, "
                + "pt.taskCount AS taskCount, " + "ss.servicesCount AS servicesCount, "
                + "c.componentsCount AS componentsCount " + "FROM groups g "
                + "JOIN (SELECT u.firstname AS firstname, u.lastname AS lastname , r.home_group_id "
                + "FROM pfo_user_role ur " + "JOIN users u ON u.user_id = ur.user_id "
                + "JOIN pfo_role r ON r.role_id = ur.role_id "
                + "WHERE r.role_name = 'Admin') x on g.group_id = x.home_group_id "
                + "LEFT JOIN stats_project s ON s.group_id = g.group_id LEFT JOIN "
                + "(SELECT count(*) AS taskCount, group_project_id AS id FROM project_task "
                + "GROUP BY group_project_id) AS pt ON pt.id = g.group_id LEFT JOIN "
                + "(SELECT count(*) AS servicesCount, group_id AS id FROM service_subscriptions "
                + "GROUP BY group_id) AS ss ON ss.id = g.group_id LEFT JOIN "
                + "(SELECT count(*) AS componentsCount, group_id AS id FROM cem_objects "
                + "GROUP BY group_id) AS c ON c.id = g.group_id ";
        return query;
    }

    protected Project readProjectInfoFromResultSet(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        long due_date = 0;
        String thumbnail = "";
        String largeUrl = "";
        FeatureImage image = new FeatureImage(thumbnail, largeUrl);

        int projectId = resultSet.getInt("id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        Timestamp t = resultSet.getTimestamp("due_date");
        if (null == t) {
            due_date = 0;
        } else {
            due_date = t.getTime();
        }

        if (description == null)
            description = "";

        int num_discussions = resultSet.getInt("count");
        int num_components = resultSet.getInt("componentsCount");
        int num_tasks = resultSet.getInt("taskCount");
        int num_services = resultSet.getInt("servicesCount");

        ProjectTask task = new ProjectTask(num_tasks, projectId);
        ProjectService service = new ProjectService(num_services, projectId);
        ProjectDiscussion discussion = new ProjectDiscussion(num_discussions, projectId);
        ProjectComponent component = new ProjectComponent(num_components, projectId);

        String projectManager = resultSet.getString("firstname") + " " + resultSet.getString("lastname");
        ServiceLogger.log(logTag,
                "projectId: " + projectId + "num_discussions: " + num_discussions + "num_components: " + num_components
                        + "num_tasks: " + num_tasks + "num_services: " + num_services + "description: " + description);

        project.setId(projectId);
        project.setTitle(title);
        project.setDescription(description);
        project.setImages("");
        project.setFeatureImage(image);
        project.setTasks(task);
        project.setServices(service);
        project.setDiscussions(discussion);
        project.setComponents(component);
        project.setProjectManager(projectManager);
        project.setDueDate(due_date);

        ServiceLogger.log(logTag, project.toString());

        return project;
    }

    public Id createProject(String projectname, String unixname, String description, String projectType,
            String userEPPN, long dueDate) throws SQLException, JSONException, Exception {
        Connection connection = DBConnector.connection();
        // let's start a transaction
        connection.setAutoCommit(false);

        try {
            int projectId = -1;
            // look up userID
            int userID = UserDao.getUserID(userEPPN);
            int isPublic = Project.IsPublic(projectType);

            // create new project in groups table
            String createProjectQuery = "insert into groups(group_name, unix_group_name, short_description, register_purpose, is_public, user_id, due_date) values ( ?, ?, ?, ?, ?, ?, ? )";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(createProjectQuery);
            preparedStatement.setString(1, projectname);
            preparedStatement.setString(2, unixname);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, description);
            preparedStatement.setInt(5, isPublic);
            preparedStatement.setInt(6, userID);
            preparedStatement.setTimestamp(7, new Timestamp(dueDate));
            preparedStatement.executeUpdate();

            // since no parameters can use execute query safely
            String getProjectsIdQuery = "select currval('groups_pk_seq') as id";
            resultSet = DBConnector.executeQuery(getProjectsIdQuery);
            if (resultSet.next()) {
                // id = resultSet.getString("id");
                projectId = resultSet.getInt("id");
            } // TODO:Add error handling after if

            String createProjectListQuery = "INSERT into project_group_list (group_id, project_name, is_public, description, send_all_posts_to) values (?, ?, ?, ?, ?)";
            preparedStatement = DBConnector.prepareStatement(createProjectListQuery);
            preparedStatement.setInt(1, projectId);
            preparedStatement.setString(2, projectname);
            preparedStatement.setInt(3, isPublic);
            preparedStatement.setString(4, description);
            preparedStatement.setString(5, "none");
            preparedStatement.executeUpdate();

            // create project member role
            createProjectRole("Project Member", projectId);

            // create project admin role
            createProjectRole("Admin", projectId);

            // get project admin role ID
            int projectAdminRoleId = -1;
            String getProjectAdminRoleIdQuery = "select role_id from pfo_role where home_group_id = ? and role_name = 'Admin'";
            preparedStatement = DBConnector.prepareStatement(getProjectAdminRoleIdQuery);
            preparedStatement.setInt(1, projectId);
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                // id = resultSet.getString("id");
                projectAdminRoleId = resultSet.getInt("role_id");
            } else {
                throw new Exception("expected to get project admin role id, but failed");
            }

            // add user as admin of project
            String setUserAsAdminQuery = "INSERT into pfo_user_role (user_id, role_id) values (?, ?)";
            preparedStatement = DBConnector.prepareStatement(setUserAsAdminQuery);
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, projectAdminRoleId);
            preparedStatement.executeUpdate();

            // create user as a member of the project
            createProjectJoinRequest(Integer.toString(projectId), Integer.toString(userID), userID);

            if (Config.IS_TEST == null) {
                ServiceLogger.log(logTag, "SolR indexing turned off");
                // String indexResponse = SolrUtils.invokeFulIndexingProjects();
                // ServiceLogger.log(logTag, "SolR indexing triggered for
                // project: " + projectId);
            }

            connection.commit();

            return new Id.IdBuilder(projectId).build();
        } catch (SQLException ex) {
            ServiceLogger.log(logTag, "got SQLException in createProject: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(logTag, "Transaction Project Create Rolled back");
                connection.rollback();
            }
            throw ex;
        } catch (JSONException ex) {
            ServiceLogger.log(logTag, "got JSONException in createProject: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(logTag, "Transaction Project Create Rolled back");
                connection.rollback();
            }
            throw ex;
        } catch (Exception ex) {
            ServiceLogger.log(logTag, "got Exception in createProject: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(logTag, "Transaction Project Create Rolled back");
                connection.rollback();
            }
            throw ex;
        } finally {
            // let's end the transaction
            if (null != connection) {
                connection.setAutoCommit(true);
            }
        }

    }

    public Id createProject(String jsonStr, String userEPPN) throws SQLException, JSONException, Exception {

        JSONObject json = new JSONObject(jsonStr);
        String projectname = json.getString("projectname");
        String unixname = json.getString("unixname");

        return createProject(projectname, unixname, projectname, Project.PRIVATE, userEPPN, 0);
    }

    public Id createProject(ProjectCreateRequest project, String userEPPN)
            throws SQLException, JSONException, Exception {

        String projectname = project.getTitle();
        String unixname = project.getTitle();
        String description = project.getDescription();
        long dueDate = project.getDueDate();

        return createProject(projectname, unixname, description, Project.PRIVATE, userEPPN, dueDate);
    }

    public Id updateProject(int id, Project project, String userEPPN) throws DMCServiceException {

        Util util = Util.getInstance();
        connection = DBConnector.connection();
        String query;
        PreparedStatement statement;
        int projectId;

        Timestamp dueDate = new Timestamp(project.getDueDate());

        ServiceLogger.log(logTag, "Update Payload: \n" + project.toString());

        try {

            connection.setAutoCommit(false);

            // update the project
            query = "UPDATE groups SET group_name = ?, short_description = ?, due_date = ? " + "WHERE group_id = ?";

            statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, project.getTitle());
            statement.setString(2, project.getDescription());
            statement.setTimestamp(3, dueDate);
            statement.setInt(4, id);

            statement.executeUpdate();
            projectId = util.getGeneratedKey(statement, "group_id");
            if (id != projectId) {
                throw new DMCServiceException(DMCError.OtherSQLError, "mismatched project ids: expecting " + id + " found " + projectId);
            }
            ServiceLogger.log(logTag, "updated project " + projectId);

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    ServiceLogger.log(logTag, "Transaction updateProject Rolled back");
                    connection.rollback();
                } catch (SQLException ex) {
                    ServiceLogger.log(logTag, ex.getMessage());
                }
                throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
            }
            ServiceLogger.log(logTag, e.getMessage());
            return null;
        } catch (JSONException e) {
            ServiceLogger.log(logTag, e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    ServiceLogger.log(logTag, ex.getMessage());
                }
            }
        }
        return new Id.IdBuilder(projectId).build();

    }

    void createProjectRole(String roleName, int projectId) throws SQLException {
        // create project member role
        String createProjectMemberRoleQuery = "insert into pfo_role (role_name, role_class, home_group_id, is_public) values (?, 1, ?, FALSE)";
        PreparedStatement preparedStatement = DBConnector.prepareStatement(createProjectMemberRoleQuery);
        preparedStatement.setString(1, roleName);
        preparedStatement.setInt(2, projectId);
        preparedStatement.executeUpdate();

        // TODO: check insert was successful
    }

    //
    // Sample query:
    // SELECT
    // role_name
    // FROM
    // public.pfo_user_role,
    // public.pfo_role
    // WHERE
    // pfo_user_role.role_id = pfo_role.role_id AND
    // pfo_role.home_group_id = 6 AND
    // pfo_user_role.user_id = 102
    public boolean hasProjectRole(int projectId, String userEPPN) throws SQLException {
        int userId = UserDao.getUserID(userEPPN);
        String findUsersRoleInProjectQuery = "SELECT " + "role_name " + "FROM  " + "  public.pfo_user_role, "
                + "  public.pfo_role " + "WHERE  " + "  pfo_user_role.role_id = pfo_role.role_id AND "
                + "  pfo_role.home_group_id = ? AND  " + "  pfo_user_role.user_id = ?";
        PreparedStatement preparedStatement = DBConnector.prepareStatement(findUsersRoleInProjectQuery);
        preparedStatement.setInt(1, projectId);
        preparedStatement.setInt(2, userId);
        preparedStatement.execute();
        resultSet = preparedStatement.getResultSet();
        if (!resultSet.next()) {
            // user has no role in project
            return false;
        }
        // else user has a role
        return true;
    }

    public ArrayList<ProjectJoinRequest> getProjectJoinRequest(ArrayList<String> projects, ArrayList<String> profiles,
            String userEPPN) throws Exception {
        try {
            int userId = UserDao.getUserID(userEPPN);

            // requesting user must be administrator of the project to get the
            // list of members.
            String projectJoinRequestQuery = "SELECT gjr.group_id, gjr.user_id, gjr.requester_id "
                    + "FROM group_join_request gjr ";
            String projectsInClause = CreateInClause(projects);
            String profilesInClause = CreateInClause(profiles);
            if (projectsInClause.length() > 0 && profilesInClause.length() > 0) {
                projectJoinRequestQuery += "WHERE gjr.group_id " + projectsInClause;
                projectJoinRequestQuery += "AND gjr.user_id " + profilesInClause;
            } else if (projectsInClause.length() > 0) {
                projectJoinRequestQuery += "WHERE gjr.group_id " + projectsInClause;
            } else if (profilesInClause.length() > 0) {
                projectJoinRequestQuery += "WHERE gjr.user_id " + profilesInClause;
            }

            return getProjectJoinRequestsFromQuery(projectJoinRequestQuery, userId);
        } catch (SQLException se) {
            ServiceLogger.log(logTag, se.getMessage());
        }
        return null;
    }

    private String CreateInClause(ArrayList<String> listOfInts) throws Exception {
        String clause = "";
        if (null != listOfInts && listOfInts.size() > 0) {
            clause = "IN (";
            for (String item : listOfInts) {
                clause += Integer.parseInt(item);
                clause += ",";
            }
            clause = clause.substring(0, clause.length() - 1);
            clause += ")";
        }
        return clause;
    }

    private ArrayList<ProjectJoinRequest> getProjectJoinRequestsFromQuery(String query, int userIdEPPN) {
        ArrayList<ProjectJoinRequest> list = new ArrayList<ProjectJoinRequest>();
        try {
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.execute();
            // TODO: limit query by who is requesting

            resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                // id = resultSet.getString("id");
                ProjectJoinRequest pjr = new ProjectJoinRequest();
                String project = Integer.toString(resultSet.getInt("group_id"));
                String profile = Integer.toString(resultSet.getInt("user_id"));
                String requester = Integer.toString(resultSet.getInt("requester_id"));
                pjr.setId(project + "-" + profile + "-" + requester);
                pjr.setProjectId(project);
                pjr.setProfileId(profile);
                list.add(pjr);
            }

        } catch (SQLException se) {
            ServiceLogger.log(logTag, se.getMessage());
            return null;
        }
        return list;
    }

    public ProjectJoinRequest createProjectJoinRequest(PostProjectJoinRequest json, String userEPPN)
            throws SQLException, JSONException, Exception {

        String projectId = json.getProjectId();
        String profileId = json.getProfileId();
        int userId = UserDao.getUserID(userEPPN);

        return createProjectJoinRequest(projectId, profileId, userId);
    }

    private ProjectJoinRequest createProjectJoinRequest(String projectId, String profileId, int requesterId)
            throws SQLException, Exception {

        String createProjectJoinRequestQuery = "insert into group_join_request (group_id, user_id, requester_id, request_date) values (?, ?, ?, now())";
        PreparedStatement preparedStatement = DBConnector.prepareStatement(createProjectJoinRequestQuery);
        preparedStatement.setInt(1, Integer.parseInt(projectId));
        preparedStatement.setInt(2, Integer.parseInt(profileId));
        preparedStatement.setInt(3, requesterId);
        preparedStatement.executeUpdate();

        ArrayList<String> projects = new ArrayList<String>();
        projects.add(projectId);
        ArrayList<String> profiles = new ArrayList<String>();
        profiles.add(profileId);
        ArrayList<ProjectJoinRequest> requests = getProjectJoinRequest(projects, profiles,
                Integer.toString(requesterId));
        // TODO - should only be one, but we aren't restricting by requester in
        // query, so need to do it here.
        for (ProjectJoinRequest pjr : requests) {
            if (pjr.getId().equals(projectId + "-" + profileId + "-" + Integer.toString(requesterId)))
                return pjr;
        }

        throw new Exception("Failed to create join request");
    }

    // sample query for member 111 by fforgeadmin user (102), If by and member
    // are same, then do not use the AND clause.
    public boolean deleteProjectRequest(String id, String userEPPN) throws SQLException, Exception {
        String[] values = id.split("-");
        if (values.length != 3)
            throw new Exception("invalid id");
        // TODO: probably expand permission to other project admins
        int deleteRequester = UserDao.getUserID(userEPPN);
        int projectId = Integer.parseInt(values[0]);
        int profileId = Integer.parseInt(values[1]);
        int requesterId = Integer.parseInt(values[2]);
        if (deleteRequester != profileId && deleteRequester != requesterId)
            throw new Exception("you are not allowed to delete this request");
        String createProjectJoinRequestQuery = "delete from group_join_request where group_id = ? and user_id = ? and requester_id = ?";
        PreparedStatement preparedStatement = DBConnector.prepareStatement(createProjectJoinRequestQuery);
        preparedStatement.setInt(1, projectId);
        preparedStatement.setInt(2, profileId);
        preparedStatement.setInt(3, requesterId);
        int count = preparedStatement.executeUpdate();
        if (count != 1)
            return false;
        else
            return true;
    }
}
