package org.dmc.services.projects;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.dmc.services.DBConnector;
import org.dmc.services.DMCError;
import org.dmc.services.DMCServiceException;
import org.dmc.services.Id;
import org.dmc.services.sharedattributes.Util;

public class ProjectsTagsDao {

    public ProjectsTagsDao() {
    }

    /**
     * Create Projects Tags
     * 
     * @param tag
     * @param userEPPN
     * @return
     * @throws DMCServiceException
     */
    public ProjectTag createProjectTags(ProjectTag tag, String userEPPN) throws DMCServiceException {
        try {
            final Util util = Util.getInstance();

            final String query = "INSERT INTO project_tags (project_id, tag_name) VALUES (?, ?)";

            final PreparedStatement statement = DBConnector.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, Integer.parseInt(tag.getProjectId()));
            statement.setString(2, tag.getName());
            statement.executeUpdate();
            final int tagId = util.getGeneratedKey(statement, "tag_id");
            tag.setId(String.valueOf(tagId));

            return tag;

        } catch (SQLException e) {
            throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
        }
    }

    /**
     * Create Projects Tags
     * 
     * @param tag
     * @param userEPPN
     * @return
     * @throws DMCServiceException
     */
    public ArrayList<ProjectTag> getProjectTags(Integer projectId, String userEPPN) throws DMCServiceException {
        final ArrayList<ProjectTag> tags = new ArrayList<ProjectTag>();

        try {
            String query = "SELECT * FROM project_tags ";
            if (null != projectId) {
                query += "WHERE project_id = ?";

            }

            final PreparedStatement statement = DBConnector.prepareStatement(query);
            if (null != projectId) {
                statement.setInt(1, projectId);
            }
            final ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                final ProjectTag tag = new ProjectTag();
                tag.setId(String.valueOf(rs.getInt("tag_id")));
                tag.setProjectId(String.valueOf(rs.getInt("project_id")));
                tag.setName(rs.getString("tag_name"));
                tags.add(tag);
            }

            return tags;

        } catch (SQLException e) {
            throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
        } catch (Exception e) {
            throw new DMCServiceException(DMCError.Generic, e.getMessage());
        }
    }

    /**
     * Delete Projects Tags
     * 
     * @param tag
     * @param userEPPN
     * @return
     * @throws DMCServiceException
     */
    public Id deleteProjectTag(int tagId, String userEPPN) throws DMCServiceException {
        try {
            final String query = "DELETE FROM project_tags WHERE tag_id = ?";

            final PreparedStatement statement = DBConnector.prepareStatement(query);
            statement.setInt(1, tagId);
            final int affectedRows = statement.executeUpdate();
            if (1 != affectedRows) {
                throw new DMCServiceException(DMCError.NoExistingRequest, "tag " + tagId + " not found to delete");
            }

        } catch (SQLException e) {
            throw new DMCServiceException(DMCError.OtherSQLError, e.getMessage());
        }

        return new Id.IdBuilder(tagId).build();
    }
}
