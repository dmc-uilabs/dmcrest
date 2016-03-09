package org.dmc.services.projects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dmc.services.Config;
import org.dmc.services.DBConnector;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.users.UserDao;

import org.json.JSONObject;
import org.json.JSONException;
import org.dmc.solr.SolrUtils;

public class ProjectDao {



	private final String logTag = ProjectDao.class.getName();
	private ResultSet resultSet;

	public ProjectDao(){}
	
	public Project getProject(int projectId, String userEPPN) {
		
		// check if user has a role in project
		try {
			if(!hasProjectRole(projectId, userEPPN)) {
				return null;
			}
		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		
		int id = 0;
		int num_tasks = 0, num_discussions = 0, num_services = 0, num_components = 0;
		String title = "", description = "", query;
		String thumbnail = "";
		String largeUrl = "";
		FeatureImage image = new FeatureImage(thumbnail, largeUrl);
		ProjectDiscussion discussion;
		ProjectService service;
		ProjectTask task;
		ProjectComponent component;
		String projectManager = "None";

		try {

			query = "SELECT g.group_id AS id, g.group_name AS title, "
				+ "g.short_description AS description, s.msg_posted AS count, "
				+"pt.taskCount AS taskCount, "
				+"ss.servicesCount AS servicesCount, "
				+"c.componentsCount AS componentsCount "
 				+ "FROM groups g LEFT JOIN stats_project s ON s.group_id = g.group_id LEFT JOIN "
 				+ "(SELECT count(*) AS taskCount, group_project_id AS id FROM project_task"
 				+ " GROUP BY group_project_id) AS pt ON pt.id = g.group_id LEFT JOIN "
 				+ "(SELECT count(*) AS servicesCount, group_id AS id FROM service_subscriptions"
 				+ " GROUP BY group_id) AS ss ON ss.id = g.group_id LEFT JOIN "
 				+ "(SELECT count(*) AS componentsCount, group_id AS id FROM cem_objects "
 				+ "GROUP BY group_id) AS c ON c.id = g.group_id "
				+ "WHERE g.group_id = ?";

			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, projectId);

			ServiceLogger.log(logTag, "getService, id: " + projectId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				id = resultSet.getInt("id");
				title = resultSet.getString("title");
				description = resultSet.getString("description");

				if (description == null)
					description = "";

				num_discussions = resultSet.getInt("count");
				num_components = resultSet.getInt("componentsCount");
			}

			task = new ProjectTask(num_tasks, projectId);

			service = new ProjectService(num_services, projectId);

			discussion = new ProjectDiscussion(num_discussions, projectId);

			component = new ProjectComponent(num_components, projectId);

			query = "SELECT u.firstname AS firstname, u.lastname AS lastname "
					+ "FROM user_roles r "
					+ "JOIN users u ON u.user_id = r.user_id WHERE r.role_id = 1 AND "
					+ "r.group_id = ?";
			preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.setInt(1, projectId);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()){
				projectManager = resultSet.getString("firstname")
						+ " " + resultSet.getString("lastname");
			}

			return new Project.ProjectBuilder(projectId, title, description)
					.imgLink()
					.image(image)
					.task(task)
					.service(service)
					.discussion(discussion)
					.component(component).projectManager(projectManager).build();


		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}

	public Id createProject(String projectname, String unixname, String userEPPN) throws SQLException, JSONException, Exception {
		ServiceLogger.log(logTag,  "starting createProject with arguments");
		try {
		int projectId = -1;
		// look up userID
        int userID = UserDao.getUserID(userEPPN);
        		
		// create new project in groups table
		String createProjectQuery = "insert into groups(group_name, unix_group_name, user_id) values ( ?, ?, ? )";
		PreparedStatement preparedStatement = DBConnector.prepareStatement(createProjectQuery);
		preparedStatement.setString(1, projectname);
		preparedStatement.setString(2, unixname);
		preparedStatement.setInt(3, userID);
		preparedStatement.executeUpdate();

		// since no parameters can use execute query safely
		String getProjectsIdQuery = "select currval('groups_pk_seq') as id";
		resultSet = DBConnector.executeQuery(getProjectsIdQuery);
		if (resultSet.next()) {
			//id = resultSet.getString("id");
			projectId = resultSet.getInt("id");
		}//TODO:Add error handling after if
		
		
		String createProjectListQuery = "INSERT into project_group_list (group_id, project_name, is_public, description, send_all_posts_to) values (?, ?, ?, ?, ?)";
		preparedStatement = DBConnector.prepareStatement(createProjectListQuery);
		preparedStatement.setInt(1, projectId);
		preparedStatement.setString(2,"projectname");
		preparedStatement.setInt(3,0);
		preparedStatement.setString(4, "fill-in");
		preparedStatement.setString(5, "none");		
		preparedStatement.executeUpdate();
		
		// create project member role
		createProjectRole("Project Member", projectId);
		
		// create project admin role
		createProjectRole("Admin", projectId);
		
		//get project admin role ID
		int projectAdminRoleId = -1;
		String getProjectAdminRoleIdQuery = "select role_id from pfo_role where home_group_id = ? and role_name = 'Admin'";
		preparedStatement = DBConnector.prepareStatement(getProjectAdminRoleIdQuery);
		preparedStatement.setInt(1,projectId);
		preparedStatement.execute();
		resultSet = preparedStatement.getResultSet();
		if (resultSet.next()) {
			//id = resultSet.getString("id");
			projectAdminRoleId = resultSet.getInt("role_id");
		}//TODO:Add error handling after if
		
		//add user as admin of project
		String setUserAsAdminQuery = "INSERT into pfo_user_role (user_id, role_id) values (?, ?)";
		preparedStatement = DBConnector.prepareStatement(setUserAsAdminQuery);
		preparedStatement.setInt(1, userID);
		preparedStatement.setInt(2,projectAdminRoleId);
		preparedStatement.executeUpdate();
		
		
		if (Config.IS_TEST == null){
            String indexResponse = SolrUtils.invokeFulIndexingProjects();
            ServiceLogger.log(logTag, "SolR indexing triggered for project: " + projectId);
		}
		
		return new Id.IdBuilder(projectId).build();
		} catch (SQLException ex) {
			ServiceLogger.log(logTag,  "got SQLException in createProject: " + ex.getMessage());
			throw ex;
		} catch (JSONException ex) {
			ServiceLogger.log(logTag,  "got JSONException in createProject: " + ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			ServiceLogger.log(logTag,  "got Exception in createProject: " + ex.getMessage());
			throw ex;
		}
	}

	public Id createProject(String jsonStr, String userEPPN) throws SQLException, JSONException, Exception {
		ServiceLogger.log(logTag,  "starting createProject with json as string");

		//String id = "null";
		int id = -99999;
		JSONObject json = new JSONObject(jsonStr);
		String projectname = json.getString("projectname");
		String unixname = json.getString("unixname");
        
        return createProject(projectname, unixname, userEPPN);
	}
        
	public Id createProject(ProjectCreateRequest json, String userEPPN) throws SQLException, JSONException, Exception {
		ServiceLogger.log(logTag,  "starting createProject with ProjectCreateRequest");

		String projectname = json.getName();
		String unixname = json.getName();

        return createProject(projectname, unixname, userEPPN);
	}

    void createProjectRole(String roleName, int projectId) throws SQLException {
    	// create project member role
    	String createProjectMemberRoleQuery = "insert into pfo_role (role_name, role_class, home_group_id, is_public, old_role_id) values (?, 1, ?, FALSE, 0)";
    	PreparedStatement preparedStatement = DBConnector.prepareStatement(createProjectMemberRoleQuery);
    	preparedStatement.setString(1,roleName);
    	preparedStatement.setInt(2,projectId);
   		preparedStatement.executeUpdate();
   		
   		// TODO: check insert was successful
    }
    
    boolean hasProjectRole(int projectId, String userEPPN) throws SQLException {
    	int userId = UserDao.getUserID(userEPPN);
    	String findUsersRoleInProjectQuery = 	"SELECT "+
    											"role_name "+
    											"FROM  "+
    											"  public.pfo_user_role, "+
    											"  public.pfo_role "+
    											"WHERE  "+
    											"  pfo_user_role.role_id = pfo_role.role_id AND "+
    											"  pfo_role.home_group_id = ? AND  "+
    											"  pfo_user_role.user_id = ?";
    	PreparedStatement preparedStatement = DBConnector.prepareStatement(findUsersRoleInProjectQuery);
    	preparedStatement.setInt(1,projectId);
    	preparedStatement.setInt(2,userId);
    	preparedStatement.execute();
		resultSet = preparedStatement.getResultSet();
		if (!resultSet.next()) {
			// user has no role in project
			return false;
		} 
		// else user has a role
    	return true;
    }
}
