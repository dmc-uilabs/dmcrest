package vehicle_forge;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import org.json.JSONObject;
import org.json.JSONException;

public class ProjectDao {
	
	

	private final String logTag = ProjectDao.class.getName();
	private ResultSet resultSet;
	
	public ProjectDao(){}

	public Project getProject(int projectId) {
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
	// leaving this as an example of how to work with parameter to URL
	// instead of json, but json is probably preferable
	public Id createProject(String projectname, String unixname) throws SQLException, JSONException, Exception {

		//String id = "null";
		int id = -99999;
		String query = "insert into groups(group_name, unix_group_name) values ( ?, ? )";

		PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
		preparedStatement.setString(1, projectname);
		preparedStatement.setString(2, unixname);
		preparedStatement.executeUpdate();

		// since no parameters can use execute query safely
		query = "select currval('groups_pk_seq') as id";
		resultSet = DBConnector.executeQuery(query);
		while (resultSet.next()) {
			//id = resultSet.getString("id");
			id = resultSet.getInt("id");
		}

		return new Id.IdBuilder(id)
		.build();
	}
	public Id createProject(String jsonStr) throws SQLException, JSONException, Exception {

		//String id = "null";
		int id = -99999;
		JSONObject json = new JSONObject(jsonStr);
		String projectname = json.getString("projectname");
		String unixname = json.getString("unixname");
		String query = "insert into groups(group_name, unix_group_name) values ( ?, ? )";

		PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
		preparedStatement.setString(1, projectname);
		preparedStatement.setString(2, unixname);
		preparedStatement.executeUpdate();

		// since no parameters can use execute query safely
		query = "select currval('groups_pk_seq') as id";
		resultSet = DBConnector.executeQuery(query);
		while (resultSet.next()) {
			//id = resultSet.getString("id");
			id = resultSet.getInt("id");
		}

		return new Id.IdBuilder(id)
		.build();
	}
}
