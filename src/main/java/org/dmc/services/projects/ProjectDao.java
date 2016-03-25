package org.dmc.services.projects;

import java.util.ArrayList;
import java.util.Date;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
		long due_date = 0;
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
				+ "g.short_description AS description, g.due_date, s.msg_posted AS count, "
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
				Timestamp t = resultSet.getTimestamp("due_date");
				if (null == t) {
					due_date = 0;
				} else {
					due_date = t.getTime();
				}

				if (description == null)
					description = "";

				num_discussions = resultSet.getInt("count");
				num_components = resultSet.getInt("componentsCount");
			}

			task = new ProjectTask(num_tasks, projectId);

			service = new ProjectService(num_services, projectId);

			discussion = new ProjectDiscussion(num_discussions, projectId);

			component = new ProjectComponent(num_components, projectId);

			// sample query:			
			//	SELECT u.firstname AS firstname, u.lastname AS lastname 
			//	FROM pfo_user_roles ur 
			//	JOIN users u ON u.user_id = r.user_id 
			//	JOIN pfo_roles r ON r.role_id = ur.role_id
			//	WHERE r.role_id = 1 AND 
			//		ur.group_id = 5
			query = "SELECT u.firstname AS firstname, u.lastname AS lastname "
					+ "FROM pfo_user_role ur "
					+ "JOIN users u ON u.user_id = ur.user_id "
					+ "JOIN pfo_role r ON r.role_id = ur.role_id "
					+ "WHERE r.role_name = 'Admin' AND "
					+ "r.home_group_id = ?";
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
					.component(component)
					.projectManager(projectManager)
					.dueDate(due_date)
					.build();


		} catch (SQLException e) {
			ServiceLogger.log(logTag, e.getMessage());
		}
		return null;
	}

	public Id createProject(String projectname, String unixname, String description, String projectType, String userEPPN, long dueDate) throws SQLException, JSONException, Exception {
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
		preparedStatement.setString(3,  description);
		preparedStatement.setString(4,  description);
		preparedStatement.setInt(5,  isPublic);
		preparedStatement.setInt(6, userID);
		preparedStatement.setTimestamp(7, new Timestamp(dueDate));
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
		preparedStatement.setString(2, projectname);
		preparedStatement.setInt(3, isPublic);
		preparedStatement.setString(4, description);
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
		} else {
			throw new Exception("expected to get project admin role id, but failed");
		}
		
		//add user as admin of project
		String setUserAsAdminQuery = "INSERT into pfo_user_role (user_id, role_id) values (?, ?)";
		preparedStatement = DBConnector.prepareStatement(setUserAsAdminQuery);
		preparedStatement.setInt(1, userID);
		preparedStatement.setInt(2,projectAdminRoleId);
		preparedStatement.executeUpdate();
		
		
		if (Config.IS_TEST == null){
            ServiceLogger.log(logTag, "SolR indexing turned off");
            //String indexResponse = SolrUtils.invokeFulIndexingProjects();
            //ServiceLogger.log(logTag, "SolR indexing triggered for project: " + projectId);
		}
		
		connection.commit();

		return new Id.IdBuilder(projectId).build();
		} catch (SQLException ex) {
			ServiceLogger.log(logTag,  "got SQLException in createProject: " + ex.getMessage());
			if (null != connection) {
				ServiceLogger.log(logTag, "Transaction Project Create Rolled back");
				connection.rollback();
			}
			throw ex;
		} catch (JSONException ex) {
			ServiceLogger.log(logTag,  "got JSONException in createProject: " + ex.getMessage());
			if (null != connection) {
				ServiceLogger.log(logTag, "Transaction Project Create Rolled back");
				connection.rollback();
			}
			throw ex;
		} catch (Exception ex) {
			ServiceLogger.log(logTag,  "got Exception in createProject: " + ex.getMessage());
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
        
	public Id createProject(ProjectCreateRequest json, String userEPPN) throws SQLException, JSONException, Exception {

		String projectname = json.getTitle();
		String unixname = json.getTitle();
		String description = json.getDescription();
		long dueDate = json.getDueDate();

        return createProject(projectname, unixname, description, Project.PRIVATE, userEPPN, dueDate);
	}

    void createProjectRole(String roleName, int projectId) throws SQLException {
    	// create project member role
    	String createProjectMemberRoleQuery = "insert into pfo_role (role_name, role_class, home_group_id, is_public) values (?, 1, ?, FALSE)";
    	PreparedStatement preparedStatement = DBConnector.prepareStatement(createProjectMemberRoleQuery);
    	preparedStatement.setString(1,roleName);
    	preparedStatement.setInt(2,projectId);
   		preparedStatement.executeUpdate();
   		
   		// TODO: check insert was successful
    }
    
    //
    // Sample query: 
	//  SELECT
	//	role_name
	//	FROM  
	//	  public.pfo_user_role, 
	//	  public.pfo_role 
	//	WHERE  
	//	  pfo_user_role.role_id = pfo_role.role_id AND 
	//	  pfo_role.home_group_id = 6 AND 
	//	  pfo_user_role.user_id = 102    
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

    public ArrayList<ProjectMember> getProjectMemberList(int projectId, String userEPPN) {
    	ServiceLogger.log(logTag, "WARNING: not yet implemented, so returning an empty list for testing/temporary front-end integration");
    	return new ArrayList<ProjectMember>();
    }
    

    public ArrayList<ProjectTag> getProjectTagList(int projectId, String userEPPN) {
    	ServiceLogger.log(logTag, "WARNING: not yet implemented, so returning an empty list for testing/temporary front-end integration");
    	return new ArrayList<ProjectTag>();
    }

// sample query for fforgeadmin user (102)
//    SELECT r.role_name, u.user_id, u.lastname, u.firstname, u.user_name, r.home_group_id
//    FROM pfo_user_role ur
//    JOIN users u ON u.user_id = ur.user_id
//    JOIN pfo_role r ON r.role_id = ur.role_id
//    WHERE r.home_group_id in (SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = 102 and adr.role_name = 'Admin')
//    order by role_name, lastname, firstname
	public ArrayList<ProjectMember> getProjectMembers(String userEPPN){
		ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();

		try {
			int userId = UserDao.getUserID(userEPPN);

			// requesting user must be administrator of the project to get the list of members.
			String projectMembersQuery = "SELECT r.role_name, u.user_id, u.lastname, u.firstname, u.user_name, r.home_group_id "
					+ "FROM pfo_user_role ur "
					+ "JOIN users u ON u.user_id = ur.user_id "
					+ "JOIN pfo_role r ON r.role_id = ur.role_id "
					+ "WHERE r.home_group_id in (SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = " + userId + " and adr.role_name = 'Admin') "
					+ "order by role_name, lastname, firstname ";

			list = getProjectsMembersFromQuery(projectMembersQuery);
		} catch (SQLException se)
		{
			ServiceLogger.log(logTag, se.getMessage());
			return null;
		}
		return list;
	}
	
	// sample query for member 111 by fforgeadmin user (102), If by and member are same, then do not use the AND clause.
	public ArrayList<ProjectJoinRequest> getProjectJoinRequest(ArrayList<String> projects, ArrayList<String> profiles, String userEPPN){
		
		ArrayList<ProjectJoinRequest> list = new ArrayList<ProjectJoinRequest>();
		return list;
	}

	// sample query for member 111 by fforgeadmin user (102), If by and member are same, then do not use the AND clause.
	public boolean deleteProjectRequest(String id, String userEPPN){
		return false;
	}

	// sample query for member 111 by fforgeadmin user (102), If by and member are same, then do not use the AND clause.
//  SELECT r.role_name, u.user_id, u.lastname, u.firstname, u.user_name, r.home_group_id
//  FROM pfo_user_role ur
//  JOIN users u ON u.user_id = ur.user_id
//  JOIN pfo_role r ON r.role_id = ur.role_id
//  WHERE ur.user_id = 111
//  AND r.home_group_id in (SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = 102)
//  order by role_name, lastname, firstname
	public ArrayList<ProjectMember> getProjectsForMember(String memberIdString, String userEPPN){
		
		ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();

		try {
			int userId = UserDao.getUserID(userEPPN);
			int memberId = UserDao.getUserID(memberIdString);

			// requesting user must be administrator of the project to get the list of members.
			String projectMembersQuery = "SELECT r.role_name, u.user_id, u.lastname, u.firstname, u.user_name, r.home_group_id "
					+ "FROM pfo_user_role ur "
					+ "JOIN users u ON u.user_id = ur.user_id "
					+ "JOIN pfo_role r ON r.role_id = ur.role_id "
					+ "WHERE ur.user_id = " + memberId;
			if (memberId != userId) {
				projectMembersQuery += "AND r.home_group_id in (SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = " + userId + " and adr.role_name = 'Admin') ";
			}
			projectMembersQuery += "order by role_name, lastname, firstname ";
			
			list = getProjectsMembersFromQuery(projectMembersQuery);
		} catch (SQLException se)
		{
			ServiceLogger.log(logTag, se.getMessage());
			return null;
		}
		return list;
	}
	
	// sample query for fforgeadmin user (102)
//  SELECT r.role_name, u.user_id, u.lastname, u.firstname, u.user_name, r.home_group_id
//  FROM pfo_user_role ur
//  JOIN users u ON u.user_id = ur.user_id
//  JOIN pfo_role r ON r.role_id = ur.role_id
//  WHERE r.home_group_id in (SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = 102 and adr.role_name = 'Admin')
//  order by role_name, lastname, firstname
	public ArrayList<ProjectMember> getMembersForProject(String projectIdString, String userEPPN)
		throws Exception
	{
		ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();

		try {
			int userId = UserDao.getUserID(userEPPN);
			int projectId = Integer.parseInt(projectIdString);

			// requesting user must be administrator of the project to get the list of members.
			String projectMembersQuery = "SELECT r.role_name, u.user_id, u.lastname, u.firstname, u.user_name, r.home_group_id "
					+ "FROM pfo_user_role ur "
					+ "JOIN users u ON u.user_id = ur.user_id "
					+ "JOIN pfo_role r ON r.role_id = ur.role_id "
					+ "WHERE r.home_group_id = " + projectId + " "
					+ "AND r.home_group_id in (SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = " + userId + " and adr.role_name = 'Admin') "
					+ "order by role_name, lastname, firstname ";

			list = getProjectsMembersFromQuery(projectMembersQuery);
		} catch (SQLException se)
		{
			ServiceLogger.log(logTag, se.getMessage());
			return null;
		}
		return list;
	}
	
	private ArrayList<ProjectMember> getProjectsMembersFromQuery(String query) {
		ArrayList<ProjectMember> list = new ArrayList<ProjectMember>();
		try {
			PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
			preparedStatement.execute();
	
			resultSet = preparedStatement.getResultSet();
			while (resultSet.next()) {
				//id = resultSet.getString("id");
				ProjectMember member = new ProjectMember();
				member.setProfileId(Integer.toString(resultSet.getInt("user_id")));
				member.setProjectId(Integer.toString(resultSet.getInt("home_group_id")));
				member.setAccept(true);
				member.setFromProfileId("-1");
				member.setFrom("unknown");
				member.setDate(0);
				list.add(member);
			}
	
		} catch (SQLException se)
		{
			ServiceLogger.log(logTag, se.getMessage());
			return null;
		}
		return list;
	}

	//
	// sample queries for userEPPN = 102 (fforgeadmin), memberId = 111 (testUser), and projectId = 6
	// first confirm that userEPPN is admin of project projectId
	// 		SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = 102 and adr.role_name = 'Admin' and adr.home_group_id = 6)
	// next get Project Member role for projectId as role_id
	//      SELECT r.role_id from pfo_role where r.home_group_id = 6 and r.role_name = 'Project Member'
	// then insert into the pfo_user_role table
	// 		INSERT pfo_user_role (user_id, role_id) values (111, 27);
	//
	public ProjectMember addProjectMember(ProjectMember member, String userEPPN)
		throws Exception
	{
		int userId = UserDao.getUserID(userEPPN);
		int memberId = Integer.parseInt(member.getProfileId());
		int projectId = Integer.parseInt(member.getProjectId());
		return acceptMemberInProject(projectId, memberId, userId);
	}
	public ProjectMember acceptMemberInProject(String projectIdString, String memberIdString, String userEPPN)
		throws Exception 
	{	
		int userId = UserDao.getUserID(userEPPN);
		int memberId = UserDao.getUserID(memberIdString);
		int projectId = Integer.parseInt(projectIdString);
		return acceptMemberInProject(projectId, memberId, userId);
	}		
	//
	// sample queries for userEPPN = 102 (fforgeadmin), memberId = 111 (testUser), and projectId = 6
	// first confirm that userEPPN is admin of project projectId
	// 		SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = 102 and adr.role_name = 'Admin' and adr.home_group_id = 6)
	// next get Project Member role for projectId as role_id
	//      SELECT r.role_id from pfo_role where r.home_group_id = 6 and r.role_name = 'Project Member'
	// then insert into the pfo_user_role table
	// 		INSERT pfo_user_role (user_id, role_id) values (111, 27);
	//
	private ProjectMember acceptMemberInProject(int projectId, int memberId, int userId)
			throws Exception 
		{	
		boolean ok = IsRequesterAdmin(projectId, userId);
		if (!ok) {
			throw new Exception(userId  + " does not have permission to accept members, you must be a project Admin");
		}
		
		int roleId = GetRole(projectId, "Project Member");
		
		ProjectMember member = acceptMember(memberId, roleId, projectId, userId);
		
		if (null == member) {
			throw new Exception("problem adding user " + memberId + " from project " + projectId);
		}
		
		return member;
	}

	private boolean IsRequesterAdmin(int projectId, int userId)
		throws Exception
	{
		boolean isAdmin = false;
		try {
			String checkRequesterAuthority = "SELECT adr.home_group_id from pfo_role adr join pfo_user_role adu on adr.role_id = adu.role_id where adu.user_id = ? and adr.role_name = 'Admin' and adr.home_group_id = ?";
	    	PreparedStatement preparedStatement = DBConnector.prepareStatement(checkRequesterAuthority);
	    	preparedStatement.setInt(1, userId);
	    	preparedStatement.setInt(2, projectId);
	    	resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				isAdmin = true;
			}
			resultSet.close();
		} catch (SQLException se)
		{
			ServiceLogger.log(logTag, se.getMessage());
			throw new Exception("unable to check if user is admin: " + userId + " for project " + projectId);
		}
		return isAdmin;
	}
	
	private int GetRole(int projectId, String roleName)
		throws Exception
	{
		int roleId = -1;
		try {
			String getRoleQuery = "SELECT role_id from pfo_role where role_name = ? and home_group_id = ?";
	    	PreparedStatement preparedStatement = DBConnector.prepareStatement(getRoleQuery);
	    	preparedStatement.setString(1, roleName);
	    	preparedStatement.setInt(2, projectId);
	    	resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				roleId = resultSet.getInt(1);
			}
			resultSet.close();
		} catch (SQLException se)
		{
			ServiceLogger.log(logTag, se.getMessage());
			throw new Exception("unable to query role: " + roleName + " for project " + projectId);
		}
		return roleId;
	}
	
	private ProjectMember acceptMember(int memberId, int roleId, int projectId, int fromUserId)
		throws Exception
	{
		Connection connection = DBConnector.connection();
		connection.setAutoCommit(false);
        // let's start a transaction
		try {
			// requesting user must be administrator of the project to get the list of members.
			String acceptMemberQuery = "INSERT into pfo_user_role (user_id, role_id, group_id) values ( ?, ?, ?)";
	    	PreparedStatement preparedStatement = DBConnector.prepareStatement(acceptMemberQuery);
	    	preparedStatement.setInt(1, memberId);
	    	preparedStatement.setInt(2, roleId);
	    	preparedStatement.setInt(3, projectId);
	   		int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected == 1) {		   		
				ProjectMember member = new ProjectMember();
				member.setProfileId(Integer.toString(memberId));
				member.setProjectId(Integer.toString(projectId));
				member.setAccept(true);
				member.setFromProfileId(Integer.toString(fromUserId));
				member.setFrom("????");
				Date now = new Date();
				member.setDate(now.getTime());
				return member;
			} else {
				// if we have < 1, then nothing happened, probably member is already in the project
				// so rollback shouldn't cost us anything.
				// if we have > 1 that would be weird
				connection.rollback();
			}
		} catch (SQLException se)
		{
			ServiceLogger.log(logTag, se.getMessage());
			connection.rollback();
			throw new Exception("unable to add user " + memberId + " to project " + projectId);
			
		} finally {
			connection.setAutoCommit(true);
		}
		return null;
	}
	//
	// sample queries for userEPPN = 102 (fforgeadmin), memberId = 111 (testUser), and projectId = 6
	// first confirm that userEPPN is admin of project projectId
	//      SELECT r.role_id from pfo_role where
	// make sure there is another admin if this user is an admin
	//      SELECT count(r.role_id) from pfo_role where 
	// then delete into the pfo_user_role table
	// 		DELETE pfo_user_role where user_id = 111;
	//
	public ProjectMember rejectMemberInProject(String projectIdString, String memberIdString, String userEPPN)
		throws Exception 
	{
		
		int userId = UserDao.getUserID(userEPPN);
		int memberId = UserDao.getUserID(memberIdString);
		int projectId = Integer.parseInt(projectIdString);
		
		boolean ok = IsRequesterAdmin(projectId, userId);
		if (!ok) {
			throw new Exception(userEPPN  + " does not have permission to remove members, you must be a project Admin");
		}
		
		if (userId == memberId) {
			int count = GetCountOfMembersWithRole(projectId, "Admin");
			if (count < 2) {
				throw new Exception("user " + userEPPN + " is the only Admin of project " + projectId + " Another member must be added to Admin role.");
			}
		}
		
		ProjectMember member = deleteMember(memberId, projectId, userId);
		if (null == member) {
			throw new Exception("problem deleting user " + memberIdString + " from project " + projectIdString);
		}
		
		return member;
	}

	private int GetCountOfMembersWithRole(int projectId, String roleName)
		throws Exception
	{
		int count = -1;
		String getRoleQuery = "SELECT COUNT(role_id) from pfo_role where role_name = ? and home_group_id = ?";
			
    	PreparedStatement preparedStatement = DBConnector.prepareStatement(getRoleQuery);
    	preparedStatement.setString(1, roleName);
    	preparedStatement.setInt(2, projectId);
    	resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			count = resultSet.getInt(1);
		}
		resultSet.close();
		return -1;
	}

	private ProjectMember deleteMember(int memberId, int projectId, int fromUserId)
		throws Exception
	{
		Connection connection = DBConnector.connection();
		connection.setAutoCommit(false);
        // let's start a transaction
		try {
			// requesting user must be administrator of the project to get the list of members.
			String acceptMemberQuery = "DELETE from pfo_user_role where user_id = ? and role_id in (select role_id from pfo_role where = home_group_id = ?)";
	    	PreparedStatement preparedStatement = DBConnector.prepareStatement(acceptMemberQuery);
	    	preparedStatement.setInt(1, memberId);
	    	preparedStatement.setInt(2, projectId);
	   		int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected == 1) {		   		
				ProjectMember member = new ProjectMember();
				member.setProfileId(Integer.toString(memberId));
				member.setProjectId(Integer.toString(projectId));
				member.setAccept(false);
				member.setFromProfileId(Integer.toString(fromUserId));
				member.setFrom("????");
				Date now = new Date();
				member.setDate(now.getTime());
				return member;
			} else {
				// if we have < 1, then nothing happened, probably member is not already in the project
				// so rollback shouldn't cost us anything.
				// if we have > 1 that would be weird
				connection.rollback();
			}
		} catch (SQLException se)
		{
			ServiceLogger.log(logTag, se.getMessage());
			connection.rollback();
			throw new Exception("unable to remove user " + memberId + " from project " + projectId);
			
		} finally {
			connection.setAutoCommit(true);
		}
		return null;
	}
	
}
