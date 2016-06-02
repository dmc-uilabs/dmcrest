package org.dmc.services.projects;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.ServiceLogger;
import org.dmc.services.sharedattributes.Util;
import org.dmc.services.users.UserDao;


public class ProjectsTagsDao {

	private Connection connection;
	private final String logTag = ProjectsTagsDao.class.getName();
	private ResultSet resultSet;

	public ProjectsTagsDao() {
	}

	/**
	 * Create Projects Tags
	 * @param tag
	 * @param userEPPN
	 * @return
	 * @throws DMCServiceException
	 */
	public ProjectTag createProjectTags(ProjectTag tag, String userEPPN) throws DMCServiceException {
		
		Util util = Util.getInstance();
		String query;
		PreparedStatement statement;
		int tagId;
		
		try {
			
			query = "INSERT INTO project_tags (project_id, tag_name) VALUES (?, ?)";
			
			statement  = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, Integer.parseInt(tag.getProjectId()));
			statement.setString(2, tag.getName());
			statement.executeUpdate();
			tagId = util.getGeneratedKey(statement, "tag_id");
			tag.setId(String.valueOf(tagId));
			
			return tag;
			
		} catch (SQLException e) {
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
	}
	
	/**
	 * Create Projects Tags
	 * @param tag
	 * @param userEPPN
	 * @return
	 * @throws DMCServiceException
	 */
	public ArrayList<ProjectTag> getProjectTags(int projectId, String userEPPN) throws DMCServiceException {
		
		String query;
		PreparedStatement statement;
		ResultSet rs = null;
		ArrayList<ProjectTag> tags = new ArrayList<ProjectTag>();

		try {
			
			query = "SELECT * FROM project_tags WHERE project_id = ?";
			
			statement  = DBConnector.prepareStatement(query);
			statement.setInt(1, projectId);
			rs = statement.executeQuery();
			
            while (rs.next()) {
            	ProjectTag tag = new ProjectTag();
                tag.setId(String.valueOf(rs.getInt("tag_id")));
                tag.setProjectId(String.valueOf(rs.getInt("project_id")));
                tag.setName(rs.getString("tag_name"));
                tags.add(tag);
            } 
            
            return tags;
			
		} catch (SQLException e) {
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
	}
	
	/**
	 * Delete Projects Tags
	 * @param tag
	 * @param userEPPN
	 * @return
	 * @throws DMCServiceException
	 */
	public Id deleteProjectTag(int tagId, String userEPPN) throws DMCServiceException {

		String query;
		PreparedStatement statement;
		
		try {
			
			query = "DELETE FROM project_tags WHERE tag_id = ?";
			
			statement  = DBConnector.prepareStatement(query);
			statement.setInt(1, tagId);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
		}
		
		return new Id.IdBuilder(tagId).build();
	}
}
