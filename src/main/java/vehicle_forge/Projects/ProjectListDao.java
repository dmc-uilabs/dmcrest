package vehicle_forge;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import java.util.Arrays;

public class ProjectListDao {

	private final String logTag = ProjectListDao.class.getName();
	private ResultSet resultSet;
	
	public ProjectListDao(){}
	
	public ArrayList<Project> getProjectList(String userEPPN){
		
		ArrayList<Project> projects = new ArrayList<Project>();
		
		/*String username;
		String password;
		
		byte[] byteArray = Base64.decodeBase64(encoded.getBytes());
		
		String decoded = new String(byteArray);
		
		int colonLocation = decoded.indexOf(":");
		
		ServiceLogger.log(logTag, "decoded String");
		
		if (colonLocation == -1){
			return null;
		}
		
		if (decoded.substring(0, 6) != "Basic "){
			return null;
		}
		
		ServiceLogger.log(logTag, "attempting to authenticate");
		
		username = decoded.substring(0, colonLocation);
		password = decoded.substring(colonLocation + 1, decoded.length());
		
		
		String query = "SELECT u.user_id AS user_id, u.user_pw AS pwd FROM users u WHERE u.user_name = \'" 
		+ username + "\';";  
		//ServiceLogger.log(logTag, query);
		try{
			
			int user_id = -1;
			resultSet = DBConnector.executeQuery(query);
			
			//ServiceLogger.log(logTag, "user password: " + password);
			
			while(resultSet.next()){
				user_id = resultSet.getInt("user_id");
				//ServiceLogger.log(logTag, "db listed password: " + resultSet.getString("pwd"));
				if (!password.equals(resultSet.getString("pwd"))){
					return null;
				}
			}
		*/	
			/*ServiceLogger.log(logTag, "signed in");
			
			query = "SELECT group_id FROM user_roles";// WHERE user_id = " + user_id;
			resultSet = DBConnector.executeQuery(query);
			ProjectDao pLookup = new ProjectDao();
			
			while(resultSet.next()){
				int projectId = resultSet.getInt("group_id");
				projects.add(pLookup.getProject(projectId));
			
			}
			
			return projects;*/
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
		String projectManager = "";
		
		
		String outerQuery = "SELECT g.group_id AS id from groups g";
		
		
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
 				+ "GROUP BY group_id) AS c ON c.id = g.group_id ";
				//+ "WHERE g.group_id = " + projectId;


		String groupIdList = "select * from (" + query + ") as project_info, (SELECT pfo_role.home_group_id"+
                                      " FROM  pfo_role,  pfo_user_role, users"+                                                                         
                                      " WHERE  pfo_role.role_id = pfo_user_role.role_id AND"+                                                           
                                      " pfo_role.home_group_id IS NOT NULL AND"+                                                                        
                                      " pfo_user_role.user_id =users.user_id AND users.user_name = '" + userEPPN + "') as project_id"+                     
		    " where project_info.id = project_id.home_group_id";
		
		ProjectDao pLookup = new ProjectDao();
		
		ServiceLogger.log(logTag, "groupIdList: " + groupIdList);
		try {
				resultSet = DBConnector.executeQuery(groupIdList);
				ServiceLogger.log(logTag, "ResultSet: " + resultSet);
				while (resultSet.next()) {
		       			 int projectId = resultSet.getInt("id");
					 ServiceLogger.log(logTag, "projectId: " + projectId );
					 num_discussions = resultSet.getInt("count");
					 ServiceLogger.log(logTag, "num_discussions: " + num_discussions );
					 num_components = resultSet.getInt("componentsCount");
					 ServiceLogger.log(logTag, "num_components: " + num_components );
					/*ServiceLogger.log(logTag, "Project ID " + projectId + " has " 
					+ num_tasks + " discussions.");*/
					 num_tasks = resultSet.getInt("taskCount");
					 ServiceLogger.log(logTag, "num_tasks: " + num_tasks );
					/*ServiceLogger.log(logTag, "Project ID " + projectId + " has " 
					+ num_tasks + " total tasks.");*/
					 num_services = resultSet.getInt("servicesCount");
					 ServiceLogger.log(logTag, "num_services: " + num_services );
					/*ServiceLogger.log(logTag, "Project ID " + projectId + " has " 
					+ num_tasks + " service subscriptions.");*/
					 thumbnail = "";
					 largeUrl = "";
					 image = new FeatureImage(thumbnail, largeUrl);
					 task = new ProjectTask(num_tasks, projectId);
					
					 service = new ProjectService(num_services, projectId);
					 discussion = new ProjectDiscussion(num_discussions, projectId);
					 
					 description = resultSet.getString("description");
					 ServiceLogger.log(logTag, "description: " + description );
					 if (description == null)
							description = "";
					 ServiceLogger.log(logTag, "projectId: " + projectId + "num_discussions: " + num_discussions + 
							   "num_components: " + num_components + "num_tasks: " + num_tasks + 
							   "num_services: " + num_services + "description: " + description );
					 component = new ProjectComponent(num_components, projectId);
					projects.add(new Project.ProjectBuilder(resultSet.getInt("id"), 
							resultSet.getString("title"), description)
							.imgLink()
							.image(image)
							.task(task)
							.service(service)
							.discussion(discussion)
							.component(component).projectManager("None").build());
					
					
								
			}
				
				return projects;
				
				
				
				/*query = "SELECT u.firstname AS firstname, u.lastname AS lastname FROM user_roles r "
						+ "JOIN users u ON u.user_id = r.user_id WHERE r.role_id = 1 AND "
						+ "r.group_id = " + projectId;
				
				resultSet = DBConnector.executeQuery(query);
				
				while(resultSet.next()){
					projectManager = resultSet.getString("firstname") 
							+ " " + resultSet.getString("lastname");
				}*/
				
				
			
		}
		
		catch(SQLException e){
			ServiceLogger.log(logTag, e.getMessage());
		}
		catch(Exception e){
		    ServiceLogger.log(logTag, e.getMessage());
                }
		

		
		return null;
	}
	
}
