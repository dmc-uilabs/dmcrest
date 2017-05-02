package org.dmc.services.projects;


import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ResourceGroupService;
import org.dmc.services.ServiceLogger;
import org.dmc.services.data.dao.user.UserDao;
import org.dmc.services.data.entities.Directory;
import org.dmc.services.data.entities.DocumentParentType;
import org.dmc.services.data.entities.ProjectJoinApprovalRequest;
import org.dmc.services.data.entities.ProjectJoinApprovalRequest.ProjectJoinApprovalRequestStatus;
import org.dmc.services.data.entities.User;
import org.dmc.services.data.repositories.DirectoryRepository;
import org.dmc.services.data.repositories.ProjectJoinApprovalRequestRepository;
import org.dmc.services.data.repositories.UserRepository;
import org.dmc.services.search.SearchException;
import org.dmc.services.security.SecurityRoles;
import org.dmc.services.sharedattributes.FeatureImage;
import org.dmc.services.sharedattributes.Util;
import org.dmc.solr.SolrUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectDao {

    private Connection connection;
    private static final String LOGTAG = ProjectDao.class.getName();

    @Inject
    private ResourceGroupService resourceGroupService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private DirectoryRepository directoryRepository;

    @Inject
    private ProjectJoinApprovalRequestRepository projectJoinApprovalRequestRepository;

    @Inject
    private ProjectMemberDao projectMemberDao;

    public ProjectDao() {
    }

    // get project info if user has a role in the project or project is public
    public Project getProject(int projectId, String userEPPN) {
        ResultSet resultSet = null;
        // check if user has a role in project or project is public
        try {
            if (!isProjectPublic(projectId) && !hasProjectRole(projectId, userEPPN)) {
                return null;
            }
            String query = getSelectProjectQuery(null);
            query += "WHERE g.group_id = ? AND g.use_webdav = 1";
            final PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.setInt(1, projectId);

            ServiceLogger.log(LOGTAG, "getProject, id: " + projectId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return readProjectInfoFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            ServiceLogger.log(LOGTAG, e.getMessage());
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
    public ArrayList<Project> getProjectList(String userEPPN, Integer limit, Integer offset, String order) {

        final ArrayList<Project> projects = new ArrayList<Project>();

        final String query = getSelectProjectQuery(order);
        String groupIdList = "select * from (" + query + ") as project_info, " + "(SELECT distinct gjr.group_id " +
                "FROM group_join_request gjr, users WHERE gjr.user_id = users.user_id and users.user_name = ? AND " +
                "gjr.accept_date IS NOT NULL) as joined_projects where project_info.id = joined_projects.group_id and " +
                "project_info.usewebdav = 1 ORDER BY id DESC";
//                "(SELECT distinct pfo_role.home_group_id"
//                + " FROM  pfo_role,  pfo_user_role, users" + " WHERE  pfo_role.role_id = pfo_user_role.role_id AND"
//                + " pfo_role.home_group_id IS NOT NULL AND"
//                + " pfo_user_role.user_id =users.user_id AND users.user_name = ?) as project_id"
//                + " where project_info.id = project_id.home_group_id and project_info.usewebdav = 1 ORDER BY id DESC";

        if (limit != null && offset != null) {
            groupIdList += " LIMIT " + limit + " OFFSET " + offset;
        }

        ServiceLogger.log(LOGTAG, "groupIdList: " + groupIdList);
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = DBConnector.prepareStatement(groupIdList);
            preparedStatement.setString(1, userEPPN);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Project project = readProjectInfoFromResultSet(resultSet);
                ServiceLogger.log(LOGTAG, "adding project : " + project.getId());
                projects.add(project);
            }
        }

        catch (SQLException e) {
            ServiceLogger.log(LOGTAG, e.getMessage());
        } catch (Exception e) {
            ServiceLogger.log(LOGTAG, e.getMessage());
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    // don't care
                }
            }
        }

        return projects;
    }

    // Hack to get around having to unravel and re-write the SQL in getAllProjects
    // Being refactored to use spring-data-jpa within next few sprints
    public List<Project> getPublicProjects(Integer limit, Integer offset, String order) {
    	List<Project> projects = new ArrayList<Project>();

    	String query = "SELECT DISTINCT id, title, description, due_date, discussionsCount, componentsCount, taskCount, servicesCount, userName, isPublic, requires_approval, creatorUserId, directory_id, register_time"
    			+ " FROM (" + getSelectProjectQuery(order) + ") as project"
    			+ " WHERE project.isPublic = 1 AND project.useWebdav = 1 ORDER BY id DESC";

    	if (limit != null && offset != null) {
    	    query += " LIMIT " + limit + " OFFSET " + offset;
        }

    	ResultSet resultSet = null;
    	try {
            PreparedStatement preparedStatement = DBConnector.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Project project = readProjectInfoFromResultSet(resultSet);
                ServiceLogger.log(LOGTAG, "adding project : " + project.getId());
                projects.add(project);
            }
        }

        catch (SQLException e) {
            ServiceLogger.log(LOGTAG, e.getMessage());
        } catch (Exception e) {
            ServiceLogger.log(LOGTAG, e.getMessage());
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (Exception ex) {
                    // don't care
                }
            }
        }


    	return projects;
    }

    protected String getSelectProjectQuery(String sortOrder) {
        String query = "SELECT g.group_id AS id, g.group_name AS title, x.userName AS userName, "
                + "g.short_description AS description, g.due_date, s.msg_posted AS count, g.is_public as isPublic, g.use_webdav as useWebdav, g.requires_approval as requires_approval, "
                + "g.directory_id, g.register_time, "
                + "pt.taskCount AS taskCount, " + "ss.servicesCount AS servicesCount, g.user_id as creatorUserId, "
                + "c.componentsCount AS componentsCount, d.discussionsCount AS discussionsCount " + "FROM groups g "
                + "JOIN (SELECT u.realname AS userName, r.home_group_id "
                + "FROM pfo_user_role ur " + "JOIN users u ON u.user_id = ur.user_id "
                + "JOIN pfo_role r ON r.role_id = ur.role_id "
                + "WHERE r.role_name = 'Admin') x on g.group_id = x.home_group_id "
                + "LEFT JOIN stats_project s ON s.group_id = g.group_id LEFT JOIN "
                + "(SELECT count(*) AS taskCount, group_project_id AS id FROM project_task "
                + "GROUP BY group_project_id) AS pt ON pt.id = g.group_id LEFT JOIN "
                + "(SELECT count(*) AS servicesCount, project_id AS id FROM service "
                + "GROUP BY project_id) AS ss ON ss.id = g.group_id LEFT JOIN "
                + "(SELECT count(*) AS componentsCount, group_id AS id FROM cem_objects "
                + "GROUP BY group_id) AS c ON c.id = g.group_id LEFT JOIN "
                + "(SELECT count(*) AS discussionsCount, project_id AS id FROM individual_discussions "
                + "GROUP BY project_id) AS d ON d.id = g.group_id ";

        if (sortOrder != null) {
            query += "ORDER BY id " + sortOrder;
        }

        return query;
    }

    protected Project readProjectInfoFromResultSet(ResultSet resultSet) throws SQLException {
        final Project project = new Project();
        long due_date = 0;
        final String thumbnail = "";
        final String largeUrl = "";
        final FeatureImage image = new FeatureImage(thumbnail, largeUrl);

        final int projectId = resultSet.getInt("id");
        final String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        final Timestamp t = resultSet.getTimestamp("due_date");
        final int createdOn = resultSet.getInt("register_time");
        if (null == t) {
            due_date = 0;
        } else {
            due_date = t.getTime();
        }

        if (description == null)
            description = "";

        final int num_discussions = resultSet.getInt("discussionsCount");
        final int num_components = resultSet.getInt("componentsCount");
        final int num_tasks = resultSet.getInt("taskCount");
        final int num_services = resultSet.getInt("servicesCount");
        final Boolean requiresApproval = resultSet.getBoolean("requires_approval");
        final Boolean isPublic = resultSet.getBoolean("isPublic");
        final Integer directoryId = resultSet.getInt("directory_id");
        final Integer userId = resultSet.getInt("creatorUserId");

        final ProjectTask task = new ProjectTask(num_tasks, projectId);
        final ProjectService service = new ProjectService(num_services, projectId);
        final ProjectDiscussion discussion = new ProjectDiscussion(num_discussions, projectId);
        final ProjectComponent component = new ProjectComponent(num_components, projectId);

        final String projectManager = resultSet.getString("userName");
        ServiceLogger.log(LOGTAG,
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
        project.setRequiresAdminApprovalToJoin(requiresApproval);
        project.setIsPublic(isPublic);
        project.setDirectoryId(directoryId);
        project.setProjectManagerId(userId);
        project.setCreatedOn(createdOn);

        ServiceLogger.log(LOGTAG, project.toString());

        return project;
    }

    @Transactional
    public Id createProject(String projectname, String unixname, String description, String projectType, String approvalOption,
            String userEPPN, long dueDate, int createdOn) throws SQLException, JSONException, Exception {
        connection = DBConnector.connection();
        // let's start a transaction
        connection.setAutoCommit(false);
        ResultSet resultSet = null;
        // look up userID
        final int userID = UserDao.getUserID(userEPPN);
        final int isPublic = Project.IsPublic(projectType);
        int projectId = -1;

        try {

            Boolean requiresApproval = Project.needAdminApprovalToJoin(approvalOption);

            Directory projectDirectory = new Directory();
            projectDirectory.setName("home");
            projectDirectory.setParent(null);
            projectDirectory.setIsDeleted(false);
            projectDirectory = directoryRepository.save(projectDirectory);
            Integer directoryId = null;
            if(projectDirectory != null) {
            	directoryId = projectDirectory.getId();
            }

            // create new project in groups table
            String createProjectQuery = "insert into groups(group_name, unix_group_name, short_description, register_purpose, is_public, user_id, due_date, requires_approval, directory_id, register_time) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
            PreparedStatement preparedStatement = DBConnector.prepareStatement(createProjectQuery);
            preparedStatement.setString(1, projectname);
            preparedStatement.setString(2, unixname);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, description);
            preparedStatement.setInt(5, isPublic);
            preparedStatement.setInt(6, userID);
            preparedStatement.setTimestamp(7, new Timestamp(dueDate));
            preparedStatement.setBoolean(8, requiresApproval);
            preparedStatement.setInt(9, directoryId);
            preparedStatement.setInt(10, createdOn);
            preparedStatement.executeUpdate();

            // since no parameters can use execute query safely
            final String getProjectsIdQuery = "select currval('groups_pk_seq') as id";
            resultSet = DBConnector.executeQuery(getProjectsIdQuery);
            if (resultSet.next()) {
                // id = resultSet.getString("id");
                projectId = resultSet.getInt("id");
            } // TODO:Add error handling after if

            final String createProjectListQuery = "INSERT into project_group_list (group_id, project_name, is_public, description, send_all_posts_to) values (?, ?, ?, ?, ?)";
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
            final String getProjectAdminRoleIdQuery = "select role_id from pfo_role where home_group_id = ? and role_name = 'Admin'";
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
            final String setUserAsAdminQuery = "INSERT into pfo_user_role (user_id, role_id) values (?, ?)";
            preparedStatement = DBConnector.prepareStatement(setUserAsAdminQuery);
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, projectAdminRoleId);
            preparedStatement.executeUpdate();

            // create user as a member of the project
            createProjectJoinRequest(Integer.toString(projectId), Integer.toString(userID), userID, userEPPN);

            connection.commit();

            try {
                SolrUtils.triggerFullIndexing(SolrUtils.CORE_GFORGE_PROJECTS);
            } catch (SearchException e) {
                ServiceLogger.log(LOGTAG, e.getMessage());
            }

            return new Id.IdBuilder(projectId).build();
        } catch (SQLException ex) {
            ServiceLogger.log(LOGTAG, "got SQLException in createProject: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(LOGTAG, "Transaction Project Create Rolled back");
                connection.rollback();
            }
            throw ex;
        } catch (JSONException ex) {
            ServiceLogger.log(LOGTAG, "got JSONException in createProject: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(LOGTAG, "Transaction Project Create Rolled back");
                connection.rollback();
            }
            throw ex;
        } catch (DMCServiceException ex) {
            ServiceLogger.log(LOGTAG, "got DMCServiceException in createProject: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(LOGTAG, "Transaction Project Create Rolled back");
                connection.rollback();
            }
            throw ex;
        } catch (Exception ex) {
            ServiceLogger.log(LOGTAG, "got Exception in createProject: " + ex.getMessage());
            if (null != connection) {
                ServiceLogger.log(LOGTAG, "Transaction Project Create Rolled back");
                connection.rollback();
            }
            throw ex;
        } finally {
            // let's end the transaction
            if (null != connection) {
                connection.setAutoCommit(true);
            }

            //bolt on resource access process
            //make sure the project was actually added
            if (projectId != -1) {

            	//create new resource groups for new project
            	resourceGroupService.newCreate(DocumentParentType.PROJECT, projectId);

    			User user = userRepository.getOne(userID);
    			//give the creating user the admin role
    			resourceGroupService.addUserResourceGroup(user, DocumentParentType.PROJECT, projectId, SecurityRoles.ADMIN);
    			//add member role
    			resourceGroupService.addUserResourceGroup(user, DocumentParentType.PROJECT, projectId, SecurityRoles.MEMBER);
    		}
        }

    }

    public Id createProject(String jsonStr, String userEPPN) throws SQLException, JSONException, Exception {

        final JSONObject json = new JSONObject(jsonStr);
        final String projectname = json.getString("projectname");
        final String unixname = json.getString("unixname");
        final String type = json.getString("type");
        final String approvalOption = json.getString("approvalOption");

        return createProject(projectname, unixname, projectname, type, approvalOption, userEPPN, 0, 0);
    }

    public Id createProject(ProjectCreateRequest project, String userEPPN)
            throws SQLException, JSONException, Exception {

        final String projectname = project.getTitle();
        final String unixname = project.getTitle();
        final String description = project.getDescription();
        final long dueDate = project.getDueDate();
        final int createdOn = project.getCreatedOn();
        final String type = project.getProjectType();
        final String approvalOption = project.getApprovalOption();

        return createProject(projectname, unixname, description, type, approvalOption, userEPPN, dueDate, createdOn);
    }

    public Id updateProject(int id, Project project, String userEPPN) throws DMCServiceException {

        final Util util = Util.getInstance();
        connection = DBConnector.connection();
        final Timestamp dueDate = new Timestamp(project.getDueDate());
        ServiceLogger.log(LOGTAG, "Update Payload: \n" + project.toString());

        try {

            connection.setAutoCommit(false);

            // update the project
            final String query = "UPDATE groups SET group_name = ?, short_description = ?, due_date = ? " + "WHERE group_id = ?";

            final PreparedStatement statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, project.getTitle());
            statement.setString(2, project.getDescription());
            statement.setTimestamp(3, dueDate);
            statement.setInt(4, id);

            statement.executeUpdate();
            final int projectId = util.getGeneratedKey(statement, "group_id");
            if (id != projectId) {
                throw new DMCServiceException(DMCError.OtherSQLError, "mismatched project ids: expecting " + id + " found " + projectId);
            }
            ServiceLogger.log(LOGTAG, "updated project " + projectId);

            connection.commit();

            try {
                SolrUtils.triggerFullIndexing(SolrUtils.CORE_GFORGE_PROJECTS);
            } catch (SearchException e) {
                ServiceLogger.log(LOGTAG, e.getMessage());
            }

            return new Id.IdBuilder(projectId).build();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    ServiceLogger.log(LOGTAG, "Transaction updateProject Rolled back");
                    connection.rollback();
                } catch (SQLException ex) {
                    ServiceLogger.log(LOGTAG, ex.getMessage());
                }
                throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
            }
            ServiceLogger.log(LOGTAG, e.getMessage());
            return null;
        } catch (JSONException e) {
            ServiceLogger.log(LOGTAG, e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    ServiceLogger.log(LOGTAG, ex.getMessage());
                }
            }
        }

    }

    void createProjectRole(String roleName, int projectId) throws SQLException {
        // create project member role
        final String createProjectMemberRoleQuery = "insert into pfo_role (role_name, role_class, home_group_id, is_public) values (?, 1, ?, FALSE)";
        final PreparedStatement preparedStatement = DBConnector.prepareStatement(createProjectMemberRoleQuery);
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
        ResultSet resultSet = null;
        final int userId = UserDao.getUserID(userEPPN);
        final String findUsersRoleInProjectQuery = "SELECT " + "role_name " + "FROM  " + "  public.pfo_user_role, "
                + "  public.pfo_role " + "WHERE  " + "  pfo_user_role.role_id = pfo_role.role_id AND "
                + "  pfo_role.home_group_id = ? AND  " + "  pfo_user_role.user_id = ?";
        final PreparedStatement preparedStatement = DBConnector.prepareStatement(findUsersRoleInProjectQuery);
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

    public boolean isProjectPublic(int projectId) throws SQLException {
    	ResultSet resultSet = null;
    	String query = "SELECT is_public FROM groups g WHERE g.is_public = 1 AND g.group_id = ?";
    	PreparedStatement statement = DBConnector.prepareStatement(query);
    	statement.setInt(1, projectId);
    	statement.execute();
    	resultSet = statement.getResultSet();
    	if (!resultSet.next()) {
    		return false;
    	} else {
    		return true;
    	}
    }

    public ArrayList<ProjectJoinRequest> getProjectJoinRequest(ArrayList<String> projects, ArrayList<String> profiles,
            String userEPPN) throws Exception {
        final int userId = UserDao.getUserID(userEPPN);
        return getProjectJoinRequest(projects, profiles, userId);
    }

    public ArrayList<ProjectJoinRequest> getProjectJoinRequest(ArrayList<String> projects, ArrayList<String> profiles,
            Integer userId) throws Exception {
        // requesting user must be administrator of the project to get the
        // list of members.
        String projectJoinRequestQuery = "SELECT gjr.group_id, gjr.user_id, gjr.requester_id "
                + "FROM group_join_request gjr ";
        final String projectsInClause = CreateInClause(projects);
        final String profilesInClause = CreateInClause(profiles);
        if (projectsInClause.length() > 0 && profilesInClause.length() > 0) {
            projectJoinRequestQuery += "WHERE gjr.group_id " + projectsInClause;
            projectJoinRequestQuery += "AND gjr.user_id " + profilesInClause;
        } else if (projectsInClause.length() > 0) {
            projectJoinRequestQuery += "WHERE gjr.group_id " + projectsInClause;
        } else if (profilesInClause.length() > 0) {
            projectJoinRequestQuery += "WHERE gjr.user_id " + profilesInClause;
        }

        return getProjectJoinRequestsFromQuery(projectJoinRequestQuery, userId);
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
        final ArrayList<ProjectJoinRequest> list = new ArrayList<ProjectJoinRequest>();
        ResultSet resultSet = null;
        try {
            final PreparedStatement preparedStatement = DBConnector.prepareStatement(query);
            preparedStatement.execute();
            // TODO: limit query by who is requesting

            resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                // id = resultSet.getString("id");
                final ProjectJoinRequest pjr = new ProjectJoinRequest();
                final String project = Integer.toString(resultSet.getInt("group_id"));
                final String profile = Integer.toString(resultSet.getInt("user_id"));
                final String requester = Integer.toString(resultSet.getInt("requester_id"));
                pjr.setId(project + "-" + profile + "-" + requester);
                pjr.setProjectId(project);
                pjr.setProfileId(profile);
                list.add(pjr);
            }

        } catch (SQLException se) {
            ServiceLogger.log(LOGTAG, se.getMessage());
            return null;
        }
        return list;
    }

    public ProjectJoinRequest createProjectJoinRequest(PostProjectJoinRequest json, String userEPPN)
            throws SQLException, JSONException, Exception {

        final String projectId = json.getProjectId();
        final String profileId = json.getProfileId();
        final int userId = UserDao.getUserID(userEPPN);

        return createProjectJoinRequest(projectId, profileId, userId, userEPPN);
    }

    // AutoJoin is applicable for self joining OR when there is an aproved join request
    private boolean autoJoin(int projectId, int profileId, int requesterId, Boolean approvedJoinRequest) {
        if (profileId == requesterId || approvedJoinRequest) {
            final String autoJoinProject = "UPDATE group_join_request SET accept_date = now() WHERE user_id = ? AND requester_id = ? and group_id = ?";
            final PreparedStatement preparedStatement = DBConnector.prepareStatement(autoJoinProject);
            try {
                preparedStatement.setInt(1, profileId);
                preparedStatement.setInt(2, requesterId);
                preparedStatement.setInt(3, projectId);
                preparedStatement.executeUpdate();

                return true;
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                throw new DMCServiceException(DMCError.UnknownSQLError, e.getMessage());
            }
        }

        return false;
    }

    public ProjectJoinRequest createProjectJoinRequest(String projectIdAsString, String profileIdAsString, int requesterId, String userEPPN)
            throws SQLException, Exception {

        Integer projectId = Integer.parseInt(projectIdAsString);
        Integer profileId = Integer.parseInt(profileIdAsString);
        Project project = this.getProject(projectId, userEPPN);

        Boolean allowed = false;
        Boolean approvedJoinRequest = false;

        if (projectMemberDao.isUserProjectAdmin(projectId, requesterId)) {
            allowed = true;
        }

        // Non-admins are allowed to add themselves to projects that don't require admin approval
        if (profileId == requesterId && project != null && !project.getRequiresAdminApprovalToJoin()) {
        	allowed = true;
        }

        if (approvedProjectJoinApprovalRequestExists(projectId, profileId)) {
        	allowed = true;
        	approvedJoinRequest = true;
        }

        if (!allowed) {
        	throw new DMCServiceException(DMCError.NotProjectAdmin, requesterId + " is not allowed to invite new members to project");
        }

        final String createProjectJoinRequestQuery = "insert into group_join_request (group_id, user_id, requester_id, request_date) values (?, ?, ?, now())";
        final PreparedStatement preparedStatement = DBConnector.prepareStatement(createProjectJoinRequestQuery);
        preparedStatement.setInt(1, projectId);
        preparedStatement.setInt(2, profileId);
        preparedStatement.setInt(3, requesterId);
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // if it is a duplicate we'll continue without error,
            // other errors will fail
            if (!e.getMessage().contains("duplicate key")) {
                throw e;
            }
        }

        if (this.autoJoin(projectId, profileId, requesterId, approvedJoinRequest))
            ServiceLogger.log(LOGTAG, "selfAutoJoin done");
        else
            ServiceLogger.log(LOGTAG, "not a selfAutoJoin");

        final ArrayList<String> projects = new ArrayList<String>();
        projects.add(projectIdAsString);
        final ArrayList<String> profiles = new ArrayList<String>();
        profiles.add(profileIdAsString);
        final ArrayList<ProjectJoinRequest> requests = getProjectJoinRequest(projects, profiles, requesterId);
        ServiceLogger.log(LOGTAG, "getProjectJoinRequest returned " + requests.size() + " entries");
        // TODO - should only be one, but we aren't restricting by requester in
        // query, so need to do it here.
        for (ProjectJoinRequest pjr : requests) {
            ServiceLogger.log(LOGTAG, "checking project join request: " + pjr.getId() + " equals? " + projectId + "-" + profileId + "-" + Integer.toString(requesterId));
            if (pjr.getId().equals(projectId + "-" + profileId + "-" + Integer.toString(requesterId)))
                return pjr;
        }
        ServiceLogger.log(LOGTAG, "going to fail now because we didn't find the expected ProjectJoinRequest");
        throw new DMCServiceException(DMCError.NoExistingRequest, "Failed to create join request - we should have found one and returned");
    }

    private Boolean approvedProjectJoinApprovalRequestExists(Integer projectId, Integer userId) {
    	ProjectJoinApprovalRequest request = projectJoinApprovalRequestRepository.findByProjectIdAndUserId(projectId, userId);

    	if (request != null && request.getStatus().equals(ProjectJoinApprovalRequestStatus.APPROVED)) {
    		return true;
    	} else {
    		return false;
    	}
    }

    // sample query for member 111 by fforgeadmin user (102), If by and member
    // are same, then do not use the AND clause.
    public boolean deleteProjectRequest(String id, String userEPPN) throws SQLException, Exception {
        final String[] values = id.split("-");
        if (values.length != 3)
            throw new Exception("invalid id");
        // TODO: probably expand permission to other project admins
        final int deleteRequester = UserDao.getUserID(userEPPN);
        final int projectId = Integer.parseInt(values[0]);
        final int profileId = Integer.parseInt(values[1]);
        final int requesterId = Integer.parseInt(values[2]);
        if (deleteRequester != profileId && deleteRequester != requesterId)
            throw new Exception("you are not allowed to delete this request");
        final String createProjectJoinRequestQuery = "delete from group_join_request where group_id = ? and user_id = ? and requester_id = ?";
        final PreparedStatement preparedStatement = DBConnector.prepareStatement(createProjectJoinRequestQuery);
        preparedStatement.setInt(1, projectId);
        preparedStatement.setInt(2, profileId);
        preparedStatement.setInt(3, requesterId);
        final int count = preparedStatement.executeUpdate();
        if (count != 1) {
            return false;
        } else {
            return true;
        }
    }
}
